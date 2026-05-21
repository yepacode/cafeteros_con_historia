package com.cafeteros.historia.data.remote.firestore

import com.cafeteros.historia.data.model.ChatMessage
import com.cafeteros.historia.data.model.Conversation
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Fuente de datos remota para conversaciones y sus mensajes.
 *
 * Usa una **subcollección** `/conversations/{id}/messages` para los
 * mensajes individuales — así las queries de "mensajes de esta
 * conversación" no requieren índice y son baratas.
 *
 * Para "mis conversaciones" se filtra `/conversations` por
 * `array-contains` del uid del usuario en `participants` — Firestore
 * soporta esto sin índice compuesto.
 */
class ConversationsRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collection = firestore.collection(COLLECTION)

    /**
     * Crea la conversación si no existe (idempotente por el id
     * determinístico) o la actualiza con el último mensaje. Usa
     * `set(..., merge)` para no pisar otros campos.
     */
    suspend fun upsertConversation(conversation: Conversation) {
        require(conversation.id.isNotBlank()) { "Conversation.id es obligatorio." }
        val data = mapOf(
            FIELD_ID to conversation.id,
            FIELD_PARTICIPANTS to conversation.participants,
            FIELD_PARTICIPANT_NAMES to conversation.participantNames,
            FIELD_LAST_MESSAGE to conversation.lastMessage,
            FIELD_LAST_MESSAGE_AT to FieldValue.serverTimestamp()
        )
        collection.document(conversation.id).set(data, SetOptions.merge()).await()
    }

    /**
     * Envía un mensaje nuevo a la conversación [conversationId]. También
     * actualiza el `lastMessage` y `lastMessageAt` del doc padre para
     * que el Inbox se actualice automáticamente.
     */
    suspend fun sendMessage(conversationId: String, message: ChatMessage) {
        val msgRef = collection.document(conversationId)
            .collection(SUBCOLLECTION_MESSAGES)
            .document()
        val msgData = mapOf(
            FIELD_SENDER_UID to message.senderUid,
            FIELD_TEXT to message.text,
            FIELD_CREATED_AT to FieldValue.serverTimestamp()
        )
        msgRef.set(msgData).await()

        // Actualizar metadatos del padre para el Inbox.
        collection.document(conversationId).set(
            mapOf(
                FIELD_LAST_MESSAGE to message.text,
                FIELD_LAST_MESSAGE_AT to FieldValue.serverTimestamp()
            ),
            SetOptions.merge()
        ).await()
    }

    /** Flow reactivo de mis conversaciones (donde participo). */
    fun observeMyConversations(uid: String): Flow<List<Conversation>> = callbackFlow {
        val registration: ListenerRegistration = collection
            .whereArrayContains(FIELD_PARTICIPANTS, uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeMyConversations failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val conversations = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toConversationOrNull() }
                    .sortedByDescending { it.lastMessageAtEpochMillis }
                trySend(conversations)
            }
        awaitClose { registration.remove() }
    }

    /** Flow reactivo de los mensajes de una conversación, orden cronológico. */
    fun observeMessages(conversationId: String): Flow<List<ChatMessage>> = callbackFlow {
        val registration = collection.document(conversationId)
            .collection(SUBCOLLECTION_MESSAGES)
            .orderBy(FIELD_CREATED_AT, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeMessages failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toMessageOrNull() }
                trySend(messages)
            }
        awaitClose { registration.remove() }
    }

    @Suppress("UNCHECKED_CAST")
    private fun DocumentSnapshot.toConversationOrNull(): Conversation? {
        if (!exists()) return null
        val participants = (get(FIELD_PARTICIPANTS) as? List<*>)
            ?.filterIsInstance<String>().orEmpty()
        val names = (get(FIELD_PARTICIPANT_NAMES) as? Map<String, String>).orEmpty()
        return Conversation(
            id = id,
            participants = participants,
            participantNames = names,
            lastMessage = getString(FIELD_LAST_MESSAGE).orEmpty(),
            lastMessageAtEpochMillis = getTimestamp(FIELD_LAST_MESSAGE_AT)?.toDate()?.time
                ?: System.currentTimeMillis()
        )
    }

    private fun DocumentSnapshot.toMessageOrNull(): ChatMessage? {
        if (!exists()) return null
        return ChatMessage(
            id = id,
            senderUid = getString(FIELD_SENDER_UID).orEmpty(),
            text = getString(FIELD_TEXT).orEmpty(),
            createdAtEpochMillis = getTimestamp(FIELD_CREATED_AT)?.toDate()?.time
                ?: System.currentTimeMillis()
        )
    }

    private companion object {
        const val TAG = "ConversationsRemoteDS"
        const val COLLECTION = "conversations"
        const val SUBCOLLECTION_MESSAGES = "messages"
        const val FIELD_ID = "id"
        const val FIELD_PARTICIPANTS = "participants"
        const val FIELD_PARTICIPANT_NAMES = "participantNames"
        const val FIELD_LAST_MESSAGE = "lastMessage"
        const val FIELD_LAST_MESSAGE_AT = "lastMessageAt"
        const val FIELD_SENDER_UID = "senderUid"
        const val FIELD_TEXT = "text"
        const val FIELD_CREATED_AT = "createdAt"
    }
}

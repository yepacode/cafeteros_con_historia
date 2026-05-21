package com.cafeteros.historia.data.repository

import com.cafeteros.historia.data.model.ChatMessage
import com.cafeteros.historia.data.model.Conversation
import com.cafeteros.historia.data.remote.firestore.ConversationsRemoteDataSource
import kotlinx.coroutines.flow.Flow

/**
 * Punto único de acceso a la mensajería 1-a-1.
 *
 * Concentra dos operaciones clave:
 *  - Iniciar conversación: crea o reutiliza la conversación entre dos
 *    uids (id determinístico) y envía el primer mensaje atómicamente.
 *  - Enviar mensaje a una conversación existente.
 *
 * Mantiene los nombres de los participantes en el doc para que el Inbox
 * no necesite hacer joins con `/users`.
 */
class ConversationRepository(
    private val remote: ConversationsRemoteDataSource
) {

    /**
     * Asegura que exista la conversación entre dos usuarios y envía el
     * primer mensaje. Si ya existían, simplemente agrega el mensaje
     * (idempotente). Devuelve el id de la conversación.
     */
    suspend fun startOrSend(
        senderUid: String,
        senderName: String,
        receiverUid: String,
        receiverName: String,
        text: String
    ): String {
        val convId = Conversation.conversationIdFor(senderUid, receiverUid)
        val conversation = Conversation(
            id = convId,
            participants = listOf(senderUid, receiverUid),
            participantNames = mapOf(senderUid to senderName, receiverUid to receiverName),
            lastMessage = text
        )
        remote.upsertConversation(conversation)
        remote.sendMessage(
            conversationId = convId,
            message = ChatMessage(senderUid = senderUid, text = text)
        )
        return convId
    }

    /** Envía un mensaje a una conversación ya existente. */
    suspend fun sendMessage(conversationId: String, senderUid: String, text: String) {
        remote.sendMessage(
            conversationId = conversationId,
            message = ChatMessage(senderUid = senderUid, text = text)
        )
    }

    /** Flow de las conversaciones donde [uid] participa, ordenadas. */
    fun observeMyConversations(uid: String): Flow<List<Conversation>> =
        remote.observeMyConversations(uid)

    /** Flow de los mensajes de [conversationId], orden cronológico. */
    fun observeMessages(conversationId: String): Flow<List<ChatMessage>> =
        remote.observeMessages(conversationId)
}

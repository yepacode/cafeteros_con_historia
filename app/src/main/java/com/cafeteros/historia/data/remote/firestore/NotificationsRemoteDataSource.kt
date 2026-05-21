package com.cafeteros.historia.data.remote.firestore

import com.cafeteros.historia.data.model.Notification
import com.cafeteros.historia.data.model.NotificationType
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Fuente de datos remota para `/notifications`. Cada doc apunta a un
 * usuario destino vía [Notification.targetUid] y se consume reactivamente
 * en el `NotificationsScreen` del caficultor.
 */
class NotificationsRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collection = firestore.collection(COLLECTION)

    /** Crea una notificación. Firestore asigna el id. */
    suspend fun create(notification: Notification): String {
        val docRef = collection.document()
        docRef.set(notification.toFirestoreMap()).await()
        return docRef.id
    }

    /** Flujo reactivo de notifs dirigidas al usuario [uid]. */
    fun observeForUser(uid: String): Flow<List<Notification>> = callbackFlow {
        val registration: ListenerRegistration = collection
            .whereEqualTo(FIELD_TARGET_UID, uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeForUser failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val notifs = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toNotificationOrNull() }
                    .sortedByDescending { it.createdAtEpochMillis }
                trySend(notifs)
            }
        awaitClose { registration.remove() }
    }

    /** Marca una notificación como leída. */
    suspend fun markAsRead(id: String) {
        collection.document(id).update(FIELD_READ, true).await()
    }

    private fun Notification.toFirestoreMap(): Map<String, Any?> = mapOf(
        FIELD_TARGET_UID to targetUid,
        FIELD_TYPE to type.name,
        FIELD_TITLE to title,
        FIELD_BODY to body,
        FIELD_RELATED_ID to relatedId,
        FIELD_READ to read,
        FIELD_CREATED_AT to FieldValue.serverTimestamp()
    )

    private fun DocumentSnapshot.toNotificationOrNull(): Notification? {
        if (!exists()) return null
        return Notification(
            id = id,
            targetUid = getString(FIELD_TARGET_UID).orEmpty(),
            type = NotificationType.fromNameOrDefault(getString(FIELD_TYPE)),
            title = getString(FIELD_TITLE).orEmpty(),
            body = getString(FIELD_BODY).orEmpty(),
            relatedId = getString(FIELD_RELATED_ID).orEmpty(),
            read = getBoolean(FIELD_READ) ?: false,
            createdAtEpochMillis = getTimestamp(FIELD_CREATED_AT)?.toDate()?.time
                ?: System.currentTimeMillis()
        )
    }

    private companion object {
        const val TAG = "NotificationsRemoteDS"
        const val COLLECTION = "notifications"
        const val FIELD_TARGET_UID = "targetUid"
        const val FIELD_TYPE = "type"
        const val FIELD_TITLE = "title"
        const val FIELD_BODY = "body"
        const val FIELD_RELATED_ID = "relatedId"
        const val FIELD_READ = "read"
        const val FIELD_CREATED_AT = "createdAt"
    }
}

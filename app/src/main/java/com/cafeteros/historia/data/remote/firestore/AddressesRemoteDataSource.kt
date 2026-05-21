package com.cafeteros.historia.data.remote.firestore

import com.cafeteros.historia.data.model.SavedAddress
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Fuente de datos remota para las direcciones del comprador.
 *
 * Las direcciones viven como **subcollección** bajo el usuario:
 * `/users/{uid}/addresses/{addressId}`. Esto evita índices compuestos
 * (la query "mis direcciones" no necesita where) y mantiene la lectura
 * privada al dueño con reglas de Firestore triviales.
 */
class AddressesRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private fun collectionFor(uid: String) =
        firestore.collection("users").document(uid).collection("addresses")

    /** Crea o actualiza una dirección. Si id está vacío, Firestore asigna uno. */
    suspend fun upsert(uid: String, address: SavedAddress): String {
        val docRef = if (address.id.isBlank()) collectionFor(uid).document()
        else collectionFor(uid).document(address.id)
        val data = mapOf(
            FIELD_LABEL to address.label,
            FIELD_LINE to address.line,
            FIELD_RECIPIENT_NAME to address.recipientName,
            FIELD_RECIPIENT_PHONE to address.recipientPhone,
            FIELD_NOTES to address.notes,
            FIELD_IS_DEFAULT to address.isDefault,
            FIELD_CREATED_AT to FieldValue.serverTimestamp()
        )
        docRef.set(data, SetOptions.merge()).await()
        return docRef.id
    }

    /** Borra una dirección. */
    suspend fun delete(uid: String, addressId: String) {
        collectionFor(uid).document(addressId).delete().await()
    }

    /**
     * Marca una dirección como predeterminada y desmarca las otras. Hace
     * dos pasadas (read all, then update each) — para un volumen típico
     * de 3-5 direcciones es trivial.
     */
    suspend fun setDefault(uid: String, addressId: String) {
        val all = collectionFor(uid).get().await()
        all.documents.forEach { doc ->
            doc.reference.update(FIELD_IS_DEFAULT, doc.id == addressId).await()
        }
    }

    /** Flow reactivo de las direcciones del usuario [uid]. */
    fun observe(uid: String): Flow<List<SavedAddress>> = callbackFlow {
        val registration: ListenerRegistration = collectionFor(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observe addresses failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val addresses = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toAddressOrNull() }
                    .sortedByDescending { it.isDefault }
                trySend(addresses)
            }
        awaitClose { registration.remove() }
    }

    private fun DocumentSnapshot.toAddressOrNull(): SavedAddress? {
        if (!exists()) return null
        return SavedAddress(
            id = id,
            label = getString(FIELD_LABEL).orEmpty(),
            line = getString(FIELD_LINE).orEmpty(),
            recipientName = getString(FIELD_RECIPIENT_NAME).orEmpty(),
            recipientPhone = getString(FIELD_RECIPIENT_PHONE).orEmpty(),
            notes = getString(FIELD_NOTES).orEmpty(),
            isDefault = getBoolean(FIELD_IS_DEFAULT) ?: false,
            createdAtEpochMillis = getTimestamp(FIELD_CREATED_AT)?.toDate()?.time
                ?: System.currentTimeMillis()
        )
    }

    private companion object {
        const val TAG = "AddressesRemoteDS"
        const val FIELD_LABEL = "label"
        const val FIELD_LINE = "line"
        const val FIELD_RECIPIENT_NAME = "recipientName"
        const val FIELD_RECIPIENT_PHONE = "recipientPhone"
        const val FIELD_NOTES = "notes"
        const val FIELD_IS_DEFAULT = "isDefault"
        const val FIELD_CREATED_AT = "createdAt"
    }
}

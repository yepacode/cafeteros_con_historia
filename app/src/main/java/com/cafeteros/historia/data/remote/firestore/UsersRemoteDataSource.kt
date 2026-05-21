package com.cafeteros.historia.data.remote.firestore

import com.cafeteros.historia.data.model.ApprovalStatus
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Fuente de datos remota para la colección `/users` de Firestore.
 *
 * Conceptualmente es el reemplazo del antiguo `UserDao` de Room. Cada
 * documento es un perfil de usuario indexado por su `uid` de Firebase Auth.
 *
 * Estructura del documento:
 * ```
 * /users/{uid} {
 *   uid:          String   // mismo que el id del documento
 *   email:        String   // duplicado para consultas
 *   name:         String
 *   phone:        String
 *   roleId:       Int      // 1=Comprador, 2=Caficultor, 3=Administrador
 *   photoUrl:     String?  // opcional, para perfil del caficultor
 *   createdAt:    Timestamp (server)
 *   disabled:     Boolean  // soft-delete para gestión admin
 * }
 * ```
 */
class UsersRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collection = firestore.collection(COLLECTION_USERS)

    /**
     * Crea o sobreescribe el documento de perfil del usuario [uid].
     *
     * Se usa al registrarse: en el mismo momento que Firebase Auth crea la
     * cuenta, se guarda el perfil con rol y datos personales.
     */
    suspend fun upsertProfile(
        uid: String,
        email: String,
        name: String,
        phone: String,
        userType: UserType,
        approvalStatus: ApprovalStatus
    ) {
        val data = mapOf(
            FIELD_UID to uid,
            FIELD_EMAIL to email.trim().lowercase(),
            FIELD_NAME to name.trim(),
            FIELD_PHONE to phone.trim(),
            FIELD_ROLE_ID to userType.roleId,
            FIELD_APPROVAL_STATUS to approvalStatus.name,
            FIELD_DISABLED to false,
            FIELD_CREATED_AT to FieldValue.serverTimestamp()
        )
        collection.document(uid).set(data).await()
    }

    /**
     * Cambia el estado de aprobación de un usuario. Lo usa el admin
     * desde el panel de "Caficultores pendientes" para aprobar o
     * rechazar la solicitud.
     */
    suspend fun setApprovalStatus(uid: String, status: ApprovalStatus) {
        collection.document(uid).update(FIELD_APPROVAL_STATUS, status.name).await()
    }

    /**
     * [Flow] de los usuarios que están pendientes de aprobación.
     * Filtra por approvalStatus = PENDING_APPROVAL en cliente (el
     * volumen esperado es bajo, no requiere índice compuesto).
     */
    fun observePendingApprovals(): Flow<List<User>> = callbackFlow {
        val registration = collection
            .whereEqualTo(FIELD_APPROVAL_STATUS, ApprovalStatus.PENDING_APPROVAL.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("UsersRemoteDS", "observePendingApprovals failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val users = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toUserOrNull() }
                trySend(users)
            }
        awaitClose { registration.remove() }
    }

    /** Lee una sola vez el perfil del usuario [uid]. Devuelve null si no existe. */
    suspend fun findByUid(uid: String): User? {
        val snapshot = collection.document(uid).get().await()
        return snapshot.toUserOrNull()
    }

    /**
     * [Flow] reactivo del documento `/users/{uid}`. Se actualiza solo si el
     * documento cambia en Firestore (útil cuando el admin edita un perfil).
     */
    fun observeByUid(uid: String): Flow<User?> = callbackFlow {
        val registration: ListenerRegistration = collection.document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toUserOrNull())
            }
        awaitClose { registration.remove() }
    }

    /**
     * [Flow] de todos los usuarios para el panel de Administrador.
     * Ordenados por fecha de creación descendente.
     */
    fun observeAll(): Flow<List<User>> = callbackFlow {
        val registration = collection
            .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val users = snapshot?.documents.orEmpty().mapNotNull { it.toUserOrNull() }
                trySend(users)
            }
        awaitClose { registration.remove() }
    }

    /** Actualiza campos puntuales (nombre, teléfono, rol). */
    suspend fun updateFields(uid: String, fields: Map<String, Any?>) {
        collection.document(uid).update(fields).await()
    }

    /** Marca el usuario como deshabilitado (soft-delete). */
    suspend fun setDisabled(uid: String, disabled: Boolean) {
        collection.document(uid).update(FIELD_DISABLED, disabled).await()
    }

    /** Borra el documento. Nota: NO borra la cuenta de Firebase Auth. */
    suspend fun deleteDocument(uid: String) {
        collection.document(uid).delete().await()
    }

    /**
     * Cuenta total de usuarios y cuántos hay por rol.
     * Implementado con `get()` simple para no requerir índices agregados.
     */
    suspend fun countByRole(): Map<Int, Int> {
        val all = collection.get().await()
        return all.documents
            .mapNotNull { it.getLong(FIELD_ROLE_ID)?.toInt() }
            .groupingBy { it }
            .eachCount()
    }

    /**
     * Mapea un [DocumentSnapshot] al modelo de dominio [User].
     * Devuelve null si el documento no existe o le falta el rol — el campo
     * roleId es la única columna sin la cual no podemos enrutar al usuario.
     */
    private fun DocumentSnapshot.toUserOrNull(): User? {
        if (!exists()) return null
        val roleId = getLong(FIELD_ROLE_ID)?.toInt() ?: return null
        return User(
            id = id,
            email = getString(FIELD_EMAIL).orEmpty(),
            name = getString(FIELD_NAME).orEmpty(),
            phone = getString(FIELD_PHONE).orEmpty(),
            userType = UserType.fromRoleId(roleId),
            // Backfill: si el doc no tiene approvalStatus (usuarios antiguos
            // creados antes de la migración), lo tratamos como APPROVED.
            approvalStatus = ApprovalStatus.fromNameOrDefault(
                getString(FIELD_APPROVAL_STATUS)
            )
        )
    }

    private companion object {
        const val COLLECTION_USERS = "users"
        const val FIELD_UID = "uid"
        const val FIELD_EMAIL = "email"
        const val FIELD_NAME = "name"
        const val FIELD_PHONE = "phone"
        const val FIELD_ROLE_ID = "roleId"
        const val FIELD_APPROVAL_STATUS = "approvalStatus"
        const val FIELD_DISABLED = "disabled"
        const val FIELD_CREATED_AT = "createdAt"
    }
}

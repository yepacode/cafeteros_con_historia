package com.cafeteros.historia.data.repository

import com.cafeteros.historia.data.local.preferences.EncryptedCredentialsStore
import com.cafeteros.historia.data.local.preferences.SessionDataStore
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.data.remote.auth.AuthOperationResult
import com.cafeteros.historia.data.remote.auth.FirebaseAuthDataSource
import com.cafeteros.historia.data.remote.firestore.UsersRemoteDataSource
import com.cafeteros.historia.ui.features.auth.components.UserType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

/** Resultado de un intento de login. Modelo expuesto a la capa UI. */
sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data object UserNotFound : LoginResult()
    data object WrongPassword : LoginResult()
    data object AccountDisabled : LoginResult()
    data class UnknownError(val message: String) : LoginResult()
}

/** Resultado de un intento de registro. Modelo expuesto a la capa UI. */
sealed class RegisterResult {
    data class Success(val user: User) : RegisterResult()
    data object EmailAlreadyExists : RegisterResult()
    data object WeakPassword : RegisterResult()
    data object InvalidEmail : RegisterResult()
    data class UnknownError(val message: String) : RegisterResult()
}

/** Resultado del envío de un correo de recuperación de contraseña. */
sealed class PasswordRecoveryResult {
    data object Sent : PasswordRecoveryResult()
    data class UnknownError(val message: String) : PasswordRecoveryResult()
}

/**
 * Punto único de acceso a usuario y sesión.
 *
 * Orquesta:
 *  - [FirebaseAuthDataSource] (credenciales y tokens de Firebase Auth).
 *  - [UsersRemoteDataSource]  (perfil del usuario en Firestore `/users/{uid}`).
 *  - [SessionDataStore]       (banderas locales del dispositivo: biometría,
 *                              último email).
 *
 * Los ViewModels nunca tocan estas piezas individualmente — solo este
 * repositorio.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class UserRepository(
    private val authDataSource: FirebaseAuthDataSource,
    private val usersRemote: UsersRemoteDataSource,
    private val sessionDataStore: SessionDataStore,
    private val credentialsStore: EncryptedCredentialsStore
) {

    /**
     * [Flow] del usuario actualmente logueado, o `null` si no hay sesión.
     *
     * Se construye combinando el flujo de auth state de Firebase con el
     * snapshot del documento `/users/{uid}` para que cambios de perfil
     * (ej. admin actualiza el rol) se reflejen reactivamente.
     */
    val currentUserFlow: Flow<User?> = authDataSource.currentUidFlow()
        .flatMapLatest { uid ->
            if (uid == null) flowOf(null) else usersRemote.observeByUid(uid)
        }

    /** Snapshot one-shot del usuario actual. */
    suspend fun getCurrentUser(): User? = currentUserFlow.first()

    /** UID del usuario actualmente logueado, o `null`. Útil sin coroutines. */
    fun currentUid(): String? = authDataSource.currentUid()

    /**
     * Registra una cuenta nueva en Firebase Auth y crea el documento de
     * perfil en Firestore. Si todo sale bien, la sesión queda activa.
     */
    suspend fun register(
        email: String,
        name: String,
        phone: String,
        plainPassword: String,
        userType: UserType
    ): RegisterResult {
        val normalizedEmail = email.trim().lowercase()
        // Política de aprobación:
        //  - Compradores y admins → APPROVED al instante.
        //  - Caficultores → PENDING_APPROVAL hasta que un admin revise.
        val approvalStatus = if (userType == UserType.CAFICULTOR) {
            com.cafeteros.historia.data.model.ApprovalStatus.PENDING_APPROVAL
        } else {
            com.cafeteros.historia.data.model.ApprovalStatus.APPROVED
        }
        return when (val authResult = authDataSource.signUp(normalizedEmail, plainPassword)) {
            is AuthOperationResult.Success -> {
                runCatching {
                    usersRemote.upsertProfile(
                        uid = authResult.uid,
                        email = normalizedEmail,
                        name = name,
                        phone = phone,
                        userType = userType,
                        approvalStatus = approvalStatus
                    )
                    sessionDataStore.setLastEmail(normalizedEmail)
                    val user = User(
                        id = authResult.uid,
                        email = normalizedEmail,
                        name = name.trim(),
                        phone = phone.trim(),
                        userType = userType,
                        approvalStatus = approvalStatus
                    )
                    RegisterResult.Success(user)
                }.getOrElse { error ->
                    // El usuario quedó creado en Auth pero falló el doc Firestore.
                    // Devolvemos error; el usuario podrá iniciar sesión y la app
                    // intentará crear el doc de nuevo en su siguiente abrir.
                    RegisterResult.UnknownError(error.message ?: "Error al guardar el perfil")
                }
            }
            AuthOperationResult.EmailAlreadyExists -> RegisterResult.EmailAlreadyExists
            AuthOperationResult.WeakPassword -> RegisterResult.WeakPassword
            AuthOperationResult.InvalidEmail -> RegisterResult.InvalidEmail
            is AuthOperationResult.UnknownError -> RegisterResult.UnknownError(authResult.message)
            // Estos casos no se producen al registrar; los mapeamos a UnknownError defensivamente.
            AuthOperationResult.UserNotFound,
            AuthOperationResult.WrongPassword -> RegisterResult.UnknownError("Error inesperado")
        }
    }

    /**
     * Valida credenciales contra Firebase Auth y lee el perfil de Firestore.
     * Tras un login exitoso guarda:
     *  - El email para autocompletar el campo en la próxima apertura.
     *  - Las credenciales completas (cifradas) para que el botón "Iniciar
     *    con huella" pueda autenticar al usuario sin pedir contraseña.
     */
    suspend fun login(email: String, plainPassword: String): LoginResult {
        val normalizedEmail = email.trim().lowercase()
        return when (val authResult = authDataSource.signIn(normalizedEmail, plainPassword)) {
            is AuthOperationResult.Success -> {
                val profile = usersRemote.findByUid(authResult.uid)
                    ?: return LoginResult.UnknownError(
                        "No se encontró el perfil del usuario. Contacta soporte."
                    )
                sessionDataStore.setLastEmail(normalizedEmail)
                credentialsStore.saveCredentials(normalizedEmail, plainPassword)
                LoginResult.Success(profile)
            }
            AuthOperationResult.UserNotFound -> LoginResult.UserNotFound
            AuthOperationResult.WrongPassword -> LoginResult.WrongPassword
            AuthOperationResult.InvalidEmail -> LoginResult.UnknownError("Correo inválido.")
            is AuthOperationResult.UnknownError -> LoginResult.UnknownError(authResult.message)
            // Estos casos no se producen al hacer login.
            AuthOperationResult.EmailAlreadyExists,
            AuthOperationResult.WeakPassword -> LoginResult.UnknownError("Error inesperado")
        }
    }

    /**
     * Inicia sesión usando las credenciales guardadas tras una autenticación
     * biométrica exitosa. Si no hay credenciales (el usuario nunca se logueó
     * o el dispositivo fue limpiado) devuelve [LoginResult.UserNotFound]
     * como señal para que la UI pida login con contraseña.
     */
    suspend fun loginWithStoredCredentials(): LoginResult {
        val saved = credentialsStore.getCredentials()
            ?: return LoginResult.UserNotFound
        return login(saved.email, saved.password)
    }

    /** Indica si hay credenciales guardadas para hacer login biométrico. */
    fun hasStoredCredentials(): Boolean = credentialsStore.hasCredentials()

    /**
     * Cierra la sesión en Firebase y limpia las banderas locales. NO borra
     * las credenciales cifradas — así el botón "Iniciar con huella" sigue
     * funcionando para volver a entrar a la misma cuenta. Si se desea
     * "olvidar" el dispositivo, usar [forgetDevice].
     */
    suspend fun logout() {
        authDataSource.signOut()
        sessionDataStore.clear()
    }

    /**
     * Logout + borrado de las credenciales cifradas. Tras esto la huella ya
     * no puede iniciar sesión; el usuario debe escribir email y contraseña.
     * Pensado para una opción "olvidar este dispositivo".
     */
    suspend fun forgetDevice() {
        authDataSource.signOut()
        sessionDataStore.clear()
        credentialsStore.clearCredentials()
    }

    /**
     * Elimina permanentemente la cuenta del usuario actualmente logueado:
     * borra el documento de perfil en Firestore Y la cuenta de Firebase Auth.
     * Si no hay sesión activa, no hace nada.
     *
     * @return `true` si la operación borró la cuenta; `false` si no había sesión.
     */
    suspend fun deleteCurrentAccount(): Boolean {
        val uid = authDataSource.currentUid() ?: return false
        // Primero borramos el doc Firestore; si Auth.delete falla después, al
        // menos el perfil ya no aparece como activo en el panel admin.
        runCatching { usersRemote.deleteDocument(uid) }
        val authResult = authDataSource.deleteCurrentAccount()
        sessionDataStore.clear()
        return authResult is AuthOperationResult.Success
    }

    /**
     * Dispara el flujo de recuperación de contraseña: Firebase envía un correo
     * con un link para restablecerla. La app no maneja OTP propio.
     */
    suspend fun sendPasswordRecovery(email: String): PasswordRecoveryResult =
        when (val result = authDataSource.sendPasswordResetEmail(email.trim().lowercase())) {
            is AuthOperationResult.Success -> PasswordRecoveryResult.Sent
            is AuthOperationResult.UnknownError -> PasswordRecoveryResult.UnknownError(result.message)
            // Por seguridad, otros casos (usuario no existente, email inválido)
            // se reportan como Sent: no queremos enumerar cuentas válidas.
            else -> PasswordRecoveryResult.Sent
        }

    // ── Funciones de gestión por el Administrador ─────────────────────

    /**
     * [Flow] reactivo de todos los usuarios para el panel de administración.
     */
    val allUsersFlow: Flow<List<User>> = usersRemote.observeAll()

    /**
     * Soft-delete: marca un usuario como deshabilitado. No borra la cuenta
     * de Firebase Auth (requiere Admin SDK del lado servidor), pero impide
     * que aparezca en listados y bloquea operaciones.
     */
    suspend fun disableUser(uid: String) {
        usersRemote.setDisabled(uid, disabled = true)
    }

    /** Reactiva un usuario previamente deshabilitado. */
    suspend fun enableUser(uid: String) {
        usersRemote.setDisabled(uid, disabled = false)
    }

    // ── Aprobación de caficultores ─────────────────────────────

    /**
     * [Flow] de caficultores en estado PENDING_APPROVAL. Lo consume la
     * pantalla del admin para listar las cuentas que esperan revisión.
     */
    fun observePendingApprovals(): Flow<List<User>> =
        usersRemote.observePendingApprovals()

    /** Aprueba la cuenta del caficultor [uid] — pasa a APPROVED. */
    suspend fun approveAccount(uid: String) {
        usersRemote.setApprovalStatus(
            uid = uid,
            status = com.cafeteros.historia.data.model.ApprovalStatus.APPROVED
        )
    }

    /** Rechaza la cuenta del caficultor [uid] — pasa a REJECTED. */
    suspend fun rejectAccount(uid: String) {
        usersRemote.setApprovalStatus(
            uid = uid,
            status = com.cafeteros.historia.data.model.ApprovalStatus.REJECTED
        )
    }

    /**
     * Borra el documento del usuario en Firestore. Mantén en mente que la
     * cuenta de Firebase Auth seguirá existiendo. Para producción se debe
     * borrar también desde un Cloud Function con Admin SDK.
     */
    suspend fun deleteUserDocument(uid: String) {
        usersRemote.deleteDocument(uid)
    }

    /** Cambia el rol de un usuario (caso típico: convertir comprador en admin). */
    suspend fun updateUserRole(uid: String, newRole: UserType) {
        usersRemote.updateFields(uid, mapOf("roleId" to newRole.roleId))
    }

    /** Actualiza nombre y teléfono del usuario [uid]. */
    suspend fun updateUserContact(uid: String, name: String, phone: String) {
        usersRemote.updateFields(
            uid = uid,
            fields = mapOf(
                "name" to name.trim(),
                "phone" to phone.trim()
            )
        )
    }

    /**
     * Estadísticas rápidas para el dashboard del Administrador.
     * Hace una lectura única; no es reactiva.
     */
    suspend fun getUserStats(): UserStats {
        val byRole = usersRemote.countByRole()
        return UserStats(
            totalUsers = byRole.values.sum(),
            buyers = byRole[UserType.COMPRADOR.roleId] ?: 0,
            farmers = byRole[UserType.CAFICULTOR.roleId] ?: 0,
            admins = byRole[UserType.ADMINISTRADOR.roleId] ?: 0
        )
    }

    /** Nombre legible del rol — derivado del enum, no requiere I/O. */
    fun getRoleName(roleId: Int): String = when (roleId) {
        UserType.COMPRADOR.roleId -> "Comprador"
        UserType.CAFICULTOR.roleId -> "Caficultor"
        UserType.ADMINISTRADOR.roleId -> "Administrador"
        else -> "Desconocido"
    }
}

/** Snapshot de estadísticas de usuarios para el dashboard de Administrador. */
data class UserStats(
    val totalUsers: Int,
    val buyers: Int,
    val farmers: Int,
    val admins: Int
)

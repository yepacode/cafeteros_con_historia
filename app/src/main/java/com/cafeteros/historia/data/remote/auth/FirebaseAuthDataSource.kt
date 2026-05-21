package com.cafeteros.historia.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Resultado de una operación de autenticación contra Firebase Auth.
 *
 * Cada caso mapea uno de los errores tipados que devuelve el SDK a un valor
 * de dominio. La UI sólo conoce esta jerarquía — nunca importa clases de
 * Firebase ni códigos numéricos.
 */
sealed interface AuthOperationResult {
    /** Éxito: la sesión queda activa en [FirebaseAuth]. */
    data class Success(val uid: String) : AuthOperationResult

    /** El correo no está registrado (login). */
    data object UserNotFound : AuthOperationResult

    /** Contraseña incorrecta (login) o credencial inválida. */
    data object WrongPassword : AuthOperationResult

    /** Ya existe una cuenta con ese correo (registro). */
    data object EmailAlreadyExists : AuthOperationResult

    /** Contraseña que no cumple los requisitos mínimos de Firebase. */
    data object WeakPassword : AuthOperationResult

    /** Formato de correo inválido. */
    data object InvalidEmail : AuthOperationResult

    /** Falla de red u otra causa genérica. Conserva el mensaje del SDK para logs. */
    data class UnknownError(val message: String) : AuthOperationResult
}

/**
 * Fuente de datos remota que aísla todo el contacto con Firebase Auth.
 *
 * Cualquier capa superior (repositorios, ViewModels) interactúa con esta
 * clase y nunca con [FirebaseAuth] directamente. Esto permite swap-out en
 * tests o futuras migraciones a otro proveedor (Auth0, Supabase, etc.).
 */
class FirebaseAuthDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    /** UID del usuario logueado, o `null` si no hay sesión activa. */
    fun currentUid(): String? = auth.currentUser?.uid

    /** Email del usuario logueado, o `null`. */
    fun currentEmail(): String? = auth.currentUser?.email

    /**
     * [Flow] reactivo del UID del usuario logueado. Emite cada vez que cambia
     * el estado de autenticación (login, logout, expiración).
     */
    fun currentUidFlow(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    /**
     * Inicia sesión con email y contraseña. Devuelve [AuthOperationResult.Success]
     * con el UID si las credenciales son válidas; un caso específico de error
     * en caso contrario.
     */
    suspend fun signIn(email: String, password: String): AuthOperationResult =
        runCatching {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            AuthOperationResult.Success(authResult.user?.uid.orEmpty())
        }.getOrElse { error -> mapAuthError(error) }

    /**
     * Registra una nueva cuenta con email/contraseña. La sesión queda activa
     * automáticamente al terminar — Firebase persiste los tokens internamente.
     */
    suspend fun signUp(email: String, password: String): AuthOperationResult =
        runCatching {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            AuthOperationResult.Success(authResult.user?.uid.orEmpty())
        }.getOrElse { error -> mapAuthError(error) }

    /** Cierra la sesión local. Los tokens persistidos se eliminan. */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Envía un correo de restablecimiento de contraseña a la dirección dada.
     * Firebase se encarga del template, expiración y validación del link.
     *
     * Por seguridad, Firebase NO informa si el correo existe o no — la función
     * devuelve éxito siempre que la solicitud se haya despachado.
     */
    suspend fun sendPasswordResetEmail(email: String): AuthOperationResult =
        runCatching {
            auth.sendPasswordResetEmail(email).await()
            AuthOperationResult.Success(uid = "")
        }.getOrElse { error -> mapAuthError(error) }

    /** Acceso de bajo nivel para casos puntuales (ej. eliminar cuenta). */
    fun rawCurrentUser(): FirebaseUser? = auth.currentUser

    /**
     * Elimina la cuenta Firebase Auth del usuario actualmente logueado.
     * Si no hay sesión activa, no hace nada. Tras borrar la cuenta, el SDK
     * también limpia los tokens locales.
     */
    suspend fun deleteCurrentAccount(): AuthOperationResult {
        val user = auth.currentUser ?: return AuthOperationResult.UserNotFound
        return runCatching {
            user.delete().await()
            AuthOperationResult.Success(uid = "")
        }.getOrElse { error -> mapAuthError(error) }
    }

    /**
     * Convierte la excepción tipada del SDK en un [AuthOperationResult].
     *
     * Se centraliza aquí para que las funciones públicas se mantengan cortas
     * y para que las reglas de mapeo sean fáciles de auditar.
     */
    private fun mapAuthError(error: Throwable): AuthOperationResult = when (error) {
        is FirebaseAuthInvalidUserException -> AuthOperationResult.UserNotFound
        is FirebaseAuthInvalidCredentialsException -> {
            // El SDK reusa esta excepción para "email mal formado" y
            // "contraseña incorrecta". Diferenciamos por el código.
            if (error.errorCode == "ERROR_INVALID_EMAIL") {
                AuthOperationResult.InvalidEmail
            } else {
                AuthOperationResult.WrongPassword
            }
        }
        is FirebaseAuthUserCollisionException -> AuthOperationResult.EmailAlreadyExists
        is FirebaseAuthWeakPasswordException -> AuthOperationResult.WeakPassword
        else -> AuthOperationResult.UnknownError(error.message ?: "Error desconocido")
    }
}

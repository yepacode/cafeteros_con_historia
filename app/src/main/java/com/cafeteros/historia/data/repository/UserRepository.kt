package com.cafeteros.historia.data.repository

import com.cafeteros.historia.data.local.database.dao.RoleDao
import com.cafeteros.historia.data.local.database.dao.UserDao
import com.cafeteros.historia.data.local.preferences.SessionDataStore
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.data.model.toDomain
import com.cafeteros.historia.data.security.PasswordHasher
import com.cafeteros.historia.ui.features.auth.components.UserType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Resultado de un intento de login.
 *
 * Se modela como [sealed] para que la UI pueda hacer `when` exhaustivo y
 * mostrar mensajes específicos por caso.
 */
sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data object UserNotFound : LoginResult()
    data object WrongPassword : LoginResult()
}

/**
 * Resultado de un intento de registro.
 */
sealed class RegisterResult {
    data class Success(val user: User) : RegisterResult()
    data object EmailAlreadyExists : RegisterResult()
    data class UnknownError(val cause: Throwable) : RegisterResult()
}

/**
 * Punto único de acceso a los datos de usuario y sesión.
 *
 * Encapsula toda la lógica de negocio relacionada con cuentas:
 *  - Hash y verificación de contraseñas (vía [PasswordHasher]).
 *  - Persistencia de cuentas en Room ([UserDao]).
 *  - Gestión de la sesión activa en DataStore ([SessionDataStore]).
 *
 * Los ViewModels deben llamar SOLO a este repositorio; nunca al DAO o al
 * DataStore directamente.
 */
class UserRepository(
    private val userDao: UserDao,
    private val roleDao: RoleDao,
    private val sessionDataStore: SessionDataStore
) {

    /**
     * Devuelve el nombre del rol tal como está en la tabla `roles`.
     *
     * Útil para verificar visualmente que la relación FK funciona: si esta
     * función devuelve "Comprador" o "Vendedor", significa que la tabla roles
     * está sembrada y el JOIN users.role_id → roles.id es exitoso.
     */
    suspend fun getRoleName(roleId: Int): String? = roleDao.findById(roleId)?.name


    /** Flujo del usuario actualmente logueado, o `null` si no hay sesión. */
    val currentUserFlow: Flow<User?> = sessionDataStore.currentUserIdFlow.map { userId ->
        userId?.let { userDao.findById(it)?.toDomain() }
    }

    /** Snapshot one-shot del usuario actual. Útil cuando no se necesita reactividad. */
    suspend fun getCurrentUser(): User? = currentUserFlow.first()

    /**
     * Registra una cuenta nueva. Si todo sale bien, deja al usuario
     * automáticamente con sesión iniciada.
     */
    suspend fun register(
        email: String,
        name: String,
        phone: String,
        plainPassword: String,
        userType: UserType
    ): RegisterResult {
        val emailNormalized = email.trim().lowercase()
        if (userDao.findByEmail(emailNormalized) != null) {
            return RegisterResult.EmailAlreadyExists
        }
        return runCatching {
            val entity = com.cafeteros.historia.data.local.database.entities.UserEntity(
                email = emailNormalized,
                name = name.trim(),
                phone = phone.trim(),
                passwordHash = PasswordHasher.hash(plainPassword),
                roleId = userType.roleId
            )
            val newId = userDao.insert(entity)
            val savedUser = entity.copy(id = newId).toDomain()
            sessionDataStore.setCurrentUserId(newId)
            RegisterResult.Success(savedUser)
        }.getOrElse { error ->
            RegisterResult.UnknownError(error)
        }
    }

    /**
     * Verifica credenciales. Si son correctas, deja al usuario logueado
     * (actualiza la sesión).
     */
    suspend fun login(email: String, plainPassword: String): LoginResult {
        val emailNormalized = email.trim().lowercase()
        val entity = userDao.findByEmail(emailNormalized)
            ?: return LoginResult.UserNotFound

        if (!PasswordHasher.matches(plainPassword, entity.passwordHash)) {
            return LoginResult.WrongPassword
        }
        sessionDataStore.setCurrentUserId(entity.id)
        return LoginResult.Success(entity.toDomain())
    }

    /** Cierra la sesión sin borrar la cuenta del usuario. */
    suspend fun logout() {
        sessionDataStore.clear()
    }

    /**
     * Elimina permanentemente la cuenta del usuario actualmente logueado:
     * borra su fila de la tabla `users` y limpia la sesión activa.
     *
     * Si no hay sesión activa, no hace nada.
     *
     * @return `true` si se eliminó alguna cuenta; `false` si no había sesión.
     */
    suspend fun deleteCurrentAccount(): Boolean {
        val current = getCurrentUser() ?: return false
        userDao.deleteById(current.id)
        sessionDataStore.clear()
        return true
    }

    /**
     * Cambia la contraseña del usuario identificado por [email].
     *
     * @return `true` si la actualización afectó alguna fila, `false` si el
     *  correo no existe en la DB.
     */
    suspend fun updatePassword(email: String, newPlainPassword: String): Boolean {
        val emailNormalized = email.trim().lowercase()
        val newHash = PasswordHasher.hash(newPlainPassword)
        val rowsAffected = userDao.updatePasswordByEmail(emailNormalized, newHash)
        return rowsAffected > 0
    }

    /** Consulta si existe un usuario con ese correo (útil para validar antes de enviar el OTP). */
    suspend fun emailExists(email: String): Boolean =
        userDao.findByEmail(email.trim().lowercase()) != null
}

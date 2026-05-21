package com.cafeteros.historia.ui.features.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.data.repository.PasswordRecoveryResult
import com.cafeteros.historia.data.repository.RegisterResult
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.features.auth.components.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado UI de la pantalla de gestión de usuarios.
 *
 * @property feedback Mensaje transitorio para mostrar como Snackbar; null si no hay.
 * @property isProcessing true mientras se ejecuta una operación CRUD; deshabilita botones.
 */
data class UserManagementUiState(
    val feedback: String? = null,
    val isProcessing: Boolean = false
)

/**
 * ViewModel para [UserManagementScreen].
 *
 * Expone:
 *  - [users]: `StateFlow<List<User>>` reactivo desde Firestore (snapshotListener).
 *  - [uiState]: feedback transitorio (Snackbar) y flag de "procesando".
 *
 * Las operaciones CRUD se delegan al [UserRepository]. La lista se actualiza
 * automáticamente porque Firestore emite cambios por su listener.
 *
 * **Sobre eliminar usuarios:** Borrar la cuenta de Firebase Auth requiere el
 * Admin SDK que sólo se puede llamar desde un servidor (Cloud Function). Como
 * no tenemos backend propio, este panel usa "soft delete": marca el campo
 * `disabled=true` en el documento Firestore, lo que impide que el usuario
 * aparezca como activo. La cuenta Auth sigue existiendo pero queda inerte.
 */
class UserManagementViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as CafeterosApplication).userRepository

    val users: StateFlow<List<User>> = userRepository.allUsersFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _uiState = MutableStateFlow(UserManagementUiState())
    val uiState: StateFlow<UserManagementUiState> = _uiState.asStateFlow()

    /**
     * Crea un usuario nuevo en Firebase Auth + perfil en Firestore.
     *
     * Después de crear la cuenta, Firebase Auth deja la sesión activa con el
     * usuario recién creado (no con el admin). Esta es una limitación del SDK
     * cliente; en producción se usaría un Cloud Function con Admin SDK. Para
     * la demo, advertimos al admin que cierre y vuelva a iniciar sesión.
     */
    fun createUser(
        email: String,
        name: String,
        phone: String,
        password: String,
        userType: UserType
    ) {
        if (_uiState.value.isProcessing) return
        if (email.isBlank() || name.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(feedback = "Completa los campos obligatorios.")
            return
        }
        _uiState.value = _uiState.value.copy(isProcessing = true)
        viewModelScope.launch {
            val result = userRepository.register(
                email = email,
                name = name,
                phone = phone,
                plainPassword = password,
                userType = userType
            )
            _uiState.value = when (result) {
                is RegisterResult.Success ->
                    UserManagementUiState(
                        feedback = "Usuario creado: ${result.user.email}. " +
                                "Vuelve a iniciar sesión como admin para continuar."
                    )
                RegisterResult.EmailAlreadyExists ->
                    UserManagementUiState(feedback = "Ese correo ya está registrado.")
                RegisterResult.WeakPassword ->
                    UserManagementUiState(feedback = "La contraseña debe tener al menos 6 caracteres.")
                RegisterResult.InvalidEmail ->
                    UserManagementUiState(feedback = "El correo tiene un formato inválido.")
                is RegisterResult.UnknownError ->
                    UserManagementUiState(feedback = "Error: ${result.message}")
            }
        }
    }

    /**
     * Soft-delete: marca al usuario como deshabilitado en Firestore.
     * Su cuenta de Firebase Auth sigue existiendo pero queda sin perfil activo.
     */
    fun disableUser(user: User) {
        if (_uiState.value.isProcessing) return
        _uiState.value = _uiState.value.copy(isProcessing = true)
        viewModelScope.launch {
            runCatching { userRepository.disableUser(user.id) }
                .onSuccess {
                    _uiState.value = UserManagementUiState(
                        feedback = "Usuario deshabilitado: ${user.email}"
                    )
                }
                .onFailure { error ->
                    _uiState.value = UserManagementUiState(
                        feedback = "No se pudo deshabilitar: ${error.message ?: "error desconocido"}"
                    )
                }
        }
    }

    /** Cambia el rol del usuario (ej. promover Comprador → Administrador). */
    fun updateRole(user: User, newRole: UserType) {
        if (_uiState.value.isProcessing) return
        _uiState.value = _uiState.value.copy(isProcessing = true)
        viewModelScope.launch {
            runCatching { userRepository.updateUserRole(user.id, newRole) }
                .onSuccess {
                    _uiState.value = UserManagementUiState(
                        feedback = "Rol actualizado: ${user.email} → ${newRole.name}"
                    )
                }
                .onFailure { error ->
                    _uiState.value = UserManagementUiState(
                        feedback = "Error: ${error.message ?: "no se pudo cambiar el rol"}"
                    )
                }
        }
    }

    /**
     * Envía un correo de restablecimiento de contraseña al usuario.
     * Firebase se encarga de generar el link único y enviarlo. El admin nunca
     * conoce la nueva contraseña — el usuario la escribe directamente desde
     * el correo. Más seguro que un "resetear contraseña" del lado admin.
     */
    fun sendPasswordReset(user: User) {
        if (_uiState.value.isProcessing) return
        _uiState.value = _uiState.value.copy(isProcessing = true)
        viewModelScope.launch {
            _uiState.value = when (val result = userRepository.sendPasswordRecovery(user.email)) {
                PasswordRecoveryResult.Sent ->
                    UserManagementUiState(
                        feedback = "Correo de restablecimiento enviado a ${user.email}"
                    )
                is PasswordRecoveryResult.UnknownError ->
                    UserManagementUiState(feedback = "Error: ${result.message}")
            }
        }
    }

    fun consumeFeedback() {
        _uiState.value = _uiState.value.copy(feedback = null)
    }
}

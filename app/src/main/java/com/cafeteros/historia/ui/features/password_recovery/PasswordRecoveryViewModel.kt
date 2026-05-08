package com.cafeteros.historia.ui.features.password_recovery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Estado de las operaciones del flujo de recuperación. */
data class PasswordRecoveryUiState(
    val isCheckingEmail: Boolean = false,
    val isSavingPassword: Boolean = false,
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null
)

/**
 * ViewModel del flujo de recuperación de contraseña.
 *
 * Centraliza la lógica de:
 *  - Verificar que el correo exista antes de "enviar el código".
 *  - Actualizar la contraseña en la base de datos al final del flujo.
 *
 * El estado de los pasos visuales (qué pantalla está activa, código OTP)
 * vive en [PasswordRecoveryFlow] como antes; este ViewModel solo se
 * encarga de los efectos con la base de datos.
 */
class PasswordRecoveryViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as CafeterosApplication).userRepository

    private val _uiState = MutableStateFlow(PasswordRecoveryUiState())
    val uiState: StateFlow<PasswordRecoveryUiState> = _uiState.asStateFlow()

    /**
     * Verifica que el correo esté registrado antes de avanzar al paso 2.
     *
     * @param email correo escrito por el usuario.
     * @param onEmailFound callback cuando el correo existe — la UI debe avanzar.
     */
    fun checkEmailAndAdvance(email: String, onEmailFound: () -> Unit) {
        if (_uiState.value.isCheckingEmail) return

        _uiState.value = _uiState.value.copy(isCheckingEmail = true, emailErrorMessage = null)
        viewModelScope.launch {
            val exists = userRepository.emailExists(email)
            _uiState.value = if (exists) {
                onEmailFound()
                _uiState.value.copy(isCheckingEmail = false)
            } else {
                _uiState.value.copy(
                    isCheckingEmail = false,
                    emailErrorMessage = "No encontramos una cuenta con ese correo."
                )
            }
        }
    }

    /**
     * Actualiza la contraseña del usuario identificado por [email].
     *
     * @param onPasswordSaved callback cuando la actualización fue exitosa — la
     *  UI debe pasar a la pantalla de éxito.
     */
    fun savePassword(email: String, newPassword: String, onPasswordSaved: () -> Unit) {
        if (_uiState.value.isSavingPassword) return

        _uiState.value = _uiState.value.copy(isSavingPassword = true, passwordErrorMessage = null)
        viewModelScope.launch {
            val updated = userRepository.updatePassword(email, newPassword)
            _uiState.value = if (updated) {
                onPasswordSaved()
                _uiState.value.copy(isSavingPassword = false)
            } else {
                _uiState.value.copy(
                    isSavingPassword = false,
                    passwordErrorMessage = "No se pudo actualizar la contraseña."
                )
            }
        }
    }

    fun consumeEmailError() {
        _uiState.value = _uiState.value.copy(emailErrorMessage = null)
    }

    fun consumePasswordError() {
        _uiState.value = _uiState.value.copy(passwordErrorMessage = null)
    }
}

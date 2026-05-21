package com.cafeteros.historia.ui.features.password_recovery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.repository.PasswordRecoveryResult
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado UI del flujo de recuperación de contraseña con Firebase Auth.
 *
 * @param isSending true mientras Firebase procesa el envío del correo.
 * @param wasSent true cuando Firebase confirmó el envío; la UI avanza a la
 *  pantalla de "te enviamos un correo".
 * @param errorMessage error genérico (red, etc.) listo para mostrar.
 */
data class PasswordRecoveryUiState(
    val isSending: Boolean = false,
    val wasSent: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel del flujo de recuperación de contraseña.
 *
 * Con Firebase Auth no necesitamos OTP propio: Firebase envía un correo con
 * un link único y temporal al usuario para que restablezca su contraseña
 * directamente. Por eso este flujo se redujo de 4 pasos a 2:
 *
 *  1. El usuario escribe su correo.
 *  2. Mostramos "te enviamos un correo" con instrucciones.
 *
 * El "verificar OTP" y el "escribir nueva contraseña" desaparecen — los hace
 * la página oficial de Firebase a la que llega el link.
 */
class PasswordRecoveryViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as CafeterosApplication).userRepository

    private val _uiState = MutableStateFlow(PasswordRecoveryUiState())
    val uiState: StateFlow<PasswordRecoveryUiState> = _uiState.asStateFlow()

    /**
     * Envía el correo de recuperación al [email]. Por seguridad, Firebase no
     * revela si el correo está registrado o no — la respuesta es "exitosa"
     * incluso cuando el usuario no existe, para evitar enumeración de cuentas.
     */
    fun sendRecoveryEmail(email: String) {
        if (_uiState.value.isSending) return
        if (email.isBlank() || !email.contains("@")) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Escribe un correo válido."
            )
            return
        }
        _uiState.value = PasswordRecoveryUiState(isSending = true)
        viewModelScope.launch {
            _uiState.value = when (val result = userRepository.sendPasswordRecovery(email)) {
                PasswordRecoveryResult.Sent ->
                    PasswordRecoveryUiState(wasSent = true)
                is PasswordRecoveryResult.UnknownError ->
                    PasswordRecoveryUiState(errorMessage = result.message)
            }
        }
    }

    /** Resetea el flag de error después de mostrarlo (evita doble Toast). */
    fun consumeError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

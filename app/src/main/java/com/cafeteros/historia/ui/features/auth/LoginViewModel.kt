package com.cafeteros.historia.ui.features.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.local.preferences.SessionDataStore
import com.cafeteros.historia.data.repository.LoginResult
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado UI del flujo de Login.
 *
 * @param isSubmitting true mientras Firebase valida las credenciales.
 * @param errorMessage mensaje a mostrar al usuario; null si no hay error.
 * @param loggedInSuccessfully true cuando el login fue exitoso; la Activity
 *  observa este flag para navegar a Home.
 */
data class LoginUiState(
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val loggedInSuccessfully: Boolean = false
)

/**
 * Estado de las preferencias locales del login.
 *
 * @param lastEmail último correo con el que se ingresó en este dispositivo,
 *  para autocompletar el campo.
 * @param canUseBiometric true si el usuario ya activó explícitamente la
 *  biometría desde la pantalla de Configuración Y existe un email previo en
 *  el dispositivo. Cuando es true, la UI muestra el botón de huella en el
 *  Login; cuando es false, lo oculta.
 */
data class LoginPreferencesState(
    val lastEmail: String? = null,
    val canUseBiometric: Boolean = false
)

/**
 * ViewModel del Login.
 *
 * Valida credenciales contra Firebase Auth (vía [UserRepository]) y expone
 * además las preferencias locales para autocompletar el campo y decidir si
 * mostrar el botón de huella.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val sessionPreferences: SessionDataStore = app.sessionPreferences

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Preferencias del login: último email y si la biometría está habilitada.
     * El botón huella se muestra mientras haya credenciales cifradas
     * guardadas y biometría disponible — incluso después de un logout, para
     * que el usuario pueda volver a entrar con su huella.
     */
    val preferences: StateFlow<LoginPreferencesState> = combine(
        sessionPreferences.lastEmailFlow,
        sessionPreferences.biometricEnabledFlow
    ) { lastEmail, biometricEnabled ->
        LoginPreferencesState(
            lastEmail = lastEmail,
            canUseBiometric = userRepository.hasStoredCredentials()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LoginPreferencesState(
            canUseBiometric = userRepository.hasStoredCredentials()
        )
    )

    fun login(email: String, password: String) {
        if (_uiState.value.isSubmitting) return

        _uiState.value = LoginUiState(isSubmitting = true)
        viewModelScope.launch {
            val result = userRepository.login(email = email, plainPassword = password)
            _uiState.value = when (result) {
                is LoginResult.Success -> {
                    // Persistimos el correo (normalizado a minúsculas y sin
                    // espacios) para autocompletar el campo en próximos
                    // arranques. La activación de la huella ya NO es
                    // automática aquí: el usuario la habilita explícitamente
                    // desde la pantalla de Configuración tras un
                    // BiometricPrompt de verificación.
                    sessionPreferences.setLastEmail(email.trim().lowercase())
                    LoginUiState(loggedInSuccessfully = true)
                }
                LoginResult.UserNotFound ->
                    LoginUiState(errorMessage = "No encontramos una cuenta con ese correo.")
                LoginResult.WrongPassword ->
                    LoginUiState(errorMessage = "Contraseña incorrecta. Intenta de nuevo.")
                LoginResult.AccountDisabled ->
                    LoginUiState(errorMessage = "Esta cuenta está deshabilitada. Contacta al administrador.")
                is LoginResult.UnknownError ->
                    LoginUiState(errorMessage = "No pudimos iniciar sesión: ${result.message}")
            }
        }
    }

    /**
     * Indica si hay credenciales cifradas que permitan iniciar sesión con
     * huella. Distinto de "tener sesión Firebase activa" — esto perdura
     * incluso después de un logout, mientras el usuario no haya pedido
     * olvidar el dispositivo.
     */
    fun canLoginWithBiometric(): Boolean = userRepository.hasStoredCredentials()

    /**
     * Después de que la huella confirma la identidad, recupera las
     * credenciales cifradas y autentica contra Firebase. Si las credenciales
     * ya no son válidas (la contraseña cambió en otro dispositivo, por
     * ejemplo), publica un error apropiado.
     */
    fun loginWithBiometric() {
        if (_uiState.value.isSubmitting) return
        _uiState.value = LoginUiState(isSubmitting = true)
        viewModelScope.launch {
            val result = userRepository.loginWithStoredCredentials()
            _uiState.value = when (result) {
                is LoginResult.Success -> {
                    sessionPreferences.setBiometricEnabled(true)
                    LoginUiState(loggedInSuccessfully = true)
                }
                LoginResult.UserNotFound -> LoginUiState(
                    errorMessage = "No hay credenciales guardadas. Inicia con contraseña."
                )
                LoginResult.WrongPassword -> LoginUiState(
                    errorMessage = "Tu contraseña cambió desde otro dispositivo. Vuelve a iniciar con contraseña."
                )
                LoginResult.AccountDisabled -> LoginUiState(
                    errorMessage = "Esta cuenta está deshabilitada."
                )
                is LoginResult.UnknownError -> LoginUiState(
                    errorMessage = "No pudimos iniciar sesión: ${result.message}"
                )
            }
        }
    }

    fun consumeError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

package com.cafeteros.historia.ui.features.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.local.preferences.SessionDataStore
import com.cafeteros.historia.data.repository.RegisterResult
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.features.auth.components.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado UI del flujo de Registro.
 *
 * @param isSubmitting true mientras Firebase crea la cuenta.
 * @param errorMessage mensaje a mostrar al usuario; null si no hay error.
 * @param registeredSuccessfully true cuando la cuenta se creó y se inició sesión;
 *  la Activity observa este flag para navegar a Home.
 */
data class RegisterUiState(
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val registeredSuccessfully: Boolean = false
)

/**
 * ViewModel que orquesta el registro de un nuevo usuario.
 *
 * Llama a [UserRepository.register] (que detrás crea la cuenta en Firebase
 * Auth y el perfil en Firestore) y expone el resultado vía [uiState]. La
 * Activity solo dibuja la UI y reacciona al estado.
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository

    /**
     * Banderas locales del dispositivo. Aquí solo nos interesa guardar el
     * último email tras un registro exitoso, para que el próximo arranque de
     * la app autocomplete el campo de Login. La activación de huella se
     * maneja desde la pantalla de Configuración como un opt-in explícito.
     */
    private val sessionPreferences: SessionDataStore = app.sessionPreferences

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * Lanza el registro. Garantiza que solo haya un registro en curso a la vez.
     * Cuando termina, actualiza [uiState] con el resultado.
     */
    fun register(
        email: String,
        name: String,
        phone: String,
        password: String,
        userType: UserType
    ) {
        if (_uiState.value.isSubmitting) return

        _uiState.value = RegisterUiState(isSubmitting = true)
        viewModelScope.launch {
            val result = userRepository.register(
                email = email,
                name = name,
                phone = phone,
                plainPassword = password,
                userType = userType
            )
            _uiState.value = when (result) {
                is RegisterResult.Success -> {
                    // Tras crear la cuenta, Firebase Auth ya dejó al usuario
                    // con sesión activa. Persistimos el correo (normalizado)
                    // para que el próximo arranque autocomplete el campo de
                    // Login. NO activamos la biometría automáticamente: se
                    // habilita desde Configuración con verificación previa.
                    sessionPreferences.setLastEmail(email.trim().lowercase())
                    RegisterUiState(registeredSuccessfully = true)
                }
                RegisterResult.EmailAlreadyExists ->
                    RegisterUiState(errorMessage = "Ya existe una cuenta con ese correo. Inicia sesión.")
                RegisterResult.WeakPassword ->
                    RegisterUiState(errorMessage = "La contraseña debe tener al menos 6 caracteres.")
                RegisterResult.InvalidEmail ->
                    RegisterUiState(errorMessage = "El correo tiene un formato inválido.")
                is RegisterResult.UnknownError ->
                    RegisterUiState(errorMessage = "No pudimos crear tu cuenta: ${result.message}")
            }
        }
    }

    /** Resetea el flag de error después de mostrarlo (evita que se muestre dos veces). */
    fun consumeError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

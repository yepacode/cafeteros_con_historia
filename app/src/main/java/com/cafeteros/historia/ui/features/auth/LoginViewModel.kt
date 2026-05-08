package com.cafeteros.historia.ui.features.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.repository.LoginResult
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado UI del flujo de Login.
 *
 * @param isSubmitting true mientras se valida la contraseña.
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
 * ViewModel del Login.
 *
 * Valida credenciales contra el [UserRepository] y publica el resultado
 * vía [uiState]. La Activity solo se preocupa por dibujar y navegar.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as CafeterosApplication).userRepository

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (_uiState.value.isSubmitting) return

        _uiState.value = LoginUiState(isSubmitting = true)
        viewModelScope.launch {
            val result = userRepository.login(email = email, plainPassword = password)
            _uiState.value = when (result) {
                is LoginResult.Success -> LoginUiState(loggedInSuccessfully = true)
                is LoginResult.UserNotFound -> LoginUiState(
                    errorMessage = "No encontramos una cuenta con ese correo."
                )
                is LoginResult.WrongPassword -> LoginUiState(
                    errorMessage = "Contraseña incorrecta. Intenta de nuevo."
                )
            }
        }
    }

    fun consumeError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

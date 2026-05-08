package com.cafeteros.historia.ui.features.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
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
 * @param isSubmitting true mientras se ejecuta el insert en DB.
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
 * Recibe los datos del formulario, los pasa al [UserRepository] y expone el
 * resultado vía [uiState]. La Activity solo se preocupa por dibujar la UI y
 * reaccionar al estado.
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as CafeterosApplication).userRepository

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
                is RegisterResult.Success -> RegisterUiState(registeredSuccessfully = true)
                is RegisterResult.EmailAlreadyExists -> RegisterUiState(
                    errorMessage = "Ya existe una cuenta con ese correo. Inicia sesión."
                )
                is RegisterResult.UnknownError -> RegisterUiState(
                    errorMessage = "No pudimos crear tu cuenta. Intenta de nuevo."
                )
            }
        }
    }

    /** Resetea el flag de error después de mostrarlo (evita que se muestre dos veces). */
    fun consumeError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

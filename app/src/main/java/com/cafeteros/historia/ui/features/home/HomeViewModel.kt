package com.cafeteros.historia.ui.features.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado UI del Home: usuario logueado + nombre del rol resuelto desde la DB.
 *
 * @param user usuario logueado, o null si no hay sesión.
 * @param roleNameFromDb nombre del rol leído de la tabla `roles` mediante
 *  un JOIN implícito (FK). Útil como prueba visual de que la integridad
 *  referencial funciona.
 */
data class HomeUiState(
    val user: User? = null,
    val roleNameFromDb: String? = null
)

/**
 * ViewModel del Home.
 *
 * Combina dos fuentes:
 *  - El usuario logueado (vía `currentUserFlow` del repositorio).
 *  - El nombre del rol asociado a ese usuario, resuelto desde la tabla
 *    `roles` (no del enum) — confirmación de que el JOIN funciona.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as CafeterosApplication).userRepository

    private val _roleName = MutableStateFlow<String?>(null)

    /** Usuario actualmente logueado, o `null` si no hay sesión. */
    val currentUser: StateFlow<User?> = userRepository.currentUserFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
        initialValue = null
    )

    /** Nombre del rol del usuario activo, leído de la tabla `roles`. */
    val roleName: StateFlow<String?> = _roleName.asStateFlow()

    init {
        // Cuando cambie el usuario logueado, refresca también el nombre del rol.
        viewModelScope.launch {
            currentUser.collect { user ->
                _roleName.value = user?.let {
                    userRepository.getRoleName(it.userType.roleId)
                }
            }
        }
    }

    /** Cierra la sesión actual (no borra la cuenta). */
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}

package com.cafeteros.historia.ui.features.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado UI del Home: usuario logueado + nombre legible del rol.
 *
 * @param user usuario logueado, o null si no hay sesión.
 * @param roleName nombre del rol (Comprador / Caficultor / Administrador),
 *  derivado del enum del usuario para mostrar como insignia.
 */
data class HomeUiState(
    val user: User? = null,
    val roleName: String? = null
)

/**
 * ViewModel del Home.
 *
 * Observa el usuario logueado vía [UserRepository.currentUserFlow] (que a su
 * vez combina Firebase Auth + Firestore) y expone el nombre del rol asociado.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as CafeterosApplication).userRepository

    /** Usuario actualmente logueado, o `null` si no hay sesión. */
    val currentUser: StateFlow<User?> = userRepository.currentUserFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
        initialValue = null
    )

    /** Nombre del rol del usuario activo, listo para mostrar en la UI. */
    val roleName: StateFlow<String?> = userRepository.currentUserFlow
        .map { user -> user?.let { userRepository.getRoleName(it.userType.roleId) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = null
        )

    /** Cierra la sesión actual (no borra la cuenta). */
    fun logout() {
        viewModelScope.launch { userRepository.logout() }
    }
}

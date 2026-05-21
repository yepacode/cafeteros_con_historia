package com.cafeteros.historia.ui.features.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.data.repository.UserStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado UI del dashboard de Administrador.
 *
 * @property stats Snapshot de usuarios por rol; `null` mientras carga.
 * @property publishedProducts Conteo de productos publicados (mock por ahora).
 * @property monthlySalesCop Ventas estimadas del mes en pesos colombianos (mock).
 * @property pendingOrders Pedidos pendientes de procesar (mock).
 */
data class AdminDashboardUiState(
    val stats: UserStats? = null,
    val publishedProducts: Int = 12,
    val monthlySalesCop: Long = 4_850_000L,
    val pendingOrders: Int = 7,
    /** Cantidad de caficultores con cuenta esperando revisión del admin. */
    val pendingApprovalsCount: Int = 0
)

/**
 * ViewModel del dashboard del Administrador.
 *
 * Lee las estadísticas reales de usuarios desde Room vía [UserRepository.getUserStats].
 * Los KPIs de productos y ventas son mock por ahora — cuando exista backend o
 * persistencia de productos en Room, se reemplazan con queries reales.
 */
class AdminDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as CafeterosApplication).userRepository

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    /**
     * Flow reactivo del conteo de caficultores pendientes. Cuando un
     * nuevo caficultor se registra o el admin aprueba/rechaza uno,
     * la card del dashboard se actualiza sola.
     */
    private val pendingCountFlow: StateFlow<Int> =
        userRepository.observePendingApprovals()
            .map { it.size }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    init {
        refreshStats()
        // Mantener el contador reactivo sobre el state principal.
        viewModelScope.launch {
            pendingCountFlow.collect { count ->
                _uiState.value = _uiState.value.copy(pendingApprovalsCount = count)
            }
        }
    }

    /**
     * Recarga las estadísticas. Se invoca al entrar a la pantalla y se puede
     * volver a llamar tras una operación CRUD desde otra activity.
     */
    fun refreshStats() {
        viewModelScope.launch {
            val stats = userRepository.getUserStats()
            _uiState.value = _uiState.value.copy(stats = stats)
        }
    }

    /**
     * Cierra la sesión del administrador y dispara [onLoggedOut] cuando termina.
     * El callback está pensado para navegar a Login desde la Activity.
     */
    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            userRepository.logout()
            onLoggedOut()
        }
    }
}

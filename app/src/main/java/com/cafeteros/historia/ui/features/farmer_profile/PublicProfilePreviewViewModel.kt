package com.cafeteros.historia.ui.features.farmer_profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

/** Estado de la pantalla "Ver perfil como me ven los compradores". */
data class PublicPreviewUiState(
    val userName: String = "",
    val farm: FarmProfile = FarmProfile(),
    val products: List<Product> = emptyList()
)

/**
 * ViewModel de [PublicProfilePreviewActivity].
 *
 * Combina tres flujos para reproducir la vista que vería un comprador:
 *  - El usuario logueado (nombre real).
 *  - La finca del caficultor (con foto principal y todos sus datos).
 *  - Los productos publicados (para mostrarlos como catálogo en el perfil).
 *
 * Se re-emite cuando cualquiera cambia. Sin sesión activa, expone state
 * vacío sin crashear.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class PublicProfilePreviewViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val farmRepository: FarmRepository = app.farmRepository
    private val productRepository: ProductRepository = app.productRepository

    val uiState: StateFlow<PublicPreviewUiState> = userRepository.currentUserFlow
        .flatMapLatest { user ->
            if (user == null) {
                flowOf(PublicPreviewUiState())
            } else {
                combine(
                    farmRepository.observeMyFarm(user.id),
                    productRepository.observeMyProducts(user.id)
                ) { farm, products ->
                    PublicPreviewUiState(
                        userName = user.name,
                        farm = farm,
                        products = products
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PublicPreviewUiState()
        )
}

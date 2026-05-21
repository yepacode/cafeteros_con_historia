package com.cafeteros.historia.ui.features.farmer_profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

/**
 * Estado UI compactado de la pantalla "Mi perfil" del caficultor.
 *
 * @param fullName nombre completo del usuario logueado.
 * @param farmName nombre comercial de la finca (puede estar vacío si el
 *  caficultor aún no completó su perfil).
 * @param region región/departamento de la finca, formateado para el badge.
 * @param productCount cantidad de productos publicados por el caficultor.
 * @param completionPercent porcentaje de campos de finca llenos (medidor
 *  motivacional).
 */
data class MyProfileUiState(
    val fullName: String = "",
    val farmName: String = "",
    val region: String = "",
    val farmPhotoBase64: String? = null,
    val farmerPhotoBase64: String? = null,
    val productCount: Int = 0,
    val completionPercent: Int = 0
)

/**
 * ViewModel de [MyProfileActivity].
 *
 * Combina tres fuentes de datos del caficultor logueado:
 *  1. Su [com.cafeteros.historia.data.model.User] (nombre, email).
 *  2. Su [FarmProfile] (nombre de finca, región, completitud).
 *  3. Sus productos publicados (sólo para el contador del badge).
 *
 * Re-emite cuando cualquiera cambia. Si no hay sesión activa, expone un
 * estado vacío sin crashear.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MyProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val farmRepository: FarmRepository = app.farmRepository
    private val productRepository: ProductRepository = app.productRepository

    val uiState: StateFlow<MyProfileUiState> = userRepository.currentUserFlow
        .flatMapLatest { user ->
            if (user == null) {
                flowOf(MyProfileUiState())
            } else {
                combine(
                    farmRepository.observeMyFarm(user.id),
                    productRepository.observeMyProducts(user.id)
                ) { farm, products ->
                    MyProfileUiState(
                        fullName = user.name,
                        farmName = farm.name.ifBlank { "Completa tu finca" },
                        region = farm.region.ifBlank { "Sin región" },
                        farmPhotoBase64 = farm.principalPhotoBase64,
                        farmerPhotoBase64 = farm.farmerPhotoBase64,
                        productCount = products.size,
                        completionPercent = farm.completionPercent()
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MyProfileUiState()
        )

    /** Snapshot puntual del state (útil para componentes one-shot). */
    @Suppress("unused")
    fun currentFarmName(): String = uiState.value.farmName

    /** Acceso a la última versión leída del flow (para llamados síncronos). */
    @Suppress("unused")
    fun currentSnapshotFor(default: FarmProfile = FarmProfile()): FarmProfile = default
}

package com.cafeteros.historia.ui.features.explore

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.features.explore.model.CaficultorBadge
import com.cafeteros.historia.ui.features.explore.model.CoffeeProduct
import com.cafeteros.historia.ui.features.explore.model.CoffeeRegion
import com.cafeteros.historia.ui.features.explore.model.FeaturedCaficultor
import com.cafeteros.historia.ui.theme.BrandColors
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

/**
 * Estado UI de la pantalla Explore. Replaza los datos mock que el
 * Composable usaba como default y los mapea desde Firestore.
 *
 * @param userFirstName primer nombre del comprador (saludo del greeting).
 * @param regions zonas únicas extraídas de los FarmProfile, con conteo.
 * @param caficultores top de fincas para "Caficultores destacados".
 * @param popularProducts productos activos para "Lo más pedido esta semana".
 */
data class ExploreUiState(
    val userFirstName: String = "",
    val regions: List<CoffeeRegion> = emptyList(),
    val caficultores: List<FeaturedCaficultor> = emptyList(),
    val popularProducts: List<CoffeeProduct> = emptyList()
)

/**
 * ViewModel del home del comprador.
 *
 * Observa tres fuentes y mapea cada una al modelo visual que ya consume
 * ExploreScreen (CoffeeRegion / FeaturedCaficultor / CoffeeProduct).
 * Mantener los modelos mock evita rehacer las cards y respeta el diseño
 * original — solo cambian las fuentes de datos.
 *
 * **Por qué no usar Story:** las "Historias que enamoran" siguen como
 * mock por ahora porque no tenemos modelo `/stories` en Firestore. Es
 * un nice-to-have del MVP.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ExploreViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val farmRepository: FarmRepository = app.farmRepository
    private val productRepository: ProductRepository = app.productRepository

    val uiState: StateFlow<ExploreUiState> = userRepository.currentUserFlow
        .flatMapLatest { user ->
            combine(
                farmRepository.observeAllFarms(),
                productRepository.observeActive()
            ) { farms, products ->
                ExploreUiState(
                    userFirstName = user?.name?.trim()
                        ?.takeIf { it.isNotBlank() }
                        ?.substringBefore(' ')
                        .orEmpty(),
                    regions = farms.toRegionCards(),
                    caficultores = farms.toFeaturedCards(),
                    popularProducts = products.toProductCards()
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ExploreUiState()
        )

    /**
     * Agrupa las fincas por región y produce una card por cada región
     * con el conteo de caficultores. Las regiones vacías ("Sin región")
     * se filtran. Asigna colores rotando por la paleta de zonas.
     */
    private fun List<FarmProfile>.toRegionCards(): List<CoffeeRegion> {
        return groupBy { it.region.trim().ifBlank { "—" } }
            .filterKeys { it != "—" }
            .map { (region, list) ->
                CoffeeRegion(
                    name = region,
                    caficultorCount = list.size,
                    imageRes = null,
                    placeholderColor = regionColorFor(region),
                    regionKey = region
                )
            }
            .sortedByDescending { it.caficultorCount }
    }

    private fun List<FarmProfile>.toFeaturedCards(): List<FeaturedCaficultor> {
        return filter { it.name.isNotBlank() }
            .take(8)
            .map { farm ->
                FeaturedCaficultor(
                    farmName = farm.name,
                    location = farm.region.ifBlank { "Colombia" },
                    rating = 5.0,
                    reviewCount = 0,
                    badge = CaficultorBadge.ORGANICO,
                    avatarRes = null,
                    avatarPlaceholderColor = BrandColors.FarmerPrimary,
                    caficultorUid = farm.caficultorUid,
                    avatarBase64 = farm.farmerPhotoBase64
                )
            }
    }

    private fun List<Product>.toProductCards(): List<CoffeeProduct> {
        return take(12).map { product ->
            CoffeeProduct(
                name = product.name,
                farmName = "", // se rellenaría con join al FarmProfile si se quiere
                formattedPrice = "$" + "%,d".format(product.priceCop).replace(',', '.'),
                imageRes = null,
                placeholderColor = BrandColors.CoffeeBrown,
                productId = product.id,
                imageBase64 = product.imageBase64
            )
        }
    }

    /** Asigna un color de la paleta de zonas cafeteras a cada región. */
    private fun regionColorFor(region: String): Color = when (region.lowercase()) {
        "huila" -> BrandColors.ZoneHuila
        "santander" -> BrandColors.ZoneSantander
        "nariño", "narino" -> BrandColors.ZoneSierraNevada
        "antioquia", "eje cafetero", "quindío", "risaralda", "caldas" -> BrandColors.ZoneEjeCafetero
        "cundinamarca", "boyacá", "boyaca" -> BrandColors.ZoneCundinamarca
        else -> BrandColors.CoffeeBrown
    }
}

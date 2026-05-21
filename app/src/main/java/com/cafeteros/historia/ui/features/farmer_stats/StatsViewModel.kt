package com.cafeteros.historia.ui.features.farmer_stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Resumen estadístico que mostramos en el panel del caficultor mientras
 * no exista el módulo de pedidos. Cuando llegue, este state se enriquecerá
 * con `orderCount`, `revenueCop`, `ratingAverage`, etc.
 *
 * @param inventoryValueCop suma de `precio × stock` entre productos activos
 *  (no pausados). Es el "valor en bodega".
 * @param activeProductCount cantidad de productos publicados y activos.
 * @param pausedProductCount cantidad de productos pausados (visible en
 *  inventario pero no en el catálogo del comprador).
 * @param outOfStockCount cantidad de productos activos con stock = 0.
 * @param totalStockUnits suma de unidades en stock entre los activos.
 * @param topByValue top 4 productos ordenados por (precio × stock)
 *  descendente. Se muestran como ranking en la sección "Productos destacados".
 */
data class StatsUiState(
    val inventoryValueCop: Long = 0L,
    val activeProductCount: Int = 0,
    val pausedProductCount: Int = 0,
    val outOfStockCount: Int = 0,
    val totalStockUnits: Int = 0,
    val topByValue: List<Product> = emptyList()
)

/**
 * ViewModel de [StatsActivity]. Mientras no haya pedidos reales en
 * Firestore, las métricas que aquí se exponen son las del **inventario**
 * actual del caficultor — siguen siendo útiles para que la pantalla
 * muestre algo real al usuario.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val productRepository: ProductRepository = app.productRepository

    val uiState: StateFlow<StatsUiState> = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else productRepository.observeMyProducts(uid)
        }
        .map { products ->
            val active = products.filter { !it.isPaused }
            val paused = products.size - active.size
            val outOfStock = active.count { it.stockUnits == 0 }
            val totalStock = active.sumOf { it.stockUnits }
            val totalValue = active.sumOf { it.priceCop.toLong() * it.stockUnits }
            val top = active
                .sortedByDescending { it.priceCop.toLong() * it.stockUnits }
                .take(4)
            StatsUiState(
                inventoryValueCop = totalValue,
                activeProductCount = active.size,
                pausedProductCount = paused,
                outOfStockCount = outOfStock,
                totalStockUnits = totalStock,
                topByValue = top
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StatsUiState()
        )
}

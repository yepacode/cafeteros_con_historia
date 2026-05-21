package com.cafeteros.historia.ui.features.farmer_home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.OrderRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

/**
 * Umbral por debajo del cual un producto se considera "stock bajo" en el
 * panel del caficultor. Idéntico al usado en la pantalla de Inventario.
 */
private const val LOW_STOCK_THRESHOLD: Int = 10

/**
 * Estado UI del panel principal del caficultor.
 *
 * @param firstName primer nombre del caficultor para el saludo del header.
 * @param farmName nombre comercial de la finca (vacío si no se completó).
 * @param activeProductsCount cantidad de productos NO pausados.
 * @param totalStockUnits suma de unidades en stock entre todos los productos.
 * @param inventoryValueCop valor total del inventario: Σ(precio × stock).
 * @param lowStock productos con stock entre 1 y [LOW_STOCK_THRESHOLD]
 *  (excluye agotados).
 * @param outOfStockCount productos con `stockUnits == 0`.
 */
data class FarmerHomeUiState(
    val firstName: String = "",
    val farmName: String = "",
    val activeProductsCount: Int = 0,
    val totalStockUnits: Int = 0,
    val inventoryValueCop: Long = 0L,
    val lowStock: List<Product> = emptyList(),
    val outOfStockCount: Int = 0,
    /**
     * Pedidos que aún no se entregaron ni cancelaron. Se muestran en la
     * sección "Pedidos por empacar" del home para que el caficultor vea
     * de un vistazo qué le falta procesar.
     */
    val pendingOrders: List<Order> = emptyList(),
    /**
     * Ingreso semanal acumulado: suma de `totalCop` de orders entregadas
     * en los últimos 7 días. Útil para el gráfico de ventas y el resumen.
     */
    val weekRevenueCop: Long = 0L
)

/**
 * ViewModel del panel principal del caficultor (`FarmerHomeScreen`).
 *
 * Combina User + FarmProfile + lista de productos para calcular las métricas
 * "vivas" que se muestran en el panel (resumen del día, productos por
 * reabastecer, etc.). Las secciones que dependen de Orders (ventas semanales,
 * pedidos por empacar, billetera) siguen con mock hasta que exista el
 * módulo de pedidos.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class FarmerHomeViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val farmRepository: FarmRepository = app.farmRepository
    private val productRepository: ProductRepository = app.productRepository
    private val orderRepository: OrderRepository = app.orderRepository

    val uiState: StateFlow<FarmerHomeUiState> = userRepository.currentUserFlow
        .flatMapLatest { user ->
            if (user == null) {
                flowOf(FarmerHomeUiState())
            } else {
                combine(
                    farmRepository.observeMyFarm(user.id),
                    productRepository.observeMyProducts(user.id),
                    orderRepository.observeMySales(user.id)
                ) { farm, products, orders ->
                    val active = products.filter { !it.isPaused }
                    val lowStockList = active.filter { it.stockUnits in 1..LOW_STOCK_THRESHOLD }
                    val outOfStock = active.count { it.stockUnits == 0 }
                    val totalUnits = active.sumOf { it.stockUnits }
                    val totalValue = active.sumOf { it.priceCop.toLong() * it.stockUnits }

                    // Pedidos pendientes de procesar (todo lo que no está
                    // entregado ni cancelado se considera "por empacar").
                    val pendingStatuses = setOf(
                        OrderStatus.PENDING,
                        OrderStatus.ACCEPTED,
                        OrderStatus.PACKED
                    )
                    val pending = orders.filter { it.status in pendingStatuses }

                    // Ingreso semanal: orders entregadas en los últimos 7 días.
                    val sevenDaysAgo = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000
                    val weekRevenue = orders
                        .filter { it.status == OrderStatus.DELIVERED }
                        .filter { it.createdAtEpochMillis >= sevenDaysAgo }
                        .sumOf { it.totalCop.toLong() }

                    FarmerHomeUiState(
                        firstName = user.name.trim()
                            .takeIf { it.isNotBlank() }
                            ?.substringBefore(' ')
                            .orEmpty(),
                        farmName = farm.name,
                        activeProductsCount = active.size,
                        totalStockUnits = totalUnits,
                        inventoryValueCop = totalValue,
                        lowStock = lowStockList,
                        outOfStockCount = outOfStock,
                        pendingOrders = pending,
                        weekRevenueCop = weekRevenue
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FarmerHomeUiState()
        )
}

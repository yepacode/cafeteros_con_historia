package com.cafeteros.historia.ui.features.productdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.local.cart.CartStore
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderItem
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.NotificationRepository
import com.cafeteros.historia.data.repository.OrderOperationResult
import com.cafeteros.historia.data.repository.OrderRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado UI del detalle del producto.
 *
 * @param product el producto cargado desde Firestore; null mientras carga.
 * @param farm perfil de la finca del caficultor dueño; null mientras carga
 *  o si no completó su perfil.
 * @param caficultorName nombre del caficultor para mostrar.
 * @param quantity cantidad seleccionada para comprar/agregar al carrito.
 * @param outcome resultado de la última acción del usuario; usado para
 *  mostrar feedback (toast).
 */
data class ProductDetailUiState(
    val product: Product? = null,
    val farm: FarmProfile? = null,
    val caficultorName: String = "",
    val quantity: Int = 1,
    val outcome: ActionOutcome? = null
)

sealed class ActionOutcome {
    data class AddedToCart(val productName: String) : ActionOutcome()
    data class OrderPlaced(val orderId: String) : ActionOutcome()
    data class Error(val message: String) : ActionOutcome()
}

/**
 * ViewModel de la pantalla de detalle del producto desde la vista del
 * comprador. Carga el producto y el perfil del caficultor en paralelo.
 *
 * Soporta dos acciones:
 *  - **Agregar al carrito**: agrega `quantity` unidades al [CartStore].
 *  - **Comprar ahora**: bypasea el carrito, crea una `Order` directa con
 *    `quantity` unidades. Útil para compras impulsivas de 1-2 unidades.
 */
class ProductDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val productRepository: ProductRepository = app.productRepository
    private val farmRepository: FarmRepository = app.farmRepository
    private val userRepository: UserRepository = app.userRepository
    private val orderRepository: OrderRepository = app.orderRepository
    private val notificationRepository: NotificationRepository = app.notificationRepository

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    /** Carga el producto y el perfil del caficultor dueño. */
    fun load(productId: String) {
        if (productId.isBlank()) return
        viewModelScope.launch {
            val product = productRepository.findById(productId)
            if (product == null) {
                _uiState.value = _uiState.value.copy(
                    outcome = ActionOutcome.Error("No encontramos este producto.")
                )
                return@launch
            }
            val farm = farmRepository.findByCaficultor(product.caficultorUid)
            _uiState.value = _uiState.value.copy(
                product = product,
                farm = farm,
                caficultorName = farm?.name?.takeIf { it.isNotBlank() }
                    ?: "Caficultor"
            )
        }
    }

    fun setQuantity(value: Int) {
        val p = _uiState.value.product ?: return
        val clamped = value.coerceIn(1, p.stockUnits.coerceAtLeast(1))
        _uiState.value = _uiState.value.copy(quantity = clamped)
    }

    /** Agrega N unidades al carrito local. */
    fun addToCart() {
        val state = _uiState.value
        val product = state.product ?: return
        CartStore.add(productId = product.id, quantity = state.quantity)
        _uiState.value = state.copy(
            outcome = ActionOutcome.AddedToCart(product.name)
        )
    }

    /**
     * Compra inmediata: crea una Order con las N unidades del producto.
     * No pasa por el carrito; el comprador queda con un toast y el
     * pedido aparece directo en "Mis pedidos".
     */
    fun buyNow() {
        val state = _uiState.value
        val product = state.product ?: return

        viewModelScope.launch {
            val buyer = userRepository.getCurrentUser()
            if (buyer == null) {
                _uiState.value = state.copy(
                    outcome = ActionOutcome.Error("Necesitas iniciar sesión.")
                )
                return@launch
            }
            if (product.stockUnits < state.quantity) {
                _uiState.value = state.copy(
                    outcome = ActionOutcome.Error("Stock insuficiente. Solo quedan ${product.stockUnits}.")
                )
                return@launch
            }
            val item = OrderItem(
                productId = product.id,
                productName = product.name,
                unitPriceCop = product.priceCop,
                quantity = state.quantity,
                caficultorUid = product.caficultorUid
            )
            val order = Order(
                compradorUid = buyer.id,
                caficultorUid = product.caficultorUid,
                compradorName = buyer.name,
                items = listOf(item),
                totalCop = item.subtotalCop,
                shippingCop = 0,
                shippingAddress = "Por confirmar",
                status = OrderStatus.PENDING
            )
            val result = orderRepository.createOrder(order)
            _uiState.value = state.copy(
                outcome = when (result) {
                    is OrderOperationResult.Success -> {
                        notificationRepository.notifyNewOrder(
                            caficultorUid = product.caficultorUid,
                            buyerName = buyer.name,
                            orderId = result.orderId,
                            totalCop = order.totalCop
                        )
                        ActionOutcome.OrderPlaced(result.orderId)
                    }
                    is OrderOperationResult.Error -> ActionOutcome.Error(result.message)
                }
            )
        }
    }

    fun consumeOutcome() {
        _uiState.value = _uiState.value.copy(outcome = null)
    }
}

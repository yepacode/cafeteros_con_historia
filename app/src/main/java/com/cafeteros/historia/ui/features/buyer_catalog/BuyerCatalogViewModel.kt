package com.cafeteros.historia.ui.features.buyer_catalog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderItem
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.NotificationRepository
import com.cafeteros.historia.data.repository.OrderOperationResult
import com.cafeteros.historia.data.repository.OrderRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel del catálogo del comprador.
 *
 * Observa todos los productos activos (no pausados) de todos los caficultores
 * y permite al comprador "comprar 1" directamente — crea una `Order` en
 * Firestore al instante con quantity=1 y status PENDING.
 *
 * **MVP simplificado**: no usa carrito multi-producto ni checkout multi-paso.
 * Si en el futuro se quiere multi-item, se reemplaza este flow con la UI
 * existente de `CartActivity` + `OrderReviewActivity` que llamará a este
 * mismo `OrderRepository.createOrder`.
 */
class BuyerCatalogViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val productRepository: ProductRepository = app.productRepository
    private val orderRepository: OrderRepository = app.orderRepository
    private val userRepository: UserRepository = app.userRepository
    private val notificationRepository: NotificationRepository = app.notificationRepository

    val products: StateFlow<List<Product>> = productRepository.observeActive()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast

    private val _isBuying = MutableStateFlow(false)
    val isBuying: StateFlow<Boolean> = _isBuying

    /**
     * Crea un pedido con una sola línea: 1 unidad del [product]. El
     * comprador es el usuario logueado; el caficultor es el dueño del
     * producto. La dirección queda vacía en MVP — se llenará cuando se
     * conecte la libreta de direcciones.
     */
    fun buyOne(product: Product) {
        if (_isBuying.value) return
        _isBuying.value = true

        viewModelScope.launch {
            val buyer = userRepository.getCurrentUser()
            if (buyer == null) {
                _isBuying.value = false
                _toast.value = "Necesitas iniciar sesión para comprar."
                return@launch
            }
            if (product.stockUnits <= 0) {
                _isBuying.value = false
                _toast.value = "Este producto está agotado."
                return@launch
            }
            val item = OrderItem(
                productId = product.id,
                productName = product.name,
                unitPriceCop = product.priceCop,
                quantity = 1,
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
            _isBuying.value = false
            _toast.value = when (result) {
                is OrderOperationResult.Success -> {
                    // Notificar al caficultor del nuevo pedido. Si falla
                    // (red caída, etc.) no abortamos — la venta ya está.
                    notificationRepository.notifyNewOrder(
                        caficultorUid = product.caficultorUid,
                        buyerName = buyer.name,
                        orderId = result.orderId,
                        totalCop = order.totalCop
                    )
                    "¡Pedido realizado! El caficultor lo verá en sus ventas."
                }
                is OrderOperationResult.Error ->
                    "No pudimos crear el pedido: ${result.message}"
            }
        }
    }

    fun consumeToast() {
        _toast.value = null
    }
}

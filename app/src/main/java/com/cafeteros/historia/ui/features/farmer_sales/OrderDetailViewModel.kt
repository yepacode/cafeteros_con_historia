package com.cafeteros.historia.ui.features.farmer_sales

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.data.repository.NotificationRepository
import com.cafeteros.historia.data.repository.OrderOperationResult
import com.cafeteros.historia.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel del detalle de un pedido.
 *
 * Carga el pedido por id desde Firestore al inicializarse y permite al
 * caficultor avanzar su estado (PENDING → ACCEPTED → PACKED → SHIPPED →
 * DELIVERED) o cancelarlo. Cada cambio persiste y refresca el state.
 *
 * **Nota:** podríamos usar el flow reactivo `observeMySales().filter`
 * pero como esta pantalla es para UN pedido específico, una lectura
 * one-shot con refresh manual es más simple y barato.
 */
class OrderDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val orderRepository: OrderRepository =
        (application as CafeterosApplication).orderRepository
    private val notificationRepository: NotificationRepository =
        (application as CafeterosApplication).notificationRepository

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating.asStateFlow()

    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast.asStateFlow()

    /**
     * Carga el pedido [orderId]. La Activity llama esto en onCreate con el
     * id que viene del intent extra. Idempotente: re-llamar con el mismo
     * id refresca el state.
     */
    fun load(orderId: String) {
        viewModelScope.launch {
            _order.value = orderRepository.findById(orderId)
        }
    }

    /**
     * Cambia el estado del pedido al siguiente en la línea de tiempo y
     * persiste en Firestore. Después refresca el state local para que la
     * UI muestre el cambio inmediatamente.
     */
    fun changeStatus(newStatus: OrderStatus) {
        val current = _order.value ?: return
        if (_isUpdating.value) return
        _isUpdating.value = true
        viewModelScope.launch {
            val result = orderRepository.updateStatus(current.id, newStatus)
            _isUpdating.value = false
            when (result) {
                is OrderOperationResult.Success -> {
                    _order.value = current.copy(status = newStatus)
                    _toast.value = "Pedido marcado como ${newStatus.label.lowercase()}"
                    // Avisar al comprador del cambio de estado.
                    notificationRepository.notifyStatusChanged(
                        buyerUid = current.compradorUid,
                        orderId = current.id,
                        newStatusLabel = newStatus.label.lowercase()
                    )
                }
                is OrderOperationResult.Error -> {
                    _toast.value = result.message
                }
            }
        }
    }

    fun consumeToast() {
        _toast.value = null
    }
}

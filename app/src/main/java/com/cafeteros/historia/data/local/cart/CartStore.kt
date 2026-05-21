package com.cafeteros.historia.data.local.cart

import com.cafeteros.historia.data.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Carrito de compras del comprador, vive solo en memoria de la app.
 *
 * Es un `object` singleton (no requiere inyección) y expone un
 * [StateFlow] reactivo para que cualquier pantalla pueda observar los
 * cambios sin acoplarse a la implementación.
 *
 * **Por qué no persistir:**
 *  - Un carrito típico tiene 1-3 productos; perder eso al cerrar la app
 *    es aceptable para el MVP.
 *  - Persistir en Firestore inflaría costo de lecturas (el carrito cambia
 *    mucho al ajustar cantidades).
 *  - DataStore local sería una alternativa razonable a futuro, pero no
 *    bloquea el flujo de compra inicial.
 *
 * Al confirmar el pago, [items] se transforma en `Order.items` y se
 * persiste como pedido completo en `/orders/{orderId}`.
 */
object CartStore {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    /** Cuántas líneas (productos distintos) tiene el carrito. */
    val lineCount: Int get() = _items.value.size

    /** Total de unidades sumando todas las líneas. */
    val totalUnits: Int get() = _items.value.sumOf { it.quantity }

    /**
     * Agrega [productId] al carrito. Si el producto ya está, suma la
     * cantidad nueva a la existente; si no, crea una línea nueva.
     */
    fun add(productId: String, quantity: Int = 1) {
        val current = _items.value
        val existing = current.firstOrNull { it.productId == productId }
        _items.value = if (existing != null) {
            current.map {
                if (it.productId == productId) it.copy(quantity = it.quantity + quantity)
                else it
            }
        } else {
            current + CartItem(productId = productId, quantity = quantity)
        }
    }

    /** Reemplaza la cantidad de un producto. Si llega a 0, lo elimina. */
    fun setQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            remove(productId)
            return
        }
        _items.value = _items.value.map {
            if (it.productId == productId) it.copy(quantity = quantity) else it
        }
    }

    /** Quita una línea entera del carrito. */
    fun remove(productId: String) {
        _items.value = _items.value.filterNot { it.productId == productId }
    }

    /** Limpia el carrito tras un checkout exitoso o un "vaciar carrito". */
    fun clear() {
        _items.value = emptyList()
    }
}

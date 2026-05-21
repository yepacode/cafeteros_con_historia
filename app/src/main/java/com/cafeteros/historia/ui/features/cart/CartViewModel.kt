package com.cafeteros.historia.ui.features.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.local.cart.CartStore
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Línea del carrito ya resuelta con los datos del producto.
 *
 * El [CartStore] solo guarda `productId` y `quantity`; el carrito real
 * que ve el comprador necesita además nombre, foto, precio, etc. Esta
 * data class los une.
 *
 * @property product producto correspondiente al productId. Si el producto
 *  fue eliminado de Firestore desde que se agregó al carrito, será null
 *  y la UI puede ofrecer "remover" la línea.
 * @property quantity unidades que el comprador quiere.
 */
data class CartLine(
    val productId: String,
    val product: Product?,
    val quantity: Int
) {
    /** Subtotal de la línea (precio × qty). 0 si el producto desapareció. */
    val subtotalCop: Int get() = (product?.priceCop ?: 0) * quantity
}

data class CartUiState(
    val lines: List<CartLine> = emptyList(),
    val totalCop: Long = 0L
)

/**
 * ViewModel del carrito del comprador.
 *
 * El carrito real (lo que el usuario quiere comprar) vive en
 * [CartStore] como singleton en memoria. Pero para mostrar foto/nombre/
 * precio, hay que resolver cada `productId` con el [ProductRepository].
 *
 * Estrategia: observamos `CartStore.items`. Cada vez que cambia, hacemos
 * un fetch puntual (`findById`) por cada productId distinto y guardamos
 * el resultado para que la lista de UI tenga todo lo necesario.
 *
 * Esto es más simple que mantener un Flow combinado de carrito + N
 * productos, y para los volúmenes esperados (carrito con 1-5 items) la
 * diferencia es imperceptible.
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository =
        (application as CafeterosApplication).productRepository

    /** Cache local de productos resueltos por id. */
    private val productCache = mutableMapOf<String, Product?>()

    val uiState: StateFlow<CartUiState> = CartStore.items
        .map { items ->
            // Resolver productos faltantes en el cache.
            items.forEach { item ->
                if (item.productId !in productCache) {
                    productCache[item.productId] = productRepository.findById(item.productId)
                }
            }
            val lines = items.map { item ->
                CartLine(
                    productId = item.productId,
                    product = productCache[item.productId],
                    quantity = item.quantity
                )
            }
            CartUiState(
                lines = lines,
                totalCop = lines.sumOf { it.subtotalCop.toLong() }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CartUiState()
        )

    fun incQuantity(productId: String) {
        val line = uiState.value.lines.firstOrNull { it.productId == productId } ?: return
        val max = line.product?.stockUnits ?: Int.MAX_VALUE
        if (line.quantity < max) {
            CartStore.setQuantity(productId, line.quantity + 1)
        }
    }

    fun decQuantity(productId: String) {
        val line = uiState.value.lines.firstOrNull { it.productId == productId } ?: return
        CartStore.setQuantity(productId, line.quantity - 1)
    }

    fun remove(productId: String) {
        CartStore.remove(productId)
    }

    fun clearCart() {
        CartStore.clear()
    }
}

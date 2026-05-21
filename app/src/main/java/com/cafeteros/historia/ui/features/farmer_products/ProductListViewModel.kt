package com.cafeteros.historia.ui.features.farmer_products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla "Mis Cosechas" (lista de productos del caficultor).
 *
 * Observa reactivamente los productos del usuario logueado a través del
 * [ProductRepository]. Cualquier cambio en Firestore (crear, editar, pausar,
 * eliminar) se refleja automáticamente sin recargar la pantalla.
 *
 * **Borrado y pausa**: además de exponer la lista, ofrece acciones para
 * eliminar o pausar/activar un producto desde la propia lista (a través de
 * un long-press o menú contextual a futuro).
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val productRepository: ProductRepository = app.productRepository
    private val userRepository: UserRepository = app.userRepository

    /**
     * Flujo de los productos del caficultor logueado. Si no hay sesión activa
     * (caso defensivo: la UI no debería abrir esta pantalla sin login),
     * emite lista vacía.
     */
    val products: StateFlow<List<Product>> = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else productRepository.observeMyProducts(uid)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /** Mensaje transitorio para el usuario (toast). null cuando no hay nada que mostrar. */
    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast

    /** Pausa o reactiva el producto. */
    fun togglePaused(product: Product) {
        viewModelScope.launch {
            runCatching {
                productRepository.setPaused(product.id, !product.isPaused)
            }.onFailure {
                _toast.value = "No se pudo actualizar el producto"
            }
        }
    }

    /** Elimina el producto del catálogo. */
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            val result = productRepository.deleteProduct(product.id)
            _toast.value = when (result) {
                is com.cafeteros.historia.data.repository.ProductOperationResult.Success ->
                    "Producto eliminado"
                is com.cafeteros.historia.data.repository.ProductOperationResult.Error ->
                    result.message
            }
        }
    }

    /** Resetea el toast tras mostrarlo (evita repetirlo en recomposiciones). */
    fun consumeToast() {
        _toast.value = null
    }
}

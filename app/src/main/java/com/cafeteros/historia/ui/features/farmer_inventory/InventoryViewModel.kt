package com.cafeteros.historia.ui.features.farmer_inventory

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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de [InventoryActivity]. Su única responsabilidad es exponer
 * los productos del caficultor logueado y propagar las modificaciones de
 * stock al [ProductRepository].
 *
 * Comparte la fuente de datos con [com.cafeteros.historia.ui.features.farmer_products.ProductListViewModel];
 * se mantienen separados porque cada pantalla tiene reglas de UI distintas
 * (filtros de inventario, alertas, etc.) y porque los ciclos de vida son
 * independientes.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val productRepository: ProductRepository = app.productRepository
    private val userRepository: UserRepository = app.userRepository

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

    /**
     * Persiste el nuevo stock en Firestore. El valor se "satura" en cero
     * (no se permite stock negativo) dentro del repositorio.
     */
    fun updateStock(productId: String, newStock: Int) {
        viewModelScope.launch {
            productRepository.updateStock(productId, newStock)
        }
    }
}

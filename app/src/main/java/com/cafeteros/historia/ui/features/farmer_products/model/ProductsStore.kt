package com.cafeteros.historia.ui.features.farmer_products.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Store en memoria de los productos del caficultor (singleton).
 *
 * Esta es una solución temporal mientras no exista tabla `products` en Room
 * ni endpoint de backend. Vive durante la sesión del proceso de la app y se
 * pierde al matar el proceso — es suficiente para que el caficultor cree
 * productos, los vea en la lista y los edite durante el demo.
 *
 * Cuando se conecte el backend, esta clase se reemplaza por un repositorio
 * con [androidx.room.Dao] o llamadas HTTP, manteniendo la misma interfaz
 * pública (`productsFlow`, `add`, `update`, `delete`, `findById`) para que
 * los ViewModels no cambien.
 */
object ProductsStore {

    private val _productsFlow = MutableStateFlow<List<Product>>(emptyList())
    val productsFlow: StateFlow<List<Product>> = _productsFlow.asStateFlow()

    /** Inserta un producto nuevo al inicio de la lista. */
    fun add(product: Product) {
        _productsFlow.value = listOf(product) + _productsFlow.value
    }

    /**
     * Reemplaza un producto existente identificado por su `id`. Si no existe
     * ningún producto con ese id, no hace nada (deja la lista intacta).
     */
    fun update(updated: Product) {
        _productsFlow.value = _productsFlow.value.map { existing ->
            if (existing.id == updated.id) updated else existing
        }
    }

    /** Elimina un producto por id. */
    fun delete(productId: String) {
        _productsFlow.value = _productsFlow.value.filter { it.id != productId }
    }

    /** Busca un producto por id. Útil al abrir la pantalla de edición. */
    fun findById(productId: String): Product? =
        _productsFlow.value.firstOrNull { it.id == productId }
}

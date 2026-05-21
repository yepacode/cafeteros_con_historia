package com.cafeteros.historia.data.repository

import android.content.Context
import android.net.Uri
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.remote.firestore.ProductsRemoteDataSource
import com.cafeteros.historia.data.util.ImageBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/** Resultado expuesto al ViewModel tras crear/actualizar un producto. */
sealed class ProductOperationResult {
    /** Operación exitosa; opcionalmente devuelve el id del documento. */
    data class Success(val productId: String) : ProductOperationResult()

    /** Error genérico con mensaje legible para mostrar al usuario. */
    data class Error(val message: String) : ProductOperationResult()
}

/**
 * Punto único de acceso a la colección de productos.
 *
 * Orquesta:
 *  - [ProductsRemoteDataSource] (operaciones contra Firestore).
 *  - [ImageBase64] (compresión y codificación de la foto antes de persistir).
 *
 * Los ViewModels solo conocen este repositorio; ni Firestore ni la
 * compresión están expuestos directamente a la capa UI.
 */
class ProductRepository(
    private val productsRemote: ProductsRemoteDataSource,
    private val appContext: Context
) {

    /**
     * Crea un producto nuevo. Si [photoUri] no es null, comprime la imagen
     * y la incrusta como Base64 en el documento. El [caficultorUid] debe
     * ser el uid del usuario logueado (lo pone la capa de UI al construir
     * el [Product]).
     */
    suspend fun createProduct(
        product: Product,
        photoUri: Uri?
    ): ProductOperationResult = withContext(Dispatchers.IO) {
        runCatching {
            val withImage = product.copy(
                imageBase64 = photoUri?.let { ImageBase64.encodeFromUri(appContext, it) }
                    ?: product.imageBase64
            )
            val id = productsRemote.create(withImage)
            ProductOperationResult.Success(id)
        }.getOrElse { error ->
            ProductOperationResult.Error(error.message ?: "No se pudo crear el producto")
        }
    }

    /**
     * Actualiza un producto existente. [photoUri] solo se procesa si no es
     * null — si el usuario no cambió la foto durante la edición, se conserva
     * la `imageBase64` actual del [product].
     */
    suspend fun updateProduct(
        productId: String,
        product: Product,
        photoUri: Uri?
    ): ProductOperationResult = withContext(Dispatchers.IO) {
        runCatching {
            val withImage = product.copy(
                imageBase64 = photoUri?.let { ImageBase64.encodeFromUri(appContext, it) }
                    ?: product.imageBase64
            )
            productsRemote.update(productId, withImage)
            ProductOperationResult.Success(productId)
        }.getOrElse { error ->
            ProductOperationResult.Error(error.message ?: "No se pudo actualizar el producto")
        }
    }

    /** Elimina un producto por id. */
    suspend fun deleteProduct(productId: String): ProductOperationResult =
        withContext(Dispatchers.IO) {
            runCatching {
                productsRemote.delete(productId)
                ProductOperationResult.Success(productId)
            }.getOrElse { error ->
                ProductOperationResult.Error(error.message ?: "No se pudo eliminar el producto")
            }
        }

    /** Marca el producto como pausado/activo (no lo elimina). */
    suspend fun setPaused(productId: String, paused: Boolean) {
        productsRemote.setPaused(productId, paused)
    }

    /** Ajusta solo el stock — la operación más frecuente desde inventario. */
    suspend fun updateStock(productId: String, newStock: Int) {
        productsRemote.updateStock(productId, newStock.coerceAtLeast(0))
    }

    /** Lectura puntual del producto [productId]. */
    suspend fun findById(productId: String): Product? = productsRemote.findById(productId)

    /**
     * [Flow] de los productos del caficultor [caficultorUid]. Reactivo:
     * cualquier cambio en Firestore se refleja sin recargar la pantalla.
     */
    fun observeMyProducts(caficultorUid: String): Flow<List<Product>> =
        productsRemote.observeByCaficultor(caficultorUid)

    /**
     * [Flow] de todos los productos activos para la pantalla de exploración
     * del comprador.
     */
    fun observeActive(): Flow<List<Product>> = productsRemote.observeActive()
}

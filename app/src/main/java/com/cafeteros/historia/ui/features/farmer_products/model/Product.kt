package com.cafeteros.historia.ui.features.farmer_products.model

import android.net.Uri
import java.util.UUID

/**
 * Modelo de dominio de un producto del caficultor.
 *
 * Mientras no exista backend ni tabla en Room, esta data class vive solo en
 * memoria a través de [ProductsStore]. Cuando llegue persistencia real, se
 * mapeará a una entidad Room (ej. `ProductEntity`) con las mismas columnas.
 *
 * Las listas de chips (variedad, notas de cata) se guardan como `Set<String>`
 * para que añadir/quitar chips sea O(1) y evite duplicados.
 *
 * @property photoUri URI de la foto principal del producto. Puede ser una
 *  URI de cámara (`content://...fileprovider/...`) o de galería. Como la
 *  store es in-memory, guardar [Uri] directamente es seguro; cuando se
 *  pase a Room se convertirá a `String`.
 */
data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: ProductCategory,
    val shortDescription: String,
    val fullDescription: String,
    val tags: List<String>,
    val varietyChips: Set<String>,
    val tastingNotes: Set<String>,
    val format: CoffeeFormat,
    val weightGrams: Int,
    val priceCop: Int,
    val stockUnits: Int,
    val isOrganic: Boolean,
    val photoUri: Uri? = null,
    val isPaused: Boolean = false,
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)

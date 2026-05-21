package com.cafeteros.historia.ui.features.farmer_products.model

import android.net.Uri
import com.cafeteros.historia.data.model.CoffeeFormat
import com.cafeteros.historia.data.model.DEFAULT_WEIGHT_OPTIONS_GRAMS
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.model.ProductCategory

/**
 * Estado en memoria del formulario de creación/edición de producto.
 *
 * Es inmutable; las modificaciones del usuario producen copias nuevas vía
 * `copy(...)` en [com.cafeteros.historia.ui.features.farmer_products.ProductCreateViewModel].
 *
 * **Imagen:** el formulario maneja la foto como [Uri] (lo que el sistema
 * devuelve al usar cámara o galería). La conversión a Base64 para Firestore
 * la hace el repositorio al publicar. Si se está editando un producto que
 * ya tenía foto guardada en Base64, ese valor vive aparte en
 * [existingImageBase64] y se pasa al repo solo si el usuario no subió una
 * nueva foto (es decir, [photoUri] sigue null).
 *
 * @property editingProductId si es no-null, este formulario representa la
 *  edición de un producto existente (no la creación de uno nuevo).
 * @property existingImageBase64 Base64 de la foto actual del producto cuando
 *  se está editando, para conservarla si el usuario no sube una nueva.
 */
data class ProductFormState(
    val editingProductId: String? = null,
    val name: String = "",
    val category: ProductCategory = ProductCategory.DEFAULT,
    val shortDescription: String = "",
    val fullDescription: String = "",
    val tagsInput: String = "",
    val tags: List<String> = emptyList(),
    val varietyChips: Set<String> = emptySet(),
    val tastingNotes: Set<String> = emptySet(),
    val format: CoffeeFormat = CoffeeFormat.DEFAULT,
    val weightGrams: Int = DEFAULT_WEIGHT_OPTIONS_GRAMS.first(),
    val priceCopInput: String = "",
    val stockUnitsInput: String = "",
    val isOrganic: Boolean = false,
    val photoUri: Uri? = null,
    val existingImageBase64: String? = null
) {
    /**
     * Validación mínima para habilitar el botón "Publicar producto" del paso
     * 3. Lo más esencial: nombre no vacío y precio numérico positivo. El
     * resto se puede quedar en blanco y el caficultor lo completa después.
     */
    fun canPublish(): Boolean {
        val price = priceCopInput.toIntOrNull() ?: 0
        return name.isNotBlank() && price > 0
    }

    /**
     * Construye el [Product] final que va al repositorio al pulsar Publicar.
     *
     * El campo `imageBase64` queda con el valor de [existingImageBase64] —
     * en edición sin foto nueva eso conserva la foto vieja; en creación es
     * `null` y el repositorio lo rellena tras comprimir la nueva [photoUri].
     *
     * @param caficultorUid uid del usuario logueado que está publicando.
     */
    fun toProduct(caficultorUid: String): Product = Product(
        id = editingProductId.orEmpty(),
        caficultorUid = caficultorUid,
        name = name.trim(),
        category = category,
        shortDescription = shortDescription.trim(),
        fullDescription = fullDescription.trim(),
        tags = tags,
        varietyChips = varietyChips,
        tastingNotes = tastingNotes,
        format = format,
        weightGrams = weightGrams,
        priceCop = priceCopInput.toIntOrNull() ?: 0,
        stockUnits = stockUnitsInput.toIntOrNull() ?: 0,
        isOrganic = isOrganic,
        imageBase64 = existingImageBase64
    )
}

/** Sugerencias predefinidas de variedades cafeteras (chips paso 1). */
val SUGGESTED_VARIETIES: List<String> = listOf(
    "Caturra", "Castillo", "Colombia", "Típica", "Bourbon",
    "Geisha", "Tabi", "Pache"
)

/** Sugerencias predefinidas de notas de cata (chips paso 1). */
val SUGGESTED_TASTING_NOTES: List<String> = listOf(
    "Chocolate", "Caramelo", "Cítricos", "Floral", "Nuez",
    "Frutos rojos", "Miel", "Vainilla"
)

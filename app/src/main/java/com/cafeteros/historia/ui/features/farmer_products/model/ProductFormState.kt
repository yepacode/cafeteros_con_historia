package com.cafeteros.historia.ui.features.farmer_products.model

import android.net.Uri

/**
 * Estado en memoria del formulario de creación/edición de producto.
 *
 * Es inmutable; las modificaciones del usuario producen copias nuevas vía
 * `copy(...)` en [com.cafeteros.historia.ui.features.farmer_products.ProductCreateViewModel].
 *
 * @property editingProductId si es no-null, este formulario representa la
 *  edición de un producto existente (no la creación de uno nuevo). Evita
 *  duplicarlo en la store cuando se publique.
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
    val photoUri: Uri? = null
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

    /** Construye el [Product] final que va a la store al pulsar Publicar. */
    fun toProduct(): Product = Product(
        id = editingProductId ?: java.util.UUID.randomUUID().toString(),
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
        photoUri = photoUri
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

package com.cafeteros.historia.ui.features.searchresults.model

import androidx.annotation.DrawableRes

/**
 * Producto mostrado en el grid de resultados de búsqueda.
 *
 * @property id identificador estable usado como key del LazyVerticalGrid.
 * @property name nombre comercial ("Café Bourbon Rosado").
 * @property farmLabel finca o caficultor en mayúsculas
 *   ("FINCA LA ESPERANZA").
 * @property zoneLabel etiqueta de la zona que se renderiza como badge
 *   naranja sobre la imagen ("HUILA").
 * @property rating valor numérico de la rating (4.9).
 * @property reviewCount total de reseñas que se muestra entre paréntesis.
 * @property formattedPrice precio ya formateado ("$54,000").
 * @property imageRes drawable de la foto principal.
 */
data class SearchResultProduct(
    val id: String,
    val name: String,
    val farmLabel: String,
    val zoneLabel: String,
    val rating: Double,
    val reviewCount: Int,
    val formattedPrice: String,
    @param:DrawableRes val imageRes: Int
)

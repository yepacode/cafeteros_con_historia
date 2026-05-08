package com.cafeteros.historia.ui.features.searchresults.model

import androidx.compose.ui.graphics.Color

/**
 * Chip de filtro activo que aparece bajo el top bar de resultados.
 *
 * Cada chip refleja un filtro aplicado en [com.cafeteros.historia.ui.features.filters.model.OriginFilters]
 * y se puede quitar desde acá tocando la X. Cuando el filtro corresponde
 * a una zona, [accentColor] pinta el punto a la izquierda; para los
 * demás filtros es `null` y el chip va sin punto.
 *
 * @property id identificador estable para el LazyRow.
 * @property label texto visible.
 * @property accentColor color del punto cuando aplica (zonas).
 */
data class SearchActiveFilter(
    val id: String,
    val label: String,
    val accentColor: Color? = null
)

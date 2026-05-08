package com.cafeteros.historia.ui.features.search.model

/**
 * Un chip de "Tendencias de la semana" (ej. "Microlotes Huila", "Geisha").
 *
 * Es solo un valor de texto envuelto en data class para tener una key
 * estable cuando se renderiza en `LazyRow`/grid y para que el día que el
 * backend agregue metadata (popularidad, deeplink) no haya que tocar la UI.
 *
 * @property label texto visible dentro del chip.
 */
data class TrendingSearch(
    val label: String
)

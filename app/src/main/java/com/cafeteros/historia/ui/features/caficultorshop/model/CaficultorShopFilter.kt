package com.cafeteros.historia.ui.features.caficultorshop.model

/**
 * Chip outline de un filtro aplicado en la cabecera del catálogo
 * "el caficultor" (ej. "Origen: Huila", "Tueste: Medio").
 *
 * A diferencia de los chips de la pantalla de resultados, estos no tienen
 * X individual: son informativos. Para limpiar filtros existe el botón
 * "Limpiar filtros" del empty state.
 *
 * @property id identificador estable usado como key del LazyRow.
 * @property label texto visible ("Origen: Huila").
 */
data class CaficultorShopFilter(
    val id: String,
    val label: String
)

package com.cafeteros.historia.ui.features.filters.model

/**
 * Opciones del bloque "ORDENAR POR" (excluyente).
 */
enum class FilterSortOption(val label: String) {
    RELEVANCIA(label = "Relevancia"),
    PRECIO_MENOR_MAYOR(label = "Precio: menor a mayor"),
    MEJOR_CALIFICADOS(label = "Mejor calificados")
}

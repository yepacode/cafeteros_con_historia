package com.cafeteros.historia.ui.features.searchresults.model

/**
 * Opciones del dropdown de orden alineado a la derecha del toolbar de
 * resultados ("Relevancia ⌄").
 *
 * Replica el mismo set que [com.cafeteros.historia.ui.features.filters.model.FilterSortOption]
 * pero vive aquí porque la pantalla de resultados puede sortear sin haber
 * abierto los filtros. Cuando exista una capa de dominio común, ambos
 * enums podrán fusionarse.
 */
enum class SearchResultsSortOption(val label: String) {
    RELEVANCIA(label = "Relevancia"),
    PRECIO_MENOR_MAYOR(label = "Precio: menor a mayor"),
    PRECIO_MAYOR_MENOR(label = "Precio: mayor a menor"),
    MEJOR_CALIFICADOS(label = "Mejor calificados")
}

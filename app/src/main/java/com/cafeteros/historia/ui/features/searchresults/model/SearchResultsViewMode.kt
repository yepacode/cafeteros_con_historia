package com.cafeteros.historia.ui.features.searchresults.model

/**
 * Toggle de vista del grid de resultados.
 *
 * Hoy ambas opciones renderizan el mismo grid de 2 columnas; la
 * diferencia visual del icono es solo informativa. Cuando se conecte la
 * vista de lista, [LIST] cambiará a un layout vertical de filas.
 */
enum class SearchResultsViewMode {
    GRID,
    LIST
}

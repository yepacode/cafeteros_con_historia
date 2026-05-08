package com.cafeteros.historia.ui.features.searchresults.model

/**
 * Tabs de scope de los resultados (Productos / Caficultores / Zonas).
 *
 * Cambian el tipo de resultado mostrado en la grilla. Hoy solo "Productos"
 * tiene contenido renderizado; las otras dos muestran un placeholder hasta
 * que existan sus modelos de resultado.
 */
enum class SearchResultsTab(val label: String) {
    PRODUCTOS(label = "Productos"),
    CAFICULTORES(label = "Caficultores"),
    ZONAS(label = "Zonas")
}

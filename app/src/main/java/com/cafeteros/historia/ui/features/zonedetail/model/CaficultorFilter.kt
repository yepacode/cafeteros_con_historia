package com.cafeteros.historia.ui.features.zonedetail.model

/**
 * Filtros sobre la grilla de caficultores en el detalle de zona.
 *
 * Como en otros features, el [label] vive en el enum para evitar
 * dispersar strings. [TODOS] es el default y se renderiza activo.
 */
enum class CaficultorFilter(val label: String) {
    TODOS(label = "Todos"),
    ORGANICO(label = "Orgánico"),
    PREMIOS(label = "Premios"),
    ALTITUD(label = "Altitud")
}

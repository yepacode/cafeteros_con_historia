package com.cafeteros.historia.ui.features.coffeemap.model

/**
 * Filtros disponibles en la fila inferior del mapa cafetero.
 *
 * Cada valor expone su [label] visible (en el idioma de la app) para que el
 * componente [com.cafeteros.historia.ui.features.coffeemap.components.MapFilterChip]
 * no tenga que mantener su propio mapping. El filtro [TODOS] es el default
 * y se distingue visualmente por aparecer como chip oscuro/seleccionado.
 */
enum class MapFilter(val label: String) {
    TODOS(label = "Todos"),
    ORGANICO(label = "Orgánico"),
    SPECIALTY(label = "Specialty"),
    LAVADO(label = "Lavado"),
    NATURAL(label = "Natural"),
    HONEY(label = "Honey")
}

package com.cafeteros.historia.ui.features.filters.model

/**
 * Procesos de beneficio del café seleccionables en el bloque "PROCESO".
 *
 * Es excluyente: el usuario solo elige uno. "Todos" funciona como reset
 * dentro de esta sección.
 */
enum class FilterProcess(val label: String) {
    TODOS(label = "Todos"),
    LAVADO(label = "Lavado"),
    HONEY(label = "Honey"),
    NATURAL(label = "Natural")
}

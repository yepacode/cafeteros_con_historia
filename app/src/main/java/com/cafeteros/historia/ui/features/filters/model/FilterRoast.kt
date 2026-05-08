package com.cafeteros.historia.ui.features.filters.model

/**
 * Niveles de tueste del bloque "TUESTE".
 *
 * Cada nivel se renderiza como un ícono de taza con su etiqueta debajo
 * (CLARO, MEDIO-C, MEDIO, MEDIO-O, OSCURO). El orden de declaración
 * define el orden visual y NO debe alterarse.
 */
enum class FilterRoast(val label: String) {
    CLARO(label = "CLARO"),
    MEDIO_CLARO(label = "MEDIO-C"),
    MEDIO(label = "MEDIO"),
    MEDIO_OSCURO(label = "MEDIO-O"),
    OSCURO(label = "OSCURO")
}

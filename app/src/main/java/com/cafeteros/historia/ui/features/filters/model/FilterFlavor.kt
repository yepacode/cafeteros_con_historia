package com.cafeteros.historia.ui.features.filters.model

/**
 * Notas de sabor seleccionables en el bloque "PERFIL DE SABOR".
 *
 * Cada chip representa un descriptor del perfil del café (ej. "Chocolate",
 * "Cítrico"). El usuario puede seleccionar varios; la pantalla guarda el
 * conjunto de seleccionados como `Set<FilterFlavor>`.
 */
enum class FilterFlavor(val label: String) {
    CHOCOLATE(label = "Chocolate"),
    FRUTADO(label = "Frutado"),
    FLORAL(label = "Floral"),
    CITRICO(label = "Cítrico"),
    CARAMELO(label = "Caramelo"),
    NUEZ(label = "Nuez"),
    MIEL(label = "Miel"),
    ESPECIADO(label = "Especiado"),
    VINOSO(label = "Vinoso"),
    TERROSO(label = "Terroso")
}

package com.cafeteros.historia.ui.features.filters.model

/**
 * Rangos de altitud cafetera del bloque "ALTITUD".
 *
 * Se elige uno solo. Si ningún valor está seleccionado, no se aplica filtro
 * por altitud.
 */
enum class FilterAltitude(val label: String) {
    BAJA_MEDIA(label = "1.200 - 1.500 msnm"),
    ALTURAS(label = "1.800+ msnm (Alturas)")
}

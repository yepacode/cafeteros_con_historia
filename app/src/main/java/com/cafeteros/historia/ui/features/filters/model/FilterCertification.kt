package com.cafeteros.historia.ui.features.filters.model

/**
 * Certificaciones marcables en el bloque "CERTIFICACIONES".
 *
 * El usuario puede marcar varias; la pantalla guarda el conjunto como
 * `Set<FilterCertification>`.
 */
enum class FilterCertification(val label: String) {
    ORGANICO(label = "Orgánico"),
    COMERCIO_JUSTO(label = "Comercio justo"),
    RAINFOREST_ALLIANCE(label = "Rainforest Alliance")
}

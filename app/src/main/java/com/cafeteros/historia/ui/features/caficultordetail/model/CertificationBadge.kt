package com.cafeteros.historia.ui.features.caficultordetail.model

/**
 * Distintivos verdes en pill que aparecen bajo el nombre del caficultor en
 * la profile card.
 *
 * El [label] vive en el enum para que añadir un nuevo distintivo (ej.
 * "PRÁCTICAS REGENERATIVAS") sea sumar un valor aquí. Todos comparten la
 * misma paleta verde definida en
 * [com.cafeteros.historia.ui.theme.BrandColors.CertificationBadgeBackground].
 */
enum class CertificationBadge(val label: String) {
    ORGANICO_CERTIFICADO(label = "ORGÁNICO CERTIFICADO"),
    COMERCIO_JUSTO(label = "COMERCIO JUSTO"),
    DESDE_1982(label = "DESDE 1982"),
    PRACTICAS_REGENERATIVAS(label = "PRÁCTICAS REGENERATIVAS")
}

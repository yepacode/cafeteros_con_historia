package com.cafeteros.historia.ui.features.zonedetail.model

/**
 * Característica de sabor de la zona evaluada en una escala discreta.
 *
 * El [level] es un valor textual ("BAJA", "MEDIA", "ALTA") que aparece
 * literalmente en la UI a la derecha del slider, mientras que [fillRatio]
 * (0.0 – 1.0) determina cuánto se rellena la barra dorada. Mantenerlos
 * desacoplados permite, en el futuro, traducir el label sin romper la
 * representación visual y viceversa.
 */
enum class FlavorLevel(val label: String, val fillRatio: Float) {
    BAJA(label = "BAJA", fillRatio = 0.20f),
    MEDIA(label = "MEDIA", fillRatio = 0.55f),
    ALTA(label = "ALTA", fillRatio = 0.90f),
    ALTO(label = "ALTO", fillRatio = 0.92f)
}

/**
 * Una fila del slider en la card "Perfil de sabor".
 *
 * @property name etiqueta a la izquierda del slider ("ACIDEZ", "CUERPO"...).
 * @property level nivel cualitativo + ratio de relleno.
 */
data class FlavorTrait(
    val name: String,
    val level: FlavorLevel
)

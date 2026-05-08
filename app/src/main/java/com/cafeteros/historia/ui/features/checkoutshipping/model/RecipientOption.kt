package com.cafeteros.historia.ui.features.checkoutshipping.model

/**
 * Opciones excluyentes del bloque "¿A quién avisamos cuando llegue?".
 *
 * En el diseño la opción seleccionada se renderiza como un cuadrado
 * oscuro con check ✓, y la NO seleccionada como un círculo outline
 * vacío. Esa diferencia visual se resuelve en el componente.
 */
enum class RecipientOption(val label: String) {
    USAR_MIS_DATOS(label = "Usar mis datos"),
    ALGUIEN_MAS(label = "Alguien más recibirá")
}

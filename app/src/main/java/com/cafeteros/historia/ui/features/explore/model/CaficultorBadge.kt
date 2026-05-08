package com.cafeteros.historia.ui.features.explore.model

import androidx.compose.ui.graphics.Color
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Distintivo del caficultor mostrado al pie de su card destacada.
 *
 * El enum centraliza el texto y la pareja de colores (fondo + texto) de cada
 * tipo de badge para evitar magia repetida en los composables. Si en el
 * futuro se añaden distintivos como "PREMIUM" o "DIRECTO", basta con sumar
 * un nuevo valor aquí.
 */
enum class CaficultorBadge(
    val label: String,
    val backgroundColor: Color,
    val textColor: Color
) {
    ORGANICO(
        label = "ORGÁNICO",
        backgroundColor = BrandColors.BadgeOrganicBackground,
        textColor = BrandColors.BadgeOrganicText
    ),
    SOSTENIBLE(
        label = "SOSTENIBLE",
        backgroundColor = BrandColors.BadgeSostenibleBackground,
        textColor = BrandColors.BadgeSostenibleText
    )
}

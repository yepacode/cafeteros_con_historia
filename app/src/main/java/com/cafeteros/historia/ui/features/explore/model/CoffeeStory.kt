package com.cafeteros.historia.ui.features.explore.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Historia editorial mostrada en la sección "Historias que enamoran".
 *
 * @property title titular de la historia ("Don Alberto y 40 años cultivando café").
 * @property readTimeMinutes tiempo estimado de lectura.
 * @property imageRes drawable opcional con la foto editorial.
 * @property placeholderColor color de respaldo cuando no hay imagen.
 */
data class CoffeeStory(
    val title: String,
    val readTimeMinutes: Int,
    @param:DrawableRes val imageRes: Int? = null,
    val placeholderColor: Color
)

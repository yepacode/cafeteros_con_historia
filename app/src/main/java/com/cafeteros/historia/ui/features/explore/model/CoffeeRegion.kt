package com.cafeteros.historia.ui.features.explore.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Zona cafetera representada en el carrusel "Explora por zona".
 *
 * Si [imageRes] está disponible, la card lo usa como fondo. En caso contrario
 * cae a un color sólido [placeholderColor] para que la pantalla siga viéndose
 * coherente sin assets reales (placeholder de marca).
 *
 * @property name nombre de la zona ("Huila", "Sierra Nevada", "Nariño").
 * @property caficultorCount conteo a mostrar bajo el nombre ("42 caficultores").
 * @property imageRes drawable opcional con la foto del paisaje.
 * @property placeholderColor color de fondo cuando no hay imagen.
 */
data class CoffeeRegion(
    val name: String,
    val caficultorCount: Int,
    @param:DrawableRes val imageRes: Int? = null,
    val placeholderColor: Color
)

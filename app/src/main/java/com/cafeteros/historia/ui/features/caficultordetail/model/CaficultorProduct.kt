package com.cafeteros.historia.ui.features.caficultordetail.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Producto vendido por el caficultor en la sección "Productos (n)" del
 * detalle del perfil.
 *
 * @property name nombre comercial ("Café Huila Pitalito").
 * @property weight peso del producto ("250g").
 * @property formattedPrice precio listo para mostrar ("$48.000").
 * @property imageRes drawable opcional con la foto del paquete.
 * @property placeholderColor color de respaldo cuando no hay foto.
 */
data class CaficultorProduct(
    val name: String,
    val weight: String,
    val formattedPrice: String,
    @param:DrawableRes val imageRes: Int? = null,
    val placeholderColor: Color
)

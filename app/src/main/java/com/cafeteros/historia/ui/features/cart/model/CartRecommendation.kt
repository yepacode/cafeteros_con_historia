package com.cafeteros.historia.ui.features.cart.model

import androidx.annotation.DrawableRes

/**
 * Card sugerida en la sección "Recomendaciones del Origen" del empty
 * state del carrito.
 *
 * @property id identificador estable.
 * @property name nombre del producto / colección ("Finca El Diviso").
 * @property imageRes drawable de la foto.
 * @property metaLabel línea uppercase con metadata corta
 *   ("HUILA · LAVADO", "ORGÁNICO · MIEL").
 */
data class CartRecommendation(
    val id: String,
    val name: String,
    @param:DrawableRes val imageRes: Int,
    val metaLabel: String
)

package com.cafeteros.historia.ui.features.cart.model

import androidx.annotation.DrawableRes

/**
 * Una línea del carrito de compras.
 *
 * @property id identificador estable usado como key de la lista.
 * @property name nombre del producto ("Café Huila Pitalito 250g").
 * @property meta línea bajo el nombre con peso y tipo ("250g · Molido medio").
 * @property unitPrice precio unitario actual en pesos (sin formatear).
 * @property originalUnitPrice precio anterior tachado, opcional.
 * @property imageRes drawable de la foto del producto.
 * @property quantity cantidad actual elegida en el stepper.
 */
data class CartItem(
    val id: String,
    val name: String,
    val meta: String,
    val unitPrice: Int,
    val originalUnitPrice: Int? = null,
    @param:DrawableRes val imageRes: Int,
    val quantity: Int = 1
)

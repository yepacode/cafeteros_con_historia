package com.cafeteros.historia.ui.features.cart.model

import androidx.annotation.DrawableRes

/**
 * Agrupación de items del carrito por caficultor.
 *
 * El header del grupo muestra el avatar y el nombre del caficultor + un
 * badge con su zona; debajo van todas las líneas [items] que vienen de
 * ese caficultor.
 *
 * @property id identificador estable.
 * @property caficultorName nombre del caficultor / finca.
 * @property avatarRes drawable del avatar circular.
 * @property zoneLabel etiqueta de la zona ("HUILA").
 * @property items líneas del carrito asociadas a este caficultor.
 */
data class CartCaficultorGroup(
    val id: String,
    val caficultorName: String,
    @param:DrawableRes val avatarRes: Int,
    val zoneLabel: String,
    val items: List<CartItem>
)

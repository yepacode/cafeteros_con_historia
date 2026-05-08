package com.cafeteros.historia.ui.features.cart.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Tabs de la bottom bar del carrito.
 *
 * A diferencia de [com.cafeteros.historia.ui.features.explore.components.ExploreTab]
 * (5 tabs con texto: EXPLORAR/BUSCAR/CARRITO/PEDIDOS/PERFIL), esta dock
 * tiene solo 4 tabs con labels uppercase y el activo en dorado. Vive
 * dentro del feature `cart/` porque es el único contexto donde aparece.
 *
 * @property label texto uppercase debajo del icono.
 * @property icon icono del tab.
 */
enum class CartBottomTab(
    val label: String,
    val icon: ImageVector
) {
    ORIGEN(label = "ORIGEN", icon = Icons.Outlined.Landscape),
    CATALOGO(label = "CATÁLOGO", icon = Icons.Outlined.Coffee),
    MI_BOLSA(label = "MI BOLSA", icon = Icons.Filled.ShoppingBag),
    PERFIL(label = "PERFIL", icon = Icons.Outlined.Person)
}

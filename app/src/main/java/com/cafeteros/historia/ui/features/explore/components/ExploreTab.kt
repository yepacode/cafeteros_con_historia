package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Pestañas disponibles en la barra inferior del comprador.
 *
 * Cada tab encapsula su etiqueta visible y el ícono asociado, evitando dos
 * listas paralelas en la pantalla. El orden del enum determina el orden de
 * pintado en pantalla.
 */
enum class ExploreTab(
    val label: String,
    val icon: ImageVector
) {
    EXPLORAR(label = "EXPLORAR", icon = Icons.Outlined.Explore),
    BUSCAR(label = "BUSCAR", icon = Icons.Outlined.Search),
    CARRITO(label = "CARRITO", icon = Icons.Outlined.ShoppingCart),
    PEDIDOS(label = "PEDIDOS", icon = Icons.Outlined.Receipt),
    PERFIL(label = "PERFIL", icon = Icons.Outlined.Person)
}

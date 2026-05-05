package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Tipos de usuario que pueden registrarse en Origen.
 *
 * El selector de la pantalla de Registro renderiza una card por cada valor.
 * Los textos de UI viven en la enum para mantener acopladas las opciones a
 * sus etiquetas (en vez de duplicar strings en el composable).
 */
enum class UserType(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
) {
    COMPRADOR(
        title = "Soy Comprador",
        subtitle = "Quiero descubrir café",
        icon = Icons.Outlined.ShoppingBag
    ),
    CAFICULTOR(
        title = "Soy Caficultor",
        subtitle = "Quiero vender mi café",
        icon = Icons.Outlined.Eco
    )
}

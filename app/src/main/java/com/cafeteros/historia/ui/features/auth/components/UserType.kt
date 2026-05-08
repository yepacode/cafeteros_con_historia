package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Tipos de usuario que pueden registrarse en Origen.
 *
 * El selector de la pantalla de Registro renderiza una card por cada valor
 * en el orden de declaración (Comprador a la izquierda, Caficultor a la
 * derecha), por lo que ese orden es parte del contrato visual y NO debe
 * alterarse sin revisar el diseño.
 *
 * El campo [roleId] mapea cada rol al identificador numérico que se usará
 * cuando se conecte la base de datos:
 *  - Comprador  → 1
 *  - Caficultor → 2
 *
 * Estos valores deben coincidir con los IDs definidos en la tabla `roles`
 * (o equivalente) que está armando la otra compañera. Si más adelante se
 * usan UUIDs o strings ("ROLE_BUYER"), basta con cambiar el tipo de este
 * campo en un solo lugar.
 */
enum class UserType(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val roleId: Int
) {
    COMPRADOR(
        title = "Soy Comprador",
        subtitle = "Quiero descubrir café",
        icon = Icons.Outlined.ShoppingBag,
        roleId = 1
    ),
    CAFICULTOR(
        title = "Soy Caficultor",
        subtitle = "Quiero vender mi café",
        icon = Icons.Outlined.Eco,
        roleId = 2
    )
}

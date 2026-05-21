package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Tipos de usuario que pueden registrarse en Origen.
 *
 * Cada valor del enum corresponde a una fila de la tabla `roles` de la base
 * de datos:
 *  - [COMPRADOR]      ↔ roles.id = 1, name = "Comprador"
 *  - [CAFICULTOR]     ↔ roles.id = 2, name = "Vendedor"
 *  - [ADMINISTRADOR]  ↔ roles.id = 3, name = "Administrador"
 *
 * El campo [roleId] es el puente entre el enum (UI) y la FK persistida.
 *
 * Nota de naming: la UI mantiene "Caficultor" (más alineado con la marca de
 * café colombiano), aunque internamente la tabla roles lo etiqueta como
 * "Vendedor" (genérico). Si en el futuro quieres unificar la nomenclatura,
 * basta con cambiar el `name` en la siembra del Callback en `AppDatabase`.
 */
enum class UserType(
    val roleId: Int,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
) {
    COMPRADOR(
        roleId = 1,
        title = "Soy Comprador",
        subtitle = "Quiero descubrir café",
        icon = Icons.Outlined.ShoppingBag
    ),
    CAFICULTOR(
        roleId = 2,
        title = "Soy Caficultor",
        subtitle = "Quiero vender mi café",
        icon = Icons.Outlined.Eco
    ),
    ADMINISTRADOR(
        roleId = 3,
        title = "Soy Administrador",
        subtitle = "Gestiono la plataforma",
        icon = Icons.Outlined.AdminPanelSettings
    );

    companion object {
        /**
         * Resuelve el [UserType] correspondiente al `role_id` almacenado en
         * la base de datos. Lanza si recibe un id desconocido — eso indica
         * inconsistencia entre la tabla roles y el enum y debe corregirse
         * en código (no debería ocurrir en runtime con datos válidos).
         */
        fun fromRoleId(id: Int): UserType =
            entries.firstOrNull { it.roleId == id }
                ?: error("Role id desconocido: $id. Revisa la tabla 'roles' y el enum UserType.")
    }
}

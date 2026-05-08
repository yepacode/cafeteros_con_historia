package com.cafeteros.historia.ui.features.search.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Map
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Categorías del grid "Explora por" que aparece en la pantalla de búsqueda.
 *
 * El orden de declaración define el orden visual del grid (fila 1: ZONA y
 * SABOR; fila 2: PROCESO y CERTIFICACION) y NO debe alterarse sin revisar
 * el diseño.
 *
 * @property title texto bold dentro de la card.
 * @property icon ícono que se renderiza arriba en la card.
 * @property iconTint color del ícono — uno por categoría para diferenciarlas.
 */
enum class SearchCategory(
    val title: String,
    val icon: ImageVector,
    val iconTint: Color
) {
    ZONA(
        title = "Por zona",
        icon = Icons.Outlined.Map,
        iconTint = BrandColors.CategoryIconZone
    ),
    PERFIL_SABOR(
        title = "Por perfil de sabor",
        icon = Icons.Outlined.Coffee,
        iconTint = BrandColors.CategoryIconFlavor
    ),
    PROCESO(
        title = "Por proceso",
        icon = Icons.Filled.PanTool,
        iconTint = BrandColors.CategoryIconProcess
    ),
    CERTIFICACION(
        title = "Por certificación",
        icon = Icons.Filled.Verified,
        iconTint = BrandColors.CategoryIconCertification
    )
}

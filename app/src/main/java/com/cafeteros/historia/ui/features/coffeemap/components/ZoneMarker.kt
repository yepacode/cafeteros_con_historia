package com.cafeteros.historia.ui.features.coffeemap.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.coffeemap.model.CoffeeZone
import com.cafeteros.historia.ui.features.coffeemap.model.ProductionIntensity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Marcador de zona cafetera dibujado sobre el lienzo del mapa ilustrado.
 *
 * Composición vertical:
 *  1. Burbuja circular grande con el color identitario de la zona y un
 *     ícono Material centrado (taza, montaña, hoja, ola, trofeo…).
 *  2. Badge oscuro con el número de caficultores anclado arriba-derecha
 *     del círculo.
 *  3. Label en mayúsculas con el nombre de la zona, en el color de la zona.
 *
 * El marcador hace una pequeña animación de escala cuando [isSelected]
 * cambia a true, para dar feedback visual al toque.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param zone datos de la zona a renderizar.
 * @param isSelected true si la zona es la actualmente seleccionada en el
 *  bottom sheet (afecta solo el feedback visual).
 * @param onClick callback al tocar el marcador.
 */
@Composable
fun ZoneMarker(
    modifier: Modifier = Modifier,
    zone: CoffeeZone,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        label = "markerScale"
    )

    Column(
        modifier = modifier
            .scale(scale)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(zone.color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = zone.icon,
                    contentDescription = zone.name,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            ZoneCountBadge(
                count = zone.caficultorCount,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Text(
            text = zone.name,
            style = BrandTypography.ZoneMarkerLabel.copy(color = zone.color)
        )
    }
}

/**
 * Badge oscuro pequeño con el conteo numérico ("12", "84"…). Privado al
 * componente porque solo el [ZoneMarker] lo usa con esta proporción.
 */
@Composable
private fun ZoneCountBadge(
    modifier: Modifier = Modifier,
    count: Int
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 26.dp, minHeight = 22.dp)
            .clip(CircleShape)
            .background(BrandColors.MarkerCountBadgeBackground)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            style = BrandTypography.ZoneMarkerCount
        )
    }
}

@Preview(name = "ZoneMarker", showBackground = true, widthDp = 160, heightDp = 160)
@Composable
private fun ZoneMarkerPreview() {
    CafeterosTheme {
        ZoneMarker(
            zone = CoffeeZone(
                name = "EJE CAFETERO",
                caficultorCount = 84,
                color = BrandColors.ZoneEjeCafetero,
                icon = Icons.Filled.Coffee,
                normalizedPosition = 0.5f to 0.5f,
                intensity = ProductionIntensity.ALTA,
                latitude = 5.0,
                longitude = -75.5
            )
        )
    }
}

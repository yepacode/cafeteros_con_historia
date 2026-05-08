package com.cafeteros.historia.ui.features.coffeemap.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.coffeemap.model.MapFilter
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Chip individual de la fila de filtros bajo las pills de zonas.
 *
 * Cuando [isSelected] es true se pinta con fondo café oscuro y texto blanco
 * (estilo "Todos" en el diseño). Cuando es false usa fondo beige claro y
 * texto gris. La transición de colores se anima suavemente para dar feedback
 * al toque.
 *
 * @param modifier modifier opcional.
 * @param filter filtro a renderizar.
 * @param isSelected estado actual.
 * @param onClick callback al pulsar.
 */
@Composable
fun MapFilterChip(
    modifier: Modifier = Modifier,
    filter: MapFilter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background by animateColorAsState(
        targetValue = if (isSelected) {
            BrandColors.FilterChipSelectedBackground
        } else {
            BrandColors.FilterChipUnselectedBackground
        },
        label = "chipBackground"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            BrandColors.FilterChipSelectedText
        } else {
            BrandColors.FilterChipUnselectedText
        },
        label = "chipText"
    )

    Text(
        text = filter.label,
        style = BrandTypography.MapFilterChipLabel.copy(color = textColor),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}

@Preview(name = "FilterChip – seleccionado", showBackground = true)
@Composable
private fun MapFilterChipSelectedPreview() {
    CafeterosTheme {
        MapFilterChip(filter = MapFilter.TODOS, isSelected = true, onClick = {})
    }
}

@Preview(name = "FilterChip – no seleccionado", showBackground = true)
@Composable
private fun MapFilterChipUnselectedPreview() {
    CafeterosTheme {
        MapFilterChip(filter = MapFilter.ORGANICO, isSelected = false, onClick = {})
    }
}

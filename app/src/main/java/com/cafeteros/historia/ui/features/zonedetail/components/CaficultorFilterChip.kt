package com.cafeteros.historia.ui.features.zonedetail.components

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
import com.cafeteros.historia.ui.features.zonedetail.model.CaficultorFilter
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Chip de filtro sobre la grilla de caficultores ("Todos", "Orgánico"…).
 *
 * Visualmente equivalente al [com.cafeteros.historia.ui.features.coffeemap.components.MapFilterChip]
 * — fondo oscuro/blanco según el estado, texto bold con un toque de tracking.
 * Vive en este feature como componente propio para no acoplar el detalle
 * de zona con el módulo del mapa; si en el futuro se decide compartirlo,
 * se extrae a un `ui/common`.
 */
@Composable
fun CaficultorFilterChip(
    modifier: Modifier = Modifier,
    filter: CaficultorFilter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background by animateColorAsState(
        targetValue = if (isSelected) {
            BrandColors.FilterChipSelectedBackground
        } else {
            BrandColors.FilterChipUnselectedBackground
        },
        label = "caficultorChipBackground"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            BrandColors.FilterChipSelectedText
        } else {
            BrandColors.FilterChipUnselectedText
        },
        label = "caficultorChipText"
    )

    Text(
        text = filter.label,
        style = BrandTypography.MapFilterChipLabel.copy(color = textColor),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Preview(name = "CaficultorFilterChip – seleccionado", showBackground = true)
@Composable
private fun CaficultorFilterChipSelectedPreview() {
    CafeterosTheme {
        CaficultorFilterChip(
            filter = CaficultorFilter.TODOS,
            isSelected = true,
            onClick = {}
        )
    }
}

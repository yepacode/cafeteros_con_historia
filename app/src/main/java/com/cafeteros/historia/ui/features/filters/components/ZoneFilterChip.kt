package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.filters.model.FilterZone
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Chip individual de una zona cafetera dentro del grid del filtro.
 *
 * Visual:
 *  - Punto del color de la zona a la izquierda.
 *  - Nombre de la zona (bold).
 *  - Conteo de caficultores en gris a la derecha.
 *  - Cuando [selected] es true: fondo café oscuro, texto blanco.
 *  - Cuando NO: fondo blanco con borde sutil.
 *
 * @param modifier modifier opcional.
 * @param zone datos a renderizar.
 * @param selected estado del chip.
 * @param onClick callback al pulsar.
 */
@Composable
fun ZoneFilterChip(
    modifier: Modifier = Modifier,
    zone: FilterZone,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val containerModifier = if (selected) {
        Modifier
            .clip(shape)
            .background(BrandColors.CoffeeBrown)
    } else {
        Modifier
            .clip(shape)
            .background(BrandColors.CardBackground)
            .border(
                BorderStroke(1.dp, BrandColors.PresentationChipUnselectedBorder),
                shape
            )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(containerModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(zone.accentColor)
            )
            Text(
                text = zone.name,
                style = BrandTypography.ZoneChipName.copy(
                    color = if (selected) Color.White else BrandColors.TextPrimary
                )
            )
        }
        Text(
            text = zone.count.toString(),
            style = BrandTypography.ZoneChipCount.copy(
                color = if (selected) Color.White.copy(alpha = 0.85f) else BrandColors.TextSecondary
            )
        )
    }
}

@Preview(name = "ZoneFilterChip", showBackground = true, widthDp = 200)
@Composable
private fun ZoneFilterChipPreview() {
    CafeterosTheme {
        ZoneFilterChip(
            modifier = Modifier.padding(BrandSpacing.md),
            zone = FilterZone(
                name = "Santander",
                count = 38,
                accentColor = BrandColors.ZoneSantander
            ),
            selected = true,
            onClick = {}
        )
    }
}

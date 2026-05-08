package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.filters.model.FilterRoast
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "TUESTE": cinco tazas en una pill horizontal con la taza activa
 * destacada (fondo blanco, borde oscuro, ícono café oscuro).
 *
 * Las tazas se renderizan con un único ícono de café tintado a distinto
 * color según el estado — cuando exista arte real (5 ilustraciones), basta
 * con reemplazar [Icons.Filled.Coffee] por las nuevas ilustraciones.
 *
 * @param modifier modifier opcional.
 * @param selected nivel de tueste activo. `null` = ninguno seleccionado.
 * @param onSelect callback al pulsar una taza.
 */
@Composable
fun RoastFilterSection(
    modifier: Modifier = Modifier,
    selected: FilterRoast?,
    onSelect: (FilterRoast) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        FilterSectionLabel(text = "TUESTE")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BrandColors.RoastSelectorBackground)
                .padding(BrandSpacing.sm),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterRoast.entries.forEach { roast ->
                RoastCup(
                    roast = roast,
                    selected = roast == selected,
                    onClick = { onSelect(roast) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RoastCup(
    modifier: Modifier = Modifier,
    roast: FilterRoast,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val containerModifier = if (selected) {
        Modifier
            .clip(shape)
            .background(BrandColors.RoastCupActiveBackground)
            .border(BorderStroke(1.5.dp, BrandColors.RoastCupActiveBorder), shape)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .then(containerModifier)
            .clickable(onClick = onClick)
            .padding(vertical = BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Coffee,
            contentDescription = null,
            tint = if (selected) BrandColors.RoastCupActiveTint else BrandColors.RoastCupInactiveTint,
            modifier = Modifier.size(28.dp)
        )
        Text(text = roast.label, style = BrandTypography.RoastCupCaption)
    }
}

@Preview(name = "RoastFilterSection", showBackground = true, widthDp = 360)
@Composable
private fun RoastFilterSectionPreview() {
    CafeterosTheme {
        RoastFilterSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            selected = FilterRoast.MEDIO,
            onSelect = {}
        )
    }
}

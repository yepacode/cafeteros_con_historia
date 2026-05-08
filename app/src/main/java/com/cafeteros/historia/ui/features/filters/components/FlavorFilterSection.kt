package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.filters.model.FilterFlavor
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "PERFIL DE SABOR": chips ovalados con borde delgado que envuelven
 * cuando no caben en el ancho disponible.
 *
 * Cuando un chip está seleccionado, su borde y su texto se pintan en verde
 * profundo; cuando no, ambos van en gris-beige.
 *
 * @param modifier modifier opcional.
 * @param selected conjunto de sabores marcados.
 * @param onToggle callback al pulsar un chip.
 */
@Composable
fun FlavorFilterSection(
    modifier: Modifier = Modifier,
    selected: Set<FilterFlavor>,
    onToggle: (FilterFlavor) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        FilterSectionLabel(text = "PERFIL DE SABOR")
        WrappingFlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalSpacing = BrandSpacing.sm,
            verticalSpacing = BrandSpacing.sm
        ) {
            FilterFlavor.entries.forEach { flavor ->
                FlavorChip(
                    flavor = flavor,
                    selected = flavor in selected,
                    onClick = { onToggle(flavor) }
                )
            }
        }
    }
}

@Composable
private fun FlavorChip(
    modifier: Modifier = Modifier,
    flavor: FilterFlavor,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(BrandColors.CardBackground)
            .border(
                BorderStroke(
                    width = if (selected) 1.5.dp else 1.dp,
                    color = if (selected) BrandColors.FlavorChipSelectedBorder
                    else BrandColors.FlavorChipBorder
                ),
                shape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = flavor.label,
            style = if (selected) BrandTypography.FlavorChipSelected
            else BrandTypography.FlavorChipUnselected
        )
    }
}

@Preview(name = "FlavorFilterSection", showBackground = true, widthDp = 360)
@Composable
private fun FlavorFilterSectionPreview() {
    CafeterosTheme {
        FlavorFilterSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            selected = setOf(FilterFlavor.CHOCOLATE, FilterFlavor.CITRICO),
            onToggle = {}
        )
    }
}

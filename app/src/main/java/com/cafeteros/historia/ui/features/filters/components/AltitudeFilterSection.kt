package com.cafeteros.historia.ui.features.filters.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.filters.model.FilterAltitude
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "ALTITUD": dos cards apiladas, cada una con un radio button al
 * final. Selección excluyente.
 *
 * Cuando una card está seleccionada, su fondo cambia a verde claro, su
 * texto a verde profundo y su radio button se rellena. Toda la card es
 * clickable.
 *
 * @param modifier modifier opcional.
 * @param selected altitud activa. `null` = ninguna seleccionada.
 * @param onSelect callback al pulsar una card.
 */
@Composable
fun AltitudeFilterSection(
    modifier: Modifier = Modifier,
    selected: FilterAltitude?,
    onSelect: (FilterAltitude) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        FilterSectionLabel(text = "ALTITUD")
        FilterAltitude.entries.forEach { option ->
            AltitudeCard(
                altitude = option,
                selected = option == selected,
                onClick = { onSelect(option) }
            )
        }
    }
}

@Composable
private fun AltitudeCard(
    modifier: Modifier = Modifier,
    altitude: FilterAltitude,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val containerModifier = if (selected) {
        Modifier
            .clip(shape)
            .background(BrandColors.AltitudeCardSelectedBackground)
            .border(width = 1.5.dp, color = BrandColors.AltitudeCardSelectedBorder, shape = shape)
    } else {
        Modifier
            .clip(shape)
            .background(BrandColors.AltitudeCardUnselectedBackground)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(containerModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = altitude.label,
            style = if (selected) BrandTypography.AltitudeCardLabelSelected
            else BrandTypography.AltitudeCardLabel
        )
        AltitudeRadio(selected = selected)
    }
}

@Composable
private fun AltitudeRadio(modifier: Modifier = Modifier, selected: Boolean) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = if (selected) BrandColors.AltitudeCardSelectedBorder
                else BrandColors.RadioUnselected,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(BrandColors.AltitudeCardSelectedBorder)
            )
        }
    }
}

@Preview(name = "AltitudeFilterSection", showBackground = true, widthDp = 360)
@Composable
private fun AltitudeFilterSectionPreview() {
    CafeterosTheme {
        AltitudeFilterSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            selected = FilterAltitude.ALTURAS,
            onSelect = {}
        )
    }
}

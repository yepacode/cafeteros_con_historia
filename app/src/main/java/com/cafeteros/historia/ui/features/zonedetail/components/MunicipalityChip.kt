package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.zonedetail.model.Municipality
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Chip horizontal de municipio en la sección "Municipios".
 *
 * Composición vertical: nombre arriba, conteo "{n} FINCAS" abajo en gris.
 * El chip se anima entre dos estados (seleccionado / no seleccionado): el
 * default visual del seleccionado es fondo café oscuro con texto blanco;
 * los demás van en beige claro con texto oscuro.
 *
 * @param modifier modifier opcional.
 * @param municipality datos a renderizar.
 * @param isSelected estado actual.
 * @param onClick callback al pulsar.
 */
@Composable
fun MunicipalityChip(
    modifier: Modifier = Modifier,
    municipality: Municipality,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background by animateColorAsState(
        targetValue = if (isSelected) {
            BrandColors.MunicipalityChipSelectedBackground
        } else {
            BrandColors.MunicipalityChipUnselectedBackground
        },
        label = "municipalityBackground"
    )
    val nameColor by animateColorAsState(
        targetValue = if (isSelected) {
            BrandColors.MunicipalityChipSelectedText
        } else {
            BrandColors.TextPrimary
        },
        label = "municipalityName"
    )
    val countColor by animateColorAsState(
        targetValue = if (isSelected) {
            BrandColors.MunicipalityChipSelectedText.copy(alpha = 0.8f)
        } else {
            BrandColors.TextSecondary
        },
        label = "municipalityCount"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = municipality.name,
            style = BrandTypography.MunicipalityName.copy(color = nameColor)
        )
        Text(
            text = "${municipality.fincaCount} FINCAS",
            style = BrandTypography.MunicipalityCount.copy(color = countColor)
        )
    }
}

@Preview(name = "MunicipalityChip – seleccionado", showBackground = true)
@Composable
private fun MunicipalityChipSelectedPreview() {
    CafeterosTheme {
        MunicipalityChip(
            municipality = Municipality("San Gil", 12),
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview(name = "MunicipalityChip – no seleccionado", showBackground = true)
@Composable
private fun MunicipalityChipUnselectedPreview() {
    CafeterosTheme {
        MunicipalityChip(
            municipality = Municipality("Socorro", 8),
            isSelected = false,
            onClick = {}
        )
    }
}

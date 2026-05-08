package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.zonedetail.model.FlavorLevel
import com.cafeteros.historia.ui.features.zonedetail.model.FlavorTrait
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila individual de la card "Perfil de sabor": etiqueta a la izquierda,
 * barra de progreso al centro y nivel cualitativo a la derecha.
 *
 * La barra es puramente visual (no interactiva): el ratio se toma del
 * [FlavorLevel] del trait y se renderiza con un Box hijo cuya `fillMaxWidth`
 * usa la fracción correspondiente.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param trait característica a renderizar.
 */
@Composable
fun FlavorTraitSlider(
    modifier: Modifier = Modifier,
    trait: FlavorTrait
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = trait.name, style = BrandTypography.FlavorTraitLabel)
            Text(text = trait.level.label, style = BrandTypography.FlavorTraitValue)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(BrandColors.FlavorSliderTrack),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(trait.level.fillRatio)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(BrandColors.FlavorSliderFill)
            )
        }
    }
}

@Preview(name = "FlavorTraitSlider – ALTO", showBackground = true, widthDp = 360)
@Composable
private fun FlavorTraitSliderAltoPreview() {
    CafeterosTheme {
        FlavorTraitSlider(
            trait = FlavorTrait(name = "CUERPO", level = FlavorLevel.ALTO)
        )
    }
}

@Preview(name = "FlavorTraitSlider – BAJA", showBackground = true, widthDp = 360)
@Composable
private fun FlavorTraitSliderBajaPreview() {
    CafeterosTheme {
        FlavorTraitSlider(
            trait = FlavorTrait(name = "ACIDEZ", level = FlavorLevel.BAJA)
        )
    }
}

package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.zonedetail.model.FlavorLevel
import com.cafeteros.historia.ui.features.zonedetail.model.FlavorTrait
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card "Perfil de sabor": título + filas de pills (envueltas manualmente
 * cuando exceden el ancho) + cuatro [FlavorTraitSlider].
 *
 * El wrap manual se hace troceando la lista de tags en grupos de [tagsPerRow]
 * y renderizando un [Row] por grupo. Este enfoque es más simple que un
 * FlowRow real y nos evita depender de APIs experimentales o de versiones
 * específicas de Compose Foundation.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param tags notas de sabor a mostrar como pills.
 * @param traits características a mostrar como sliders.
 * @param tagsPerRow número máximo de pills por fila. Con 2 los textos
 *  largos como "Chocolate amargo" + "Cuerpo fuerte" caben sin ser
 *  aplastados en pantallas estrechas.
 */
@Composable
fun FlavorProfileCard(
    modifier: Modifier = Modifier,
    tags: List<String>,
    traits: List<FlavorTrait>,
    tagsPerRow: Int = 2
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BrandColors.FlavorCardBackground)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(text = "Perfil de sabor", style = BrandTypography.FlavorCardTitle)

        Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
            tags.chunked(tagsPerRow).forEach { rowTags ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
                ) {
                    rowTags.forEach { tag ->
                        FlavorTagPill(label = tag)
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
            traits.forEach { trait ->
                FlavorTraitSlider(trait = trait)
            }
        }
    }
}

@Preview(name = "FlavorProfileCard", showBackground = true, widthDp = 360)
@Composable
private fun FlavorProfileCardPreview() {
    CafeterosTheme {
        FlavorProfileCard(
            tags = listOf("Chocolate amargo", "Cuerpo fuerte", "Notas a nuez"),
            traits = listOf(
                FlavorTrait("ACIDEZ", FlavorLevel.BAJA),
                FlavorTrait("CUERPO", FlavorLevel.ALTO),
                FlavorTrait("DULZURA", FlavorLevel.MEDIA),
                FlavorTrait("AMARGOR", FlavorLevel.ALTO)
            )
        )
    }
}

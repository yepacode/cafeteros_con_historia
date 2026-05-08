package com.cafeteros.historia.ui.features.coffeemap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card de leyenda "PRODUCCIÓN" con barra de degradado horizontal.
 *
 * Visual: card blanca redondeada flotando arriba a la derecha del mapa,
 * con la etiqueta "PRODUCCIÓN" en mayúsculas, una barra de gradiente que
 * va de claro a oscuro de izquierda a derecha, y el caption "Alta" alineado
 * a la derecha bajo el extremo final del gradiente.
 *
 * El degradado usa los tokens [BrandColors.ProductionGradientStart] →
 * [BrandColors.ProductionGradientEnd] para mantener la paleta centralizada.
 *
 * @param modifier modifier opcional aplicado al contenedor de la card.
 */
@Composable
fun MapProductionLegend(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(BrandColors.MapInstructionCardBackground)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm)
            .width(140.dp)
    ) {
        Text(
            text = "PRODUCCIÓN",
            style = BrandTypography.MapLegendLabel
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            BrandColors.ProductionGradientStart,
                            BrandColors.ProductionGradientEnd
                        )
                    )
                )
        )
        Text(
            text = "Alta",
            style = BrandTypography.MapLegendCaption,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 2.dp)
        )
    }
}

@Preview(name = "MapProductionLegend", showBackground = true, widthDp = 200)
@Composable
private fun MapProductionLegendPreview() {
    CafeterosTheme {
        MapProductionLegend()
    }
}

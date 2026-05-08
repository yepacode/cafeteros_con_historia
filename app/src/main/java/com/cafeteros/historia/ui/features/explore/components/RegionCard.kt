package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.explore.model.CoffeeRegion
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card de zona cafetera dentro del carrusel "Explora por zona".
 *
 * Apila la imagen de la región como fondo, una capa con degradado vertical
 * (transparente → negro 60%) para asegurar contraste del texto, y al pie a
 * la izquierda el nombre de la zona y el conteo de caficultores en blanco.
 *
 * Cuando la región no trae imagen real, el fondo cae a su color sólido de
 * placeholder, manteniendo la composición del texto.
 *
 * @param modifier modifier opcional aplicado al [Box] contenedor.
 * @param region datos a renderizar.
 * @param onClick callback al pulsar la card.
 */
@Composable
fun RegionCard(
    modifier: Modifier = Modifier,
    region: CoffeeRegion,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(180.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(region.placeholderColor)
            .clickable(onClick = onClick)
    ) {
        if (region.imageRes != null) {
            Image(
                painter = painterResource(id = region.imageRes),
                contentDescription = region.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.65f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = region.name,
                style = BrandTypography.RegionCardTitle
            )
            Text(
                text = "${region.caficultorCount} caficultores",
                style = BrandTypography.RegionCardSubtitle
            )
        }
    }
}

@Preview(name = "RegionCard", showBackground = true, widthDp = 200)
@Composable
private fun RegionCardPreview() {
    CafeterosTheme {
        RegionCard(
            region = CoffeeRegion(
                name = "Huila",
                caficultorCount = 42,
                placeholderColor = Color(0xFF4F6E3F)
            )
        )
    }
}

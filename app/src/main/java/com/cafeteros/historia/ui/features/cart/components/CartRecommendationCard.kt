package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.features.cart.model.CartRecommendation
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card individual de "Recomendaciones del Origen" en el empty state del
 * carrito.
 *
 * Visual: fondo cream con esquinas redondeadas, imagen del producto con
 * borde redondeado arriba, nombre serif bold y línea uppercase con
 * metadata abajo.
 *
 * @param modifier modifier opcional.
 * @param recommendation datos a renderizar.
 * @param onClick callback al pulsar la card.
 */
@Composable
fun CartRecommendationCard(
    modifier: Modifier = Modifier,
    recommendation: CartRecommendation,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BrandColors.RecommendationCardBackground)
            .clickable(onClick = onClick)
            .padding(BrandSpacing.sm),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrandColors.CartItemImageBackground)
        ) {
            Image(
                painter = painterResource(id = recommendation.imageRes),
                contentDescription = recommendation.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = BrandSpacing.xs, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = recommendation.name,
                style = BrandTypography.RecommendationCardName,
                maxLines = 1
            )
            Text(
                text = recommendation.metaLabel,
                style = BrandTypography.RecommendationCardMeta,
                maxLines = 1
            )
        }
    }
}

@Preview(name = "CartRecommendationCard", showBackground = true, widthDp = 200)
@Composable
private fun CartRecommendationCardPreview() {
    CafeterosTheme {
        CartRecommendationCard(
            modifier = Modifier.padding(BrandSpacing.md),
            recommendation = CartRecommendation(
                id = "x",
                name = "Finca El Diviso",
                imageRes = R.drawable.cafe_origen_narino,
                metaLabel = "HUILA · LAVADO"
            )
        )
    }
}

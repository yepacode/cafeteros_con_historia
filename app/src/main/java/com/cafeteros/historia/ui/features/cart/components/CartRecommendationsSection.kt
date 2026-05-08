package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.cart.model.CartRecommendation
import com.cafeteros.historia.ui.features.cart.model.CartSampleData
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Recomendaciones del Origen" del empty state del carrito.
 *
 * Tiene un header italic alineado a la izquierda y, debajo, una fila con
 * dos [CartRecommendationCard] de igual ancho.
 *
 * @param modifier modifier opcional.
 * @param recommendations lista de recomendaciones (la pantalla limita
 *   visualmente a 2 cards por diseño; las extras se ignoran).
 * @param onRecommendationClick callback al pulsar una card.
 */
@Composable
fun CartRecommendationsSection(
    modifier: Modifier = Modifier,
    recommendations: List<CartRecommendation>,
    onRecommendationClick: (CartRecommendation) -> Unit = {}
) {
    if (recommendations.isEmpty()) return
    val visible = recommendations.take(2)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Recomendaciones del Origen", style = BrandTypography.RecommendationsHeader)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            visible.forEach { recommendation ->
                CartRecommendationCard(
                    recommendation = recommendation,
                    onClick = { onRecommendationClick(recommendation) },
                    modifier = Modifier.weight(1f)
                )
            }
            if (visible.size == 1) Box(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(name = "CartRecommendationsSection", showBackground = true, widthDp = 360)
@Composable
private fun CartRecommendationsSectionPreview() {
    CafeterosTheme {
        CartRecommendationsSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            recommendations = CartSampleData.recommendations
        )
    }
}

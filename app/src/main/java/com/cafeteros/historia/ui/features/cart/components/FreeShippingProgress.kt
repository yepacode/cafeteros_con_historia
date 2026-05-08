package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bloque de "Te faltan $X.XXX para envío gratis 🚚" + porcentaje a la
 * derecha + barra horizontal de progreso.
 *
 * Cuando [progress] llega a 1f (envío gratis ya alcanzado), el mensaje
 * cambia a "¡Tu envío es gratis!" — la decisión de copy queda en este
 * componente para que la pantalla parent solo le pase el porcentaje.
 *
 * @param modifier modifier opcional.
 * @param amountToFreeShipping cuánto falta en pesos para envío gratis.
 *   Si es 0, ya se alcanzó.
 * @param formattedAmount el [amountToFreeShipping] ya formateado en pesos
 *   ("$15.000") — se inyecta para mantener el componente puro.
 * @param progress 0..1, fracción ya cubierta del umbral de envío gratis.
 */
@Composable
fun FreeShippingProgress(
    modifier: Modifier = Modifier,
    amountToFreeShipping: Int,
    formattedAmount: String,
    progress: Float
) {
    val percentText = "${(progress * 100).toInt()}%"

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (amountToFreeShipping > 0) {
                Text(
                    text = "Te faltan $formattedAmount para envío gratis 🚚",
                    style = BrandTypography.FreeShippingMessage
                )
            } else {
                Text(
                    text = "¡Tu envío es gratis! 🚚",
                    style = BrandTypography.FreeShippingMessage
                )
            }
            Text(text = percentText, style = BrandTypography.FreeShippingPercent)
        }

        ProgressTrack(progress = progress)
    }
}

@Composable
private fun ProgressTrack(modifier: Modifier = Modifier, progress: Float) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(shape)
            .background(BrandColors.FreeShippingProgressTrack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .height(6.dp)
                .clip(shape)
                .background(BrandColors.FreeShippingProgressFill)
        )
    }
}

@Preview(name = "FreeShippingProgress", showBackground = true, widthDp = 360)
@Composable
private fun FreeShippingProgressPreview() {
    CafeterosTheme {
        FreeShippingProgress(
            modifier = Modifier.padding(BrandSpacing.lg),
            amountToFreeShipping = 15_000,
            formattedAmount = "$15.000",
            progress = 0.8f
        )
    }
}

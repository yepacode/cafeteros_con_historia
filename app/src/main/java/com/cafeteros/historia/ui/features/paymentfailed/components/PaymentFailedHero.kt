package com.cafeteros.historia.ui.features.paymentfailed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Hero del paso 4 (fallo): círculo rojo grande con un signo de exclamación
 * blanco. Sin confeti — el tono visual es de pausa y no de celebración.
 *
 * @param modifier modifier opcional.
 */
@Composable
fun PaymentFailedHero(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(BrandColors.PaymentFailedHeroBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.PriorityHigh,
                contentDescription = null,
                tint = BrandColors.PaymentFailedHeroIcon,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Preview(name = "PaymentFailedHero", showBackground = true, widthDp = 360)
@Composable
private fun PaymentFailedHeroPreview() {
    CafeterosTheme {
        PaymentFailedHero()
    }
}

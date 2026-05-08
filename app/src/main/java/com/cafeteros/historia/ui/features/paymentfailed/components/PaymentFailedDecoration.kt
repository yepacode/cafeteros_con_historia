package com.cafeteros.historia.ui.features.paymentfailed.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Ilustración decorativa muy tenue del grano de café para el paso 4 (fallo).
 *
 * Sirve como pausa visual entre el checklist y los CTAs sin competir con
 * ellos. Mientras no exista un asset vectorial dedicado, se usa el emoji
 * 🫘 con baja opacidad y tamaño grande — cuando se reciba el SVG real basta
 * con sustituir este Composable.
 *
 * @param modifier modifier opcional.
 */
@Composable
fun PaymentFailedDecoration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🫘",
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 96.sp,
                color = BrandColors.PaymentFailedDecorationTint
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "PaymentFailedDecoration", showBackground = true, widthDp = 360)
@Composable
private fun PaymentFailedDecorationPreview() {
    CafeterosTheme {
        PaymentFailedDecoration()
    }
}

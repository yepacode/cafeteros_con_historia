package com.cafeteros.historia.ui.features.orderreview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bottom bar fijo del paso 3 del checkout: CTA "Confirmar y pagar" con el
 * total embebido y un texto pequeño bajo el botón con el footer legal.
 *
 * El CTA queda deshabilitado visualmente cuando [enabled] es false (mientras
 * el usuario aún no acepta los términos).
 *
 * @param modifier modifier opcional.
 * @param formattedTotal total a pagar ya formateado ("$146.000").
 * @param enabled si false, el CTA se deshabilita visualmente.
 * @param onConfirm callback del CTA "Confirmar y pagar".
 */
@Composable
fun ConfirmPaymentBottomBar(
    modifier: Modifier = Modifier,
    formattedTotal: String,
    enabled: Boolean = true,
    onConfirm: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.OrderReviewConfirmBarBackground)
            .navigationBarsPadding()
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (enabled) BrandColors.OrderReviewConfirmCtaBackground
                    else BrandColors.OrderReviewConfirmCtaBackground.copy(alpha = 0.5f)
                )
                .clickable(enabled = enabled, onClick = onConfirm),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Confirmar y pagar $formattedTotal",
                style = BrandTypography.OrderReviewConfirmCtaLabel
            )
        }

        Text(
            text = footerLegal(),
            style = BrandTypography.OrderReviewFooterTerms,
            textAlign = TextAlign.Center
        )
    }
}

private fun footerLegal() = buildAnnotatedString {
    append("Al pagar aceptas nuestros ")
    withStyle(
        SpanStyle(
            color = BrandColors.OrderReviewFooterTermsText,
            textDecoration = TextDecoration.Underline
        )
    ) {
        append("Términos y Condiciones")
    }
}

@Preview(name = "ConfirmPaymentBottomBar — habilitado", showBackground = true, widthDp = 360)
@Composable
private fun ConfirmPaymentBottomBarEnabledPreview() {
    CafeterosTheme {
        ConfirmPaymentBottomBar(
            formattedTotal = "$146.000",
            onConfirm = {}
        )
    }
}

@Preview(name = "ConfirmPaymentBottomBar — deshabilitado", showBackground = true, widthDp = 360)
@Composable
private fun ConfirmPaymentBottomBarDisabledPreview() {
    CafeterosTheme {
        ConfirmPaymentBottomBar(
            formattedTotal = "$146.000",
            enabled = false,
            onConfirm = {}
        )
    }
}

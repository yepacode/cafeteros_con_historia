package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bottom bar fijo del paso 2: total a la izquierda, CTA "Revisar pedido"
 * a la derecha.
 *
 * @param modifier modifier opcional.
 * @param formattedTotal total a pagar ya formateado ("$146.000").
 * @param enabled si false, el CTA se deshabilita visualmente.
 * @param onReview callback del CTA "Revisar pedido".
 */
@Composable
fun PaymentBottomBar(
    modifier: Modifier = Modifier,
    formattedTotal: String,
    enabled: Boolean = true,
    onReview: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.PaymentBackground)
            .navigationBarsPadding()
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = "TOTAL A PAGAR", style = BrandTypography.PaymentTotalLabel)
            Text(text = formattedTotal, style = BrandTypography.PaymentTotalValue)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (enabled) BrandColors.PaymentReviewCtaBackground
                    else BrandColors.PaymentReviewCtaBackground.copy(alpha = 0.5f)
                )
                .clickable(enabled = enabled, onClick = onReview),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Revisar pedido", style = BrandTypography.PaymentReviewCtaLabel)
        }
    }
}

@Preview(name = "PaymentBottomBar", showBackground = true, widthDp = 360)
@Composable
private fun PaymentBottomBarPreview() {
    CafeterosTheme {
        PaymentBottomBar(
            formattedTotal = "$146.000",
            onReview = {}
        )
    }
}

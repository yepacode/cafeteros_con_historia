package com.cafeteros.historia.ui.features.orderreview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.orderreview.model.OrderReviewPaymentSummary
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card resumen del método de pago elegido en el paso 2.
 *
 * Layout: badge blanco con texto italic "VISA" a la izquierda + texto
 * "Visa **** 4567" a la derecha. Es puramente informativa — el cambio se
 * hace mediante el enlace "Editar" del header de la sección.
 *
 * @param modifier modifier opcional.
 * @param payment resumen del método de pago.
 */
@Composable
fun PaymentMethodSummaryCard(
    modifier: Modifier = Modifier,
    payment: OrderReviewPaymentSummary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.OrderReviewPaymentCardBackground)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        VisaBadge()
        Text(
            text = "${payment.brand} **** ${payment.last4}",
            style = BrandTypography.OrderReviewPaymentSummaryText
        )
    }
}

@Composable
private fun VisaBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(48.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(BrandColors.OrderReviewVisaBadgeBackground)
            .border(
                width = 1.dp,
                color = BrandColors.OrderReviewVisaBadgeBorder,
                shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "VISA", style = BrandTypography.OrderReviewVisaBadgeLabel)
    }
}

@Preview(name = "PaymentMethodSummaryCard", showBackground = true, widthDp = 360)
@Composable
private fun PaymentMethodSummaryCardPreview() {
    CafeterosTheme {
        PaymentMethodSummaryCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            payment = OrderReviewPaymentSummary(brand = "Visa", last4 = "4567")
        )
    }
}

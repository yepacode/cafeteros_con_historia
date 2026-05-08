package com.cafeteros.historia.ui.features.paymentfailed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.paymentfailed.model.PaymentFailureReason
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card rosa-roja con el motivo del fallo y el código de error.
 *
 * Layout:
 *  - Triángulo rojo a la izquierda.
 *  - Columna a la derecha: "Motivo: <reason>" en rojo bold + "CÓDIGO DE
 *    ERROR: <code>" en uppercase letter-spacing.
 *
 * @param modifier modifier opcional.
 * @param reason motivo del fallo a renderizar.
 */
@Composable
fun PaymentErrorReasonCard(
    modifier: Modifier = Modifier,
    reason: PaymentFailureReason
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(BrandColors.PaymentFailedReasonCardBackground)
            .border(
                width = 1.dp,
                color = BrandColors.PaymentFailedReasonCardBorder,
                shape = shape
            )
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            tint = BrandColors.PaymentFailedReasonAccent,
            modifier = Modifier.size(20.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Motivo: ${reason.reasonLabel}",
                style = BrandTypography.PaymentFailedReasonText
            )
            Text(
                text = "CÓDIGO DE ERROR: ${reason.errorCode}",
                style = BrandTypography.PaymentFailedReasonCode
            )
        }
    }
}

@Preview(name = "PaymentErrorReasonCard", showBackground = true, widthDp = 360)
@Composable
private fun PaymentErrorReasonCardPreview() {
    CafeterosTheme {
        PaymentErrorReasonCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            reason = PaymentFailureReason(
                reasonLabel = "Fondos insuficientes",
                errorCode = "51"
            )
        )
    }
}

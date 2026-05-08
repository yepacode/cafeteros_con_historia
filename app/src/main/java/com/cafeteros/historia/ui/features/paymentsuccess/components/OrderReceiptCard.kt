package com.cafeteros.historia.ui.features.paymentsuccess.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.paymentsuccess.model.ConfirmedOrder
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card blanca con el resumen del pedido recién confirmado.
 *
 * Layout:
 *  - Header: ícono de caja + título "Tu pedido".
 *  - 4 filas etiqueta-valor: Número, Total pagado, Entrega estimada, Método.
 *  - El método trae un pequeño ícono de tarjeta antes del label.
 *
 * @param modifier modifier opcional.
 * @param order pedido confirmado a renderizar.
 */
@Composable
fun OrderReceiptCard(
    modifier: Modifier = Modifier,
    order: ConfirmedOrder
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BrandColors.OrderReceiptCardBackground)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                tint = BrandColors.OrderReceiptIconTint,
                modifier = Modifier.size(20.dp)
            )
            Text(text = "Tu pedido", style = BrandTypography.OrderReceiptCardTitle)
        }

        ReceiptRow(label = "Número", value = order.orderNumber)
        ReceiptRow(label = "Total pagado", value = order.totalPaidFormatted)
        ReceiptRow(label = "Entrega estimada", value = order.estimatedDelivery)
        PaymentReceiptRow(label = "Método", value = order.paymentLabel)
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = BrandTypography.OrderReceiptRowLabel)
        Text(text = value, style = BrandTypography.OrderReceiptRowValue)
    }
}

@Composable
private fun PaymentReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = BrandTypography.OrderReceiptRowLabel)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
        ) {
            Icon(
                imageVector = Icons.Outlined.CreditCard,
                contentDescription = null,
                tint = BrandColors.OrderReceiptRowValue,
                modifier = Modifier.size(16.dp)
            )
            Text(text = value, style = BrandTypography.OrderReceiptRowValue)
        }
    }
}

@Preview(name = "OrderReceiptCard", showBackground = true, widthDp = 360)
@Composable
private fun OrderReceiptCardPreview() {
    CafeterosTheme {
        OrderReceiptCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            order = ConfirmedOrder(
                orderNumber = "#OR-34521",
                totalPaidFormatted = "$146.000",
                estimatedDelivery = "Martes 23 de abril",
                paymentLabel = "Visa **** 4567"
            )
        )
    }
}

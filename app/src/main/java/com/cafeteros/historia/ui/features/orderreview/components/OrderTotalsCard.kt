package com.cafeteros.historia.ui.features.orderreview.components

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
import com.cafeteros.historia.ui.features.orderreview.model.OrderTotals
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card con el resumen de costos del pedido (Subtotal, Envío, Descuento)
 * y el bloque inferior con "Total a pagar" + cifra grande dorada.
 *
 * Layout vertical en 3 zonas:
 *  1. Tres filas etiqueta-valor (subtotal, envío, descuento).
 *  2. Línea separadora horizontal.
 *  3. Fila final con "Total a pagar" + "IVA INCLUIDO" a la izquierda y la
 *     cifra grande dorada alineada a la derecha.
 *
 * @param modifier modifier opcional.
 * @param totals resumen de costos.
 */
@Composable
fun OrderTotalsCard(
    modifier: Modifier = Modifier,
    totals: OrderTotals
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BrandColors.OrderReviewTotalsCardBackground)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        TotalLine(label = "Subtotal", value = totals.subtotalFormatted)
        TotalLine(label = "Envío", value = totals.shippingFormatted)
        TotalLine(
            label = "Descuento",
            value = totals.discountFormatted,
            valueColor = BrandColors.OrderReviewDiscountValue
        )

        Box(
            modifier = Modifier
                .padding(top = BrandSpacing.xs, bottom = BrandSpacing.xs)
                .fillMaxWidth()
                .height(1.dp)
                .background(BrandColors.OrderReviewTotalsDivider)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = "Total a pagar", style = BrandTypography.OrderReviewTotalLabel)
                Text(text = "IVA INCLUIDO", style = BrandTypography.OrderReviewIvaIncluido)
            }
            Text(text = totals.totalFormatted, style = BrandTypography.OrderReviewTotalAmount)
        }
    }
}

@Composable
private fun TotalLine(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = BrandTypography.OrderReviewTotalLineLabel)
        val valueStyle = BrandTypography.OrderReviewTotalLineValue.let { base ->
            if (valueColor != null) base.copy(color = valueColor) else base
        }
        Text(text = value, style = valueStyle)
    }
}

@Preview(name = "OrderTotalsCard", showBackground = true, widthDp = 360)
@Composable
private fun OrderTotalsCardPreview() {
    CafeterosTheme {
        OrderTotalsCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            totals = OrderTotals(
                subtotalFormatted = "$144.000",
                shippingFormatted = "$12.000",
                discountFormatted = "-$10.000",
                totalFormatted = "$146.000"
            )
        )
    }
}

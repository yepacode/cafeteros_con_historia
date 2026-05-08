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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card del resumen de costos del carrito.
 *
 * Filas:
 *  - Subtotal: $X
 *  - Envío:    $X
 *  - Descuento: -$X (verde, opcional — se omite si discountFormatted es null)
 *  - Línea separadora
 *  - Total:    $X (dorado grande)
 *
 * Los precios entran ya formateados — el componente se mantiene puro.
 *
 * @param modifier modifier opcional.
 * @param subtotalFormatted subtotal en pesos formateados.
 * @param shippingFormatted envío en pesos formateados.
 * @param discountFormatted descuento positivo formateado (sin signo); el
 *   componente le antepone "-". Pasa `null` si no hay descuento.
 * @param totalFormatted total final formateado.
 */
@Composable
fun CartSummaryCard(
    modifier: Modifier = Modifier,
    subtotalFormatted: String,
    shippingFormatted: String,
    discountFormatted: String?,
    totalFormatted: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.CartSummaryBackground)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        SummaryRow(
            label = "Subtotal",
            value = subtotalFormatted,
            valueStyle = BrandTypography.CartSummaryValue
        )
        SummaryRow(
            label = "Envío",
            value = shippingFormatted,
            valueStyle = BrandTypography.CartSummaryValue
        )
        if (discountFormatted != null) {
            SummaryRow(
                label = "Descuento",
                value = "-$discountFormatted",
                valueStyle = BrandTypography.CartDiscountValue
            )
        }

        Box(
            modifier = Modifier
                .padding(vertical = BrandSpacing.sm)
                .fillMaxWidth()
                .height(1.dp)
                .background(BrandColors.CartSummaryDivider)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total", style = BrandTypography.CartTotalLabel)
            Text(text = totalFormatted, style = BrandTypography.CartTotalValue)
        }
    }
}

@Composable
private fun SummaryRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueStyle: TextStyle
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = BrandTypography.CartSummaryLabel)
        Text(text = value, style = valueStyle)
    }
}

@Preview(name = "CartSummaryCard", showBackground = true, widthDp = 360)
@Composable
private fun CartSummaryCardPreview() {
    CafeterosTheme {
        CartSummaryCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            subtotalFormatted = "$144.000",
            shippingFormatted = "$12.000",
            discountFormatted = "$10.000",
            totalFormatted = "$146.000"
        )
    }
}

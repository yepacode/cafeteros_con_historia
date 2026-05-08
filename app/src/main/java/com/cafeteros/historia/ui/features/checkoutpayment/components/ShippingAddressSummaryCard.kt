package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
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
 * Card de resumen "DIRECCIÓN DE ENVÍO" mostrada al inicio del paso 2.
 *
 * Layout: círculo verde con pin + bloque de dos líneas (label uppercase y
 * dirección) + enlace verde "Cambiar" alineado a la derecha.
 *
 * Solo es informativa: el cambio real de dirección se hace al volver al
 * paso 1, por eso [onChange] dispara la navegación back, no abre un sheet.
 *
 * @param modifier modifier opcional.
 * @param addressLine línea con dirección + ciudad ("Cra 10 #42-15, Bogotá").
 * @param onChange callback del enlace "Cambiar".
 */
@Composable
fun ShippingAddressSummaryCard(
    modifier: Modifier = Modifier,
    addressLine: String,
    onChange: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.PaymentAddressSummaryBackground)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = BrandColors.PaymentAddressSummaryIconBackground,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = BrandColors.PaymentAddressSummaryIconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "DIRECCIÓN DE ENVÍO",
                style = BrandTypography.PaymentAddressSummaryLabel
            )
            Text(text = addressLine, style = BrandTypography.PaymentAddressSummaryLine)
        }
        Text(
            text = "Cambiar",
            style = BrandTypography.PaymentAddressSummaryChange,
            modifier = Modifier
                .clickable(onClick = onChange)
                .padding(horizontal = BrandSpacing.xs, vertical = 4.dp)
        )
    }
}

@Preview(name = "ShippingAddressSummaryCard", showBackground = true, widthDp = 360)
@Composable
private fun ShippingAddressSummaryCardPreview() {
    CafeterosTheme {
        ShippingAddressSummaryCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            addressLine = "Cra 10 #42-15, Bogotá"
        )
    }
}

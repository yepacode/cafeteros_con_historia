package com.cafeteros.historia.ui.features.orderreview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
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
 * Card resumen "Envío a" del paso 3 del checkout.
 *
 * Layout: pin a la izquierda y dos líneas a la derecha — primero la dirección
 * principal (calle/número), debajo la ciudad y departamento.
 *
 * @param modifier modifier opcional.
 * @param addressLine línea principal ("Cra 10 #42-15").
 * @param secondaryLine línea secundaria con ciudad y departamento.
 */
@Composable
fun ShippingDestinationCard(
    modifier: Modifier = Modifier,
    addressLine: String,
    secondaryLine: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.OrderReviewShippingCardBackground)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Icon(
            imageVector = Icons.Filled.Place,
            contentDescription = null,
            tint = BrandColors.OrderReviewShippingPinTint,
            modifier = Modifier.size(22.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = addressLine, style = BrandTypography.OrderReviewAddressLine)
            Text(text = secondaryLine, style = BrandTypography.OrderReviewAddressSecondary)
        }
    }
}

@Preview(name = "ShippingDestinationCard", showBackground = true, widthDp = 360)
@Composable
private fun ShippingDestinationCardPreview() {
    CafeterosTheme {
        ShippingDestinationCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            addressLine = "Cra 10 #42-15",
            secondaryLine = "Bogotá, Cundinamarca"
        )
    }
}

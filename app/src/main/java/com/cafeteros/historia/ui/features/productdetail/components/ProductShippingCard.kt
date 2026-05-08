package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.LocalShipping
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
 * Card horizontal con la info de envío:
 *  - Círculo con un ícono de camión a la izquierda.
 *  - Bloque vertical "Envío a [city]" + "Llega en…".
 *
 * @param modifier modifier opcional.
 * @param city ciudad destino del envío.
 * @param eta tiempo estimado de llegada.
 */
@Composable
fun ProductShippingCard(
    modifier: Modifier = Modifier,
    city: String,
    eta: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.ShippingCardBackground)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(BrandColors.ShippingIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.LocalShipping,
                contentDescription = null,
                tint = BrandColors.ShippingIconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = "Envío a $city", style = BrandTypography.ShippingCardTitle)
            Text(text = eta, style = BrandTypography.ShippingCardSubtitle)
        }
    }
}

@Preview(name = "ProductShippingCard", showBackground = true, widthDp = 360)
@Composable
private fun ProductShippingCardPreview() {
    CafeterosTheme {
        ProductShippingCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            city = "Bogotá",
            eta = "Llega en 2-3 días hábiles"
        )
    }
}

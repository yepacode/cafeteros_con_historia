package com.cafeteros.historia.ui.features.cart.components

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
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.cart.model.CartShipping
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card de envío del carrito: ícono circular verde a la izquierda, info de
 * llegada y dirección, y enlace "Cambiar" a la derecha.
 *
 * @param modifier modifier opcional.
 * @param shipping datos a renderizar.
 * @param onChange callback del enlace "Cambiar".
 */
@Composable
fun CartShippingCard(
    modifier: Modifier = Modifier,
    shipping: CartShipping,
    onChange: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.CartShippingCardBackground)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(BrandColors.CartShippingIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.LocalShipping,
                contentDescription = null,
                tint = BrandColors.CartShippingIconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = shipping.arrivalLabel, style = BrandTypography.CartShippingTitle)
            Text(text = shipping.addressLabel, style = BrandTypography.CartShippingSubtitle)
        }

        Text(
            text = "Cambiar",
            style = BrandTypography.CartShippingChange,
            modifier = Modifier
                .clickable(onClick = onChange)
                .padding(start = BrandSpacing.sm)
        )
    }
}

@Preview(name = "CartShippingCard", showBackground = true, widthDp = 360)
@Composable
private fun CartShippingCardPreview() {
    CafeterosTheme {
        CartShippingCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            shipping = CartShipping(
                arrivalLabel = "Llega el martes 23 de abril",
                addressLabel = "Envío a Cra 10 #42-15, Bogotá"
            )
        )
    }
}

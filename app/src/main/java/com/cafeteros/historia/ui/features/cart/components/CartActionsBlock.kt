package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
 * Bloque de acciones al final del carrito:
 *  - CTA principal "Continuar al pago →" (botón ancho oscuro).
 *  - Enlace "Seguir comprando" (verde) debajo, centrado.
 *
 * @param modifier modifier opcional.
 * @param onCheckout callback del CTA principal.
 * @param onContinueShopping callback del enlace.
 */
@Composable
fun CartActionsBlock(
    modifier: Modifier = Modifier,
    onCheckout: () -> Unit = {},
    onContinueShopping: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(BrandColors.CartCheckoutButtonBackground)
                .clickable(onClick = onCheckout)
                .padding(vertical = 18.dp, horizontal = BrandSpacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Continuar al pago", style = BrandTypography.CartCheckoutCta)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = BrandColors.CartCheckoutButtonText,
                modifier = Modifier
                    .padding(start = BrandSpacing.sm)
                    .size(18.dp)
            )
        }

        Text(
            text = "Seguir comprando",
            style = BrandTypography.CartContinueShopping,
            modifier = Modifier
                .clickable(onClick = onContinueShopping)
                .padding(vertical = BrandSpacing.xs, horizontal = BrandSpacing.md)
        )
    }
}

@Preview(name = "CartActionsBlock", showBackground = true, widthDp = 360)
@Composable
private fun CartActionsBlockPreview() {
    CafeterosTheme {
        CartActionsBlock(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

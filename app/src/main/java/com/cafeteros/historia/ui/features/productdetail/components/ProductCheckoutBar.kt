package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
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
 * Bottom bar fijo de compra. A la izquierda muestra "TOTAL" pequeño y el
 * total grande; a la derecha el botón ancho café oscuro "Agregar al carrito"
 * con un ícono de bolsa.
 *
 * Se queda anclado al fondo de la pantalla (lo monta el screen como hijo
 * fuera del scroll) y respeta los gestos del navigation bar.
 *
 * @param modifier modifier opcional.
 * @param formattedTotal total ya formateado ("$48.000").
 * @param enabled si false, el botón se deshabilita visualmente.
 * @param onAddToCart callback al pulsar "Agregar al carrito".
 */
@Composable
fun ProductCheckoutBar(
    modifier: Modifier = Modifier,
    formattedTotal: String,
    enabled: Boolean = true,
    onAddToCart: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.ProductDetailBackground)
            .navigationBarsPadding()
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = "TOTAL", style = BrandTypography.CheckoutTotalLabel)
            Text(text = formattedTotal, style = BrandTypography.CheckoutTotalValue)
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    if (enabled) BrandColors.WideDarkButtonBackground
                    else BrandColors.WideDarkButtonBackground.copy(alpha = 0.5f)
                )
                .clickable(enabled = enabled, onClick = onAddToCart)
                .padding(horizontal = BrandSpacing.lg, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Agregar al carrito", style = BrandTypography.CheckoutCtaLabel)
            Icon(
                imageVector = Icons.Outlined.ShoppingBag,
                contentDescription = null,
                tint = BrandColors.PrimaryButtonText,
                modifier = Modifier
                    .padding(start = BrandSpacing.sm)
                    .size(18.dp)
            )
        }
    }
}

@Preview(name = "ProductCheckoutBar", showBackground = true, widthDp = 360)
@Composable
private fun ProductCheckoutBarPreview() {
    CafeterosTheme {
        ProductCheckoutBar(
            formattedTotal = "$48.000",
            onAddToCart = {}
        )
    }
}

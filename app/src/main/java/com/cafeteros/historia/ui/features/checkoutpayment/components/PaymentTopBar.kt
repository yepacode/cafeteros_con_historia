package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
 * Top bar del paso 2 del checkout.
 *
 * Layout: flecha back a la izquierda, título italic centrado a la izquierda,
 * y un ícono de bolsa de compras como acción a la derecha (atajo al carrito).
 *
 * Se mantiene aparte del [com.cafeteros.historia.ui.features.checkoutshipping
 * .components.CheckoutTopBar] porque ese top bar no admite acciones a la
 * derecha — añadirlas allí afectaría al paso 1.
 *
 * @param modifier modifier opcional.
 * @param title título serif italic ("Método de pago").
 * @param onBack callback de la flecha.
 * @param onOpenCart callback del ícono de la bolsa de la derecha.
 */
@Composable
fun PaymentTopBar(
    modifier: Modifier = Modifier,
    title: String = "Método de pago",
    onBack: () -> Unit = {},
    onOpenCart: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = BrandColors.TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = title,
            style = BrandTypography.PaymentTopBarTitle,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onOpenCart),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.ShoppingBag,
                contentDescription = "Ver carrito",
                tint = BrandColors.PaymentTopBarBagIcon,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Preview(name = "PaymentTopBar", showBackground = true, widthDp = 360)
@Composable
private fun PaymentTopBarPreview() {
    CafeterosTheme {
        PaymentTopBar()
    }
}

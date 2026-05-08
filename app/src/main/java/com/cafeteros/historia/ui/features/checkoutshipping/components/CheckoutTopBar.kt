package com.cafeteros.historia.ui.features.checkoutshipping.components

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
 * Top bar de las pantallas del checkout.
 *
 * Layout simple: flecha back + título italic alineado a la izquierda.
 * Sin "Limpiar" ni acciones a la derecha — el flujo de pago no permite
 * "limpiar" en este punto.
 *
 * @param modifier modifier opcional.
 * @param title título serif italic ("Dirección de Envío").
 * @param onBack callback de la flecha.
 */
@Composable
fun CheckoutTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBack: () -> Unit = {}
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
        Text(text = title, style = BrandTypography.CheckoutTopBarTitle)
    }
}

@Preview(name = "CheckoutTopBar", showBackground = true, widthDp = 360)
@Composable
private fun CheckoutTopBarPreview() {
    CafeterosTheme {
        CheckoutTopBar(title = "Dirección de Envío")
    }
}

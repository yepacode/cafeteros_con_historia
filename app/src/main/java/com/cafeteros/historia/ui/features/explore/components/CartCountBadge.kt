package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Badge numérico amarillo que se sobrepone al ícono del carrito.
 *
 * Se asegura de un tamaño mínimo (16dp) para que números de un solo dígito
 * mantengan la forma circular legible y crece horizontalmente cuando el
 * conteo tiene más dígitos.
 *
 * @param modifier modifier opcional.
 * @param count número de items; se renderiza tal cual hasta 9 y como "9+"
 *  cuando supera ese límite, siguiendo la convención de apps de e-commerce.
 */
@Composable
fun CartCountBadge(
    modifier: Modifier = Modifier,
    count: Int
) {
    val displayValue = if (count > 9) "9+" else count.toString()
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 16.dp, minHeight = 16.dp)
            .clip(CircleShape)
            .background(BrandColors.CartBadgeBackground)
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayValue,
            style = BrandTypography.CartBadgeNumber
        )
    }
}

@Preview(name = "CartCountBadge", showBackground = true)
@Composable
private fun CartCountBadgePreview() {
    CafeterosTheme {
        CartCountBadge(count = 3)
    }
}

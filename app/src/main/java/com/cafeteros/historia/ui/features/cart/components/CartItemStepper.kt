package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
 * Pill blanca con `- N +` para cambiar la cantidad de un item del carrito.
 *
 * Visualmente distinto del stepper del detalle de producto (que vive sobre
 * fondo gris-beige): aquí la pill es blanca limpia y los botones no tienen
 * fondo extra — solo el ícono es clickable.
 *
 * @param modifier modifier opcional.
 * @param quantity cantidad actual.
 * @param min mínimo permitido.
 * @param max máximo permitido.
 * @param onQuantityChange callback con la cantidad nueva.
 */
@Composable
fun CartItemStepper(
    modifier: Modifier = Modifier,
    quantity: Int,
    min: Int = 1,
    max: Int = 99,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(BrandColors.CartStepperBackground)
            .padding(horizontal = BrandSpacing.sm, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        StepperIcon(
            icon = Icons.Filled.Remove,
            contentDescription = "Disminuir cantidad",
            enabled = quantity > min,
            onClick = { if (quantity > min) onQuantityChange(quantity - 1) }
        )
        Box(
            modifier = Modifier.width(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = quantity.toString(), style = BrandTypography.CartItemStepperValue)
        }
        StepperIcon(
            icon = Icons.Filled.Add,
            contentDescription = "Aumentar cantidad",
            enabled = quantity < max,
            onClick = { if (quantity < max) onQuantityChange(quantity + 1) }
        )
    }
}

@Composable
private fun StepperIcon(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clickable(enabled = enabled, onClick = onClick, onClickLabel = contentDescription),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) BrandColors.TextPrimary else BrandColors.TextSecondary,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(name = "CartItemStepper", showBackground = true)
@Composable
private fun CartItemStepperPreview() {
    CafeterosTheme {
        CartItemStepper(
            modifier = Modifier.padding(BrandSpacing.md),
            quantity = 2,
            onQuantityChange = {}
        )
    }
}

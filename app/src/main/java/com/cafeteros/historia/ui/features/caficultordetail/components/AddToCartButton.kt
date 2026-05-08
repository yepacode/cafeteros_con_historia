package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Botón cuadrado pequeño café oscuro con un "+" blanco para añadir el
 * producto al carrito desde la card.
 *
 * @param modifier modifier opcional.
 * @param onClick callback al pulsar.
 */
@Composable
fun AddToCartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(BrandColors.AddToCartButtonBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Agregar al carrito",
            tint = BrandColors.AddToCartButtonIcon,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Preview(name = "AddToCartButton", showBackground = true)
@Composable
private fun AddToCartButtonPreview() {
    CafeterosTheme {
        AddToCartButton(onClick = {})
    }
}

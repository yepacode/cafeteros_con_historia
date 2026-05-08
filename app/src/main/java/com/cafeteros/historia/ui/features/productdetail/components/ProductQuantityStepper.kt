package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
 * Pill horizontal con la etiqueta "Cantidad" a la izquierda y, a la derecha,
 * el stepper "−  N  +" sobre fondo gris-beige.
 *
 * Limita el valor entre [min] y [max] para que la UI nunca pueda salirse
 * del rango aceptado.
 *
 * @param modifier modifier opcional.
 * @param quantity cantidad actual.
 * @param min mínimo permitido.
 * @param max máximo permitido.
 * @param onQuantityChange callback con la cantidad nueva.
 */
@Composable
fun ProductQuantityStepper(
    modifier: Modifier = Modifier,
    quantity: Int,
    min: Int = 1,
    max: Int = 99,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Cantidad", style = BrandTypography.QuantityLabel)

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(BrandColors.QuantityStepperBackground)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            StepperButton(
                onClick = { if (quantity > min) onQuantityChange(quantity - 1) },
                contentDescription = "Disminuir cantidad"
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = null,
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Box(
                modifier = Modifier.width(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = quantity.toString(), style = BrandTypography.QuantityValue)
            }

            StepperButton(
                onClick = { if (quantity < max) onQuantityChange(quantity + 1) },
                contentDescription = "Aumentar cantidad"
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun StepperButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentDescription: String,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(BrandColors.QuantityStepperButtonBackground)
            .clickable(onClick = onClick, onClickLabel = contentDescription),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(name = "ProductQuantityStepper", showBackground = true, widthDp = 360)
@Composable
private fun ProductQuantityStepperPreview() {
    CafeterosTheme {
        ProductQuantityStepper(
            modifier = Modifier.padding(BrandSpacing.lg),
            quantity = 1,
            onQuantityChange = {}
        )
    }
}

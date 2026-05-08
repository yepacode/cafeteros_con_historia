package com.cafeteros.historia.ui.features.checkoutshipping.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card "Agregar nueva dirección" con borde dasheado.
 *
 * Visual: borde dasheado redondeado, círculo amarillo con "+" centrado
 * arriba, texto verde debajo.
 *
 * El borde dasheado se dibuja con `drawBehind` + `PathEffect.dashPathEffect`
 * porque `Modifier.border` solo soporta sólido.
 *
 * @param modifier modifier opcional.
 * @param onClick callback al pulsar la card.
 */
@Composable
fun AddNewAddressCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val cornerRadius = 14f
    val strokeWidth = 2f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = BrandColors.AddNewAddressBorder,
                    cornerRadius = CornerRadius(cornerRadius * density, cornerRadius * density),
                    style = Stroke(
                        width = strokeWidth * density,
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(12f * density, 8f * density),
                            phase = 0f
                        )
                    )
                )
            }
            .clickable(onClick = onClick)
            .padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = BrandColors.AddNewAddressIconBackground,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = BrandColors.AddNewAddressIconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(text = "Agregar nueva dirección", style = BrandTypography.AddNewAddressLabel)
    }
}

@Preview(name = "AddNewAddressCard", showBackground = true, widthDp = 360)
@Composable
private fun AddNewAddressCardPreview() {
    CafeterosTheme {
        AddNewAddressCard(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

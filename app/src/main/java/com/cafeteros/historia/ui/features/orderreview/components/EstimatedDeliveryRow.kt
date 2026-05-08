package com.cafeteros.historia.ui.features.orderreview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila de "Entrega estimada" del paso 3 — sin tarjeta, solo ícono + texto.
 *
 * @param modifier modifier opcional.
 * @param rangeLabel rango ya formateado ("Martes 23 de abril -
 *   Miércoles 24 de abril").
 */
@Composable
fun EstimatedDeliveryRow(
    modifier: Modifier = Modifier,
    rangeLabel: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Icon(
            imageVector = Icons.Filled.LocalShipping,
            contentDescription = null,
            tint = BrandColors.OrderReviewDeliveryTruckTint,
            modifier = Modifier.size(28.dp)
        )
        Text(text = rangeLabel, style = BrandTypography.OrderReviewDeliveryLine)
    }
}

@Preview(name = "EstimatedDeliveryRow", showBackground = true, widthDp = 360)
@Composable
private fun EstimatedDeliveryRowPreview() {
    CafeterosTheme {
        EstimatedDeliveryRow(
            modifier = Modifier.padding(BrandSpacing.lg),
            rangeLabel = "Martes 23 de abril - Miércoles 24 de abril"
        )
    }
}

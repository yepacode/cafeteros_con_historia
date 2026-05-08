package com.cafeteros.historia.ui.features.orderreview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.orderreview.model.OrderLineItem
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila de un producto dentro del bloque "Productos (N)" del paso 3.
 *
 * Layout: thumbnail circular marrón a la izquierda, columna central con el
 * nombre del producto y la línea "N x precio_unitario", y precio total
 * alineado a la derecha. El thumbnail muestra una etiqueta editorial
 * ("CAFÉ ORIGEN") como placeholder hasta que existan imágenes reales.
 *
 * @param modifier modifier opcional.
 * @param item línea del pedido a renderizar.
 */
@Composable
fun OrderProductRow(
    modifier: Modifier = Modifier,
    item: OrderLineItem
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        ProductThumb(label = item.thumbLabel)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = item.name, style = BrandTypography.OrderReviewProductName)
            Text(
                text = "${item.quantity} x ${item.unitPriceFormatted}",
                style = BrandTypography.OrderReviewProductMeta
            )
        }
        Text(text = item.totalPriceFormatted, style = BrandTypography.OrderReviewProductPrice)
    }
}

@Composable
private fun ProductThumb(modifier: Modifier = Modifier, label: String) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.OrderReviewProductThumbBackground)
            .padding(BrandSpacing.xs),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = BrandTypography.OrderReviewProductThumbLabel,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "OrderProductRow", showBackground = true, widthDp = 360)
@Composable
private fun OrderProductRowPreview() {
    CafeterosTheme {
        OrderProductRow(
            modifier = Modifier.padding(BrandSpacing.lg),
            item = OrderLineItem(
                id = "sierra-nevada-dark",
                name = "Sierra Nevada Dark Roast",
                unitPriceFormatted = "$48.000",
                quantity = 2,
                totalPriceFormatted = "$96.000",
                thumbLabel = "CAFÉ\nORIGEN"
            )
        )
    }
}

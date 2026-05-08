package com.cafeteros.historia.ui.features.orderreview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
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
 * Fila "Ver todos los productos" mostrada justo debajo del primer producto
 * cuando el pedido tiene más de un item.
 *
 * Layout: pequeño cuadrado gris con tres puntos a la izquierda y enlace
 * verde "Ver todos los productos" a la derecha. Se renderiza completa como
 * área tappable.
 *
 * @param modifier modifier opcional.
 * @param onClick callback al pulsar la fila.
 * @param label texto del enlace, por defecto "Ver todos los productos".
 */
@Composable
fun ViewAllProductsRow(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String = "Ver todos los productos"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        MoreThumb()
        Text(text = label, style = BrandTypography.OrderReviewViewAllLink)
    }
}

@Composable
private fun MoreThumb(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.OrderReviewMoreThumbBackground),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.MoreHoriz,
            contentDescription = null,
            tint = BrandColors.OrderReviewMoreThumbIcon,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Preview(name = "ViewAllProductsRow", showBackground = true, widthDp = 360)
@Composable
private fun ViewAllProductsRowPreview() {
    CafeterosTheme {
        ViewAllProductsRow(onClick = {})
    }
}

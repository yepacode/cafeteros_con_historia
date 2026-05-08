package com.cafeteros.historia.ui.features.orderreview.components

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
 * Top bar del paso 3 del checkout ("Revisar pedido").
 *
 * Layout: flecha back a la izquierda + título italic alineado al inicio del
 * espacio restante. A diferencia del paso 2 no expone una acción a la derecha
 * — el flujo de revisión no admite atajo al carrito porque ya estamos a un
 * tap del cobro. Por eso vive en su propio archivo y no reusa
 * [com.cafeteros.historia.ui.features.checkoutpayment.components.PaymentTopBar].
 *
 * @param modifier modifier opcional.
 * @param title título serif italic ("Revisar pedido").
 * @param onBack callback de la flecha back.
 */
@Composable
fun OrderReviewTopBar(
    modifier: Modifier = Modifier,
    title: String = "Revisar pedido",
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
        Text(
            text = title,
            style = BrandTypography.OrderReviewTopBarTitle,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(name = "OrderReviewTopBar", showBackground = true, widthDp = 360)
@Composable
private fun OrderReviewTopBarPreview() {
    CafeterosTheme {
        OrderReviewTopBar()
    }
}

package com.cafeteros.historia.ui.features.paymentfailed.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Top bar minimalista del paso 4 (fallo): solo una X de cerrar alineada a
 * la **izquierda** (a diferencia del éxito, que la pone a la derecha).
 *
 * @param modifier modifier opcional.
 * @param onClose callback de la X.
 */
@Composable
fun PaymentFailedTopBar(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onClose),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Cerrar",
                tint = BrandColors.PaymentFailedCloseIcon,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Preview(name = "PaymentFailedTopBar", showBackground = true, widthDp = 360)
@Composable
private fun PaymentFailedTopBarPreview() {
    CafeterosTheme {
        PaymentFailedTopBar()
    }
}

package com.cafeteros.historia.ui.features.checkoutshipping.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Bottom bar fijo del paso 1 del checkout: subtotal a la izquierda, CTA
 * "Continuar" a la derecha.
 *
 * @param modifier modifier opcional.
 * @param formattedSubtotal subtotal ya formateado ("$146.000").
 * @param enabled si false, el botón se deshabilita visualmente.
 * @param onContinue callback del CTA.
 */
@Composable
fun CheckoutBottomBar(
    modifier: Modifier = Modifier,
    formattedSubtotal: String,
    enabled: Boolean = true,
    onContinue: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.CheckoutBottomBarBackground)
            .navigationBarsPadding()
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = "SUBTOTAL", style = BrandTypography.CheckoutSubtotalLabel)
            Text(text = formattedSubtotal, style = BrandTypography.CheckoutSubtotalValue)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (enabled) BrandColors.CheckoutContinueBackground
                    else BrandColors.CheckoutContinueBackground.copy(alpha = 0.5f)
                )
                .clickable(enabled = enabled, onClick = onContinue),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Continuar", style = BrandTypography.CheckoutContinueLabel)
        }
    }
}

@Preview(name = "CheckoutBottomBar", showBackground = true, widthDp = 360)
@Composable
private fun CheckoutBottomBarPreview() {
    CafeterosTheme {
        CheckoutBottomBar(
            formattedSubtotal = "$146.000",
            onContinue = {}
        )
    }
}

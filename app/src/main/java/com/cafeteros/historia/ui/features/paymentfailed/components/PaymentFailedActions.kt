package com.cafeteros.historia.ui.features.paymentfailed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bloque de acciones del paso 4 (fallo): CTA primario oscuro "Intentar
 * otra vez", CTA secundario verde outline "Cambiar método de pago" y
 * enlace "Contactar soporte".
 *
 * @param modifier modifier opcional.
 * @param onRetry callback del CTA primario.
 * @param onChangeMethod callback del CTA secundario.
 * @param onContactSupport callback del enlace terciario.
 */
@Composable
fun PaymentFailedActions(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    onChangeMethod: () -> Unit,
    onContactSupport: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(BrandColors.PaymentFailedPrimaryCtaBackground)
                .clickable(onClick = onRetry),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Intentar otra vez", style = BrandTypography.PrimaryDarkCtaLabel)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(
                    width = 1.5.dp,
                    color = BrandColors.PaymentFailedSecondaryCtaBorder,
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable(onClick = onChangeMethod),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Cambiar método de pago",
                style = BrandTypography.SecondaryGreenOutlineCtaLabel
            )
        }

        Text(
            text = "Contactar soporte",
            style = BrandTypography.PaymentFailedSupportLink.copy(
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier
                .padding(top = BrandSpacing.xs)
                .clickable(onClick = onContactSupport)
        )
    }
}

@Preview(name = "PaymentFailedActions", showBackground = true, widthDp = 360)
@Composable
private fun PaymentFailedActionsPreview() {
    CafeterosTheme {
        PaymentFailedActions(
            modifier = Modifier.padding(BrandSpacing.lg),
            onRetry = {},
            onChangeMethod = {},
            onContactSupport = {}
        )
    }
}

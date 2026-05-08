package com.cafeteros.historia.ui.features.paymentsuccess.components

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
 * Bloque de acciones del paso 4 (éxito): CTA primario oscuro "Seguir mi
 * pedido", CTA secundario verde outline "Seguir explorando" y enlace
 * dorado terciario "Calificar esta experiencia".
 *
 * @param modifier modifier opcional.
 * @param onTrackOrder callback del CTA primario.
 * @param onKeepExploring callback del CTA secundario.
 * @param onRateExperience callback del enlace terciario.
 */
@Composable
fun PaymentSuccessActions(
    modifier: Modifier = Modifier,
    onTrackOrder: () -> Unit,
    onKeepExploring: () -> Unit,
    onRateExperience: () -> Unit
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
                .background(BrandColors.PaymentSuccessPrimaryCtaBackground)
                .clickable(onClick = onTrackOrder),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Seguir mi pedido", style = BrandTypography.PrimaryDarkCtaLabel)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(
                    width = 1.5.dp,
                    color = BrandColors.PaymentSuccessSecondaryCtaBorder,
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable(onClick = onKeepExploring),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Seguir explorando",
                style = BrandTypography.SecondaryGreenOutlineCtaLabel
            )
        }

        Text(
            text = "Calificar esta experiencia",
            style = BrandTypography.PaymentSuccessTertiaryLink.copy(
                textDecoration = TextDecoration.None
            ),
            modifier = Modifier
                .padding(top = BrandSpacing.xs)
                .clickable(onClick = onRateExperience)
        )
    }
}

@Preview(name = "PaymentSuccessActions", showBackground = true, widthDp = 360)
@Composable
private fun PaymentSuccessActionsPreview() {
    CafeterosTheme {
        PaymentSuccessActions(
            modifier = Modifier.padding(BrandSpacing.lg),
            onTrackOrder = {},
            onKeepExploring = {},
            onRateExperience = {}
        )
    }
}

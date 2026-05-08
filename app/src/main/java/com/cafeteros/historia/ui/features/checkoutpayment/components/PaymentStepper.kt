package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
 * Variante compacta del stepper para el paso 2 del checkout.
 *
 * En el diseño del paso 2, el stepper se reduce a tres puntos pequeños
 * centrados — sin etiqueta "PASO X DE Y" — y el activo se pinta en el
 * dorado de marca [BrandColors.PaymentStepperActive] en lugar del pill
 * oscuro del paso 1. Se modela como componente aparte para evitar
 * sobrecargar [com.cafeteros.historia.ui.features.checkoutshipping
 * .components.CheckoutStepper] con un modo visual que rompe su contrato.
 *
 * @param modifier modifier opcional.
 * @param currentStep paso actual (1-based). Por defecto 2.
 * @param totalSteps total de pasos. Por defecto 3.
 */
@Composable
fun PaymentStepper(
    modifier: Modifier = Modifier,
    currentStep: Int = 2,
    totalSteps: Int = 3
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            for (step in 1..totalSteps) {
                StepDot(active = step == currentStep)
            }
        }
    }
}

@Composable
private fun StepDot(modifier: Modifier = Modifier, active: Boolean) {
    Box(
        modifier = modifier
            .size(if (active) 9.dp else 8.dp)
            .clip(CircleShape)
            .background(
                if (active) BrandColors.PaymentStepperActive
                else BrandColors.PaymentStepperInactive
            )
    )
}

@Preview(name = "PaymentStepper", showBackground = true, widthDp = 360)
@Composable
private fun PaymentStepperPreview() {
    CafeterosTheme {
        PaymentStepper(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

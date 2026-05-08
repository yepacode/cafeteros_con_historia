package com.cafeteros.historia.ui.features.checkoutshipping.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
 * Indicador de progreso del checkout: tres marcadores horizontales con
 * el activo más ancho y oscuro, y la etiqueta "PASO X DE Y" alineada a
 * la derecha.
 *
 * @param modifier modifier opcional.
 * @param currentStep número del paso actual (1-based).
 * @param totalSteps total de pasos.
 */
@Composable
fun CheckoutStepper(
    modifier: Modifier = Modifier,
    currentStep: Int = 1,
    totalSteps: Int = 3
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            for (step in 1..totalSteps) {
                StepDot(active = step == currentStep)
            }
        }
        Text(text = "PASO $currentStep DE $totalSteps", style = BrandTypography.StepperLabel)
    }
}

@Composable
private fun StepDot(modifier: Modifier = Modifier, active: Boolean) {
    if (active) {
        // Forma alargada (pill) para destacar el paso activo.
        Box(
            modifier = modifier
                .height(6.dp)
                .width(28.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(BrandColors.StepperDotActive)
        )
    } else {
        Box(
            modifier = modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(BrandColors.StepperDotInactive)
        )
    }
}

@Preview(name = "CheckoutStepper", showBackground = true, widthDp = 360)
@Composable
private fun CheckoutStepperPreview() {
    CafeterosTheme {
        CheckoutStepper(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

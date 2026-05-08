package com.cafeteros.historia.ui.features.orderreview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
 * Stepper lineal de 3 pasos para el paso 3 del checkout ("Revisar pedido").
 *
 * En el diseño del paso 3 cambia el patrón visual respecto al paso 2: los
 * puntos compactos centrados se sustituyen por **tres círculos conectados
 * por una línea horizontal rosa tenue**, mostrando explícitamente progreso.
 * Por eso se modela aparte de [com.cafeteros.historia.ui.features
 * .checkoutpayment.components.PaymentStepper] (3 puntos compactos) y de
 * [com.cafeteros.historia.ui.features.checkoutshipping.components
 * .CheckoutStepper] (pill oscuro con etiqueta). Cada paso tiene un estado
 * visual distinto:
 *  - Pasos completados (`step < currentStep`): círculo relleno verde profundo.
 *  - Paso activo (`step == currentStep`): círculo dorado con anillo interior
 *    claro para destacar el momento "estás aquí".
 *  - Pasos futuros (`step > currentStep`): círculo dorado sin relleno interior
 *    (no se ve en el diseño porque siempre llegamos a este stepper en paso 3,
 *    pero se prevé para el futuro).
 *
 * @param modifier modifier opcional.
 * @param currentStep paso actual (1-based). Por defecto 3.
 * @param totalSteps total de pasos. Por defecto 3.
 */
@Composable
fun CheckoutProgressStepper(
    modifier: Modifier = Modifier,
    currentStep: Int = 3,
    totalSteps: Int = 3
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (step in 1..totalSteps) {
            StepCircle(
                completed = step < currentStep,
                active = step == currentStep
            )
            if (step != totalSteps) {
                StepConnector(modifier = Modifier.weight(1f, fill = true))
            }
        }
    }
}

@Composable
private fun StepCircle(
    modifier: Modifier = Modifier,
    completed: Boolean,
    active: Boolean
) {
    val circleSize = if (active) 16.dp else 14.dp
    when {
        completed -> Box(
            modifier = modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(BrandColors.OrderReviewStepCompletedFill)
        )
        active -> Box(
            modifier = modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(BrandColors.OrderReviewStepActiveFill),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(BrandColors.OrderReviewStepActiveInner)
            )
        }
        else -> Box(
            modifier = modifier
                .size(circleSize)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = BrandColors.OrderReviewStepActiveFill,
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun StepConnector(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(horizontal = BrandSpacing.xs)
            .height(1.dp)
            .background(BrandColors.OrderReviewStepConnector)
    )
}

@Preview(name = "CheckoutProgressStepper — paso 3", showBackground = true, widthDp = 360)
@Composable
private fun CheckoutProgressStepperPreview() {
    CafeterosTheme {
        CheckoutProgressStepper(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

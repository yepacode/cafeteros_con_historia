package com.cafeteros.historia.ui.features.paymentsuccess.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.paymentsuccess.model.OrderProgressStep
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Timeline vertical "¿Qué sigue?" del paso 4 (éxito).
 *
 * Cada paso es una fila: círculo (relleno dorado si está completado, outline
 * rosa si es futuro) + etiqueta. Los círculos se conectan verticalmente con
 * una línea rosa tenue, salvo el último que cierra el timeline.
 *
 * @param modifier modifier opcional.
 * @param steps pasos del timeline en orden.
 */
@Composable
fun OrderProgressTimeline(
    modifier: Modifier = Modifier,
    steps: List<OrderProgressStep>
) {
    Column(modifier = modifier.fillMaxWidth()) {
        steps.forEachIndexed { index, step ->
            ProgressRow(
                step = step,
                showConnector = index != steps.lastIndex
            )
        }
    }
}

@Composable
private fun ProgressRow(
    modifier: Modifier = Modifier,
    step: OrderProgressStep,
    showConnector: Boolean
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            ProgressStepDot(completed = step.completed)
            if (showConnector) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(BrandColors.OrderProgressConnector)
                )
            }
        }
        Text(
            text = step.label,
            style = if (step.completed) {
                BrandTypography.OrderProgressStepActive
            } else {
                BrandTypography.OrderProgressStepFuture
            },
            modifier = Modifier.padding(top = 1.dp, bottom = BrandSpacing.md)
        )
    }
}

@Composable
private fun ProgressStepDot(modifier: Modifier = Modifier, completed: Boolean) {
    if (completed) {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(BrandColors.OrderProgressStepActiveFill)
        )
    } else {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(BrandColors.PaymentSuccessBackground)
                .border(
                    width = 2.dp,
                    color = BrandColors.OrderProgressStepFutureBorder,
                    shape = CircleShape
                )
        )
    }
}

@Preview(name = "OrderProgressTimeline", showBackground = true, widthDp = 360)
@Composable
private fun OrderProgressTimelinePreview() {
    CafeterosTheme {
        OrderProgressTimeline(
            modifier = Modifier.padding(BrandSpacing.lg),
            steps = listOf(
                OrderProgressStep("Confirmamos tu pedido", completed = true),
                OrderProgressStep("Don Alberto prepara tu café", completed = false),
                OrderProgressStep("Enviamos tu pedido", completed = false),
                OrderProgressStep("Entregamos en tu puerta", completed = false)
            )
        )
    }
}

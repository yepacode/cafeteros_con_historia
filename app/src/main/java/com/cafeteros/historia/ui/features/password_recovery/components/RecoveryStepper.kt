package com.cafeteros.historia.ui.features.password_recovery.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Indicador visual de progreso del flujo de recuperación de contraseña.
 *
 * Muestra los 3 pasos (Código, Verificación, Contraseña) en una fila, con
 * círculos numerados conectados por líneas horizontales. El paso activo y
 * los completados se pintan con [BrandColors.CoffeeBrown]; los pendientes
 * se atenúan con [BrandColors.IndicatorInactive].
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param currentStep número del paso actual (1, 2 o 3).
 */
@Composable
fun RecoveryStepper(
    modifier: Modifier = Modifier,
    currentStep: Int
) {
    val labels = listOf("CÓDIGO", "VERIFICACIÓN", "CONTRASEÑA")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            labels.forEachIndexed { index, _ ->
                val stepNumber = index + 1
                StepCircle(
                    number = stepNumber,
                    isCompletedOrActive = stepNumber <= currentStep
                )
                if (index < labels.lastIndex) {
                    StepConnector(
                        modifier = Modifier.weight(1f),
                        isActive = stepNumber < currentStep
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BrandSpacing.xs)
        ) {
            labels.forEachIndexed { index, label ->
                val isActive = (index + 1) == currentStep
                Text(
                    text = label,
                    style = BrandTypography.BrandTagline.copy(
                        color = if (isActive) BrandColors.CoffeeBrown else BrandColors.TextSecondary,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StepCircle(number: Int, isCompletedOrActive: Boolean) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isCompletedOrActive) {
            BrandColors.CoffeeBrown
        } else {
            BrandColors.IndicatorInactive
        },
        label = "stepCircleBackground"
    )
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(color = backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = BrandTypography.PrimaryButtonLabel.copy(
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun StepConnector(modifier: Modifier = Modifier, isActive: Boolean) {
    val color by animateColorAsState(
        targetValue = if (isActive) BrandColors.CoffeeBrown else BrandColors.IndicatorInactive,
        label = "stepConnectorColor"
    )
    Box(
        modifier = modifier
            .height(2.dp)
            .background(color)
    )
}

@Preview(name = "Stepper – paso 1", showBackground = true, widthDp = 360)
@Composable
private fun RecoveryStepperStep1Preview() {
    CafeterosTheme { RecoveryStepper(currentStep = 1) }
}

@Preview(name = "Stepper – paso 2", showBackground = true, widthDp = 360)
@Composable
private fun RecoveryStepperStep2Preview() {
    CafeterosTheme { RecoveryStepper(currentStep = 2) }
}

@Preview(name = "Stepper – paso 3", showBackground = true, widthDp = 360)
@Composable
private fun RecoveryStepperStep3Preview() {
    CafeterosTheme { RecoveryStepper(currentStep = 3) }
}

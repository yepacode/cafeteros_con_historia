package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Encabezado del flujo multi-paso del registro del caficultor.
 *
 * Compuesto por tres elementos verticales:
 *  1. Fila superior: flecha "atrás" + título "Registro" + contador "X DE Y".
 *  2. Barra de progreso segmentada (un segmento por paso, los completados +
 *     el activo se pintan en verde de marca).
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param currentStepNumber número del paso actual (base 1).
 * @param totalSteps total de pasos del flujo.
 * @param onBack callback de la flecha atrás.
 */
@Composable
fun RegistrationStepHeader(
    modifier: Modifier = Modifier,
    currentStepNumber: Int,
    totalSteps: Int,
    onBack: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandColors.TextPrimary
                )
            }
            Text(
                text = "Registro del Caficultor",
                modifier = Modifier.weight(1f),
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = BrandColors.TextPrimary
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = "$currentStepNumber de $totalSteps",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp,
                    color = BrandColors.TextSecondary
                ),
                modifier = Modifier.padding(end = BrandSpacing.md)
            )
        }

        ProgressSegments(
            currentStepNumber = currentStepNumber,
            totalSteps = totalSteps,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg)
                .padding(top = BrandSpacing.xs, bottom = BrandSpacing.sm)
        )
    }
}

/** Segmentos de progreso: rellenos hasta `currentStepNumber - 1`, gris el resto. */
@Composable
private fun ProgressSegments(
    currentStepNumber: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(totalSteps) { segmentIndex ->
            val isCompletedOrActive = segmentIndex < currentStepNumber
            val color = if (isCompletedOrActive) {
                BrandColors.FarmerPrimary
            } else {
                BrandColors.IndicatorInactive
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .background(color = color, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}

@Preview(name = "StepHeader – Paso 1 de 5", widthDp = 360)
@Composable
private fun RegistrationStepHeaderPreview() {
    Box(modifier = Modifier.background(BrandColors.AuthBackground)) {
        RegistrationStepHeader(
            currentStepNumber = 1,
            totalSteps = 5,
            onBack = {}
        )
    }
}

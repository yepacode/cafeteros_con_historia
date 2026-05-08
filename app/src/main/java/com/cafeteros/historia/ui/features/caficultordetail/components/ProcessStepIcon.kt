package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.caficultordetail.model.ProcessStep
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Ícono + label de un paso del proceso de beneficio del café.
 *
 * Composición vertical: ícono Material grande arriba en verde, label
 * mayúsculas pequeño debajo centrado en gris.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param step paso a renderizar.
 */
@Composable
fun ProcessStepIcon(
    modifier: Modifier = Modifier,
    step: ProcessStep
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = step.icon,
            contentDescription = null,
            tint = BrandColors.ProcessStepIconTint,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = step.label,
            style = BrandTypography.ProcessStepLabel,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "ProcessStepIcon", showBackground = true)
@Composable
private fun ProcessStepIconPreview() {
    CafeterosTheme {
        ProcessStepIcon(
            step = ProcessStep(label = "SECADO AL SOL", icon = Icons.Filled.WbSunny)
        )
    }
}

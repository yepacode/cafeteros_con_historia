package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.caficultordetail.model.ProcessStep
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Cómo procesan su café": título + fila con los pasos del
 * proceso distribuidos uniformemente a lo ancho.
 *
 * Cada paso usa [ProcessStepIcon] con `weight(1f)` dentro de un Row para
 * que cuatro pasos se repartan el ancho de forma equitativa.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param steps pasos a mostrar.
 */
@Composable
fun ProcessStepsRow(
    modifier: Modifier = Modifier,
    steps: List<ProcessStep>
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(text = "Cómo procesan su café", style = BrandTypography.ZoneSectionTitle)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEach { step ->
                ProcessStepIcon(
                    step = step,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(name = "ProcessStepsRow", showBackground = true, widthDp = 360)
@Composable
private fun ProcessStepsRowPreview() {
    CafeterosTheme {
        ProcessStepsRow(
            steps = listOf(
                ProcessStep("COSECHA MANUAL", Icons.Filled.Park),
                ProcessStep("DESPULPADO", Icons.Filled.LocalFlorist),
                ProcessStep("FERMENTACIÓN 24H", Icons.Filled.HourglassBottom),
                ProcessStep("SECADO AL SOL", Icons.Filled.WbSunny)
            )
        )
    }
}

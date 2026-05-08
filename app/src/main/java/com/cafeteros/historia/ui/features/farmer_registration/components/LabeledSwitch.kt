package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Fila con título + descripción a la izquierda y un Switch a la derecha.
 *
 * Pensado para preguntas de sí/no opcional en el formulario, ej.
 * "¿Reconocimientos o premios?".
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param title pregunta principal.
 * @param description texto explicativo bajo el título.
 * @param checked estado actual del switch.
 * @param onCheckedChange callback al cambiar el estado.
 */
@Composable
fun LabeledSwitch(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = BrandColors.TextSecondary
                )
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BrandColors.PrimaryButtonText,
                checkedTrackColor = BrandColors.FarmerPrimary,
                uncheckedThumbColor = BrandColors.PrimaryButtonText,
                uncheckedTrackColor = BrandColors.IndicatorInactive,
                uncheckedBorderColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun LabeledSwitchPreview() {
    LabeledSwitch(
        title = "¿Reconocimientos o premios?",
        description = "Si tu café ha sido galardonado en Taza de la Excelencia u otros.",
        checked = false,
        onCheckedChange = {}
    )
}

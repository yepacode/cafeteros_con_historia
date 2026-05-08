package com.cafeteros.historia.ui.features.checkoutshipping.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
 * Bloque "INSTRUCCIONES PARA EL REPARTIDOR (OPCIONAL)" con su label
 * uppercase + textarea multiline.
 *
 * Usa [BasicTextField] sin línea inferior para coincidir con el resto de
 * inputs de la app y permite hasta varias líneas.
 *
 * @param modifier modifier opcional.
 * @param value texto actual.
 * @param onValueChange callback al editar.
 * @param placeholder texto guía cuando el campo está vacío.
 */
@Composable
fun DeliveryInstructionsField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Ej: Tocar el timbre del apto 502, dejar en " +
            "portería si no respondo..."
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(
            text = "INSTRUCCIONES PARA EL REPARTIDOR (OPCIONAL)",
            style = BrandTypography.InstructionsLabel
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrandColors.InstructionsFieldBackground)
                .padding(BrandSpacing.md),
            contentAlignment = Alignment.TopStart
        ) {
            if (value.isEmpty()) {
                Text(text = placeholder, style = BrandTypography.InstructionsField)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = BrandTypography.InstructionsField.copy(
                    color = BrandColors.TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "DeliveryInstructionsField", showBackground = true, widthDp = 360)
@Composable
private fun DeliveryInstructionsFieldPreview() {
    CafeterosTheme {
        DeliveryInstructionsField(
            modifier = Modifier.padding(BrandSpacing.lg),
            value = "",
            onValueChange = {}
        )
    }
}

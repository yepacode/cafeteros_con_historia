package com.example.cafeteros.ui.features.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafeteros.ui.theme.BrandColors
import com.example.cafeteros.ui.theme.BrandSpacing
import com.example.cafeteros.ui.theme.BrandTypography

/**
 * Botón "Saltar" que aparece sobre la imagen hero del onboarding.
 *
 * Usa un fondo semi-transparente oscuro ([BrandColors.SkipButtonScrim]) para
 * garantizar legibilidad sobre cualquier imagen sin ocultarla por completo.
 *
 * @param modifier modifier opcional, típicamente para posicionarlo en la
 *  esquina superior derecha del contenedor padre con `Modifier.align`.
 * @param onClick callback invocado al pulsar el botón.
 * @param label texto visible. Por defecto "Saltar".
 */
@Composable
fun OnboardingSkipButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String = "Saltar"
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .padding(BrandSpacing.md)
            .background(
                color = BrandColors.SkipButtonScrim,
                shape = RoundedCornerShape(50)
            )
    ) {
        Text(
            text = label,
            style = BrandTypography.SkipButtonLabel,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

/** Preview del botón Saltar. */
@Preview(name = "SkipButton")
@Composable
private fun OnboardingSkipButtonPreview() {
    OnboardingSkipButton(onClick = {})
}

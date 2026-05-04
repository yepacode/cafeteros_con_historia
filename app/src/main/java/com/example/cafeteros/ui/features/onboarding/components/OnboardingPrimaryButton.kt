package com.example.cafeteros.ui.features.onboarding.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafeteros.ui.theme.BrandColors
import com.example.cafeteros.ui.theme.BrandTypography

/**
 * Botón primario del onboarding ("Siguiente" / "Comenzar").
 *
 * Ocupa el ancho completo del contenedor padre y mantiene altura fija para
 * un tap target cómodo (≥48dp recomendado por Material).
 *
 * @param modifier modifier opcional.
 * @param onClick callback al pulsar el botón.
 * @param label texto visible. Cambia según la página: "Siguiente" o "Comenzar".
 */
@Composable
fun OnboardingPrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandColors.PrimaryButton,
            contentColor = BrandColors.PrimaryButtonText
        )
    ) {
        Text(
            text = label,
            style = BrandTypography.PrimaryButtonLabel
        )
    }
}

@Preview(name = "PrimaryButton – Siguiente", widthDp = 320)
@Composable
private fun OnboardingPrimaryButtonNextPreview() {
    OnboardingPrimaryButton(onClick = {}, label = "Siguiente")
}

@Preview(name = "PrimaryButton – Comenzar", widthDp = 320)
@Composable
private fun OnboardingPrimaryButtonStartPreview() {
    OnboardingPrimaryButton(onClick = {}, label = "Comenzar")
}

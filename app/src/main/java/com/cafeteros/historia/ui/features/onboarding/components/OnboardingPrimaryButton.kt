package com.cafeteros.historia.ui.features.onboarding.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography

/**
 * Botón primario del onboarding ("Siguiente" / "Comenzar" / "Empezar mi
 * registro").
 *
 * Ocupa el ancho completo del contenedor padre y mantiene altura fija para
 * un tap target cómodo (≥48dp recomendado por Material).
 *
 * Los colores son parametrizables para soportar variantes de marca: el flujo
 * de comprador usa café oscuro, el de caficultor usa verde de marca.
 *
 * @param modifier modifier opcional.
 * @param onClick callback al pulsar el botón.
 * @param label texto visible. Cambia según la página y el flujo.
 * @param containerColor color de fondo del botón.
 * @param contentColor color del texto del botón.
 */
@Composable
fun OnboardingPrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    containerColor: Color = BrandColors.PrimaryButton,
    contentColor: Color = BrandColors.PrimaryButtonText
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(
            text = label,
            style = BrandTypography.PrimaryButtonLabel.copy(color = contentColor)
        )
    }
}

@Preview(name = "PrimaryButton – Siguiente (café)", widthDp = 320)
@Composable
private fun OnboardingPrimaryButtonNextPreview() {
    OnboardingPrimaryButton(onClick = {}, label = "Siguiente")
}

@Preview(name = "PrimaryButton – Empezar (verde caficultor)", widthDp = 320)
@Composable
private fun OnboardingPrimaryButtonFarmerPreview() {
    OnboardingPrimaryButton(
        onClick = {},
        label = "Empezar mi registro",
        containerColor = BrandColors.FarmerPrimary
    )
}

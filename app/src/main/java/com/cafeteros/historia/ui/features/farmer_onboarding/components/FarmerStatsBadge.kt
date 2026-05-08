package com.cafeteros.historia.ui.features.farmer_onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography

/**
 * Insignia con prueba social en la última página del onboarding del
 * caficultor. Muestra un dato corto y motivador que humaniza la cifra.
 *
 * El emoji ☕ se incluye dentro del [text] para mantener el componente
 * agnóstico y reutilizable: cualquier label que pase el caller se renderiza
 * tal cual sobre el fondo crema redondeado.
 *
 * @param modifier modifier opcional aplicado al contenedor.
 * @param text texto a mostrar (incluyendo emojis si los hay).
 */
@Composable
fun FarmerStatsBadge(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.FarmerStatsBadgeBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = BrandSpacing.md,
                vertical = BrandSpacing.sm
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = BrandTypography.OnboardingDescription.copy(
                color = BrandColors.FarmerStatsBadgeText
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "FarmerStatsBadge", widthDp = 320)
@Composable
private fun FarmerStatsBadgePreview() {
    FarmerStatsBadge(text = "☕ Ya somos +180 caficultores vendiendo en 6 zonas")
}

package com.cafeteros.historia.ui.features.farmer_onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.onboarding.components.OnboardingPageIndicator
import com.cafeteros.historia.ui.features.onboarding.components.OnboardingPrimaryButton
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography

/**
 * Tarjeta inferior del onboarding del caficultor.
 *
 * Diferencias vs [com.cafeteros.historia.ui.features.onboarding.components.OnboardingInfoCard]:
 *  - Usa la paleta verde de marca caficultor (botón, indicador y enlace).
 *  - El CTA en la última página es "Empezar mi registro" (no "Comenzar").
 *  - Soporta una insignia opcional ([statsBadgeText]) entre la descripción
 *    y el indicador de páginas (solo se muestra en la última pantalla).
 *
 * @param modifier modifier opcional.
 * @param title título de la página.
 * @param description texto descriptivo bajo el título.
 * @param statsBadgeText texto de la insignia inferior; null = no mostrarla.
 * @param pageCount número total de páginas para el indicador.
 * @param currentPage índice actual para el indicador.
 * @param isLastPage indica si esta es la última página del flujo.
 * @param onPrimaryClick callback del botón primario.
 * @param onSecondaryClick callback del enlace "Ya tengo cuenta". Solo se
 *  invoca cuando [isLastPage] es `true`.
 */
@Composable
fun FarmerInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    statsBadgeText: String?,
    pageCount: Int,
    currentPage: Int,
    isLastPage: Boolean,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = BrandColors.CardBackground,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            )
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = BrandSpacing.lg,
                vertical = BrandSpacing.xl
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(
            text = title,
            style = BrandTypography.OnboardingTitle,
            textAlign = TextAlign.Center
        )
        Text(
            text = description,
            style = BrandTypography.OnboardingDescription,
            textAlign = TextAlign.Center
        )

        if (statsBadgeText != null) {
            FarmerStatsBadge(text = statsBadgeText)
        }

        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        OnboardingPageIndicator(
            pageCount = pageCount,
            currentPage = currentPage,
            activeColor = BrandColors.FarmerIndicatorActive
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        OnboardingPrimaryButton(
            onClick = onPrimaryClick,
            label = if (isLastPage) "Empezar mi registro" else "Siguiente",
            containerColor = BrandColors.FarmerPrimary
        )
        if (isLastPage) {
            TextButton(onClick = onSecondaryClick) {
                Text(
                    text = "Ya tengo cuenta",
                    style = BrandTypography.SecondaryLinkLabel.copy(
                        color = BrandColors.FarmerPrimary
                    )
                )
            }
        }
    }
}

@Preview(name = "FarmerInfoCard – intermedia", widthDp = 360)
@Composable
private fun FarmerInfoCardMiddlePreview() {
    FarmerInfoCard(
        title = "Tú controlas tu historia y tus precios",
        description = "Sube fotos, videos y la historia de tu finca.",
        statsBadgeText = null,
        pageCount = 3,
        currentPage = 1,
        isLastPage = false,
        onPrimaryClick = {},
        onSecondaryClick = {}
    )
}

@Preview(name = "FarmerInfoCard – última con badge", widthDp = 360)
@Composable
private fun FarmerInfoCardLastPreview() {
    FarmerInfoCard(
        title = "Recibe pagos seguros y a tiempo",
        description = "Tu dinero se consigna en tu cuenta bancaria. Sin intermediarios.",
        statsBadgeText = "☕ Ya somos +180 caficultores vendiendo en 6 zonas",
        pageCount = 3,
        currentPage = 2,
        isLastPage = true,
        onPrimaryClick = {},
        onSecondaryClick = {}
    )
}

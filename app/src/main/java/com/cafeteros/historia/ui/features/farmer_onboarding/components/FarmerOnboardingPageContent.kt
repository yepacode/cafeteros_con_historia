package com.cafeteros.historia.ui.features.farmer_onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.farmer_onboarding.model.FarmerOnboardingPage
import com.cafeteros.historia.ui.features.onboarding.components.OnboardingSkipButton
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Composición de una sola página completa del onboarding del caficultor.
 *
 * Layout:
 *  - Imagen hero arriba (proporción ~55%) con degradado al fondo blanco.
 *  - Tarjeta inferior con texto, insignia opcional y CTAs (~45%).
 *  - Botón "Saltar" sobreimpuesto en la esquina superior derecha.
 *
 * Es stateless: recibe los datos y los callbacks; toda la decisión de
 * navegación vive en el padre ([com.cafeteros.historia.ui.features.farmer_onboarding.FarmerOnboardingScreen]).
 *
 * @param modifier modifier opcional aplicado al contenedor raíz.
 * @param page datos de la página a renderizar.
 * @param pageCount total de páginas (para el indicador).
 * @param currentPage índice actual (para el indicador).
 * @param isLastPage true si es la última página.
 * @param skipLabel texto del botón Saltar — distinto en página 3 ("SALTAR" en mayúsculas).
 * @param onSkipClick callback de "Saltar".
 * @param onPrimaryClick callback de "Siguiente" / "Empezar mi registro".
 * @param onSecondaryClick callback de "Ya tengo cuenta" (solo última página).
 */
@Composable
fun FarmerOnboardingPageContent(
    modifier: Modifier = Modifier,
    page: FarmerOnboardingPage,
    pageCount: Int,
    currentPage: Int,
    isLastPage: Boolean,
    skipLabel: String,
    onSkipClick: () -> Unit,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.CardBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FarmerHeroImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f),
                page = page
            )
            FarmerInfoCard(
                modifier = Modifier.weight(0.45f),
                title = page.title,
                description = page.description,
                statsBadgeText = page.statsBadgeText,
                pageCount = pageCount,
                currentPage = currentPage,
                isLastPage = isLastPage,
                onPrimaryClick = onPrimaryClick,
                onSecondaryClick = onSecondaryClick
            )
        }
        OnboardingSkipButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onSkipClick,
            label = skipLabel
        )
    }
}

@Preview(name = "FarmerPageContent – intermedia", widthDp = 360, heightDp = 720)
@Composable
private fun FarmerOnboardingPageContentMiddlePreview() {
    FarmerOnboardingPageContent(
        page = com.cafeteros.historia.ui.features.farmer_onboarding.FarmerOnboardingPages[1],
        pageCount = 3,
        currentPage = 1,
        isLastPage = false,
        skipLabel = "Saltar",
        onSkipClick = {},
        onPrimaryClick = {},
        onSecondaryClick = {}
    )
}

@Preview(name = "FarmerPageContent – última con badge", widthDp = 360, heightDp = 720)
@Composable
private fun FarmerOnboardingPageContentLastPreview() {
    FarmerOnboardingPageContent(
        page = com.cafeteros.historia.ui.features.farmer_onboarding.FarmerOnboardingPages[2],
        pageCount = 3,
        currentPage = 2,
        isLastPage = true,
        skipLabel = "SALTAR",
        onSkipClick = {},
        onPrimaryClick = {},
        onSecondaryClick = {}
    )
}

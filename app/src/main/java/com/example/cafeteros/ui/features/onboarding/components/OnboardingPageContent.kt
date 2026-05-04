package com.example.cafeteros.ui.features.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.cafeteros.ui.features.onboarding.model.OnboardingPage
import com.example.cafeteros.ui.theme.BrandColors

/**
 * Composición de una sola página completa del onboarding.
 *
 * Layout: imagen hero arriba (proporción ~55%), tarjeta blanca con texto
 * abajo (~45%) y, sobreimpuesto en la esquina superior derecha, el botón
 * "Saltar".
 *
 * Es stateless: recibe los datos y los callbacks; toda la decisión de
 * navegación vive en el padre ([com.example.cafeteros.ui.features.onboarding.OnboardingScreen]).
 *
 * @param modifier modifier opcional aplicado al contenedor raíz.
 * @param page datos de la página a renderizar.
 * @param pageCount total de páginas (para el indicador).
 * @param currentPage índice actual (para el indicador).
 * @param isLastPage true si es la última página.
 * @param onSkipClick callback de "Saltar".
 * @param onPrimaryClick callback de "Siguiente" / "Comenzar".
 * @param onSecondaryClick callback de "Ya tengo cuenta" (solo última página).
 */
@Composable
fun OnboardingPageContent(
    modifier: Modifier = Modifier,
    page: OnboardingPage,
    pageCount: Int,
    currentPage: Int,
    isLastPage: Boolean,
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
            OnboardingHeroImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f),
                page = page
            )
            OnboardingInfoCard(
                modifier = Modifier.weight(0.45f),
                title = page.title,
                description = page.description,
                pageCount = pageCount,
                currentPage = currentPage,
                isLastPage = isLastPage,
                onPrimaryClick = onPrimaryClick,
                onSecondaryClick = onSecondaryClick
            )
        }
        OnboardingSkipButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onSkipClick
        )
    }
}

@Preview(name = "PageContent – intermedia", widthDp = 360, heightDp = 720)
@Composable
private fun OnboardingPageContentMiddlePreview() {
    OnboardingPageContent(
        page = OnboardingPage(
            title = "Conoce la historia detrás de cada grano",
            description = "Cada caficultor comparte su historia y proceso.",
            placeholderIcon = Icons.Outlined.Coffee,
            placeholderColor = Color(0xFF4E342E)
        ),
        pageCount = 3,
        currentPage = 1,
        isLastPage = false,
        onSkipClick = {},
        onPrimaryClick = {},
        onSecondaryClick = {}
    )
}

@Preview(name = "PageContent – última", widthDp = 360, heightDp = 720)
@Composable
private fun OnboardingPageContentLastPreview() {
    OnboardingPageContent(
        page = OnboardingPage(
            title = "Del árbol a tu taza",
            description = "Tu café llega directo del caficultor a tu casa.",
            placeholderIcon = Icons.Outlined.Coffee,
            placeholderColor = Color(0xFF6D4C41)
        ),
        pageCount = 3,
        currentPage = 2,
        isLastPage = true,
        onSkipClick = {},
        onPrimaryClick = {},
        onSecondaryClick = {}
    )
}

package com.cafeteros.historia.ui.features.farmer_onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.farmer_onboarding.components.FarmerOnboardingPageContent
import com.cafeteros.historia.ui.features.farmer_onboarding.model.FarmerOnboardingPage
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.launch

/**
 * Pantalla raíz del onboarding del caficultor.
 *
 * 3 páginas con identidad verde de marca (vs el café del onboarding general).
 * En la última, el CTA cambia a "Empezar mi registro" y aparece la insignia
 * "+180 caficultores".
 *
 * @param modifier modifier opcional.
 * @param pages contenido de las páginas (default: [FarmerOnboardingPages]).
 * @param onSkip callback al pulsar "Saltar" / "SALTAR" desde cualquier página.
 * @param onStartRegistration callback al pulsar "Empezar mi registro".
 * @param onAlreadyHaveAccount callback de "Ya tengo cuenta".
 */
@Composable
fun FarmerOnboardingScreen(
    modifier: Modifier = Modifier,
    pages: List<FarmerOnboardingPage> = FarmerOnboardingPages,
    onSkip: () -> Unit = {},
    onStartRegistration: () -> Unit = {},
    onAlreadyHaveAccount: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { pageIndex ->
        val isLastPage = pageIndex == pages.lastIndex
        FarmerOnboardingPageContent(
            page = pages[pageIndex],
            pageCount = pages.size,
            currentPage = pagerState.currentPage,
            isLastPage = isLastPage,
            // Sutileza visual: en la última página el "Saltar" aparece en
            // mayúsculas para reforzar el momento de decisión.
            skipLabel = if (isLastPage) "SALTAR" else "Saltar",
            onSkipClick = onSkip,
            onPrimaryClick = {
                if (isLastPage) {
                    onStartRegistration()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pageIndex + 1)
                    }
                }
            },
            onSecondaryClick = onAlreadyHaveAccount
        )
    }
}

@Preview(name = "FarmerOnboardingScreen", widthDp = 360, heightDp = 720)
@Composable
private fun FarmerOnboardingScreenPreview() {
    CafeterosTheme {
        FarmerOnboardingScreen()
    }
}

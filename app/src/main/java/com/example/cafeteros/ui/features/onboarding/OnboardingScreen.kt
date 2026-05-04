package com.example.cafeteros.ui.features.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cafeteros.ui.features.onboarding.components.OnboardingPageContent
import com.example.cafeteros.ui.features.onboarding.model.OnboardingPage
import com.example.cafeteros.ui.theme.CafeterosTheme
import kotlinx.coroutines.launch

/**
 * Pantalla raíz del flujo de onboarding.
 *
 * Renderiza un [HorizontalPager] que permite al usuario deslizar entre
 * páginas con el dedo o avanzar tocando el botón "Siguiente". En la última
 * página, "Siguiente" se transforma en "Comenzar" e invoca [onFinish];
 * "Saltar" invoca [onSkip] desde cualquier página; "Ya tengo cuenta" invoca
 * [onAlreadyHaveAccount].
 *
 * Decisión de diseño: la lista de páginas se inyecta vía parámetro [pages]
 * (default: [OnboardingPages]). Esto permite previewear o testear con
 * páginas falsas sin tocar la lista de producción.
 *
 * @param modifier modifier opcional para el pager.
 * @param pages contenido de las páginas a mostrar.
 * @param onSkip callback cuando el usuario toca "Saltar".
 * @param onFinish callback cuando el usuario toca "Comenzar" en la última página.
 * @param onAlreadyHaveAccount callback del enlace "Ya tengo cuenta".
 */
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    pages: List<OnboardingPage> = OnboardingPages,
    onSkip: () -> Unit = {},
    onFinish: () -> Unit = {},
    onAlreadyHaveAccount: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { pageIndex ->
        val isLastPage = pageIndex == pages.lastIndex
        OnboardingPageContent(
            page = pages[pageIndex],
            pageCount = pages.size,
            currentPage = pagerState.currentPage,
            isLastPage = isLastPage,
            onSkipClick = onSkip,
            onPrimaryClick = {
                if (isLastPage) {
                    onFinish()
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

@Preview(name = "OnboardingScreen", widthDp = 360, heightDp = 720)
@Composable
private fun OnboardingScreenPreview() {
    CafeterosTheme {
        OnboardingScreen()
    }
}

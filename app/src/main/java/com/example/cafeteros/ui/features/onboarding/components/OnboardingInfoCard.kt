package com.example.cafeteros.ui.features.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafeteros.ui.theme.BrandColors
import com.example.cafeteros.ui.theme.BrandSpacing
import com.example.cafeteros.ui.theme.BrandTypography

/**
 * Tarjeta blanca con esquinas superiores redondeadas que contiene el texto
 * y los controles inferiores de cada página del onboarding.
 *
 * En la última página (cuando [isLastPage] es `true`):
 *  - El texto del botón primario cambia a "Comenzar".
 *  - Se muestra el enlace secundario "Ya tengo cuenta" debajo del botón.
 *
 * @param modifier modifier opcional.
 * @param title título de la página.
 * @param description texto descriptivo bajo el título.
 * @param pageCount número total de páginas para el indicador.
 * @param currentPage índice actual para el indicador.
 * @param isLastPage indica si esta es la última página del flujo.
 * @param onPrimaryClick callback del botón primario (Siguiente / Comenzar).
 * @param onSecondaryClick callback del enlace "Ya tengo cuenta". Solo se
 *  invoca cuando [isLastPage] es `true`.
 */
@Composable
fun OnboardingInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    pageCount: Int,
    currentPage: Int,
    isLastPage: Boolean,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.CardBackground,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            )
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
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        OnboardingPageIndicator(
            pageCount = pageCount,
            currentPage = currentPage
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        OnboardingPrimaryButton(
            onClick = onPrimaryClick,
            label = if (isLastPage) "Comenzar" else "Siguiente"
        )
        if (isLastPage) {
            TextButton(onClick = onSecondaryClick) {
                Text(
                    text = "Ya tengo cuenta",
                    style = BrandTypography.SecondaryLinkLabel
                )
            }
        }
    }
}

@Preview(name = "InfoCard – Página intermedia", widthDp = 360)
@Composable
private fun OnboardingInfoCardMiddlePreview() {
    OnboardingInfoCard(
        title = "Conoce la historia detrás de cada grano",
        description = "Cada caficultor comparte su historia, su finca y su proceso.",
        pageCount = 3,
        currentPage = 1,
        isLastPage = false,
        onPrimaryClick = {},
        onSecondaryClick = {}
    )
}

@Preview(name = "InfoCard – Última página", widthDp = 360)
@Composable
private fun OnboardingInfoCardLastPreview() {
    OnboardingInfoCard(
        title = "Del árbol a tu taza",
        description = "Tu café llega directo del caficultor a tu casa.",
        pageCount = 3,
        currentPage = 2,
        isLastPage = true,
        onPrimaryClick = {},
        onSecondaryClick = {}
    )
}

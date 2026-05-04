package com.example.cafeteros.ui.features.onboarding.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafeteros.ui.theme.BrandColors

/**
 * Indicador de página tipo "puntos" para el onboarding.
 *
 * El punto correspondiente a la página activa se ensancha y cambia de color
 * con animación suave, dando feedback visual de la posición actual sin
 * ocupar espacio extra.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param pageCount número total de páginas a representar.
 * @param currentPage índice (base 0) de la página actualmente activa.
 */
@Composable
fun OnboardingPageIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { pageIndex ->
            PageIndicatorDot(isActive = pageIndex == currentPage)
        }
    }
}

/** Punto individual del indicador, animado en color y ancho. */
@Composable
private fun PageIndicatorDot(isActive: Boolean) {
    val color by animateColorAsState(
        targetValue = if (isActive) {
            BrandColors.IndicatorActive
        } else {
            BrandColors.IndicatorInactive
        },
        label = "indicatorColor"
    )
    val width by animateDpAsState(
        targetValue = if (isActive) 24.dp else 8.dp,
        label = "indicatorWidth"
    )
    Box(
        modifier = Modifier
            .height(8.dp)
            .width(width)
            .background(color = color, shape = CircleShape)
            .size(8.dp)
    )
}

@Preview(name = "PageIndicator")
@Composable
private fun OnboardingPageIndicatorPreview() {
    OnboardingPageIndicator(pageCount = 3, currentPage = 1)
}

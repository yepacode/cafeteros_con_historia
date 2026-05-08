package com.cafeteros.historia.ui.features.onboarding.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Indicador de página tipo "puntos" para el onboarding.
 *
 * El punto correspondiente a la página activa se ensancha y cambia de color
 * con animación suave, dando feedback visual de la posición actual sin
 * ocupar espacio extra.
 *
 * Los colores son parametrizables: por defecto usa los tokens de marca para
 * el flujo de comprador, pero el flujo de caficultor pasa colores propios
 * (verdes) para mantener consistencia visual.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param pageCount número total de páginas a representar.
 * @param currentPage índice (base 0) de la página actualmente activa.
 * @param activeColor color del punto activo.
 * @param inactiveColor color de los puntos inactivos.
 */
@Composable
fun OnboardingPageIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
    activeColor: Color = BrandColors.IndicatorActive,
    inactiveColor: Color = BrandColors.IndicatorInactive
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { pageIndex ->
            PageIndicatorDot(
                isActive = pageIndex == currentPage,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }
    }
}

/** Punto individual del indicador, animado en color y ancho. */
@Composable
private fun PageIndicatorDot(
    isActive: Boolean,
    activeColor: Color,
    inactiveColor: Color
) {
    val color by animateColorAsState(
        targetValue = if (isActive) activeColor else inactiveColor,
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

@Preview(name = "PageIndicator – default (café)")
@Composable
private fun OnboardingPageIndicatorPreview() {
    OnboardingPageIndicator(pageCount = 3, currentPage = 1)
}

@Preview(name = "PageIndicator – verde caficultor")
@Composable
private fun OnboardingPageIndicatorFarmerPreview() {
    OnboardingPageIndicator(
        pageCount = 3,
        currentPage = 2,
        activeColor = BrandColors.FarmerIndicatorActive
    )
}

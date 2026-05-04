package com.example.cafeteros.ui.features.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafeteros.ui.features.onboarding.model.OnboardingPage
import com.example.cafeteros.ui.theme.BrandColors

/**
 * Imagen hero de una página del onboarding.
 *
 * Si [page] tiene `imageRes` definido, lo carga con [painterResource] y lo
 * recorta con [ContentScale.Crop] para llenar el área disponible. Si no,
 * dibuja un placeholder visualmente coherente (color de marca + ícono Material
 * grande centrado), permitiendo desarrollar la UI sin imágenes reales.
 *
 * Adicionalmente aplica un degradado vertical en el ~40% inferior de la
 * imagen, fundiendo desde transparente hasta [BrandColors.CardBackground].
 * Esto crea una transición suave hacia la tarjeta de texto situada debajo,
 * eliminando la línea horizontal dura entre imagen y card.
 *
 * @param modifier modifier opcional aplicado al contenedor.
 * @param page datos de la página a renderizar.
 */
@Composable
fun OnboardingHeroImage(
    modifier: Modifier = Modifier,
    page: OnboardingPage
) {
    Box(
        modifier = modifier.background(page.placeholderColor),
        contentAlignment = Alignment.Center
    ) {
        if (page.imageRes != null) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = page.contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = page.placeholderIcon,
                contentDescription = page.contentDescription,
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(160.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            BrandColors.CardBackground
                        )
                    )
                )
        )
    }
}

/** Preview del placeholder cuando no hay imagen real. */
@Preview(name = "HeroImage – placeholder", widthDp = 320, heightDp = 320)
@Composable
private fun OnboardingHeroImagePreview() {
    OnboardingHeroImage(
        modifier = Modifier.fillMaxSize(),
        page = OnboardingPage(
            title = "Demo",
            description = "Demo",
            placeholderIcon = Icons.Outlined.Landscape,
            placeholderColor = Color(0xFF2E7D32)
        )
    )
}

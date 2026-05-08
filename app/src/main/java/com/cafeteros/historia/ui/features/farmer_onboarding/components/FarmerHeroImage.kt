package com.cafeteros.historia.ui.features.farmer_onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.farmer_onboarding.model.FarmerOnboardingPage
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Imagen hero de una página del onboarding del caficultor.
 *
 * Comportamiento idéntico al hero del onboarding general: si [page] tiene
 * `imageRes`, lo renderiza con `ContentScale.Crop`; si no, dibuja un
 * placeholder con ícono Material grande sobre el color de marca.
 *
 * Aplica el degradado vertical hacia [BrandColors.CardBackground] en el ~45%
 * inferior para fundir suavemente la imagen con la tarjeta de texto.
 *
 * @param modifier modifier opcional aplicado al contenedor.
 * @param page datos de la página a renderizar.
 */
@Composable
fun FarmerHeroImage(
    modifier: Modifier = Modifier,
    page: FarmerOnboardingPage
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

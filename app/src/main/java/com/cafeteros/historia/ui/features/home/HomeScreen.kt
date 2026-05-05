package com.cafeteros.historia.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla de bienvenida que se muestra al terminar el onboarding.
 *
 * Placeholder coherente con la identidad de marca de "Origen". Aquí es donde
 * se construirá la home real (catálogo de cafés, mapa de regiones, perfil
 * del caficultor, etc.) cuando esas features se diseñen.
 */
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.CardBackground)
            .padding(BrandSpacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Coffee,
                contentDescription = null,
                tint = BrandColors.CoffeeBrown,
                modifier = Modifier.padding(BrandSpacing.md)
            )
            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Text(
                text = "¡Bienvenido a Origen!",
                style = BrandTypography.OnboardingTitle,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            Text(
                text = "Aquí construiremos pronto el catálogo de cafés y las " +
                        "historias de los caficultores.",
                style = BrandTypography.OnboardingDescription,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "HomeScreen", widthDp = 360, heightDp = 720)
@Composable
private fun HomeScreenPreview() {
    CafeterosTheme {
        HomeScreen()
    }
}

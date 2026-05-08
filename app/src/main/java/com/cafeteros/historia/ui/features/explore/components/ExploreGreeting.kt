package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Saludo personalizado + título principal de la home del comprador.
 *
 * Renderiza dos líneas de jerarquía:
 *  1. "Buenos días, {userName} ☕" — saludo sutil en sans-serif.
 *  2. "¿Qué café quieres descubrir hoy?" — pregunta editorial en serif bold.
 *
 * El emoji ☕ se concatena al userName (no se renderiza con un Icon) para
 * conservar el estilo amigable del diseño y para que sea trivial sustituirlo
 * por otro emoji (☀, 🌤) según la hora del día en el futuro.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param userName nombre corto del comprador a mostrar en el saludo.
 * @param greeting prefijo del saludo según la hora ("Buenos días").
 * @param question pregunta principal mostrada en serif grande.
 */
@Composable
fun ExploreGreeting(
    modifier: Modifier = Modifier,
    userName: String,
    greeting: String = "Buenos días",
    question: String = "¿Qué café quieres descubrir hoy?"
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        Text(
            text = "$greeting, $userName ☕",
            style = BrandTypography.ExploreGreeting
        )
        Text(
            text = question,
            style = BrandTypography.ExploreHeroTitle
        )
    }
}

@Preview(name = "ExploreGreeting", showBackground = true, widthDp = 360)
@Composable
private fun ExploreGreetingPreview() {
    CafeterosTheme {
        ExploreGreeting(userName = "Mich")
    }
}

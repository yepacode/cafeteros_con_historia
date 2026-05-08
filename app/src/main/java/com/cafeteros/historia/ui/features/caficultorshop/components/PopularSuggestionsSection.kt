package com.cafeteros.historia.ui.features.caficultorshop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.caficultorshop.model.CaficultorShopSampleData
import com.cafeteros.historia.ui.features.caficultorshop.model.PopularSuggestion
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "SUGERENCIAS POPULARES" del empty state.
 *
 * Tiene un label uppercase centrado y, debajo, una fila con dos
 * [PopularSuggestionCard] de igual ancho.
 *
 * @param modifier modifier opcional.
 * @param suggestions lista de sugerencias (la pantalla limita a 2 por
 *   diseño; si llegan más, las extras se ignoran).
 * @param onSuggestionClick callback al pulsar una card.
 */
@Composable
fun PopularSuggestionsSection(
    modifier: Modifier = Modifier,
    suggestions: List<PopularSuggestion>,
    onSuggestionClick: (PopularSuggestion) -> Unit = {}
) {
    if (suggestions.isEmpty()) return
    val visible = suggestions.take(2)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SUGERENCIAS POPULARES",
            style = BrandTypography.SuggestionsLabel,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            visible.forEach { suggestion ->
                PopularSuggestionCard(
                    suggestion = suggestion,
                    onClick = { onSuggestionClick(suggestion) },
                    modifier = Modifier.weight(1f)
                )
            }
            // Spacer para mantener el ancho de la columna izquierda cuando
            // sólo hay una sugerencia.
            if (visible.size == 1) Box(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(name = "PopularSuggestionsSection", showBackground = true, widthDp = 360)
@Composable
private fun PopularSuggestionsSectionPreview() {
    CafeterosTheme {
        PopularSuggestionsSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            suggestions = CaficultorShopSampleData.popularSuggestions
        )
    }
}

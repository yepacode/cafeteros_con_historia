package com.cafeteros.historia.ui.features.caficultorshop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.caficultorshop.model.PopularSuggestion
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card individual de "Sugerencias populares" — fondo blanco, esquinas
 * redondeadas, título serif italic arriba y acción uppercase abajo.
 *
 * @param modifier modifier opcional.
 * @param suggestion sugerencia a renderizar.
 * @param onClick callback al pulsar la card.
 */
@Composable
fun PopularSuggestionCard(
    modifier: Modifier = Modifier,
    suggestion: PopularSuggestion,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.SuggestionCardBackground)
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = suggestion.title, style = BrandTypography.SuggestionCardTitle)
        Text(text = suggestion.actionLabel, style = BrandTypography.SuggestionCardAction)
    }
}

@Preview(name = "PopularSuggestionCard", showBackground = true, widthDp = 200)
@Composable
private fun PopularSuggestionCardPreview() {
    CafeterosTheme {
        PopularSuggestionCard(
            modifier = Modifier.padding(BrandSpacing.md),
            suggestion = PopularSuggestion(
                id = "x",
                title = "Sierra Nevada",
                actionLabel = "VER COLECCIÓN"
            )
        )
    }
}

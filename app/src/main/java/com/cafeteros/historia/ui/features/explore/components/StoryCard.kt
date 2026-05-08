package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.explore.model.CoffeeStory
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card horizontal de una historia en la sección "Historias que enamoran".
 *
 * Layout: imagen cuadrada a la izquierda (84dp) + columna a la derecha con
 * título serif, tiempo de lectura y CTA "Leer historia →" en verde.
 *
 * Toda la card es clickable; el padre suele asociarla a navegación al detalle.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param story datos a renderizar.
 * @param onClick callback al pulsar la card o el CTA.
 */
@Composable
fun StoryCard(
    modifier: Modifier = Modifier,
    story: CoffeeStory,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.CardBackground)
            .clickable(onClick = onClick)
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(story.placeholderColor)
        ) {
            if (story.imageRes != null) {
                Image(
                    painter = painterResource(id = story.imageRes),
                    contentDescription = story.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = story.title,
                style = BrandTypography.StoryTitle
            )
            Text(
                text = "${story.readTimeMinutes} min de lectura",
                style = BrandTypography.StoryMeta
            )
            Text(
                text = "Leer historia →",
                style = BrandTypography.StoryAction
            )
        }
    }
}

@Preview(name = "StoryCard", showBackground = true, widthDp = 360)
@Composable
private fun StoryCardPreview() {
    CafeterosTheme {
        StoryCard(
            story = CoffeeStory(
                title = "Don Alberto y 40 años cultivando café",
                readTimeMinutes = 3,
                placeholderColor = Color(0xFF8B5A2B)
            )
        )
    }
}

package com.cafeteros.historia.ui.features.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.search.model.SearchCategory
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Una card del grid "Explora por". Tiene fondo blanco, esquinas redondeadas
 * y dos elementos apilados verticalmente:
 *  - Ícono coloreado en la esquina superior izquierda.
 *  - Título serif bold abajo.
 *
 * @param modifier modifier opcional.
 * @param category categoría a renderizar.
 * @param onClick callback al pulsar la card.
 */
@Composable
fun SearchCategoryCard(
    modifier: Modifier = Modifier,
    category: SearchCategory,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.SearchCategoryCardBackground)
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = null,
            tint = category.iconTint,
            modifier = Modifier.size(28.dp)
        )
        Text(text = category.title, style = BrandTypography.SearchCategoryTitle)
    }
}

@Preview(name = "SearchCategoryCard", showBackground = true, widthDp = 180)
@Composable
private fun SearchCategoryCardPreview() {
    CafeterosTheme {
        SearchCategoryCard(
            modifier = Modifier.padding(BrandSpacing.md),
            category = SearchCategory.ZONA
        )
    }
}

package com.cafeteros.historia.ui.features.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.search.model.SearchCategory
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Explora por": título italic + grid 2x2 de [SearchCategoryCard].
 *
 * No se usa `LazyVerticalGrid` para evitar conflicto con el scroll vertical
 * de la pantalla parent — las cuatro cards son fijas, así que un par de
 * filas de [Row] con `weight(1f)` da el mismo resultado con menos
 * complejidad.
 *
 * @param modifier modifier opcional.
 * @param onCategoryClick callback al pulsar una card.
 */
@Composable
fun SearchCategoriesGrid(
    modifier: Modifier = Modifier,
    onCategoryClick: (SearchCategory) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(text = "Explora por", style = BrandTypography.SearchSectionTitle)

        SearchCategory.entries.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                rowItems.forEach { category ->
                    SearchCategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(name = "SearchCategoriesGrid", showBackground = true, widthDp = 360)
@Composable
private fun SearchCategoriesGridPreview() {
    CafeterosTheme {
        SearchCategoriesGrid(
            modifier = Modifier.padding(BrandSpacing.lg)
        )
    }
}

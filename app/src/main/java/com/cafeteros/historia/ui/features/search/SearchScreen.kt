package com.cafeteros.historia.ui.features.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.search.components.FeaturedCaficultoresRow
import com.cafeteros.historia.ui.features.search.components.RecentSearchesSection
import com.cafeteros.historia.ui.features.search.components.SearchCategoriesGrid
import com.cafeteros.historia.ui.features.search.components.SearchTopBar
import com.cafeteros.historia.ui.features.search.components.TrendingSearchesSection
import com.cafeteros.historia.ui.features.search.model.FeaturedSearchCaficultor
import com.cafeteros.historia.ui.features.search.model.RecentSearch
import com.cafeteros.historia.ui.features.search.model.SearchCategory
import com.cafeteros.historia.ui.features.search.model.SearchSampleData
import com.cafeteros.historia.ui.features.search.model.TrendingSearch
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla de búsqueda accesible al pulsar la barra de búsqueda de la home.
 *
 * Layout vertical scrollable:
 *  1. [SearchTopBar] con back, input y "Cancelar".
 *  2. Sección "Búsquedas recientes" (se oculta cuando la lista queda vacía).
 *  3. Sección "Tendencias de la semana".
 *  4. Sección "Explora por" (grid 2x2).
 *  5. Sección "Caficultores destacados" (LazyRow horizontal).
 *
 * El padding horizontal vive en cada sección, no en el `Column` scrollable
 * — eso permite que [FeaturedCaficultoresRow] use su propio `contentPadding`
 * y tenga el "edge bleed" en el lado derecho que pide el diseño.
 *
 * Estado interactivo:
 *  - [query]: texto del input. Se mantiene local hasta que exista logica
 *    de búsqueda real.
 *  - `recents`: lista mutable para que "BORRAR TODO" y la X de cada item
 *    afecten la UI inmediatamente.
 *
 * @param modifier modifier opcional.
 * @param initialRecents recientes iniciales — útil para tests/preview.
 * @param trending lista fija que viene del repositorio.
 * @param featuredCaficultores caficultores destacados que se muestran
 *   abajo.
 * @param onBack callback de la flecha o el botón "Cancelar".
 * @param onMicClick callback del micrófono del input.
 * @param onSubmitQuery se dispara cuando el usuario confirma una búsqueda
 *   (al tocar un reciente, una tendencia o presionar enter — todavía no
 *   conectado).
 * @param onCategoryClick callback al elegir una categoría del grid.
 * @param onCaficultorClick callback al pulsar un avatar destacado.
 */
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    initialRecents: List<RecentSearch> = SearchSampleData.recentSearches,
    trending: List<TrendingSearch> = SearchSampleData.trendingSearches,
    featuredCaficultores: List<FeaturedSearchCaficultor> = SearchSampleData.featuredCaficultores,
    onBack: () -> Unit = {},
    onMicClick: () -> Unit = {},
    onSubmitQuery: (String) -> Unit = {},
    onCategoryClick: (SearchCategory) -> Unit = {},
    onCaficultorClick: (FeaturedSearchCaficultor) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val recents = remember { initialRecents.toMutableStateList() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.SearchBackground)
            .statusBarsPadding()
    ) {
        SearchTopBar(
            query = query,
            onQueryChange = { query = it },
            onBack = onBack,
            onCancel = onBack,
            onMicClick = onMicClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.xs))

            RecentSearchesSection(
                modifier = Modifier.padding(horizontal = BrandSpacing.lg),
                recents = recents,
                onClearAll = { recents.clear() },
                onRecentClick = { recent ->
                    query = recent.query
                    onSubmitQuery(recent.query)
                },
                onRemove = { recent -> recents.remove(recent) }
            )

            TrendingSearchesSection(
                modifier = Modifier.padding(horizontal = BrandSpacing.lg),
                trending = trending,
                onTrendingClick = { trend ->
                    query = trend.label
                    onSubmitQuery(trend.label)
                }
            )

            SearchCategoriesGrid(
                modifier = Modifier.padding(horizontal = BrandSpacing.lg),
                onCategoryClick = onCategoryClick
            )

            FeaturedCaficultoresRow(
                caficultores = featuredCaficultores,
                contentPadding = PaddingValues(horizontal = BrandSpacing.lg),
                onCaficultorClick = onCaficultorClick
            )

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Preview(name = "SearchScreen", widthDp = 360, heightDp = 1400)
@Composable
private fun SearchScreenPreview() {
    CafeterosTheme {
        SearchScreen()
    }
}

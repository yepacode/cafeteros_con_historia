package com.cafeteros.historia.ui.features.searchresults

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.searchresults.components.ActiveFiltersRow
import com.cafeteros.historia.ui.features.searchresults.components.ResultsToolbar
import com.cafeteros.historia.ui.features.searchresults.components.SearchResultProductCard
import com.cafeteros.historia.ui.features.searchresults.components.SearchResultsBottomBar
import com.cafeteros.historia.ui.features.searchresults.components.SearchResultsBottomTab
import com.cafeteros.historia.ui.features.searchresults.components.SearchResultsTabs
import com.cafeteros.historia.ui.features.searchresults.components.SearchResultsTopBar
import com.cafeteros.historia.ui.features.searchresults.model.SearchActiveFilter
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultProduct
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultsSampleData
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultsSortOption
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultsTab
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultsViewMode
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla "Resultados de búsqueda".
 *
 * Layout vertical:
 *  1. [SearchResultsTopBar] (back · input editable · botón filtros con dot).
 *  2. [ActiveFiltersRow] con los chips de filtros aplicados (oculto si la
 *     lista queda vacía).
 *  3. [SearchResultsTabs] (Productos / Caficultores / Zonas).
 *  4. [ResultsToolbar] con conteo, toggle de vista y orden.
 *  5. [LazyVerticalGrid] de [SearchResultProductCard] (2 columnas).
 *  6. Spinner de paginación al final del grid.
 *  7. [SearchResultsBottomBar] minimalista anclada al fondo.
 *
 * El estado vive aquí: query del input, lista mutable de filtros activos,
 * tab seleccionada, modo de vista, sort y bottom tab. Cuando exista
 * backend, todo este estado puede persistir en un `ViewModel` sin tocar
 * los composables hijos.
 *
 * @param modifier modifier opcional.
 * @param initialQuery query con la que se abre la pantalla.
 * @param initialFilters filtros activos al abrir.
 * @param products productos del grid (la lista visible — paginación futura).
 * @param totalResultsCount número que se muestra en el toolbar.
 * @param onBack callback de la flecha del top bar.
 * @param onFiltersClick callback del botón de filtros (abre el detalle).
 * @param onProductClick callback al pulsar una card del grid.
 */
@Composable
fun SearchResultsScreen(
    modifier: Modifier = Modifier,
    initialQuery: String = "café huila",
    initialFilters: List<SearchActiveFilter> = SearchResultsSampleData.activeFilters,
    products: List<SearchResultProduct> = SearchResultsSampleData.products,
    totalResultsCount: Int = SearchResultsSampleData.TOTAL_RESULTS,
    onBack: () -> Unit = {},
    onFiltersClick: () -> Unit = {},
    onProductClick: (SearchResultProduct) -> Unit = {}
) {
    var query by remember { mutableStateOf(initialQuery) }
    val activeFilters = remember { initialFilters.toMutableStateList() }
    var selectedTab by remember { mutableStateOf(SearchResultsTab.PRODUCTOS) }
    var viewMode by remember { mutableStateOf(SearchResultsViewMode.GRID) }
    var sortOption by remember { mutableStateOf(SearchResultsSortOption.RELEVANCIA) }
    var bottomTab by remember { mutableStateOf(SearchResultsBottomTab.BUSCAR) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.SearchResultsBackground)
            .statusBarsPadding()
    ) {
        SearchResultsTopBar(
            query = query,
            onQueryChange = { query = it },
            hasActiveFilters = activeFilters.isNotEmpty(),
            onBack = onBack,
            onFiltersClick = onFiltersClick
        )

        ActiveFiltersRow(
            modifier = Modifier.padding(bottom = BrandSpacing.sm),
            filters = activeFilters,
            onRemove = { activeFilters.remove(it) }
        )

        SearchResultsTabs(
            modifier = Modifier.padding(horizontal = BrandSpacing.lg),
            selected = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        ResultsToolbar(
            modifier = Modifier.padding(horizontal = BrandSpacing.lg),
            resultsCount = totalResultsCount,
            viewMode = viewMode,
            sortOption = sortOption,
            onViewModeChange = { viewMode = it },
            onSortChange = { sortOption = it }
        )

        Box(modifier = Modifier.weight(1f)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = BrandSpacing.lg,
                    end = BrandSpacing.lg,
                    top = BrandSpacing.sm,
                    bottom = BrandSpacing.lg
                ),
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = products, key = { it.id }) { product ->
                    SearchResultProductCard(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                }
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                    PaginationSpinner()
                }
            }
        }

        SearchResultsBottomBar(
            selected = bottomTab,
            onTabSelected = { bottomTab = it }
        )
    }
}

@Composable
private fun PaginationSpinner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BrandSpacing.lg),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = BrandColors.TextSecondary,
            strokeWidth = 2.dp,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(name = "SearchResultsScreen", widthDp = 360, heightDp = 1600)
@Composable
private fun SearchResultsScreenPreview() {
    CafeterosTheme {
        SearchResultsScreen()
    }
}

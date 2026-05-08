package com.cafeteros.historia.ui.features.caficultorshop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.cafeteros.historia.ui.features.caficultorshop.components.CaficultorShopFiltersRow
import com.cafeteros.historia.ui.features.caficultorshop.components.CaficultorShopTopBar
import com.cafeteros.historia.ui.features.caficultorshop.components.EmptyResultsHero
import com.cafeteros.historia.ui.features.caficultorshop.components.PopularSuggestionsSection
import com.cafeteros.historia.ui.features.caficultorshop.model.CaficultorShopFilter
import com.cafeteros.historia.ui.features.caficultorshop.model.CaficultorShopSampleData
import com.cafeteros.historia.ui.features.caficultorshop.model.PopularSuggestion
import com.cafeteros.historia.ui.features.searchresults.components.SearchResultsBottomBar
import com.cafeteros.historia.ui.features.searchresults.components.SearchResultsBottomTab
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla del catálogo "el caficultor".
 *
 * Hoy renderiza el **empty state** del catálogo: cuando no hay productos
 * que coincidan con los filtros aplicados. Cuando exista contenido, esta
 * misma pantalla mostrará la grilla de productos arriba y bajará las
 * sugerencias populares; el bloque del empty state desaparecerá.
 *
 * Layout vertical:
 *  1. [CaficultorShopTopBar] (hamburguesa · "el caficultor" · bolsa).
 *  2. [CaficultorShopFiltersRow] (botón "Filtros" + chips de filtros aplicados).
 *  3. [EmptyResultsHero] (imagen + título + cuerpo + "Limpiar filtros").
 *  4. [PopularSuggestionsSection] (label + 2 cards).
 *  5. [SearchResultsBottomBar] minimalista anclada al fondo (reusa el
 *     componente del feature `searchresults/`).
 *
 * Reutiliza la bottom bar del feature `searchresults/` porque el diseño
 * exacto coincide — los 4 mismos iconos con la lupa activa con dot.
 *
 * @param modifier modifier opcional.
 * @param activeFilters filtros aplicados que se renderizan como chips.
 * @param popularSuggestions sugerencias del bloque inferior.
 * @param onMenuClick callback de la hamburguesa del top bar.
 * @param onCartClick callback de la bolsa del top bar.
 * @param onFiltersClick callback del botón "Filtros".
 * @param onClearFilters callback de "Limpiar filtros" del empty state — si
 *   queda sin filtros y la lista de chips se vacía, la fila desaparece.
 * @param onSuggestionClick callback al pulsar una card de sugerencia.
 */
@Composable
fun CaficultorShopScreen(
    modifier: Modifier = Modifier,
    activeFilters: List<CaficultorShopFilter> = CaficultorShopSampleData.activeFilters,
    popularSuggestions: List<PopularSuggestion> = CaficultorShopSampleData.popularSuggestions,
    onMenuClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onFiltersClick: () -> Unit = {},
    onClearFilters: () -> Unit = {},
    onSuggestionClick: (PopularSuggestion) -> Unit = {}
) {
    val filters = remember { activeFilters.toMutableStateList() }
    var bottomTab by remember { mutableStateOf(SearchResultsBottomTab.BUSCAR) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.CaficultorShopBackground)
            .statusBarsPadding()
    ) {
        CaficultorShopTopBar(
            onMenuClick = onMenuClick,
            onCartClick = onCartClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            CaficultorShopFiltersRow(
                filters = filters,
                onFiltersClick = onFiltersClick
            )

            Spacer(modifier = Modifier.height(BrandSpacing.md))

            EmptyResultsHero(
                modifier = Modifier.padding(horizontal = BrandSpacing.lg),
                onClearFilters = {
                    filters.clear()
                    onClearFilters()
                }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.md))

            PopularSuggestionsSection(
                modifier = Modifier.padding(horizontal = BrandSpacing.lg),
                suggestions = popularSuggestions,
                onSuggestionClick = onSuggestionClick
            )

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }

        SearchResultsBottomBar(
            selected = bottomTab,
            onTabSelected = { bottomTab = it }
        )
    }
}

@Preview(name = "CaficultorShopScreen", widthDp = 360, heightDp = 1600)
@Composable
private fun CaficultorShopScreenPreview() {
    CafeterosTheme {
        CaficultorShopScreen()
    }
}

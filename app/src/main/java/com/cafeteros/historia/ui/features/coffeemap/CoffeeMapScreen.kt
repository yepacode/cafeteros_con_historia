package com.cafeteros.historia.ui.features.coffeemap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.coffeemap.components.CoffeeMapBottomSheet
import com.cafeteros.historia.ui.features.coffeemap.components.CoffeeMapTopBar
import com.cafeteros.historia.ui.features.coffeemap.components.IllustratedMapArea
import com.cafeteros.historia.ui.features.coffeemap.components.MapInstructionCard
import com.cafeteros.historia.ui.features.coffeemap.components.MapProductionLegend
import com.cafeteros.historia.ui.features.coffeemap.model.CoffeeMapSampleData
import com.cafeteros.historia.ui.features.coffeemap.model.CoffeeZone
import com.cafeteros.historia.ui.features.coffeemap.model.MapFilter
import com.cafeteros.historia.ui.features.explore.components.ExploreBottomBar
import com.cafeteros.historia.ui.features.explore.components.ExploreTab
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla del Mapa Cafetero.
 *
 * Layout vertical:
 *  - Top bar con flecha de regreso, título "Mapa Cafetero" y botón filtros.
 *  - Lienzo del mapa (área dominante) con los marcadores ilustrados de las
 *    zonas posicionados según [CoffeeZone.normalizedPosition].
 *  - Sobre el lienzo, dos cards flotantes:
 *      * "Toca una zona para explorar" arriba a la izquierda.
 *      * Leyenda "PRODUCCIÓN" con degradado arriba a la derecha.
 *  - Bottom sheet anclado con pills resumen de zonas + filter chips.
 *  - Bottom bar de navegación con 4 tabs (ORIGEN/EXPLORAR/CESTA/PERFIL).
 *
 * Mantiene en estado: zona seleccionada, filtro activo y pestaña activa.
 * Las navegaciones a sub-pantallas (detalle de zona, etc.) salen por
 * callbacks para que la Activity decida.
 *
 * @param modifier modifier opcional.
 * @param zones zonas a mostrar (default: muestras estáticas).
 * @param filters filtros disponibles.
 * @param onBack callback de la flecha del top bar.
 * @param onFilter callback del ícono de filtros del top bar.
 * @param onZoneClick callback al pulsar un marcador o una pill resumen.
 */
@Composable
fun CoffeeMapScreen(
    modifier: Modifier = Modifier,
    zones: List<CoffeeZone> = CoffeeMapSampleData.zones,
    filters: List<MapFilter> = CoffeeMapSampleData.filters,
    onBack: () -> Unit = {},
    onFilter: () -> Unit = {},
    onZoneClick: (CoffeeZone) -> Unit = {}
) {
    var selectedZone by remember { mutableStateOf<CoffeeZone?>(null) }
    var selectedFilter by remember { mutableStateOf(MapFilter.TODOS) }
    var selectedTab by remember { mutableStateOf(ExploreTab.EXPLORAR) }
    val cartItemCount = 3

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.MapCanvasBackground)
            .systemBarsPadding()
    ) {
        CoffeeMapTopBar(
            onBack = onBack,
            onFilter = onFilter
        )

        Box(modifier = Modifier.weight(1f)) {
            IllustratedMapArea(
                modifier = Modifier.fillMaxSize(),
                zones = zones,
                selectedZone = selectedZone,
                onZoneClick = { zone ->
                    selectedZone = zone
                    onZoneClick(zone)
                }
            )

            MapInstructionCard(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = BrandSpacing.lg, top = BrandSpacing.md)
            )

            MapProductionLegend(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = BrandSpacing.lg, top = BrandSpacing.md)
            )

            CoffeeMapBottomSheet(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                zones = zones,
                filters = filters,
                selectedZone = selectedZone,
                selectedFilter = selectedFilter,
                onZoneClick = { zone ->
                    selectedZone = zone
                    onZoneClick(zone)
                },
                onFilterSelected = { selectedFilter = it }
            )
        }

        ExploreBottomBar(
            selected = selectedTab,
            onTabSelected = { selectedTab = it },
            cartItemCount = cartItemCount
        )
    }
}

@Preview(name = "CoffeeMapScreen", widthDp = 360, heightDp = 800)
@Composable
private fun CoffeeMapScreenPreview() {
    CafeterosTheme {
        CoffeeMapScreen()
    }
}

package com.cafeteros.historia.ui.features.coffeemap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.coffeemap.model.CoffeeMapSampleData
import com.cafeteros.historia.ui.features.coffeemap.model.CoffeeZone
import com.cafeteros.historia.ui.features.coffeemap.model.MapFilter
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Panel inferior persistente del mapa con dos filas horizontales:
 *  1. Pills resumen de zonas (ej. "Santander 38", "Eje Cafetero 84"…).
 *  2. Filter chips ("Todos", "Orgánico", "Specialty"…).
 *
 * En el diseño este panel parece un bottom sheet anclado: top redondeado,
 * un grabber/handle gris al centro arriba, y las dos filas debajo. NO
 * implementamos el gesto de drag (no hay un estado "expandido" alterno en
 * el diseño); el handle es decorativo para señalar que es un panel.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param zones zonas a renderizar como pills.
 * @param filters filtros disponibles a renderizar como chips.
 * @param selectedZone zona resaltada actualmente; null = ninguna.
 * @param selectedFilter filtro activo (default = TODOS).
 * @param onZoneClick callback al pulsar una pill de zona.
 * @param onFilterSelected callback al cambiar el filtro activo.
 */
@Composable
fun CoffeeMapBottomSheet(
    modifier: Modifier = Modifier,
    zones: List<CoffeeZone>,
    filters: List<MapFilter>,
    selectedZone: CoffeeZone? = null,
    selectedFilter: MapFilter = MapFilter.TODOS,
    onZoneClick: (CoffeeZone) -> Unit = {},
    onFilterSelected: (MapFilter) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(BrandColors.MapBottomSheetBackground)
            .padding(top = BrandSpacing.sm, bottom = BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        BottomSheetHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        LazyRow(
            contentPadding = PaddingValues(horizontal = BrandSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            items(items = zones, key = { it.name }) { zone ->
                ZoneSummaryPill(
                    zone = zone,
                    onClick = { onZoneClick(zone) }
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = BrandSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            items(items = filters, key = { it.name }) { filter ->
                MapFilterChip(
                    filter = filter,
                    isSelected = filter == selectedFilter,
                    onClick = { onFilterSelected(filter) }
                )
            }
        }
    }
}

/**
 * "Grabber" gris centrado en la parte superior del bottom sheet. Solo es
 * visual: indica al usuario que el panel se podría arrastrar (cuando se
 * implemente el gesto en una iteración futura).
 */
@Composable
private fun BottomSheetHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(vertical = 6.dp)
            .width(40.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(BrandColors.BottomSheetHandle)
    )
}

@Preview(name = "CoffeeMapBottomSheet", showBackground = true, widthDp = 360)
@Composable
private fun CoffeeMapBottomSheetPreview() {
    CafeterosTheme {
        CoffeeMapBottomSheet(
            zones = CoffeeMapSampleData.zones,
            filters = CoffeeMapSampleData.filters
        )
    }
}

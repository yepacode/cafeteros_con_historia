package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.filters.model.FilterZone
import com.cafeteros.historia.ui.features.filters.model.OriginFiltersSampleData
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Grid de 2 columnas con las zonas cafeteras seleccionables.
 *
 * No usa `LazyVerticalGrid` para evitar conflicto con el scroll del padre.
 * Las zonas son una lista corta y fija, así que `chunked(2)` + Rows da el
 * mismo resultado con menos boilerplate.
 *
 * @param modifier modifier opcional.
 * @param zones lista de zonas disponibles.
 * @param selectedNames nombres de las zonas marcadas.
 * @param onZoneToggle callback al pulsar una zona.
 */
@Composable
fun ZoneFilterGrid(
    modifier: Modifier = Modifier,
    zones: List<FilterZone>,
    selectedNames: Set<String>,
    onZoneToggle: (FilterZone) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        zones.chunked(2).forEach { rowZones ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                rowZones.forEach { zone ->
                    ZoneFilterChip(
                        zone = zone,
                        selected = zone.name in selectedNames,
                        onClick = { onZoneToggle(zone) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowZones.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(name = "ZoneFilterGrid", showBackground = true, widthDp = 360)
@Composable
private fun ZoneFilterGridPreview() {
    CafeterosTheme {
        ZoneFilterGrid(
            modifier = Modifier.padding(BrandSpacing.lg),
            zones = OriginFiltersSampleData.zones,
            selectedNames = setOf("Santander"),
            onZoneToggle = {}
        )
    }
}

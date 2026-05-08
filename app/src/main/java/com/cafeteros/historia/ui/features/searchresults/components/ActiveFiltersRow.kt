package com.cafeteros.historia.ui.features.searchresults.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.searchresults.model.SearchActiveFilter
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultsSampleData
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila scrolleable con los chips de los filtros activos. Cada chip muestra
 * el label del filtro y una X para eliminarlo individualmente.
 *
 * Si el filtro tiene [SearchActiveFilter.accentColor] se pinta un punto
 * de ese color a la izquierda (caso típico: las zonas).
 *
 * Si la lista está vacía, la fila se omite — la pantalla parent decide
 * cuándo llamar este composable.
 *
 * @param modifier modifier opcional aplicado al [LazyRow].
 * @param filters filtros activos a mostrar.
 * @param onRemove callback al pulsar la X de un chip.
 */
@Composable
fun ActiveFiltersRow(
    modifier: Modifier = Modifier,
    filters: List<SearchActiveFilter>,
    onRemove: (SearchActiveFilter) -> Unit
) {
    if (filters.isEmpty()) return

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = BrandSpacing.lg),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        items(items = filters, key = { it.id }) { filter ->
            ActiveFilterChip(
                filter = filter,
                onRemove = { onRemove(filter) }
            )
        }
    }
}

@Composable
private fun ActiveFilterChip(
    modifier: Modifier = Modifier,
    filter: SearchActiveFilter,
    onRemove: () -> Unit
) {
    val hasDot = filter.accentColor != null
    val background = if (hasDot) {
        BrandColors.ActiveFilterZoneBackground
    } else {
        BrandColors.ActiveFilterBackground
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .padding(start = BrandSpacing.md, end = BrandSpacing.sm, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        if (hasDot) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(filter.accentColor!!)
            )
        }
        Text(text = filter.label, style = BrandTypography.ActiveFilterLabel)
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Quitar filtro ${filter.label}",
            tint = BrandColors.TextSecondary,
            modifier = Modifier
                .size(16.dp)
                .clickable(onClick = onRemove)
        )
    }
}

@Preview(name = "ActiveFiltersRow", showBackground = true, widthDp = 360)
@Composable
private fun ActiveFiltersRowPreview() {
    CafeterosTheme {
        ActiveFiltersRow(
            modifier = Modifier.padding(vertical = BrandSpacing.sm),
            filters = SearchResultsSampleData.activeFilters,
            onRemove = {}
        )
    }
}

package com.cafeteros.historia.ui.features.caficultorshop.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.caficultorshop.model.CaficultorShopFilter
import com.cafeteros.historia.ui.features.caficultorshop.model.CaficultorShopSampleData
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila bajo el top bar con el botón "Filtros" oscuro y los chips outline
 * de los filtros aplicados.
 *
 * Diseño:
 *  - El botón "Filtros" es un CTA prominente (fondo café oscuro) que abre
 *    la pantalla de detalle de filtros.
 *  - Los chips a la derecha son informativos: muestran qué filtros están
 *    activos pero NO se quitan individualmente — para limpiarlos existe
 *    el botón "Limpiar filtros" del empty state.
 *
 * Se usa [LazyRow] para que la fila scrolee horizontalmente cuando hay
 * más chips de los que caben en pantalla.
 *
 * @param modifier modifier opcional.
 * @param filters filtros aplicados a mostrar como chips.
 * @param onFiltersClick callback del botón "Filtros".
 */
@Composable
fun CaficultorShopFiltersRow(
    modifier: Modifier = Modifier,
    filters: List<CaficultorShopFilter>,
    onFiltersClick: () -> Unit = {}
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = BrandSpacing.lg),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            FiltersButton(onClick = onFiltersClick)
        }
        items(items = filters, key = { it.id }) { filter ->
            FilterOutlineChip(label = filter.label)
        }
    }
}

@Composable
private fun FiltersButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BrandColors.ShopFilterButtonBackground)
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = Icons.Outlined.Tune,
            contentDescription = null,
            tint = BrandColors.ShopFilterButtonText,
            modifier = Modifier.size(18.dp)
        )
        Text(text = "Filtros", style = BrandTypography.ShopFilterButtonLabel)
    }
}

@Composable
private fun FilterOutlineChip(
    modifier: Modifier = Modifier,
    label: String
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .border(BorderStroke(1.dp, BrandColors.ShopFilterChipBorder), shape)
            .padding(horizontal = BrandSpacing.md, vertical = 12.dp)
    ) {
        Text(text = label, style = BrandTypography.ShopFilterChipLabel)
    }
}

@Preview(name = "CaficultorShopFiltersRow", showBackground = true, widthDp = 360)
@Composable
private fun CaficultorShopFiltersRowPreview() {
    CafeterosTheme {
        CaficultorShopFiltersRow(
            modifier = Modifier.padding(vertical = BrandSpacing.sm),
            filters = CaficultorShopSampleData.activeFilters
        )
    }
}

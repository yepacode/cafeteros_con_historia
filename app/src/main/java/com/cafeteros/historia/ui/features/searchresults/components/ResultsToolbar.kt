package com.cafeteros.historia.ui.features.searchresults.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultsSortOption
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultsViewMode
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Toolbar entre los tabs y el grid: contador de resultados, toggle
 * grid/list y dropdown de orden.
 *
 * Distribución horizontal:
 *  - "47 resultados" a la izquierda.
 *  - Toggle de vista (grid + list) en el centro/derecha.
 *  - Separador vertical sutil + "Relevancia ⌄" alineado al borde derecho.
 *
 * @param modifier modifier opcional.
 * @param resultsCount número total de resultados.
 * @param viewMode modo de vista actual.
 * @param sortOption opción de orden actual.
 * @param onViewModeChange callback al cambiar el modo de vista.
 * @param onSortChange callback al elegir una opción de orden.
 */
@Composable
fun ResultsToolbar(
    modifier: Modifier = Modifier,
    resultsCount: Int,
    viewMode: SearchResultsViewMode,
    sortOption: SearchResultsSortOption,
    onViewModeChange: (SearchResultsViewMode) -> Unit,
    onSortChange: (SearchResultsSortOption) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(
            text = "$resultsCount resultados",
            style = BrandTypography.ResultsCountLabel,
            modifier = Modifier.weight(1f)
        )

        ViewModeToggle(
            current = viewMode,
            onChange = onViewModeChange
        )

        Box(
            modifier = Modifier
                .padding(horizontal = BrandSpacing.sm)
                .width(1.dp)
                .height(20.dp)
                .background(BrandColors.DividerLine)
        )

        SortDropdown(
            current = sortOption,
            onChange = onSortChange
        )
    }
}

@Composable
private fun ViewModeToggle(
    modifier: Modifier = Modifier,
    current: SearchResultsViewMode,
    onChange: (SearchResultsViewMode) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ViewModeIcon(
            active = current == SearchResultsViewMode.GRID,
            icon = Icons.Filled.GridView,
            contentDescription = "Vista en cuadrícula",
            onClick = { onChange(SearchResultsViewMode.GRID) }
        )
        ViewModeIcon(
            active = current == SearchResultsViewMode.LIST,
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            contentDescription = "Vista en lista",
            onClick = { onChange(SearchResultsViewMode.LIST) }
        )
    }
}

@Composable
private fun ViewModeIcon(
    modifier: Modifier = Modifier,
    active: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(shape)
            .then(
                if (active) Modifier.background(BrandColors.ViewModeActiveBackground)
                else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (active) BrandColors.ViewModeActiveTint else BrandColors.ViewModeInactive,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun SortDropdown(
    modifier: Modifier = Modifier,
    current: SearchResultsSortOption,
    onChange: (SearchResultsSortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = current.label, style = BrandTypography.ResultsSortLabel)
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Cambiar orden",
                tint = BrandColors.TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SearchResultsSortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.label, style = BrandTypography.ResultsSortLabel) },
                    onClick = {
                        onChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(name = "ResultsToolbar", showBackground = true, widthDp = 360)
@Composable
private fun ResultsToolbarPreview() {
    CafeterosTheme {
        ResultsToolbar(
            modifier = Modifier.padding(BrandSpacing.lg),
            resultsCount = 47,
            viewMode = SearchResultsViewMode.GRID,
            sortOption = SearchResultsSortOption.RELEVANCIA,
            onViewModeChange = {},
            onSortChange = {}
        )
    }
}

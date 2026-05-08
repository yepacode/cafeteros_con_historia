package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.filters.model.FilterSortOption
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "ORDENAR POR": label + lista de radios con la opción activa
 * marcada. A diferencia de [ProcessFilterSection], el radio aparece a la
 * IZQUIERDA del label.
 *
 * @param modifier modifier opcional.
 * @param selected opción activa.
 * @param onSelect callback al elegir una.
 */
@Composable
fun SortByFilterSection(
    modifier: Modifier = Modifier,
    selected: FilterSortOption,
    onSelect: (FilterSortOption) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        FilterSectionLabel(text = "ORDENAR POR")
        FilterSortOption.entries.forEach { option ->
            SortRow(
                label = option.label,
                selected = option == selected,
                onClick = { onSelect(option) }
            )
        }
    }
}

@Composable
private fun SortRow(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        SortRadio(selected = selected)
        Text(text = label, style = BrandTypography.FilterListItem)
    }
}

@Composable
private fun SortRadio(modifier: Modifier = Modifier, selected: Boolean) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = if (selected) BrandColors.RadioSelected else BrandColors.RadioUnselected,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(BrandColors.RadioSelected)
            )
        }
    }
}

@Preview(name = "SortByFilterSection", showBackground = true, widthDp = 360)
@Composable
private fun SortByFilterSectionPreview() {
    CafeterosTheme {
        SortByFilterSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            selected = FilterSortOption.RELEVANCIA,
            onSelect = {}
        )
    }
}

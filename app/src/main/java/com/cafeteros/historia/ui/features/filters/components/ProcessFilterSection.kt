package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.filters.model.FilterProcess
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "PROCESO": label + lista vertical de [RadioOptionRow]s con los
 * cuatro procesos posibles. Selección excluyente.
 *
 * @param modifier modifier opcional.
 * @param selected proceso actualmente marcado.
 * @param onSelect callback al elegir uno.
 */
@Composable
fun ProcessFilterSection(
    modifier: Modifier = Modifier,
    selected: FilterProcess,
    onSelect: (FilterProcess) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        FilterSectionLabel(text = "PROCESO")
        FilterProcess.entries.forEach { option ->
            RadioOptionRow(
                label = option.label,
                selected = option == selected,
                onClick = { onSelect(option) }
            )
        }
    }
}

@Preview(name = "ProcessFilterSection", showBackground = true, widthDp = 360)
@Composable
private fun ProcessFilterSectionPreview() {
    CafeterosTheme {
        ProcessFilterSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            selected = FilterProcess.TODOS,
            onSelect = {}
        )
    }
}

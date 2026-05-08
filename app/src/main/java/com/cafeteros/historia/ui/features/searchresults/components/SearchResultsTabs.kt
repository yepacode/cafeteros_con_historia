package com.cafeteros.historia.ui.features.searchresults.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultsTab
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Tabs del scope de resultados (Productos / Caficultores / Zonas).
 *
 * Los tabs son texto plano alineado a la izquierda, no un `TabRow` de
 * Material — la activa va con peso semibold y subrayado oscuro de ancho
 * fijo, las demás van en gris secundario sin subrayado.
 *
 * @param modifier modifier opcional.
 * @param selected tab activa.
 * @param onTabSelected callback al pulsar un tab.
 */
@Composable
fun SearchResultsTabs(
    modifier: Modifier = Modifier,
    selected: SearchResultsTab,
    onTabSelected: (SearchResultsTab) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
    ) {
        SearchResultsTab.entries.forEach { tab ->
            TabItem(
                label = tab.label,
                selected = tab == selected,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
private fun TabItem(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = BrandSpacing.sm),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            style = if (selected) BrandTypography.SearchResultsTabActive
            else BrandTypography.SearchResultsTabInactive
        )
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .height(2.dp)
                .width(if (selected) 32.dp else 0.dp)
                .background(BrandColors.SearchResultsTabIndicator)
        )
    }
}

@Preview(name = "SearchResultsTabs", showBackground = true, widthDp = 360)
@Composable
private fun SearchResultsTabsPreview() {
    CafeterosTheme {
        SearchResultsTabs(
            modifier = Modifier.padding(BrandSpacing.lg),
            selected = SearchResultsTab.PRODUCTOS,
            onTabSelected = {}
        )
    }
}

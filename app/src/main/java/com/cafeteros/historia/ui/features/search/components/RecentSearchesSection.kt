package com.cafeteros.historia.ui.features.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.search.model.RecentSearch
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Búsquedas recientes":
 *  - Header con título italic + acción "BORRAR TODO" en rojo.
 *  - Lista vertical de [RecentSearchItem] separados por padding interno.
 *
 * Si la lista está vacía, la sección entera se omite — la pantalla parent
 * decide si llamar este composable.
 *
 * @param modifier modifier opcional.
 * @param recents lista de búsquedas recientes a mostrar.
 * @param onClearAll callback del enlace "BORRAR TODO".
 * @param onRecentClick callback al tocar una fila (re-disparar búsqueda).
 * @param onRemove callback al pulsar la X de una fila individual.
 */
@Composable
fun RecentSearchesSection(
    modifier: Modifier = Modifier,
    recents: List<RecentSearch>,
    onClearAll: () -> Unit = {},
    onRecentClick: (RecentSearch) -> Unit = {},
    onRemove: (RecentSearch) -> Unit = {}
) {
    if (recents.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Búsquedas recientes", style = BrandTypography.SearchSectionTitle)
            Text(
                text = "BORRAR TODO",
                style = BrandTypography.ClearAllLabel,
                modifier = Modifier.clickable(onClick = onClearAll)
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            recents.forEach { recent ->
                RecentSearchItem(
                    query = recent.query,
                    onClick = { onRecentClick(recent) },
                    onRemove = { onRemove(recent) }
                )
            }
        }
    }
}

@Preview(name = "RecentSearchesSection", showBackground = true, widthDp = 360)
@Composable
private fun RecentSearchesSectionPreview() {
    CafeterosTheme {
        RecentSearchesSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            recents = listOf(
                RecentSearch(id = "1", query = "Café de especialidad Huila"),
                RecentSearch(id = "2", query = "Don Alberto Nariño")
            )
        )
    }
}

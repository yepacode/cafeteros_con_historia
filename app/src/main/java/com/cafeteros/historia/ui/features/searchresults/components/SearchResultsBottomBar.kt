package com.cafeteros.historia.ui.features.searchresults.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bottom bar minimalista de la pantalla de resultados.
 *
 * Diferente de [com.cafeteros.historia.ui.features.explore.components.ExploreBottomBar]:
 * tiene solo 4 iconos sin labels, y bajo el icono activo se pinta un punto
 * naranja en lugar de subrayar el label. Vive en este feature porque es
 * el único lugar del diseño donde aparece — si más adelante se reusa, se
 * mueve a `ui/components/`.
 *
 * @param modifier modifier opcional.
 * @param selected tab activo.
 * @param onTabSelected callback al pulsar un tab.
 */
@Composable
fun SearchResultsBottomBar(
    modifier: Modifier = Modifier,
    selected: SearchResultsBottomTab,
    onTabSelected: (SearchResultsBottomTab) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.ResultsBottomBarBackground)
            .navigationBarsPadding()
            .padding(vertical = BrandSpacing.sm),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchResultsBottomTab.entries.forEach { tab ->
            BottomTabItem(
                tab = tab,
                selected = tab == selected,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
private fun BottomTabItem(
    modifier: Modifier = Modifier,
    tab: SearchResultsBottomTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.contentDescription,
            tint = if (selected) BrandColors.ResultsBottomBarActive
            else BrandColors.ResultsBottomBarInactive,
            modifier = Modifier.size(24.dp)
        )
        Box(
            modifier = Modifier
                .size(if (selected) 5.dp else 0.dp)
                .clip(CircleShape)
                .background(
                    if (selected) BrandColors.ResultsBottomBarActiveDot
                    else androidx.compose.ui.graphics.Color.Transparent
                )
        )
        if (!selected) Spacer(modifier = Modifier.height(5.dp))
    }
}

/** Tabs disponibles en la bottom bar minimalista de los resultados. */
enum class SearchResultsBottomTab(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val contentDescription: String
) {
    INICIO(icon = Icons.Outlined.Home, contentDescription = "Inicio"),
    BUSCAR(icon = Icons.Outlined.Search, contentDescription = "Buscar"),
    FAVORITOS(icon = Icons.Outlined.Favorite, contentDescription = "Favoritos"),
    PERFIL(icon = Icons.Outlined.Person, contentDescription = "Perfil")
}

@Preview(name = "SearchResultsBottomBar", showBackground = true, widthDp = 360)
@Composable
private fun SearchResultsBottomBarPreview() {
    CafeterosTheme {
        SearchResultsBottomBar(
            selected = SearchResultsBottomTab.BUSCAR,
            onTabSelected = {}
        )
    }
}

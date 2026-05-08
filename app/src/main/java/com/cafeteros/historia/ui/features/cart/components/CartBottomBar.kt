package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.cart.model.CartBottomTab
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bottom bar exclusiva del carrito.
 *
 * Cuatro tabs (`ORIGEN`, `CATÁLOGO`, `MI BOLSA`, `PERFIL`) con icono y
 * label uppercase. El tab activo se pinta en dorado tanto el icono como
 * el label; los demás van en gris.
 *
 * Vive dentro del feature `cart/` porque es el único contexto donde
 * aparece este dock — distinto de `ExploreBottomBar` (5 tabs con texto)
 * y de `SearchResultsBottomBar` (4 iconos sin labels).
 *
 * @param modifier modifier opcional.
 * @param selected tab activo.
 * @param onTabSelected callback al pulsar un tab.
 */
@Composable
fun CartBottomBar(
    modifier: Modifier = Modifier,
    selected: CartBottomTab,
    onTabSelected: (CartBottomTab) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.CartBottomBarBackground)
            .navigationBarsPadding()
            .padding(vertical = BrandSpacing.sm),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CartBottomTab.entries.forEach { tab ->
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
    tab: CartBottomTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) BrandColors.CartBottomBarActive
    else BrandColors.CartBottomBarInactive

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = BrandSpacing.xs, horizontal = BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = tab.label,
            style = BrandTypography.CartBottomBarLabel.copy(color = color)
        )
    }
}

@Preview(name = "CartBottomBar", showBackground = true, widthDp = 360)
@Composable
private fun CartBottomBarPreview() {
    CafeterosTheme {
        CartBottomBar(
            selected = CartBottomTab.MI_BOLSA,
            onTabSelected = {}
        )
    }
}

package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Barra de navegación inferior del comprador.
 *
 * Renderiza las 5 pestañas definidas en [ExploreTab]: ícono + etiqueta en
 * mayúsculas. La pestaña activa pinta su ícono y label en café oscuro y
 * añade un punto debajo como indicador visual; las inactivas se muestran
 * en gris. La pestaña [ExploreTab.CARRITO] superpone un [CartCountBadge]
 * cuando [cartItemCount] es mayor que cero.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param selected pestaña activa.
 * @param onTabSelected callback al pulsar una pestaña.
 * @param cartItemCount número de productos en el carrito; 0 oculta el badge.
 */
@Composable
fun ExploreBottomBar(
    modifier: Modifier = Modifier,
    selected: ExploreTab,
    onTabSelected: (ExploreTab) -> Unit,
    cartItemCount: Int = 0
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.BottomBarBackground)
            .padding(vertical = BrandSpacing.sm),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExploreTab.entries.forEach { tab ->
            BottomBarItem(
                tab = tab,
                isSelected = tab == selected,
                badgeCount = if (tab == ExploreTab.CARRITO) cartItemCount else 0,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    tab: ExploreTab,
    isSelected: Boolean,
    badgeCount: Int,
    onClick: () -> Unit
) {
    val tint = if (isSelected) BrandColors.BottomBarActive else BrandColors.BottomBarInactive

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.xs, vertical = BrandSpacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
            if (badgeCount > 0) {
                CartCountBadge(
                    count = badgeCount,
                    modifier = Modifier.offset(x = 8.dp, y = (-6).dp)
                )
            }
        }
        Text(
            text = tab.label,
            style = BrandTypography.BottomBarLabel.copy(color = tint)
        )
        Spacer(modifier = Modifier.height(2.dp))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(BrandColors.BottomBarActive)
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(name = "ExploreBottomBar", showBackground = true, widthDp = 360)
@Composable
private fun ExploreBottomBarPreview() {
    CafeterosTheme {
        ExploreBottomBar(
            selected = ExploreTab.EXPLORAR,
            onTabSelected = {},
            cartItemCount = 3
        )
    }
}

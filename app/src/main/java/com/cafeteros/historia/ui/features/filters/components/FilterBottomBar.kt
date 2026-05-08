package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
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
 * Bottom bar fijo de la pantalla de filtros con dos acciones:
 *  - "LIMPIAR TODO" (claro, ícono refresh).
 *  - "VER X RESULTADOS" (oscuro, ícono check) — el dominante.
 *
 * Se queda anclado al fondo respetando el navigation bar gesture área.
 *
 * @param modifier modifier opcional.
 * @param resultsCount número que se muestra dentro del CTA principal.
 * @param onClearAll callback del botón "LIMPIAR TODO".
 * @param onApply callback del botón "VER X RESULTADOS".
 */
@Composable
fun FilterBottomBar(
    modifier: Modifier = Modifier,
    resultsCount: Int,
    onClearAll: () -> Unit,
    onApply: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.FiltersBackground)
            .navigationBarsPadding()
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        BottomBarButton(
            modifier = Modifier.weight(1f),
            label = "LIMPIAR TODO",
            icon = Icons.Filled.Refresh,
            onClick = onClearAll,
            primary = false
        )
        BottomBarButton(
            modifier = Modifier.weight(1.4f),
            label = "VER $resultsCount RESULTADOS",
            icon = Icons.Filled.Check,
            onClick = onApply,
            primary = true
        )
    }
}

@Composable
private fun BottomBarButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    primary: Boolean
) {
    val shape = RoundedCornerShape(14.dp)
    val containerModifier = if (primary) {
        Modifier
            .clip(shape)
            .background(BrandColors.WideDarkButtonBackground)
    } else {
        Modifier
            .clip(shape)
            .background(BrandColors.ClearFiltersButtonBackground)
            .border(BorderStroke(1.dp, BrandColors.ClearFiltersButtonBorder), shape)
    }

    Row(
        modifier = modifier
            .height(54.dp)
            .then(containerModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (primary) BrandColors.WideDarkButtonText else BrandColors.ClearFiltersButtonText,
            modifier = Modifier
                .size(16.dp)
                .padding(end = 4.dp)
        )
        Text(
            text = label,
            style = if (primary) BrandTypography.FiltersBottomCta else BrandTypography.FiltersBottomClear
        )
    }
}

@Preview(name = "FilterBottomBar", showBackground = true, widthDp = 360)
@Composable
private fun FilterBottomBarPreview() {
    CafeterosTheme {
        FilterBottomBar(
            resultsCount = 47,
            onClearAll = {},
            onApply = {}
        )
    }
}

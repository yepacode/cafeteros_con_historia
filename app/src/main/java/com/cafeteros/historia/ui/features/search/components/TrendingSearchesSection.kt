package com.cafeteros.historia.ui.features.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.search.model.TrendingSearch
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Tendencias de la semana":
 *  - Título italic.
 *  - Layout fluido casero ([WrappingFlowRow]) que envuelve los chips en
 *    nuevas filas cuando no caben en el ancho disponible.
 *
 * Se usa un layout custom en vez de `FlowRow` de `compose-foundation`
 * porque la signature de `FlowRow` cambió entre versiones del BOM y
 * causaba `NoSuchMethodError` en runtime — el layout casero es estable
 * y no depende del BOM.
 *
 * @param modifier modifier opcional.
 * @param trending lista de tendencias.
 * @param onTrendingClick callback al pulsar un chip.
 */
@Composable
fun TrendingSearchesSection(
    modifier: Modifier = Modifier,
    trending: List<TrendingSearch>,
    onTrendingClick: (TrendingSearch) -> Unit = {}
) {
    if (trending.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(text = "Tendencias de la semana", style = BrandTypography.SearchSectionTitle)

        WrappingFlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalSpacing = BrandSpacing.sm,
            verticalSpacing = BrandSpacing.sm
        ) {
            trending.forEach { item ->
                TrendingChip(
                    label = item.label,
                    onClick = { onTrendingClick(item) }
                )
            }
        }
    }
}

@Composable
private fun TrendingChip(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(BrandColors.TrendingChipBackground)
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, style = BrandTypography.TrendingChipLabel)
    }
}

/**
 * Layout que coloca los hijos en filas y los envuelve a la siguiente cuando
 * el ancho disponible se acaba. Equivalente funcional a `FlowRow` de
 * Compose Foundation pero implementado a mano para no depender del BOM.
 *
 * @param horizontalSpacing separación horizontal entre items en la misma fila.
 * @param verticalSpacing separación vertical entre filas.
 */
@Composable
private fun WrappingFlowRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: androidx.compose.ui.unit.Dp,
    verticalSpacing: androidx.compose.ui.unit.Dp,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val hSpacing = horizontalSpacing.roundToPx()
        val vSpacing = verticalSpacing.roundToPx()
        val maxWidth = constraints.maxWidth

        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }

        // Calculamos posiciones (x, y) de cada placeable iterando y
        // saltando de fila cuando el siguiente no cabe en el ancho disponible.
        val positions = IntArray(placeables.size * 2)
        var rowX = 0
        var rowY = 0
        var rowHeight = 0
        placeables.forEachIndexed { index, placeable ->
            if (rowX > 0 && rowX + placeable.width > maxWidth) {
                rowX = 0
                rowY += rowHeight + vSpacing
                rowHeight = 0
            }
            positions[index * 2] = rowX
            positions[index * 2 + 1] = rowY
            rowX += placeable.width + hSpacing
            if (placeable.height > rowHeight) rowHeight = placeable.height
        }

        val totalHeight = rowY + rowHeight
        layout(maxWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = positions[index * 2],
                    y = positions[index * 2 + 1]
                )
            }
        }
    }
}

@Preview(name = "TrendingSearchesSection", showBackground = true, widthDp = 360)
@Composable
private fun TrendingSearchesSectionPreview() {
    CafeterosTheme {
        TrendingSearchesSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            trending = listOf(
                TrendingSearch("Microlotes Huila"),
                TrendingSearch("Café premiado"),
                TrendingSearch("Tueste oscuro"),
                TrendingSearch("Geisha"),
                TrendingSearch("Nariño")
            )
        )
    }
}

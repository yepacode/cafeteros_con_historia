package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp

/**
 * Layout mínimo que coloca los hijos en filas y los envuelve a la siguiente
 * cuando el ancho disponible se agota.
 *
 * Es una alternativa estable a `FlowRow` de `compose-foundation` — su
 * signature cambió entre versiones del BOM y causaba `NoSuchMethodError`
 * en runtime. Esta implementación es independiente del BOM.
 *
 * @param modifier modifier opcional aplicado al layout.
 * @param horizontalSpacing separación horizontal entre items en la misma
 *   fila.
 * @param verticalSpacing separación vertical entre filas.
 * @param content hijos a distribuir.
 */
@Composable
fun WrappingFlowRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp,
    verticalSpacing: Dp,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val hSpacing = horizontalSpacing.roundToPx()
        val vSpacing = verticalSpacing.roundToPx()
        val maxWidth = constraints.maxWidth

        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }

        // Posiciones (x, y) de cada placeable. Saltamos de fila cuando el
        // siguiente placeable no cabría en el ancho restante.
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

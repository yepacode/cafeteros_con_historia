package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
 * Top bar de la pantalla de filtros.
 *
 * Layout horizontal en una fila:
 *  - "X" para cerrar a la izquierda.
 *  - Título "Filtros de Origen" centrado.
 *  - Texto "Limpiar" a la derecha.
 *
 * @param modifier modifier opcional.
 * @param onClose callback de la X.
 * @param onClear callback del enlace "Limpiar".
 */
@Composable
fun FilterTopBar(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    onClear: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cerrar filtros",
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = "Limpiar",
                style = BrandTypography.FiltersClearAction,
                modifier = Modifier
                    .clickable(onClick = onClear)
                    .padding(horizontal = BrandSpacing.xs, vertical = BrandSpacing.sm)
            )
        }

        Text(text = "Filtros de Origen", style = BrandTypography.FiltersTopBarTitle)
    }
}

@Preview(name = "FilterTopBar", showBackground = true, widthDp = 360)
@Composable
private fun FilterTopBarPreview() {
    CafeterosTheme {
        FilterTopBar()
    }
}

package com.cafeteros.historia.ui.features.searchresults.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Top bar de la pantalla de resultados.
 *
 * Layout en una fila:
 *  - Flecha de regreso a la izquierda.
 *  - Input editable con la query actual y un ícono de lupa a la izquierda
 *    (sin micrófono — ya hay texto, el micrófono solo aparece en la
 *    pantalla de búsqueda vacía).
 *  - Botón cuadrado de filtros a la derecha. Cuando [hasActiveFilters] es
 *    true, se pinta un punto naranja en la esquina superior derecha del
 *    botón para indicar que hay filtros aplicados.
 *
 * @param modifier modifier opcional.
 * @param query texto actual de la búsqueda.
 * @param onQueryChange callback al editar la query.
 * @param hasActiveFilters si true se muestra el dot indicador.
 * @param onBack callback de la flecha.
 * @param onFiltersClick callback del botón de filtros.
 */
@Composable
fun SearchResultsTopBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    hasActiveFilters: Boolean,
    onBack: () -> Unit = {},
    onFiltersClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = BrandColors.TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrandColors.InputBackground)
                .padding(horizontal = BrandSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = BrandColors.InputHint,
                modifier = Modifier.size(20.dp)
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = "Busca café, caficultor o zona...",
                        style = BrandTypography.SearchResultsQuery.copy(
                            color = BrandColors.InputHint
                        )
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = BrandTypography.SearchResultsQuery,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        FiltersIconButton(
            hasActiveFilters = hasActiveFilters,
            onClick = onFiltersClick
        )
    }
}

@Composable
private fun FiltersIconButton(
    modifier: Modifier = Modifier,
    hasActiveFilters: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Tune,
            contentDescription = "Abrir filtros",
            tint = BrandColors.TextPrimary,
            modifier = Modifier.size(24.dp)
        )
        if (hasActiveFilters) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp, top = 4.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(BrandColors.FilterIndicatorDot)
            )
        }
    }
}

@Preview(name = "SearchResultsTopBar", showBackground = true, widthDp = 360)
@Composable
private fun SearchResultsTopBarPreview() {
    CafeterosTheme {
        SearchResultsTopBar(
            query = "café huila",
            onQueryChange = {},
            hasActiveFilters = true
        )
    }
}

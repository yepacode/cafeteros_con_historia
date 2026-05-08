package com.cafeteros.historia.ui.features.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.Search
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
 * Top bar de la pantalla de búsqueda.
 *
 * Layout horizontal en una fila:
 *  - Flecha de regreso a la izquierda.
 *  - Input gris-beige (lupa + texto + micrófono) que ocupa el espacio sobrante.
 *  - Texto "Cancelar" a la derecha que cierra la pantalla.
 *
 * Usa [BasicTextField] para mantener el mismo look que [com.cafeteros.historia.ui.features.explore.components.ExploreSearchBar].
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param query texto actual de la búsqueda.
 * @param onQueryChange callback al editar el texto.
 * @param onBack callback de la flecha de la izquierda.
 * @param onCancel callback del botón "Cancelar".
 * @param onMicClick callback del micrófono dentro del input.
 * @param placeholder texto guía cuando el campo está vacío.
 */
@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit = {},
    onCancel: () -> Unit = {},
    onMicClick: () -> Unit = {},
    placeholder: String = "Busca café, caficultor o zona..."
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
                .clip(RoundedCornerShape(20.dp))
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
                        text = placeholder,
                        style = BrandTypography.FieldText.copy(color = BrandColors.InputHint)
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = BrandTypography.FieldText,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Icon(
                imageVector = Icons.Filled.Mic,
                contentDescription = "Búsqueda por voz",
                tint = BrandColors.TextPrimary,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onMicClick)
            )
        }

        Text(
            text = "Cancelar",
            style = BrandTypography.SearchCancelAction,
            modifier = Modifier
                .clickable(onClick = onCancel)
                .padding(horizontal = BrandSpacing.xs)
        )
    }
}

@Preview(name = "SearchTopBar", showBackground = true, widthDp = 360)
@Composable
private fun SearchTopBarPreview() {
    CafeterosTheme {
        SearchTopBar(
            query = "",
            onQueryChange = {}
        )
    }
}

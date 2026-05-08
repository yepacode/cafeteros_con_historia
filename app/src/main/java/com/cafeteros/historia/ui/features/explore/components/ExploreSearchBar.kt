package com.cafeteros.historia.ui.features.explore.components

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
 * Barra de búsqueda + botón de filtros de la pantalla de exploración.
 *
 * Combina dos elementos en una fila:
 *  - Input redondeado con ícono de lupa y placeholder "Busca café, caficultor o zona…".
 *  - Botón cuadrado verde a la derecha con ícono de filtros (sliders).
 *
 * Tiene dos modos:
 *  - **Editable** (default): la caja de texto recibe foco y permite escribir.
 *    Útil cuando esta misma barra es la única superficie de búsqueda.
 *  - **Tap-to-navigate**: cuando se pasa [onSearchClick] no nulo, la caja
 *    deja de ser editable y todo el área del input dispara `onSearchClick`.
 *    En esta home eso navega a la pantalla dedicada de búsqueda.
 *
 * Usa [BasicTextField] en vez de [androidx.compose.material3.TextField] para
 * obtener una caja de texto sin línea inferior y poder personalizar todo el
 * fondo manualmente, idéntico al diseño.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param query texto actual de la búsqueda.
 * @param onQueryChange callback al editar el texto.
 * @param onFilterClick callback al pulsar el botón verde de filtros.
 * @param onSearchClick si se provee, la barra entra en modo "tap-to-navigate":
 *   el input no es editable y todo su área dispara este callback.
 * @param placeholder texto guía cuando el campo está vacío.
 */
@Composable
fun ExploreSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit = {},
    onSearchClick: (() -> Unit)? = null,
    placeholder: String = "Busca café, caficultor o zona..."
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tapMode = onSearchClick != null
        Row(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrandColors.InputBackground)
                .then(
                    if (tapMode) Modifier.clickable(onClick = onSearchClick!!)
                    else Modifier
                )
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
                if (!tapMode) {
                    // Solo se renderiza el TextField cuando la barra es editable.
                    // En modo tap el placeholder queda visible pero no se puede
                    // escribir; el tap captura el evento y navega.
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = BrandTypography.FieldText,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrandColors.ForestGreen)
                .clickable(onClick = onFilterClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Tune,
                contentDescription = "Filtros",
                tint = BrandColors.CardBackground,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Preview(name = "ExploreSearchBar – vacío", showBackground = true, widthDp = 360)
@Composable
private fun ExploreSearchBarEmptyPreview() {
    CafeterosTheme {
        ExploreSearchBar(
            query = "",
            onQueryChange = {}
        )
    }
}

@Preview(name = "ExploreSearchBar – con texto", showBackground = true, widthDp = 360)
@Composable
private fun ExploreSearchBarFilledPreview() {
    CafeterosTheme {
        ExploreSearchBar(
            query = "Huila",
            onQueryChange = {}
        )
    }
}

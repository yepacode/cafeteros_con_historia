package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Etiqueta uppercase de cada sección del filtro ("ZONA CAFETERA",
 * "PERFIL DE SABOR", etc).
 *
 * Soporta una pieza opcional [trailing] alineada a la derecha — útil para
 * mostrar el rango activo de precio junto al título "RANGO DE PRECIO".
 *
 * @param modifier modifier opcional.
 * @param text texto en mayúsculas.
 * @param trailing slot opcional alineado al final de la fila.
 */
@Composable
fun FilterSectionLabel(
    modifier: Modifier = Modifier,
    text: String,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, style = BrandTypography.FilterSectionLabel)
        if (trailing != null) trailing()
    }
}

@Preview(name = "FilterSectionLabel", showBackground = true, widthDp = 360)
@Composable
private fun FilterSectionLabelPreview() {
    CafeterosTheme {
        FilterSectionLabel(
            text = "RANGO DE PRECIO",
            trailing = {
                Text(text = "$30.000 - $80.000", style = BrandTypography.PriceRangeValue)
            }
        )
    }
}

package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.clickable
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
 * Cabecera reutilizable para cada sección de la pantalla de exploración.
 *
 * Renderiza el título de la sección a la izquierda y, opcionalmente, una
 * acción "Ver todos / Ver mapa" a la derecha. Cuando [actionLabel] es null
 * la acción no se muestra (útil para secciones como "Historias que enamoran"
 * que no tienen link de ver más en el diseño).
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param title título de la sección (ej. "Caficultores destacados").
 * @param actionLabel texto del enlace a la derecha; null para ocultarlo.
 * @param onActionClick callback del enlace; ignorado si [actionLabel] es null.
 */
@Composable
fun ExploreSectionHeader(
    modifier: Modifier = Modifier,
    title: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = BrandTypography.SectionTitle
        )
        if (actionLabel != null) {
            Text(
                text = actionLabel,
                style = BrandTypography.SectionAction,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Preview(name = "SectionHeader – con acción", showBackground = true, widthDp = 360)
@Composable
private fun ExploreSectionHeaderWithActionPreview() {
    CafeterosTheme {
        ExploreSectionHeader(
            title = "Caficultores destacados",
            actionLabel = "Ver todos"
        )
    }
}

@Preview(name = "SectionHeader – simple", showBackground = true, widthDp = 360)
@Composable
private fun ExploreSectionHeaderSimplePreview() {
    CafeterosTheme {
        ExploreSectionHeader(title = "Historias que enamoran ✨")
    }
}

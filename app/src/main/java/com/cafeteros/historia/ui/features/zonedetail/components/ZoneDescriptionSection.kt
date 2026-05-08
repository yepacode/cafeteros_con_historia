package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Sobre {zona}" con un párrafo plegable.
 *
 * Por default muestra los primeros [collapsedLines] saltos de párrafo y un
 * link "Leer más ›" debajo. Al pulsar, el párrafo se expande y el link
 * cambia a "Leer menos". Mantenemos el estado dentro del componente porque
 * no es información que la pantalla padre necesite.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param title título de la sección ("Sobre Santander").
 * @param body párrafo completo (puede tener varios saltos de línea).
 * @param collapsedLines máximo de líneas visibles cuando está colapsado.
 */
@Composable
fun ZoneDescriptionSection(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    collapsedLines: Int = 6
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = title, style = BrandTypography.ZoneSectionTitle)
        Text(
            text = body,
            style = BrandTypography.ZoneDescriptionBody,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedLines,
            overflow = if (expanded) {
                androidx.compose.ui.text.style.TextOverflow.Visible
            } else {
                androidx.compose.ui.text.style.TextOverflow.Ellipsis
            }
        )
        Text(
            text = if (expanded) "Leer menos ‹" else "Leer más ›",
            style = BrandTypography.ZoneDescriptionReadMore,
            modifier = Modifier.clickable { expanded = !expanded }
        )
    }
}

@Preview(name = "ZoneDescriptionSection", showBackground = true, widthDp = 360)
@Composable
private fun ZoneDescriptionSectionPreview() {
    CafeterosTheme {
        ZoneDescriptionSection(
            title = "Sobre Santander",
            body = "En el corazón de la Cordillera Oriental, el departamento de Santander " +
                    "ha forjado un carácter único en sus granos. Aquí el café crece bajo " +
                    "la sombra de imponentes árboles de guamo y cítricos, protegiéndose " +
                    "del sol inclemente del cañón del Chicamocha.\n\nMunicipios como San " +
                    "Gil y El Socorro son cunas de una tradición que privilegia la intensidad."
        )
    }
}

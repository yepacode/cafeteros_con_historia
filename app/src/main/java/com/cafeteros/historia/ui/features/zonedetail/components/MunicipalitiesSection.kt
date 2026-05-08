package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.zonedetail.model.Municipality
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Municipios": título serif + fila horizontal scrollable de
 * [MunicipalityChip].
 *
 * El padre maneja qué municipio está seleccionado vía [selectedName] y
 * recibe el cambio por [onSelect]. Cuando [selectedName] es null no hay
 * ningún chip resaltado.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param selectedName nombre del municipio actualmente seleccionado.
 * @param municipalities lista a renderizar.
 * @param onSelect callback al pulsar un chip.
 */
@Composable
fun MunicipalitiesSection(
    modifier: Modifier = Modifier,
    selectedName: String?,
    municipalities: List<Municipality>,
    onSelect: (Municipality) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(text = "Municipios", style = BrandTypography.ZoneSectionTitle)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
            contentPadding = PaddingValues(end = BrandSpacing.lg)
        ) {
            items(items = municipalities, key = { it.name }) { municipality ->
                MunicipalityChip(
                    municipality = municipality,
                    isSelected = municipality.name == selectedName,
                    onClick = { onSelect(municipality) }
                )
            }
        }
    }
}

@Preview(name = "MunicipalitiesSection", showBackground = true, widthDp = 360)
@Composable
private fun MunicipalitiesSectionPreview() {
    CafeterosTheme {
        MunicipalitiesSection(
            selectedName = "San Gil",
            municipalities = listOf(
                Municipality("San Gil", 12),
                Municipality("Socorro", 8),
                Municipality("Pinchote", 5),
                Municipality("Curití", 7)
            ),
            onSelect = {}
        )
    }
}

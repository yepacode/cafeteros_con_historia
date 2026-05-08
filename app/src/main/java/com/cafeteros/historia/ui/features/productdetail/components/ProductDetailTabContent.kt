package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.productdetail.model.ProductDetailTab
import com.cafeteros.historia.ui.features.productdetail.model.ProductSpec
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Contenido que se muestra debajo de [ProductDetailTabs] según la tab
 * seleccionada.
 *
 *  - DESCRIPCION → cita italic + grilla de specs (2 columnas).
 *  - NOTAS_CATA → lista de notas en pills cremosas.
 *  - PROCESO → lista numerada vertical con cada paso del proceso.
 *
 * @param modifier modifier opcional.
 * @param selectedTab tab actualmente activa.
 * @param descriptionQuote cita italic para la tab Descripción.
 * @param specs specs para la grilla de la tab Descripción.
 * @param tastingNotes pills para la tab Notas de cata.
 * @param processSteps lista para la tab Proceso.
 */
@Composable
fun ProductDetailTabContent(
    modifier: Modifier = Modifier,
    selectedTab: ProductDetailTab,
    descriptionQuote: String,
    specs: List<ProductSpec>,
    tastingNotes: List<String>,
    processSteps: List<String>
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        when (selectedTab) {
            ProductDetailTab.DESCRIPCION -> DescriptionContent(
                quote = descriptionQuote,
                specs = specs
            )
            ProductDetailTab.NOTAS_CATA -> TastingNotesContent(notes = tastingNotes)
            ProductDetailTab.PROCESO -> ProcessContent(steps = processSteps)
        }
    }
}

@Composable
private fun DescriptionContent(
    quote: String,
    specs: List<ProductSpec>
) {
    Text(text = quote, style = BrandTypography.ProductDescriptionQuote)
    SpecsGrid(specs = specs)
}

@Composable
private fun SpecsGrid(specs: List<ProductSpec>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        specs.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                row.forEach { spec ->
                    SpecItem(spec = spec, modifier = Modifier.weight(1f))
                }
                if (row.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SpecItem(modifier: Modifier = Modifier, spec: ProductSpec) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = spec.label, style = BrandTypography.ProductSpecLabel)
        Text(text = spec.value, style = BrandTypography.ProductSpecValue)
    }
}

@Composable
private fun TastingNotesContent(notes: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        notes.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                row.forEach { note ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BrandColors.FlavorTagBackground)
                            .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = note, style = BrandTypography.FlavorTagText)
                    }
                }
                if (row.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ProcessContent(steps: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(BrandColors.CoffeeBrown),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        style = BrandTypography.ProductSpecValue.copy(color = BrandColors.PrimaryButtonText)
                    )
                }
                Text(
                    text = step,
                    style = BrandTypography.ProductDescriptionQuote.copy(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Normal
                    )
                )
            }
        }
    }
}

@Preview(name = "ProductDetailTabContent", showBackground = true, widthDp = 360)
@Composable
private fun ProductDetailTabContentPreview() {
    CafeterosTheme {
        ProductDetailTabContent(
            modifier = Modifier.padding(BrandSpacing.lg),
            selectedTab = ProductDetailTab.DESCRIPCION,
            descriptionQuote = "\"Un lote excepcional de Pitalito.\"",
            specs = listOf(
                ProductSpec(label = "VARIEDAD", value = "Caturra & Castillo"),
                ProductSpec(label = "ALTITUD", value = "1,850 msnm"),
                ProductSpec(label = "PROCESO", value = "Lavado extendido"),
                ProductSpec(label = "CERTIFICACIÓN", value = "Rainforest Alliance")
            ),
            tastingNotes = listOf("Cítricos", "Caramelo"),
            processSteps = listOf("Cosecha manual.")
        )
    }
}

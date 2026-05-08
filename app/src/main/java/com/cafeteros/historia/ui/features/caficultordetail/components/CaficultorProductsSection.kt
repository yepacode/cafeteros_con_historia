package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.caficultordetail.model.CaficultorProduct
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Productos (n)": header con el total + acción "ORDENAR ⌄" + grilla
 * de cards en 2 columnas.
 *
 * El número grande del título se renderiza con dos [Text] consecutivos —
 * "Productos" en serif bold y "(n)" en serif normal en gris — para imitar
 * el ritmo tipográfico del diseño sin tener que componer un único string
 * con `AnnotatedString`.
 *
 * @param modifier modifier opcional.
 * @param productCount total a mostrar entre paréntesis.
 * @param products productos visibles (puede ser una página del total).
 * @param onSortClick callback del link "ORDENAR".
 * @param onProductClick callback al pulsar una card.
 * @param onAddToCart callback al pulsar el botón "+" de una card.
 */
@Composable
fun CaficultorProductsSection(
    modifier: Modifier = Modifier,
    productCount: Int,
    products: List<CaficultorProduct>,
    onSortClick: () -> Unit = {},
    onProductClick: (CaficultorProduct) -> Unit = {},
    onAddToCart: (CaficultorProduct) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Productos ", style = BrandTypography.ZoneSectionTitle)
                Text(text = "($productCount)", style = BrandTypography.ProductsCountInline)
            }
            Text(
                text = "ORDENAR ⌄",
                style = BrandTypography.SortLink,
                modifier = Modifier.clickable(onClick = onSortClick)
            )
        }

        // Grilla 2-columnas con chunked(2) para evitar usar LazyVerticalGrid
        // (entraría en conflicto con el verticalScroll del padre).
        Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
            products.chunked(2).forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
                ) {
                    rowProducts.forEach { product ->
                        CaficultorProductCard(
                            product = product,
                            modifier = Modifier.weight(1f),
                            onClick = { onProductClick(product) },
                            onAddToCart = { onAddToCart(product) }
                        )
                    }
                    if (rowProducts.size == 1) {
                        // Espaciador para mantener la columna izquierda con
                        // su ancho cuando el producto queda solo en su fila.
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(name = "CaficultorProductsSection", showBackground = true, widthDp = 360)
@Composable
private fun CaficultorProductsSectionPreview() {
    CafeterosTheme {
        CaficultorProductsSection(
            productCount = 8,
            products = listOf(
                CaficultorProduct(
                    name = "Café Huila Pitalito",
                    weight = "250g",
                    formattedPrice = "$48.000",
                    placeholderColor = androidx.compose.ui.graphics.Color(0xFFE8DCC4)
                ),
                CaficultorProduct(
                    name = "Reserva Especial",
                    weight = "250g",
                    formattedPrice = "$55.000",
                    placeholderColor = androidx.compose.ui.graphics.Color(0xFF8B5A2B)
                )
            )
        )
    }
}

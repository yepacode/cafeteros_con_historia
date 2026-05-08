package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import java.util.Locale

/**
 * Bloque vertical superior con todo lo "head" del producto:
 *  1. Título serif grande.
 *  2. Subtítulo "Tueste medio · Molido o en grano".
 *  3. Rating (5 estrellas, valor numérico, conteo de reseñas) + "284 vendidos".
 *  4. Precio actual + precio anterior tachado + badge -15%.
 *
 * @param modifier modifier opcional.
 * @param name título del producto.
 * @param tagline línea bajo el título.
 * @param rating valor numérico del rating.
 * @param reviewCount total de reseñas.
 * @param salesCount unidades vendidas (texto "284 vendidos").
 * @param formattedPrice precio actual.
 * @param formattedOriginalPrice precio anterior, opcional.
 * @param discountLabel etiqueta del badge de descuento, opcional.
 */
@Composable
fun ProductTitleSection(
    modifier: Modifier = Modifier,
    name: String,
    tagline: String,
    rating: Double,
    reviewCount: Int,
    salesCount: Int,
    formattedPrice: String,
    formattedOriginalPrice: String? = null,
    discountLabel: String? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(text = name, style = BrandTypography.ProductDetailTitle)
        Text(text = tagline, style = BrandTypography.ProductDetailSubtitle)

        ProductRatingRow(
            rating = rating,
            reviewCount = reviewCount,
            salesCount = salesCount,
            modifier = Modifier.padding(top = BrandSpacing.xs)
        )

        ProductPriceRow(
            formattedPrice = formattedPrice,
            formattedOriginalPrice = formattedOriginalPrice,
            discountLabel = discountLabel,
            modifier = Modifier.padding(top = BrandSpacing.xs)
        )
    }
}

@Composable
private fun ProductRatingRow(
    modifier: Modifier = Modifier,
    rating: Double,
    reviewCount: Int,
    salesCount: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = BrandColors.RatingStar,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Text(
            text = String.format(Locale.US, "%.1f", rating),
            style = BrandTypography.RatingValue
        )
        Text(
            text = "($reviewCount reseñas)",
            style = BrandTypography.RatingCount
        )
        Text(text = "•", style = BrandTypography.ProductSalesCount)
        Text(text = "$salesCount vendidos", style = BrandTypography.ProductSalesCount)
    }
}

@Composable
private fun ProductPriceRow(
    modifier: Modifier = Modifier,
    formattedPrice: String,
    formattedOriginalPrice: String? = null,
    discountLabel: String? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(text = formattedPrice, style = BrandTypography.ProductDetailPrice)
        if (!formattedOriginalPrice.isNullOrBlank()) {
            Text(
                text = formattedOriginalPrice,
                style = BrandTypography.ProductOriginalPrice,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        if (!discountLabel.isNullOrBlank()) {
            Text(
                text = discountLabel,
                style = BrandTypography.ProductDiscountBadge,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(BrandColors.DiscountBadgeBackground)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Preview(name = "ProductTitleSection", showBackground = true, widthDp = 360)
@Composable
private fun ProductTitleSectionPreview() {
    CafeterosTheme {
        ProductTitleSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            name = "Café Huila Pitalito 250g",
            tagline = "Tueste medio · Molido o en grano",
            rating = 4.9,
            reviewCount = 127,
            salesCount = 284,
            formattedPrice = "$48.000",
            formattedOriginalPrice = "$56.000",
            discountLabel = "-15%"
        )
    }
}

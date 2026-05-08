package com.cafeteros.historia.ui.features.searchresults.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.features.explore.components.FavoriteToggleButton
import com.cafeteros.historia.ui.features.searchresults.model.SearchResultProduct
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme
import java.util.Locale

/**
 * Card individual del grid de resultados.
 *
 * Visual:
 *  - Imagen con esquinas redondeadas; arriba-izquierda un badge naranja
 *    con la zona y arriba-derecha el corazón flotante (reusa
 *    [FavoriteToggleButton] del feature de exploración).
 *  - Bloque de texto bajo la imagen: finca uppercase, nombre serif del
 *    producto, fila ★ + rating + (count), y precio dorado.
 *
 * @param modifier modifier opcional.
 * @param product producto a renderizar.
 * @param onClick callback al pulsar la card.
 */
@Composable
fun SearchResultProductCard(
    modifier: Modifier = Modifier,
    product: SearchResultProduct,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(BrandColors.InputBackground)
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(BrandSpacing.sm)
                    .clip(RoundedCornerShape(6.dp))
                    .background(BrandColors.SearchResultZoneBadgeBackground)
                    .padding(horizontal = BrandSpacing.sm, vertical = 4.dp)
            ) {
                Text(text = product.zoneLabel, style = BrandTypography.SearchResultZoneBadge)
            }

            FavoriteToggleButton(
                isFavorite = false,
                onToggle = {},
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(BrandSpacing.sm)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = product.farmLabel, style = BrandTypography.SearchResultFarmLabel)
            Text(
                text = product.name,
                style = BrandTypography.SearchResultProductName,
                maxLines = 2
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = BrandColors.RatingStar,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = String.format(Locale.US, "%.1f", product.rating),
                    style = BrandTypography.SearchResultRatingValue
                )
                Text(
                    text = "(${product.reviewCount})",
                    style = BrandTypography.SearchResultRatingCount
                )
            }
            Text(
                text = product.formattedPrice,
                style = BrandTypography.SearchResultPriceLabel,
                modifier = Modifier.padding(top = BrandSpacing.xs)
            )
        }
    }
}

@Preview(name = "SearchResultProductCard", showBackground = true, widthDp = 200)
@Composable
private fun SearchResultProductCardPreview() {
    CafeterosTheme {
        SearchResultProductCard(
            modifier = Modifier.padding(BrandSpacing.md),
            product = SearchResultProduct(
                id = "p1",
                name = "Café Bourbon Rosado",
                farmLabel = "FINCA LA ESPERANZA",
                zoneLabel = "HUILA",
                rating = 4.9,
                reviewCount = 24,
                formattedPrice = "$54,000",
                imageRes = R.drawable.cafe_origen_narino
            )
        )
    }
}

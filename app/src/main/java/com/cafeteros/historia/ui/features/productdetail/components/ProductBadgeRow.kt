package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.CheckCircle
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

/**
 * Fila de pills justo arriba del título: badge naranja con la zona y, si
 * [inStock] es true, el pill verde "En stock" con check.
 *
 * @param modifier modifier opcional.
 * @param regionLabel texto del badge de zona ("HUILA").
 * @param inStock si false, el pill verde se omite.
 */
@Composable
fun ProductBadgeRow(
    modifier: Modifier = Modifier,
    regionLabel: String,
    inStock: Boolean
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(BrandColors.RegionBadgeBackground)
                .padding(horizontal = BrandSpacing.sm, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Place,
                contentDescription = null,
                tint = BrandColors.RegionBadgeText,
                modifier = Modifier.size(12.dp)
            )
            Text(text = regionLabel, style = BrandTypography.RegionBadgeLabel)
        }

        if (inStock) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = BrandColors.StockBadgeCheck,
                    modifier = Modifier.size(14.dp)
                )
                Text(text = "En stock", style = BrandTypography.StockBadgeLabel)
            }
        }
    }
}

@Preview(name = "ProductBadgeRow", showBackground = true)
@Composable
private fun ProductBadgeRowPreview() {
    CafeterosTheme {
        ProductBadgeRow(
            modifier = Modifier.padding(BrandSpacing.md),
            regionLabel = "HUILA",
            inStock = true
        )
    }
}

package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Header de un grupo de items en el carrito: avatar + nombre del caficultor
 * a la izquierda y badge naranja de su zona a la derecha.
 *
 * @param modifier modifier opcional.
 * @param avatarRes drawable del avatar.
 * @param caficultorName nombre del caficultor / finca.
 * @param zoneLabel etiqueta de la zona ("HUILA").
 */
@Composable
fun CaficultorGroupHeader(
    modifier: Modifier = Modifier,
    avatarRes: Int,
    caficultorName: String,
    zoneLabel: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
            Text(text = caficultorName, style = BrandTypography.CartCaficultorName)
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(BrandColors.SearchResultZoneBadgeBackground)
                .padding(horizontal = BrandSpacing.sm, vertical = 4.dp)
        ) {
            Text(text = zoneLabel, style = BrandTypography.SearchResultZoneBadge)
        }
    }
}

@Preview(name = "CaficultorGroupHeader", showBackground = true, widthDp = 360)
@Composable
private fun CaficultorGroupHeaderPreview() {
    CafeterosTheme {
        CaficultorGroupHeader(
            modifier = Modifier.padding(BrandSpacing.md),
            avatarRes = R.drawable.ima_1,
            caficultorName = "Finca La Esperanza",
            zoneLabel = "HUILA"
        )
    }
}

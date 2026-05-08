package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Badge "ZONA CAFETERA" anclado al pie de la imagen hero.
 *
 * Visual: pill oscura con estrella dorada + texto en gold/amber. Es
 * meramente decorativo (no clickable) — comunica que la región tiene
 * denominación reconocida.
 */
@Composable
fun ZoneCafeteraBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(BrandColors.ZoneCafeteraBadgeBackground)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = BrandColors.ZoneCafeteraBadgeAccent,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "ZONA CAFETERA",
            style = BrandTypography.ZoneCafeteraBadge
        )
    }
}

@Preview(name = "ZoneCafeteraBadge", showBackground = true)
@Composable
private fun ZoneCafeteraBadgePreview() {
    CafeterosTheme {
        ZoneCafeteraBadge()
    }
}

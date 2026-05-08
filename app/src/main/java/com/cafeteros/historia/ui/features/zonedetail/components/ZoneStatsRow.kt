package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila de 3 stats con divisores verticales: caficultores · altitud · rating.
 *
 * Se usan tres [Box] con `weight(1f)` para repartir el ancho equitativamente
 * y dos divisores delgados verticales con `IntrinsicSize.Min` para que se
 * estiren al alto del contenido.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param caficultorCount valor del primer stat.
 * @param altitudeText valor del segundo stat ("1.400m").
 * @param rating valor del tercer stat (se muestra junto a una estrella).
 */
@Composable
fun ZoneStatsRow(
    modifier: Modifier = Modifier,
    caficultorCount: Int,
    altitudeText: String,
    rating: Double
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatColumn(
            modifier = Modifier.weight(1f),
            value = caficultorCount.toString(),
            label = "CAFICULTORES"
        )
        VerticalDivider()
        StatColumn(
            modifier = Modifier.weight(1f),
            value = altitudeText,
            label = "ALTITUD PROM."
        )
        VerticalDivider()
        StatColumn(
            modifier = Modifier.weight(1f),
            value = String.format(java.util.Locale.US, "%.1f", rating),
            label = "CALIFICACIÓN",
            valueLeadingIcon = true
        )
    }
}

@Composable
private fun StatColumn(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    valueLeadingIcon: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = value, style = BrandTypography.ZoneStatValue)
            if (valueLeadingIcon) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = BrandColors.RatingStar,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Text(text = label, style = BrandTypography.ZoneStatLabel)
    }
}

@Composable
private fun VerticalDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(1.dp)
            .background(BrandColors.StatsDivider)
    )
}

@Preview(name = "ZoneStatsRow", showBackground = true, widthDp = 360)
@Composable
private fun ZoneStatsRowPreview() {
    CafeterosTheme {
        ZoneStatsRow(
            caficultorCount = 38,
            altitudeText = "1.400m",
            rating = 4.8
        )
    }
}

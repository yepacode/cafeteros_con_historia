package com.cafeteros.historia.ui.features.coffeemap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.coffeemap.model.CoffeeZone
import com.cafeteros.historia.ui.features.coffeemap.model.ProductionIntensity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pill compacta del bottom sheet con el resumen de una zona.
 *
 * Visual: cápsula blanca redondeada con sombra suave, un pequeño círculo
 * relleno con el color identitario de la zona a la izquierda, el nombre
 * en sans-serif semibold al centro y el conteo a la derecha en gris.
 *
 * Es clickable para que el padre pueda navegar al detalle de la zona o
 * resaltar el marcador correspondiente en el mapa.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param zone datos a mostrar.
 * @param onClick callback al tocar la pill.
 */
@Composable
fun ZoneSummaryPill(
    modifier: Modifier = Modifier,
    zone: CoffeeZone,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(BrandColors.ZonePillBackground)
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(zone.color)
        )
        Text(
            text = zone.name.lowercase().replaceFirstChar { it.uppercase() }.let { capitalized ->
                // Las zonas multi-palabra ("EJE CAFETERO") deben quedar
                // capitalizadas palabra a palabra; las simples ("HUILA")
                // quedan con solo la primera letra mayúscula. Manejamos
                // ambos casos sin un import extra de WordUtils.
                capitalized.split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar { it.uppercase() }
                }
            },
            style = BrandTypography.ZonePillName
        )
        Text(
            text = zone.caficultorCount.toString(),
            style = BrandTypography.ZonePillCount
        )
    }
}

@Preview(name = "ZoneSummaryPill", showBackground = true, widthDp = 220)
@Composable
private fun ZoneSummaryPillPreview() {
    CafeterosTheme {
        ZoneSummaryPill(
            zone = CoffeeZone(
                name = "EJE CAFETERO",
                caficultorCount = 84,
                color = BrandColors.ZoneEjeCafetero,
                icon = Icons.Filled.Coffee,
                normalizedPosition = 0.5f to 0.5f,
                intensity = ProductionIntensity.ALTA,
                latitude = 5.0,
                longitude = -75.5
            )
        )
    }
}

package com.cafeteros.historia.ui.features.zonedetail.components

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.zonedetail.model.ZoneCaficultor
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card individual de la grilla "Caficultores de {zona}".
 *
 * Visual: foto rectangular arriba (200dp de alto), badge de rating dorado
 * sobrepuesto en la esquina superior derecha de la foto, y debajo un
 * bloque blanco con nombre serif, ubicación pequeña en mayúsculas y
 * "Desde $X" en café oscuro.
 *
 * Si el caficultor no tiene foto se renderiza un placeholder con un ícono
 * de persona sobre el color identitario para que la card siga viéndose
 * coherente sin retratos reales.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param caficultor datos a renderizar.
 * @param onClick callback al pulsar la card.
 */
@Composable
fun ZoneCaficultorCard(
    modifier: Modifier = Modifier,
    caficultor: ZoneCaficultor,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.CardBackground)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(caficultor.placeholderColor)
        ) {
            if (caficultor.portraitRes != null) {
                Image(
                    painter = painterResource(id = caficultor.portraitRes),
                    contentDescription = caficultor.displayName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = caficultor.displayName,
                    tint = Color.White.copy(alpha = 0.55f),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(56.dp)
                )
            }
            RatingBadge(
                rating = caficultor.rating,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(BrandSpacing.sm)
            )
        }

        Column(
            modifier = Modifier.padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = caficultor.displayName, style = BrandTypography.ZoneCaficultorName)
            Text(text = caficultor.farmAndLocation, style = BrandTypography.ZoneCaficultorLocation)
            Text(
                text = caficultor.formattedPriceFrom,
                style = BrandTypography.ZoneCaficultorPrice,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

/**
 * Badge dorado con la calificación que se sobrepone a la foto. Privado al
 * archivo porque solo esta card lo necesita con esta forma específica.
 */
@Composable
private fun RatingBadge(
    modifier: Modifier = Modifier,
    rating: Double
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BrandColors.HeroOverlayButtonBackground)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = BrandColors.RatingStar,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = String.format(java.util.Locale.US, "%.1f", rating),
            style = BrandTypography.ZoneCaficultorPrice.copy(
                fontSize = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp)
            )
        )
    }
}

@Preview(name = "ZoneCaficultorCard – con foto", showBackground = true, widthDp = 180)
@Composable
private fun ZoneCaficultorCardWithPhotoPreview() {
    CafeterosTheme {
        ZoneCaficultorCard(
            caficultor = ZoneCaficultor(
                displayName = "Don Ricardo",
                farmAndLocation = "FINCA LA ESPERANZA, SAN GIL",
                formattedPriceFrom = "Desde $45.000",
                rating = 4.9,
                portraitRes = com.cafeteros.historia.R.drawable.ima_1,
                placeholderColor = Color(0xFF8B5A2B)
            )
        )
    }
}

@Preview(name = "ZoneCaficultorCard – placeholder", showBackground = true, widthDp = 180)
@Composable
private fun ZoneCaficultorCardPlaceholderPreview() {
    CafeterosTheme {
        ZoneCaficultorCard(
            caficultor = ZoneCaficultor(
                displayName = "Doña Elena",
                farmAndLocation = "EL MIRADOR, SOCORRO",
                formattedPriceFrom = "Desde $42.000",
                rating = 4.7,
                placeholderColor = Color(0xFF7A4F2A)
            )
        )
    }
}

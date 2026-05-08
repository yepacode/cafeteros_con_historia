package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.explore.model.CaficultorBadge
import com.cafeteros.historia.ui.features.explore.model.FeaturedCaficultor
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card del caficultor destacado en el carrusel "Caficultores destacados".
 *
 * Composición vertical centrada:
 *  1. Avatar circular con borde dorado ([CaficultorAvatar]).
 *  2. Nombre de la finca en serif bold.
 *  3. Ubicación con ícono de pin.
 *  4. Rating con estrella ([CaficultorRatingRow]).
 *  5. Píldora del distintivo ([CaficultorBadgePill]).
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param caficultor datos a renderizar.
 * @param onClick callback al pulsar la card.
 */
@Composable
fun CaficultorCard(
    modifier: Modifier = Modifier,
    caficultor: FeaturedCaficultor,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(170.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.CardBackground)
            .clickable(onClick = onClick)
            .padding(vertical = BrandSpacing.md, horizontal = BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        CaficultorAvatar(caficultor = caficultor)

        Text(
            text = caficultor.farmName,
            style = BrandTypography.CaficultorName
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = caficultor.location,
                style = BrandTypography.CaficultorLocation
            )
        }

        CaficultorRatingRow(
            rating = caficultor.rating,
            reviewCount = caficultor.reviewCount
        )

        CaficultorBadgePill(
            badge = caficultor.badge,
            modifier = Modifier.padding(top = BrandSpacing.xs)
        )
    }
}

@Preview(name = "CaficultorCard – Orgánico", showBackground = true, widthDp = 200)
@Composable
private fun CaficultorCardOrganicoPreview() {
    CafeterosTheme {
        CaficultorCard(
            caficultor = FeaturedCaficultor(
                farmName = "Finca La Esperanza",
                location = "Huila • Pitalito",
                rating = 4.9,
                reviewCount = 127,
                badge = CaficultorBadge.ORGANICO,
                avatarPlaceholderColor = Color(0xFF8B5A2B)
            )
        )
    }
}

@Preview(name = "CaficultorCard – Sostenible", showBackground = true, widthDp = 200)
@Composable
private fun CaficultorCardSosteniblePreview() {
    CafeterosTheme {
        CaficultorCard(
            caficultor = FeaturedCaficultor(
                farmName = "El Mirador",
                location = "Cundinamarca",
                rating = 4.8,
                reviewCount = 94,
                badge = CaficultorBadge.SOSTENIBLE,
                avatarPlaceholderColor = Color(0xFF4F6E3F)
            )
        )
    }
}

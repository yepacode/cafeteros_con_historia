package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.features.caficultordetail.model.CertificationBadge
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card flotante con la identidad del caficultor: avatar con medalla,
 * rating, nombres, ubicación y certificaciones.
 *
 * Pensada para sobreponerse al hero — el padre la posiciona con un
 * `offset(y = -...)` para crear el efecto de superposición.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param avatarRes drawable del avatar.
 * @param farmName nombre de la finca grande ("Finca La Esperanza").
 * @param caficultorName nombre del caficultor secundario.
 * @param locationLine ubicación + altitud ("Pitalito, Huila · 1.650 msnm").
 * @param rating valor 0–5.
 * @param reviewCount total de reseñas.
 * @param certifications distintivos a renderizar como pills verdes.
 */
@Composable
fun CaficultorProfileCard(
    modifier: Modifier = Modifier,
    avatarRes: Int,
    farmName: String,
    caficultorName: String,
    locationLine: String,
    rating: Double,
    reviewCount: Int,
    certifications: List<CertificationBadge>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(BrandColors.CardBackground)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CaficultorAvatarWithMedal(avatarRes = avatarRes)
            ProfileRatingRow(rating = rating, reviewCount = reviewCount)
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = farmName, style = BrandTypography.ProfileFarmName)
            Text(text = caficultorName, style = BrandTypography.ProfileCaficultorName)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = locationLine, style = BrandTypography.ProfileLocationLine)
        }

        // Filas envueltas manualmente en grupos de 2 pills para que los
        // textos largos como "ORGÁNICO CERTIFICADO" + "COMERCIO JUSTO"
        // quepan sin ser aplastados.
        Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)) {
            certifications.chunked(2).forEach { rowBadges ->
                Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.xs)) {
                    rowBadges.forEach { badge ->
                        CertificationBadgePill(badge = badge)
                    }
                }
            }
        }
    }
}

@Preview(name = "CaficultorProfileCard", showBackground = true, widthDp = 360)
@Composable
private fun CaficultorProfileCardPreview() {
    CafeterosTheme {
        CaficultorProfileCard(
            avatarRes = R.drawable.ima_1,
            farmName = "Finca La Esperanza",
            caficultorName = "Don Alberto Ramírez",
            locationLine = "Pitalito, Huila · 1.650 msnm",
            rating = 4.9,
            reviewCount = 127,
            certifications = listOf(
                CertificationBadge.ORGANICO_CERTIFICADO,
                CertificationBadge.COMERCIO_JUSTO,
                CertificationBadge.DESDE_1982
            )
        )
    }
}

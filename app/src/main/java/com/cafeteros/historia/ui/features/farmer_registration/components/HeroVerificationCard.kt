package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Tarjeta hero del paso 5: avatar circular + chip "EN REVISIÓN" + título +
 * mensaje personalizado con tiempo estimado.
 *
 * El avatar usa por ahora un ícono de café en placeholder; cuando exista la
 * ilustración final del producto, se reemplaza por un `Image` aquí.
 *
 * @param modifier modifier opcional aplicado al contenedor.
 * @param userFirstName nombre del usuario para personalizar el saludo
 *  ("Don {nombre}, gracias…"). Si es null/blank, se usa "caficultor".
 * @param estimatedTimeText texto del tiempo estimado (ej. "Tiempo estimado: 48 horas").
 */
@Composable
fun HeroVerificationCard(
    modifier: Modifier = Modifier,
    userFirstName: String?,
    estimatedTimeText: String = "Tiempo estimado: 48 horas."
) {
    val displayName = userFirstName?.takeIf { it.isNotBlank() } ?: "caficultor"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.HeroCardBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(
                horizontal = BrandSpacing.lg,
                vertical = BrandSpacing.xl
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        AvatarPlaceholder()
        Spacer(modifier = Modifier.height(BrandSpacing.xs))
        ReviewBadge()
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Text(
            text = "Estamos revisando tu finca",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Don $displayName, gracias por venir, nuestro equipo está " +
                    "validando tu información. $estimatedTimeText",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = BrandColors.TextSecondary
            ),
            textAlign = TextAlign.Center
        )
    }
}

/** Avatar circular (placeholder hasta tener ilustración final). */
@Composable
private fun AvatarPlaceholder() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .background(color = BrandColors.CoffeeBrown, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Coffee,
            contentDescription = null,
            tint = BrandColors.CreamWhite,
            modifier = Modifier.size(48.dp)
        )
    }
}

/** Chip amarillo "EN REVISIÓN". */
@Composable
private fun ReviewBadge() {
    Box(
        modifier = Modifier
            .background(
                color = BrandColors.ReviewBadgeBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = "EN REVISIÓN",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = BrandColors.InfoBannerAction
            )
        )
    }
}

@Preview(name = "HeroVerificationCard", widthDp = 360)
@Composable
private fun HeroVerificationCardPreview() {
    HeroVerificationCard(userFirstName = "Alberto")
}

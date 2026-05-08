package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
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
 * Barra superior translúcida que se sobrepone a la imagen hero del detalle.
 *
 * Visual: flecha de regreso a la izquierda, nombre de la zona al lado, y un
 * ícono de compartir circular flotante a la derecha. Toda la barra es sin
 * fondo (transparente) para que se vea la imagen del hero detrás.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param zoneName nombre canónico de la zona ("Santander").
 * @param onBack callback de la flecha.
 * @param onShare callback del botón compartir del top bar.
 */
@Composable
fun ZoneDetailTopBar(
    modifier: Modifier = Modifier,
    zoneName: String,
    onBack: () -> Unit = {},
    onShare: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularOverlayButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = zoneName,
                style = BrandTypography.ZoneDetailTopBarTitle,
                modifier = Modifier.padding(start = BrandSpacing.sm)
            )
        }

        CircularOverlayButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "Compartir",
                tint = BrandColors.TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/**
 * Botón circular blanco translúcido reutilizado por el top bar y los
 * controles flotantes sobre la imagen hero. Se mantiene privado al feature
 * porque solo este top bar y [ZoneHeroSection] lo necesitan en este momento.
 */
@Composable
internal fun CircularOverlayButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(BrandColors.HeroOverlayButtonBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(name = "ZoneDetailTopBar", showBackground = true, widthDp = 360)
@Composable
private fun ZoneDetailTopBarPreview() {
    CafeterosTheme {
        ZoneDetailTopBar(zoneName = "Santander")
    }
}

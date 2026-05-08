package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Cabecera visual del detalle de zona: imagen hero a sangrado + controles
 * flotantes (favorito, compartir) arriba a la derecha + badge "ZONA
 * CAFETERA" abajo a la izquierda.
 *
 * Aplica un degradado oscuro suave en la parte inferior de la imagen para
 * que el badge contraste sin perder la fotografía. La altura por default
 * (240dp) está calibrada para que el título de la zona (que va debajo)
 * quede visible sin scroll en la mayoría de pantallas.
 *
 * @param modifier modifier opcional aplicado al [Box] contenedor.
 * @param heroImageRes drawable a renderizar como fondo del hero.
 * @param initiallyFavorite estado inicial del corazón.
 * @param onShare callback al pulsar el botón compartir.
 */
@Composable
fun ZoneHeroSection(
    modifier: Modifier = Modifier,
    heroImageRes: Int,
    initiallyFavorite: Boolean = true,
    onShare: () -> Unit = {}
) {
    var isFavorite by remember { mutableStateOf(initiallyFavorite) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = heroImageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Degradado inferior para que el badge sea legible.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.45f)
                        ),
                        startY = 200f
                    )
                )
        )

        // Botones flotantes superior-derecha (favorito + compartir).
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(BrandSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            CircularOverlayButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar favorito" else "Marcar favorito",
                    tint = if (isFavorite) BrandColors.FavoriteIconActive else BrandColors.TextPrimary,
                    modifier = Modifier.size(18.dp)
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

        // Badge "ZONA CAFETERA" abajo a la izquierda.
        ZoneCafeteraBadge(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(BrandSpacing.lg)
        )
    }
}

@Preview(name = "ZoneHeroSection", showBackground = true, widthDp = 360, heightDp = 280)
@Composable
private fun ZoneHeroSectionPreview() {
    CafeterosTheme {
        ZoneHeroSection(heroImageRes = R.drawable.imag_2)
    }
}

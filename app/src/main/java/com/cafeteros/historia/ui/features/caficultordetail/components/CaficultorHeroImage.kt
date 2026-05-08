package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
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
 * Imagen hero del detalle del caficultor con tres botones flotantes:
 *  - Flecha de regreso a la izquierda.
 *  - Botón de compartir a la derecha.
 *  - Corazón de favorito a la derecha (alterna estado al toque).
 *
 * Aplica un degradado oscuro suave en la parte superior para que los
 * íconos contrasten contra cualquier paisaje, y otro degradado en la
 * parte inferior para que la profile card que se sobrepone tenga buen
 * contraste donde apoya su sombra.
 *
 * @param modifier modifier opcional.
 * @param heroImageRes drawable a renderizar como fondo.
 * @param initiallyFavorite estado inicial del corazón.
 * @param onBack callback de la flecha.
 * @param onShare callback del botón compartir.
 */
@Composable
fun CaficultorHeroImage(
    modifier: Modifier = Modifier,
    heroImageRes: Int,
    initiallyFavorite: Boolean = false,
    onBack: () -> Unit = {},
    onShare: () -> Unit = {}
) {
    var isFavorite by remember { mutableStateOf(initiallyFavorite) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = heroImageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.18f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.10f)
                        )
                    )
                )
        )

        // Botón de regreso (izquierda).
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(BrandSpacing.md)
                .align(Alignment.TopStart)
        ) {
            CircularHeroButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Botones share + favorito (derecha).
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(BrandSpacing.md)
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            CircularHeroButton(onClick = onShare) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Compartir",
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            CircularHeroButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar favorito" else "Marcar favorito",
                    tint = if (isFavorite) BrandColors.FavoriteIconActive else BrandColors.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * Botón circular blanco translúcido reutilizado por los tres controles
 * del hero. Privado al feature porque solo este hero lo necesita.
 */
@Composable
private fun CircularHeroButton(
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

@Preview(name = "CaficultorHeroImage", showBackground = true, widthDp = 360, heightDp = 320)
@Composable
private fun CaficultorHeroImagePreview() {
    CafeterosTheme {
        CaficultorHeroImage(heroImageRes = R.drawable.imag_2)
    }
}

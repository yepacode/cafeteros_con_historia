package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
 * Hero del detalle de producto.
 *
 * Visual:
 *  - Fondo cálido con un degradado vertical cremoso sobre el bokeh para
 *    enmarcar el paquete de café.
 *  - Imagen grande del paquete centrada y recortada.
 *  - Indicador horizontal de carrusel con [galleryImages].size puntos
 *    (oscuro = activo) en la base.
 *  - Sobrepuesta arriba: una fila con back (izquierda) y share / favorito /
 *    carrito (derecha) usando el mismo botón circular translúcido del
 *    detalle de caficultor.
 *
 * @param modifier modifier opcional.
 * @param heroImageRes drawable principal cuando [galleryImages] está vacío.
 * @param galleryImages galería completa para los puntos del carrusel.
 * @param cartItemCount número que se renderiza como badge sobre el carrito.
 *   Si es 0, no se muestra el badge.
 * @param initiallyFavorite estado inicial del corazón.
 * @param onBack callback de la flecha.
 * @param onShare callback del botón compartir.
 * @param onCart callback del botón carrito.
 */
@Composable
fun ProductHeroSection(
    modifier: Modifier = Modifier,
    heroImageRes: Int,
    galleryImages: List<Int> = emptyList(),
    cartItemCount: Int = 0,
    initiallyFavorite: Boolean = false,
    onBack: () -> Unit = {},
    onShare: () -> Unit = {},
    onCart: () -> Unit = {}
) {
    val effectiveGallery = if (galleryImages.isEmpty()) listOf(heroImageRes) else galleryImages
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFavorite by remember { mutableStateOf(initiallyFavorite) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(360.dp)
            .background(BrandColors.ProductHeroBackground)
    ) {
        // Velo cremoso vertical que difumina el bokeh del fondo y deja el
        // paquete del café como foco visual.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            BrandColors.ProductHeroBackground.copy(alpha = 0.05f),
                            BrandColors.ProductHeroBackground.copy(alpha = 0.45f),
                            BrandColors.ProductHeroBackground.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Image(
            painter = painterResource(id = effectiveGallery[currentIndex]),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp, bottom = 36.dp)
        )

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(BrandSpacing.md)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductHeroCircularButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProductHeroCircularButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Compartir",
                        tint = BrandColors.TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                ProductHeroCircularButton(onClick = { isFavorite = !isFavorite }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Quitar favorito" else "Marcar favorito",
                        tint = if (isFavorite) BrandColors.FavoriteIconActive else BrandColors.TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                ProductHeroCircularButton(onClick = onCart) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = BrandColors.TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        if (cartItemCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(BrandColors.NotificationDot)
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = BrandSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            effectiveGallery.forEachIndexed { index, _ ->
                val isActive = index == currentIndex
                Box(
                    modifier = Modifier
                        .size(if (isActive) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActive) BrandColors.CarouselDotActive
                            else BrandColors.CarouselDotInactive
                        )
                        .clickable { currentIndex = index }
                )
                if (index < effectiveGallery.lastIndex) {
                    Spacer(modifier = Modifier.size(0.dp))
                }
            }
        }
    }
}

@Composable
private fun ProductHeroCircularButton(
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

@Preview(name = "ProductHeroSection", widthDp = 360, heightDp = 380)
@Composable
private fun ProductHeroSectionPreview() {
    CafeterosTheme {
        Box(modifier = Modifier.background(Color.White)) {
            ProductHeroSection(
                heroImageRes = R.drawable.cafe_huila_pitalito,
                galleryImages = listOf(
                    R.drawable.cafe_huila_pitalito,
                    R.drawable.cafe_huila_pitalito,
                    R.drawable.cafe_huila_pitalito
                ),
                cartItemCount = 3
            )
        }
    }
}

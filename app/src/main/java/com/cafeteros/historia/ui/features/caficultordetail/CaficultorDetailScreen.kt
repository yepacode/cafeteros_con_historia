package com.cafeteros.historia.ui.features.caficultordetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.caficultordetail.components.CaficultorHeroImage
import com.cafeteros.historia.ui.features.caficultordetail.components.CaficultorHistorySection
import com.cafeteros.historia.ui.features.caficultordetail.components.CaficultorProductsSection
import com.cafeteros.historia.ui.features.caficultordetail.components.CaficultorProfileCard
import com.cafeteros.historia.ui.features.caficultordetail.components.CaficultorVideoCard
import com.cafeteros.historia.ui.features.caficultordetail.components.ContactCaficultorCard
import com.cafeteros.historia.ui.features.caficultordetail.components.ProcessStepsRow
import com.cafeteros.historia.ui.features.caficultordetail.components.ReviewsSection
import com.cafeteros.historia.ui.features.caficultordetail.components.ViewAllProductsButton
import com.cafeteros.historia.ui.features.caficultordetail.model.CaficultorDetailSampleData
import com.cafeteros.historia.ui.features.caficultordetail.model.CaficultorProduct
import com.cafeteros.historia.ui.features.caficultordetail.model.CaficultorProfile
import com.cafeteros.historia.ui.features.explore.components.ExploreBottomBar
import com.cafeteros.historia.ui.features.explore.components.ExploreTab
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla de detalle del caficultor.
 *
 * Layout vertical scrollable:
 *  1. Hero con imagen y botones flotantes (back, share, favorito).
 *  2. Profile card sobrepuesta al hero (offset negativo) con avatar +
 *     medalla, rating, finca, nombre, ubicación y certificaciones.
 *  3. Historia editorial con quote, párrafos, foto.
 *  4. Video de la finca.
 *  5. Cómo procesan su café (4 pasos).
 *  6. Productos (2 columnas).
 *  7. Reseñas.
 *  8. Card "¿Tienes preguntas para…?".
 *  9. Botón "Ver todos los productos".
 *  10. Bottom bar unificada de la app (5 tabs).
 *
 * @param modifier modifier opcional.
 * @param profile datos del caficultor a renderizar.
 * @param onBack callback de la flecha del hero.
 * @param onShare callback del botón compartir.
 * @param onProductClick callback al tocar una card de producto.
 * @param onAddToCart callback al pulsar el "+" de un producto.
 * @param onSeeAllProducts callback del botón ancho final.
 * @param onSeeAllReviews callback del enlace "VER TODAS" de reseñas.
 * @param onSendMessage callback del botón "Enviar mensaje".
 * @param onPlayVideo callback al tocar el thumbnail de video.
 * @param onReadFullStory callback de "Leer toda la historia".
 */
@Composable
fun CaficultorDetailScreen(
    modifier: Modifier = Modifier,
    profile: CaficultorProfile = CaficultorDetailSampleData.forName("Don Alberto"),
    onBack: () -> Unit = {},
    onShare: () -> Unit = {},
    onProductClick: (CaficultorProduct) -> Unit = {},
    onAddToCart: (CaficultorProduct) -> Unit = {},
    onSeeAllProducts: () -> Unit = {},
    onSeeAllReviews: () -> Unit = {},
    onSendMessage: () -> Unit = {},
    onPlayVideo: () -> Unit = {},
    onReadFullStory: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(ExploreTab.EXPLORAR) }
    val cartItemCount = 3

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.CaficultorDetailBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            CaficultorHeroImage(
                heroImageRes = profile.heroImageRes,
                onBack = onBack,
                onShare = onShare
            )

            // Profile card subiendo sobre el hero con offset negativo.
            CaficultorProfileCard(
                avatarRes = profile.avatarRes,
                farmName = profile.farmName,
                caficultorName = profile.caficultorName,
                locationLine = "${profile.location} · ${profile.altitudeText}",
                rating = profile.rating,
                reviewCount = profile.reviewCount,
                certifications = profile.certifications,
                modifier = Modifier
                    .offset(y = (-40).dp)
                    .padding(horizontal = BrandSpacing.lg)
            )

            // Compensa el offset para no romper el flujo del Column.
            Spacer(modifier = Modifier.height((-24).dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BrandSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
            ) {
                CaficultorHistorySection(
                    shortName = profile.displayShortName,
                    quote = profile.historyQuote,
                    part1 = profile.historyPart1,
                    imageRes = profile.historyImageRes,
                    part2 = profile.historyPart2,
                    onReadFullStoryClick = onReadFullStory
                )

                CaficultorVideoCard(
                    thumbnailRes = profile.videoThumbnailRes,
                    caption = profile.videoCaption,
                    onPlayClick = onPlayVideo
                )

                ProcessStepsRow(steps = profile.processSteps)

                CaficultorProductsSection(
                    productCount = profile.productCount,
                    products = profile.products,
                    onProductClick = onProductClick,
                    onAddToCart = onAddToCart
                )

                ReviewsSection(
                    reviews = profile.reviews,
                    onSeeAllReviews = onSeeAllReviews
                )

                ContactCaficultorCard(
                    caficultorShortName = profile.displayShortName,
                    onSendMessage = onSendMessage
                )

                ViewAllProductsButton(onClick = onSeeAllProducts)

                Spacer(modifier = Modifier.height(BrandSpacing.md))
            }
        }

        ExploreBottomBar(
            selected = selectedTab,
            onTabSelected = { selectedTab = it },
            cartItemCount = cartItemCount
        )
    }
}

@Preview(name = "CaficultorDetailScreen", widthDp = 360, heightDp = 2400)
@Composable
private fun CaficultorDetailScreenPreview() {
    CafeterosTheme {
        CaficultorDetailScreen()
    }
}

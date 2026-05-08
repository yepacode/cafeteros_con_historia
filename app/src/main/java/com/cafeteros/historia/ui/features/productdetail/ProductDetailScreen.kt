package com.cafeteros.historia.ui.features.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.productdetail.components.ProductBadgeRow
import com.cafeteros.historia.ui.features.productdetail.components.ProductCheckoutBar
import com.cafeteros.historia.ui.features.productdetail.components.ProductDetailTabContent
import com.cafeteros.historia.ui.features.productdetail.components.ProductDetailTabs
import com.cafeteros.historia.ui.features.productdetail.components.ProductHeroSection
import com.cafeteros.historia.ui.features.productdetail.components.ProductOptionDropdown
import com.cafeteros.historia.ui.features.productdetail.components.ProductPresentationSelector
import com.cafeteros.historia.ui.features.productdetail.components.ProductProducerPill
import com.cafeteros.historia.ui.features.productdetail.components.ProductQuantityStepper
import com.cafeteros.historia.ui.features.productdetail.components.ProductShippingCard
import com.cafeteros.historia.ui.features.productdetail.components.ProductTitleSection
import com.cafeteros.historia.ui.features.productdetail.model.ProductDetail
import com.cafeteros.historia.ui.features.productdetail.model.ProductDetailSampleData
import com.cafeteros.historia.ui.features.productdetail.model.ProductDetailTab
import com.cafeteros.historia.ui.features.productdetail.model.ProductGrindType
import com.cafeteros.historia.ui.features.productdetail.model.ProductPresentation
import com.cafeteros.historia.ui.features.productdetail.model.ProductRoast
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla del detalle de producto.
 *
 * Layout vertical scrollable:
 *  1. Hero con imagen del paquete y botones flotantes (back, share,
 *     favorito, carrito) + indicador de carrusel.
 *  2. Pills "HUILA" + "En stock".
 *  3. Título, subtítulo, rating, ventas, precio.
 *  4. Pill del productor con avatar y "Ver perfil →".
 *  5. Selector de presentación (250g / 500g / 1kg).
 *  6. Dropdowns "TUESTE" y "TIPO" en una fila.
 *  7. Stepper de cantidad.
 *  8. Tabs (Descripción / Notas de cata / Proceso) y su contenido.
 *  9. Card de envío.
 *  10. Bottom bar fijo con TOTAL + "Agregar al carrito".
 *
 * Toda la selección (presentación, tueste, tipo, cantidad, tab) vive como
 * estado local: la pantalla es la única "fuente de verdad" mientras no
 * exista un ViewModel.
 *
 * @param modifier modifier opcional.
 * @param product datos a renderizar.
 * @param cartItemCount número visible en el badge del carrito del hero.
 * @param onBack callback de la flecha del hero.
 * @param onShare callback del botón compartir.
 * @param onCart callback del botón carrito del hero.
 * @param onViewProducerProfile callback de "Ver perfil →".
 * @param onAddToCart callback del botón ancho del bottom bar; recibe los
 *   parámetros seleccionados para cuando exista lógica real de carrito.
 */
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    product: ProductDetail = ProductDetailSampleData.forName("Café Huila Pitalito"),
    cartItemCount: Int = 0,
    onBack: () -> Unit = {},
    onShare: () -> Unit = {},
    onCart: () -> Unit = {},
    onViewProducerProfile: () -> Unit = {},
    onAddToCart: (
        presentation: ProductPresentation,
        roast: ProductRoast,
        grind: ProductGrindType,
        quantity: Int
    ) -> Unit = { _, _, _, _ -> }
) {
    var selectedPresentation by remember { mutableStateOf(product.defaultPresentation) }
    var selectedRoast by remember { mutableStateOf(product.defaultRoast) }
    var selectedGrind by remember { mutableStateOf(product.defaultGrindType) }
    var quantity by remember { mutableIntStateOf(1) }
    var selectedTab by remember { mutableStateOf(ProductDetailTab.DESCRIPCION) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.ProductDetailBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            ProductHeroSection(
                heroImageRes = product.heroImageRes,
                galleryImages = product.heroGalleryImages,
                cartItemCount = cartItemCount,
                onBack = onBack,
                onShare = onShare,
                onCart = onCart
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BrandSpacing.lg)
                    .padding(top = BrandSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
            ) {
                ProductBadgeRow(
                    regionLabel = product.regionLabel,
                    inStock = product.inStock
                )

                ProductTitleSection(
                    name = product.name,
                    tagline = product.tagline,
                    rating = product.rating,
                    reviewCount = product.reviewCount,
                    salesCount = product.salesCount,
                    formattedPrice = product.formattedPrice,
                    formattedOriginalPrice = product.formattedOriginalPrice,
                    discountLabel = product.discountPercentLabel
                )

                ProductProducerPill(
                    avatarRes = product.producerAvatarRes,
                    producerName = product.producerName,
                    producerFarm = product.producerFarm,
                    onViewProfile = onViewProducerProfile
                )

                ProductPresentationSelector(
                    options = product.availablePresentations,
                    selected = selectedPresentation,
                    onSelect = { selectedPresentation = it }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
                ) {
                    ProductOptionDropdown(
                        modifier = Modifier.weight(1f),
                        label = "TUESTE",
                        options = product.availableRoasts,
                        selected = selectedRoast,
                        labelOf = { it.label },
                        onSelect = { selectedRoast = it }
                    )
                    ProductOptionDropdown(
                        modifier = Modifier.weight(1f),
                        label = "TIPO",
                        options = product.availableGrindTypes,
                        selected = selectedGrind,
                        labelOf = { it.label },
                        onSelect = { selectedGrind = it }
                    )
                }

                ProductQuantityStepper(
                    quantity = quantity,
                    onQuantityChange = { quantity = it }
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
                ) {
                    ProductDetailTabs(
                        selected = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                    ProductDetailTabContent(
                        selectedTab = selectedTab,
                        descriptionQuote = product.descriptionQuote,
                        specs = product.specs,
                        tastingNotes = product.tastingNotes,
                        processSteps = product.processSteps
                    )
                }

                ProductShippingCard(
                    city = product.shippingCity,
                    eta = product.shippingEta
                )

                Spacer(modifier = Modifier.height(BrandSpacing.md))
            }
        }

        ProductCheckoutBar(
            formattedTotal = product.formattedPrice,
            enabled = product.inStock,
            onAddToCart = {
                onAddToCart(selectedPresentation, selectedRoast, selectedGrind, quantity)
            }
        )
    }
}

@Preview(name = "ProductDetailScreen", widthDp = 360, heightDp = 2400)
@Composable
private fun ProductDetailScreenPreview() {
    CafeterosTheme {
        ProductDetailScreen()
    }
}

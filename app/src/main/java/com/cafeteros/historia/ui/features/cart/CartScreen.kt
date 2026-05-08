package com.cafeteros.historia.ui.features.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.cart.components.CaficultorGroupHeader
import com.cafeteros.historia.ui.features.cart.components.CartActionsBlock
import com.cafeteros.historia.ui.features.cart.components.CartBottomBar
import com.cafeteros.historia.ui.features.cart.components.CartItemCard
import com.cafeteros.historia.ui.features.cart.components.CartRecommendationsSection
import com.cafeteros.historia.ui.features.cart.components.CartShippingCard
import com.cafeteros.historia.ui.features.cart.components.CartSummaryCard
import com.cafeteros.historia.ui.features.cart.components.CartTopBar
import com.cafeteros.historia.ui.features.cart.components.DiscountCodeRow
import com.cafeteros.historia.ui.features.cart.components.EmptyCartHero
import com.cafeteros.historia.ui.features.cart.components.FreeShippingProgress
import com.cafeteros.historia.ui.features.cart.model.CartBottomTab
import com.cafeteros.historia.ui.features.cart.model.CartCaficultorGroup
import com.cafeteros.historia.ui.features.cart.model.CartRecommendation
import com.cafeteros.historia.ui.features.cart.model.CartSampleData
import com.cafeteros.historia.ui.features.cart.model.CartShipping
import com.cafeteros.historia.ui.features.cart.model.CartSummary
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla "Tu Selección" — el carrito de compras.
 *
 * Tiene **dos modos** según el estado de [groups]:
 *
 *  - **Con items** — layout completo: barra de envío gratis, grupos por
 *    caficultor con sus items, card de envío, código de descuento, resumen
 *    y CTAs `Continuar al pago` / `Seguir comprando`.
 *  - **Vacío** — el empty state: ilustración line-art de una taza, título
 *    `Tu carrito está vacío`, CTA `Explorar café`, y la sección
 *    `Recomendaciones del Origen` con dos cards.
 *
 * El top bar y el bottom bar se mantienen en ambos modos. El usuario
 * puede pasar al estado vacío en runtime al borrar todos los items o al
 * pulsar `Limpiar`, y el switch ocurre sin recomponer el chrome.
 *
 * @param modifier modifier opcional.
 * @param initialGroups grupos iniciales del carrito (vacío para forzar
 *   el empty state).
 * @param shipping información de envío.
 * @param shippingCost costo de envío en pesos.
 * @param discount descuento aplicado en pesos.
 * @param freeShippingThreshold umbral para envío gratis.
 * @param recommendations recomendaciones del empty state.
 * @param onBack callback de la flecha del top bar.
 * @param onChangeShipping callback de "Cambiar" de la card de envío.
 * @param onCheckout callback de "Continuar al pago"; recibe el total
 *   formateado que el siguiente paso del checkout muestra como subtotal.
 * @param onContinueShopping callback de "Seguir comprando".
 * @param onExploreCoffee callback de "Explorar café" del empty state.
 * @param onRecommendationClick callback al pulsar una card de recomendación.
 * @param onTabSelected callback al cambiar tab del bottom bar.
 */
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    initialGroups: List<CartCaficultorGroup> = CartSampleData.groups,
    shipping: CartShipping = CartSampleData.shipping,
    shippingCost: Int = CartSampleData.SAMPLE_SHIPPING,
    discount: Int = CartSampleData.SAMPLE_DISCOUNT,
    freeShippingThreshold: Int = CartSampleData.FREE_SHIPPING_THRESHOLD,
    recommendations: List<CartRecommendation> = CartSampleData.recommendations,
    onBack: () -> Unit = {},
    onChangeShipping: () -> Unit = {},
    onCheckout: (formattedTotal: String) -> Unit = {},
    onContinueShopping: () -> Unit = {},
    onExploreCoffee: () -> Unit = {},
    onRecommendationClick: (CartRecommendation) -> Unit = {},
    onTabSelected: (CartBottomTab) -> Unit = {}
) {
    val groups = remember { initialGroups.toMutableStateList() }
    var discountCode by remember { mutableStateOf("") }
    var bottomTab by remember { mutableStateOf(CartBottomTab.MI_BOLSA) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.CartBackground)
            .statusBarsPadding()
    ) {
        CartTopBar(
            onBack = onBack,
            onClear = { groups.clear() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            if (groups.isEmpty()) {
                EmptyCartContent(
                    recommendations = recommendations,
                    onExploreCoffee = onExploreCoffee,
                    onRecommendationClick = onRecommendationClick
                )
            } else {
                CartContent(
                    groups = groups,
                    shipping = shipping,
                    shippingCost = shippingCost,
                    discount = discount,
                    freeShippingThreshold = freeShippingThreshold,
                    discountCode = discountCode,
                    onDiscountCodeChange = { discountCode = it },
                    onChangeShipping = onChangeShipping,
                    onCheckout = onCheckout,
                    onContinueShopping = onContinueShopping
                )
            }
        }

        CartBottomBar(
            selected = bottomTab,
            onTabSelected = {
                bottomTab = it
                onTabSelected(it)
            }
        )
    }
}

/**
 * Contenido del carrito cuando hay items: barra de envío gratis, grupos,
 * card de envío, código de descuento, resumen y CTAs.
 *
 * Vive como un `private` dentro del archivo de la pantalla porque solo
 * la pantalla lo usa — sacarlo a su propio archivo agregaría boilerplate
 * sin reuso real.
 */
@Composable
private fun ColumnScope.CartContent(
    groups: androidx.compose.runtime.snapshots.SnapshotStateList<CartCaficultorGroup>,
    shipping: CartShipping,
    shippingCost: Int,
    discount: Int,
    freeShippingThreshold: Int,
    discountCode: String,
    onDiscountCodeChange: (String) -> Unit,
    onChangeShipping: () -> Unit,
    onCheckout: (formattedTotal: String) -> Unit,
    onContinueShopping: () -> Unit
) {
    val subtotal = groups.sumOf { group -> group.items.sumOf { it.unitPrice * it.quantity } }
    val total = (subtotal + shippingCost - discount).coerceAtLeast(0)
    val amountToFreeShipping = (freeShippingThreshold - subtotal).coerceAtLeast(0)
    val summary = CartSummary(
        subtotal = subtotal,
        shipping = shippingCost,
        discount = discount,
        total = total,
        amountToFreeShipping = amountToFreeShipping,
        freeShippingThreshold = freeShippingThreshold
    )

    Spacer(modifier = Modifier.height(BrandSpacing.xs))

    FreeShippingProgress(
        amountToFreeShipping = summary.amountToFreeShipping,
        formattedAmount = formatPrice(summary.amountToFreeShipping),
        progress = summary.freeShippingProgress
    )

    groups.forEach { group ->
        Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
            CaficultorGroupHeader(
                avatarRes = group.avatarRes,
                caficultorName = group.caficultorName,
                zoneLabel = group.zoneLabel
            )
            group.items.forEach { item ->
                CartItemCard(
                    item = item,
                    formattedPrice = formatPrice(item.unitPrice),
                    formattedOriginalPrice = item.originalUnitPrice?.let { formatPrice(it) },
                    onQuantityChange = { newQty ->
                        groups[groups.indexOf(group)] = group.copy(
                            items = group.items.map {
                                if (it.id == item.id) it.copy(quantity = newQty) else it
                            }
                        )
                    },
                    onRemove = {
                        val newItems = group.items.filterNot { it.id == item.id }
                        if (newItems.isEmpty()) {
                            groups.remove(group)
                        } else {
                            groups[groups.indexOf(group)] = group.copy(items = newItems)
                        }
                    }
                )
            }
        }
    }

    CartShippingCard(shipping = shipping, onChange = onChangeShipping)

    DiscountCodeRow(
        code = discountCode,
        onCodeChange = onDiscountCodeChange,
        onApply = { /* aplicar cupón cuando exista BD */ }
    )

    CartSummaryCard(
        subtotalFormatted = formatPrice(summary.subtotal),
        shippingFormatted = formatPrice(summary.shipping),
        discountFormatted = if (summary.discount > 0) formatPrice(summary.discount) else null,
        totalFormatted = formatPrice(summary.total)
    )

    CartActionsBlock(
        onCheckout = { onCheckout(formatPrice(summary.total)) },
        onContinueShopping = onContinueShopping
    )

    Spacer(modifier = Modifier.height(BrandSpacing.md))
}

/**
 * Contenido del carrito cuando está vacío: hero con ilustración + CTA y
 * sección de recomendaciones.
 */
@Composable
private fun EmptyCartContent(
    recommendations: List<CartRecommendation>,
    onExploreCoffee: () -> Unit,
    onRecommendationClick: (CartRecommendation) -> Unit
) {
    Spacer(modifier = Modifier.height(BrandSpacing.xl))

    EmptyCartHero(onCtaClick = onExploreCoffee)

    Spacer(modifier = Modifier.height(BrandSpacing.md))

    CartRecommendationsSection(
        recommendations = recommendations,
        onRecommendationClick = onRecommendationClick
    )

    Spacer(modifier = Modifier.height(BrandSpacing.lg))
}

/** Formato consistente con el resto de pantallas del comprador (es-CO). */
private fun formatPrice(value: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "CO"))
    return "$" + formatter.format(value.toLong())
}

@Preview(name = "CartScreen — con items", widthDp = 360, heightDp = 1800)
@Composable
private fun CartScreenWithItemsPreview() {
    CafeterosTheme {
        CartScreen()
    }
}

@Preview(name = "CartScreen — vacío", widthDp = 360, heightDp = 1400)
@Composable
private fun CartScreenEmptyPreview() {
    CafeterosTheme {
        CartScreen(initialGroups = emptyList())
    }
}

private typealias ColumnScope = androidx.compose.foundation.layout.ColumnScope

package com.cafeteros.historia.ui.features.orderreview

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.orderreview.components.CheckoutProgressStepper
import com.cafeteros.historia.ui.features.orderreview.components.ConfirmPaymentBottomBar
import com.cafeteros.historia.ui.features.orderreview.components.EstimatedDeliveryRow
import com.cafeteros.historia.ui.features.orderreview.components.OrderProductRow
import com.cafeteros.historia.ui.features.orderreview.components.OrderReviewTopBar
import com.cafeteros.historia.ui.features.orderreview.components.OrderTotalsCard
import com.cafeteros.historia.ui.features.orderreview.components.PaymentMethodSummaryCard
import com.cafeteros.historia.ui.features.orderreview.components.ReviewSectionHeader
import com.cafeteros.historia.ui.features.orderreview.components.ShippingDestinationCard
import com.cafeteros.historia.ui.features.orderreview.components.TermsAcceptanceRow
import com.cafeteros.historia.ui.features.orderreview.components.ViewAllProductsRow
import com.cafeteros.historia.ui.features.orderreview.model.EstimatedDelivery
import com.cafeteros.historia.ui.features.orderreview.model.OrderLineItem
import com.cafeteros.historia.ui.features.orderreview.model.OrderReviewPaymentSummary
import com.cafeteros.historia.ui.features.orderreview.model.OrderReviewSampleData
import com.cafeteros.historia.ui.features.orderreview.model.OrderTotals
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Paso 3 del checkout: "Revisar pedido".
 *
 * Layout vertical scrollable:
 *  1. [OrderReviewTopBar] (back · "Revisar pedido" italic).
 *  2. [CheckoutProgressStepper] (3 círculos conectados, paso 3 activo).
 *  3. Sección "Envío a" — header con "Editar" + [ShippingDestinationCard].
 *  4. Sección "Productos (N)" — header sin acción + [OrderProductRow] del
 *     primer item + [ViewAllProductsRow] cuando hay más de uno.
 *  5. Sección "Método de pago" — header con "Editar" +
 *     [PaymentMethodSummaryCard].
 *  6. Sección "Entrega estimada" — header sin acción + [EstimatedDeliveryRow].
 *  7. [OrderTotalsCard] con resumen de costos y total dorado.
 *  8. [TermsAcceptanceRow] con checkbox de aceptación.
 *
 * Bottom bar fijo: [ConfirmPaymentBottomBar] cuyo CTA "Confirmar y pagar"
 * solo se habilita cuando el checkbox de términos está marcado.
 *
 * Estado:
 *  - `termsAccepted`: alterna el checkbox y la habilitación del CTA.
 *
 * @param modifier modifier opcional.
 * @param shippingAddressLine línea principal de la dirección (línea 1).
 * @param shippingAddressSecondary línea secundaria (ciudad/departamento).
 * @param items items del pedido. Solo se muestra el primero, el resto se
 *   accede vía "Ver todos los productos".
 * @param totalProductsCount número total de productos (para el header
 *   "Productos (N)"); si es 0, se usa `items.size`.
 * @param payment resumen del método de pago elegido en el paso 2.
 * @param delivery rango estimado de entrega.
 * @param totals resumen de costos.
 * @param onBack callback del back del top bar.
 * @param onEditAddress callback del enlace "Editar" de la sección "Envío a".
 * @param onEditPayment callback del enlace "Editar" de la sección "Método
 *   de pago".
 * @param onViewAllProducts callback del enlace "Ver todos los productos".
 * @param onConfirm callback del CTA "Confirmar y pagar"; se invoca solo si
 *   el usuario aceptó los términos.
 */
@Composable
fun OrderReviewScreen(
    modifier: Modifier = Modifier,
    shippingAddressLine: String = OrderReviewSampleData.shippingAddressLine,
    shippingAddressSecondary: String = OrderReviewSampleData.shippingAddressSecondary,
    items: List<OrderLineItem> = OrderReviewSampleData.items,
    totalProductsCount: Int = OrderReviewSampleData.totalProducts,
    payment: OrderReviewPaymentSummary = OrderReviewSampleData.payment,
    delivery: EstimatedDelivery = OrderReviewSampleData.delivery,
    totals: OrderTotals = OrderReviewSampleData.totals,
    onBack: () -> Unit = {},
    onEditAddress: () -> Unit = {},
    onEditPayment: () -> Unit = {},
    onViewAllProducts: () -> Unit = {},
    onConfirm: (OrderTotals) -> Unit = {}
) {
    var termsAccepted by remember { mutableStateOf(false) }
    val productsHeaderCount = if (totalProductsCount > 0) totalProductsCount else items.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.OrderReviewBackground)
            .statusBarsPadding()
    ) {
        OrderReviewTopBar(onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            CheckoutProgressStepper(currentStep = 3, totalSteps = 3)

            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
                ReviewSectionHeader(title = "Envío a", onEdit = onEditAddress)
                ShippingDestinationCard(
                    addressLine = shippingAddressLine,
                    secondaryLine = shippingAddressSecondary
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
                ReviewSectionHeader(title = "Productos ($productsHeaderCount)")
                items.firstOrNull()?.let { firstItem ->
                    OrderProductRow(item = firstItem)
                }
                if (productsHeaderCount > 1 || items.size > 1) {
                    ViewAllProductsRow(onClick = onViewAllProducts)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
                ReviewSectionHeader(title = "Método de pago", onEdit = onEditPayment)
                PaymentMethodSummaryCard(payment = payment)
            }

            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
                ReviewSectionHeader(title = "Entrega estimada")
                EstimatedDeliveryRow(rangeLabel = delivery.rangeLabel)
            }

            OrderTotalsCard(totals = totals)

            TermsAcceptanceRow(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
        }

        ConfirmPaymentBottomBar(
            formattedTotal = totals.totalFormatted,
            enabled = termsAccepted,
            onConfirm = { onConfirm(totals) }
        )
    }
}

@Preview(name = "OrderReviewScreen", widthDp = 360, heightDp = 1800)
@Composable
private fun OrderReviewScreenPreview() {
    CafeterosTheme {
        OrderReviewScreen()
    }
}

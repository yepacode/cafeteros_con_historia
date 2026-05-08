package com.cafeteros.historia.ui.features.checkoutpayment

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
import com.cafeteros.historia.ui.features.checkoutpayment.components.AddNewCardLink
import com.cafeteros.historia.ui.features.checkoutpayment.components.CardBrandStrip
import com.cafeteros.historia.ui.features.checkoutpayment.components.PaymentBottomBar
import com.cafeteros.historia.ui.features.checkoutpayment.components.PaymentMethodCard
import com.cafeteros.historia.ui.features.checkoutpayment.components.PaymentSectionTitle
import com.cafeteros.historia.ui.features.checkoutpayment.components.PaymentStepper
import com.cafeteros.historia.ui.features.checkoutpayment.components.PaymentTopBar
import com.cafeteros.historia.ui.features.checkoutpayment.components.SavedCardRow
import com.cafeteros.historia.ui.features.checkoutpayment.components.SecurityNoticeBanner
import com.cafeteros.historia.ui.features.checkoutpayment.components.ShippingAddressSummaryCard
import com.cafeteros.historia.ui.features.checkoutpayment.model.PaymentMethod
import com.cafeteros.historia.ui.features.checkoutpayment.model.PaymentSampleData
import com.cafeteros.historia.ui.features.checkoutpayment.model.SavedCard
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Paso 2 del checkout: "Método de pago".
 *
 * Layout vertical:
 *  1. [PaymentTopBar] (back · "Método de pago" italic · ícono bolsa).
 *  2. [PaymentStepper] (3 puntos compactos, paso 2 activo).
 *  3. [ShippingAddressSummaryCard] (resumen "DIRECCIÓN DE ENVÍO" + Cambiar).
 *  4. [PaymentSectionTitle] ("Selecciona cómo deseas pagar").
 *  5. Listado de [PaymentMethodCard]; la tarjeta seleccionada (TARJETA)
 *     expande la fila de marcas, la tarjeta guardada y "Agregar nueva tarjeta".
 *  6. [SecurityNoticeBanner] (mensaje SSL).
 *  7. [PaymentBottomBar] (total + "Revisar pedido").
 *
 * Estado:
 *  - `selectedMethod`: método activo. Por defecto [PaymentMethod.TARJETA].
 *  - `selectedCardId`: id de la tarjeta guardada activa (solo aplica a
 *    [PaymentMethod.TARJETA]).
 *
 * @param modifier modifier opcional.
 * @param shippingAddressLine resumen de envío mostrado arriba.
 * @param methods métodos disponibles.
 * @param savedCards tarjetas guardadas del comprador.
 * @param defaultMethod método preseleccionado al abrir.
 * @param formattedTotal total a pagar formateado.
 * @param onBack callback del back del top bar.
 * @param onOpenCart callback del ícono de bolsa del top bar.
 * @param onChangeAddress callback del enlace "Cambiar" — vuelve al paso 1.
 * @param onAddCard callback de "Agregar nueva tarjeta".
 * @param onReview callback del CTA "Revisar pedido"; recibe los datos
 *   listos para enviar al paso 3 (resumen).
 */
@Composable
fun PaymentMethodScreen(
    modifier: Modifier = Modifier,
    shippingAddressLine: String = "Cra 10 #42-15, Bogotá",
    methods: List<PaymentMethod> = PaymentSampleData.methods,
    savedCards: List<SavedCard> = PaymentSampleData.savedCards,
    defaultMethod: PaymentMethod = PaymentMethod.TARJETA,
    formattedTotal: String = "$146.000",
    onBack: () -> Unit = {},
    onOpenCart: () -> Unit = {},
    onChangeAddress: () -> Unit = {},
    onAddCard: () -> Unit = {},
    onReview: (
        method: PaymentMethod,
        selectedCard: SavedCard?
    ) -> Unit = { _, _ -> }
) {
    var selectedMethod by remember { mutableStateOf(defaultMethod) }
    var selectedCardId by remember {
        mutableStateOf(savedCards.firstOrNull()?.id.orEmpty())
    }

    val activeCard: SavedCard? = savedCards.firstOrNull { it.id == selectedCardId }
    val canReview: Boolean = selectedMethod != PaymentMethod.TARJETA || activeCard != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.PaymentBackground)
            .statusBarsPadding()
    ) {
        PaymentTopBar(onBack = onBack, onOpenCart = onOpenCart)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            PaymentStepper(currentStep = 2, totalSteps = 3)

            ShippingAddressSummaryCard(
                addressLine = shippingAddressLine,
                onChange = onChangeAddress
            )

            PaymentSectionTitle()

            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                methods.forEach { method ->
                    PaymentMethodCard(
                        method = method,
                        selected = method == selectedMethod,
                        onSelect = { selectedMethod = method },
                        expandedContent = if (method == PaymentMethod.TARJETA) {
                            {
                                CardBrandStrip()
                                savedCards.forEach { card ->
                                    SavedCardRow(
                                        card = card,
                                        selected = card.id == selectedCardId,
                                        onClick = { selectedCardId = card.id }
                                    )
                                }
                                AddNewCardLink(onClick = onAddCard)
                            }
                        } else {
                            null
                        }
                    )
                }
            }

            SecurityNoticeBanner()

            Spacer(modifier = Modifier.height(BrandSpacing.md))
        }

        PaymentBottomBar(
            formattedTotal = formattedTotal,
            enabled = canReview,
            onReview = { onReview(selectedMethod, activeCard) }
        )
    }
}

@Preview(name = "PaymentMethodScreen", widthDp = 360, heightDp = 1800)
@Composable
private fun PaymentMethodScreenPreview() {
    CafeterosTheme {
        PaymentMethodScreen()
    }
}

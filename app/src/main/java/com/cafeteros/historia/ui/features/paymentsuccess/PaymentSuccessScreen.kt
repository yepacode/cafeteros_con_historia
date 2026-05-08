package com.cafeteros.historia.ui.features.paymentsuccess

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.paymentsuccess.components.CaficultorSupportRow
import com.cafeteros.historia.ui.features.paymentsuccess.components.OrderProgressTimeline
import com.cafeteros.historia.ui.features.paymentsuccess.components.OrderReceiptCard
import com.cafeteros.historia.ui.features.paymentsuccess.components.PaymentSuccessActions
import com.cafeteros.historia.ui.features.paymentsuccess.components.PaymentSuccessHero
import com.cafeteros.historia.ui.features.paymentsuccess.components.PaymentSuccessTopBar
import com.cafeteros.historia.ui.features.paymentsuccess.model.ConfirmedOrder
import com.cafeteros.historia.ui.features.paymentsuccess.model.OrderProgressStep
import com.cafeteros.historia.ui.features.paymentsuccess.model.PaymentSuccessSampleData
import com.cafeteros.historia.ui.features.paymentsuccess.model.SupportedCaficultor
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Paso 4 (éxito) del checkout: "¡Gracias por apoyar al origen!".
 *
 * Layout vertical scrollable:
 *  1. [PaymentSuccessTopBar] (X cerrar a la derecha).
 *  2. [PaymentSuccessHero] (badge dorado + confeti).
 *  3. Título serif italic + subtítulo con número de pedido.
 *  4. [OrderReceiptCard] (resumen del pedido confirmado).
 *  5. Sección "✨ Con esta compra apoyaste a:" + lista de
 *     [CaficultorSupportRow].
 *  6. Sección "¿Qué sigue?" + [OrderProgressTimeline].
 *  7. [PaymentSuccessActions] (CTA primario, secundario y enlace terciario).
 *
 * Pantalla de un solo "scroll" — sin bottom bar pegado, ya que el flujo de
 * compra terminó y los CTAs viven al final del contenido.
 *
 * @param modifier modifier opcional.
 * @param order pedido confirmado.
 * @param supportedCaficultores caficultores apoyados por la compra.
 * @param progressSteps pasos del timeline "¿Qué sigue?".
 * @param onClose callback de la X del top bar.
 * @param onTrackOrder callback del CTA "Seguir mi pedido".
 * @param onKeepExploring callback del CTA "Seguir explorando".
 * @param onRateExperience callback del enlace "Calificar esta experiencia".
 * @param onViewCaficultorProfile callback del enlace "Ver perfil"; recibe el
 *   caficultor cuya fila se tocó.
 */
@Composable
fun PaymentSuccessScreen(
    modifier: Modifier = Modifier,
    order: ConfirmedOrder = PaymentSuccessSampleData.order,
    supportedCaficultores: List<SupportedCaficultor> =
        PaymentSuccessSampleData.supportedCaficultores,
    progressSteps: List<OrderProgressStep> = PaymentSuccessSampleData.progressSteps,
    onClose: () -> Unit = {},
    onTrackOrder: () -> Unit = {},
    onKeepExploring: () -> Unit = {},
    onRateExperience: () -> Unit = {},
    onViewCaficultorProfile: (SupportedCaficultor) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.PaymentSuccessBackground)
            .statusBarsPadding()
    ) {
        PaymentSuccessTopBar(onClose = onClose)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaymentSuccessHero()

            Text(
                text = "¡Gracias por apoyar al origen!",
                style = BrandTypography.PaymentSuccessTitle,
                textAlign = TextAlign.Center
            )

            Text(
                text = orderConfirmedSubtitle(order.orderNumber),
                style = BrandTypography.PaymentSuccessSubtitle,
                textAlign = TextAlign.Center
            )

            OrderReceiptCard(order = order)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
                ) {
                    Text(text = "✨", style = BrandTypography.SupportedSectionTitle)
                    Text(
                        text = "Con esta compra apoyaste a:",
                        style = BrandTypography.SupportedSectionTitle
                    )
                }
                supportedCaficultores.forEach { caficultor ->
                    CaficultorSupportRow(
                        caficultor = caficultor,
                        onViewProfile = { onViewCaficultorProfile(caficultor) }
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                Text(
                    text = "¿Qué sigue?",
                    style = BrandTypography.OrderProgressSectionTitle
                )
                OrderProgressTimeline(steps = progressSteps)
            }

            PaymentSuccessActions(
                onTrackOrder = onTrackOrder,
                onKeepExploring = onKeepExploring,
                onRateExperience = onRateExperience
            )

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

private fun orderConfirmedSubtitle(orderNumber: String) = buildAnnotatedString {
    append("Tu pedido ")
    withStyle(
        SpanStyle(
            color = BrandColors.PaymentSuccessOrderHighlight,
            fontWeight = FontWeight.Bold
        )
    ) {
        append(orderNumber)
    }
    append(" fue confirmado")
}

@Preview(name = "PaymentSuccessScreen", widthDp = 360, heightDp = 1800)
@Composable
private fun PaymentSuccessScreenPreview() {
    CafeterosTheme {
        PaymentSuccessScreen()
    }
}

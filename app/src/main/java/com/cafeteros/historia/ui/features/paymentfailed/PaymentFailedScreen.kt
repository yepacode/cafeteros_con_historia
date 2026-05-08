package com.cafeteros.historia.ui.features.paymentfailed

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.paymentfailed.components.PaymentErrorReasonCard
import com.cafeteros.historia.ui.features.paymentfailed.components.PaymentFailedActions
import com.cafeteros.historia.ui.features.paymentfailed.components.PaymentFailedDecoration
import com.cafeteros.historia.ui.features.paymentfailed.components.PaymentFailedHero
import com.cafeteros.historia.ui.features.paymentfailed.components.PaymentFailedTopBar
import com.cafeteros.historia.ui.features.paymentfailed.components.WhatToDoChecklist
import com.cafeteros.historia.ui.features.paymentfailed.model.PaymentFailedSampleData
import com.cafeteros.historia.ui.features.paymentfailed.model.PaymentFailureReason
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Paso 4 (fallo) del checkout: "No pudimos procesar tu pago".
 *
 * Layout vertical scrollable:
 *  1. [PaymentFailedTopBar] (X cerrar a la izquierda).
 *  2. [PaymentFailedHero] (círculo rojo con !).
 *  3. Título serif bold + subtítulo tranquilizador.
 *  4. [PaymentErrorReasonCard] (motivo + código de error).
 *  5. [WhatToDoChecklist] (lista de acciones sugeridas).
 *  6. [PaymentFailedDecoration] (grano de café tenue).
 *  7. [PaymentFailedActions] (CTA primario, secundario y enlace soporte).
 *
 * @param modifier modifier opcional.
 * @param reason motivo del fallo a renderizar.
 * @param whatToDo lista de acciones sugeridas al usuario.
 * @param onClose callback de la X del top bar.
 * @param onRetry callback del CTA "Intentar otra vez".
 * @param onChangeMethod callback del CTA "Cambiar método de pago".
 * @param onContactSupport callback del enlace "Contactar soporte".
 */
@Composable
fun PaymentFailedScreen(
    modifier: Modifier = Modifier,
    reason: PaymentFailureReason = PaymentFailedSampleData.reason,
    whatToDo: List<String> = PaymentFailedSampleData.whatToDo,
    onClose: () -> Unit = {},
    onRetry: () -> Unit = {},
    onChangeMethod: () -> Unit = {},
    onContactSupport: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.PaymentFailedBackground)
            .statusBarsPadding()
    ) {
        PaymentFailedTopBar(onClose = onClose)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg),
            horizontalAlignment = Alignment.Start
        ) {
            PaymentFailedHero()

            Text(
                text = "No pudimos procesar tu pago",
                style = BrandTypography.PaymentFailedTitle,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "No te preocupes, no se realizó ningún cobro",
                style = BrandTypography.PaymentFailedSubtitle,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            PaymentErrorReasonCard(reason = reason)

            WhatToDoChecklist(items = whatToDo)

            PaymentFailedDecoration()

            PaymentFailedActions(
                onRetry = onRetry,
                onChangeMethod = onChangeMethod,
                onContactSupport = onContactSupport
            )

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Preview(name = "PaymentFailedScreen", widthDp = 360, heightDp = 1800)
@Composable
private fun PaymentFailedScreenPreview() {
    CafeterosTheme {
        PaymentFailedScreen()
    }
}

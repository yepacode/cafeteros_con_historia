package com.cafeteros.historia.ui.features.checkoutshipping

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
import com.cafeteros.historia.ui.features.checkoutshipping.components.AddNewAddressCard
import com.cafeteros.historia.ui.features.checkoutshipping.components.AddressCard
import com.cafeteros.historia.ui.features.checkoutshipping.components.AddressesSectionHeader
import com.cafeteros.historia.ui.features.checkoutshipping.components.CheckoutBottomBar
import com.cafeteros.historia.ui.features.checkoutshipping.components.CheckoutStepper
import com.cafeteros.historia.ui.features.checkoutshipping.components.CheckoutTopBar
import com.cafeteros.historia.ui.features.checkoutshipping.components.DeliveryInstructionsField
import com.cafeteros.historia.ui.features.checkoutshipping.components.RecipientChoiceSection
import com.cafeteros.historia.ui.features.checkoutshipping.model.RecipientOption
import com.cafeteros.historia.ui.features.checkoutshipping.model.ShippingAddress
import com.cafeteros.historia.ui.features.checkoutshipping.model.ShippingAddressSampleData
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Paso 1 del checkout: "Dirección de Envío".
 *
 * Layout vertical:
 *  1. [CheckoutTopBar] (back · "Dirección de Envío" italic).
 *  2. [CheckoutStepper] (3 puntos · "PASO 1 DE 3").
 *  3. [AddressesSectionHeader] ("Mis direcciones" + "Gestionar").
 *  4. Lista de [AddressCard] seleccionables.
 *  5. [AddNewAddressCard] (borde dasheado).
 *  6. [DeliveryInstructionsField] (textarea opcional).
 *  7. [RecipientChoiceSection] (Usar mis datos / Alguien más recibirá).
 *  8. [CheckoutBottomBar] (subtotal + Continuar).
 *
 * Estado:
 *  - `selectedAddressId`: id de la dirección seleccionada.
 *  - `instructions`: texto del textarea.
 *  - `recipient`: opción seleccionada del bloque receptor.
 *
 * @param modifier modifier opcional.
 * @param addresses direcciones disponibles del usuario.
 * @param defaultAddressId id de la dirección preseleccionada al abrir.
 * @param formattedSubtotal subtotal ya formateado para el bottom bar.
 * @param onBack callback de la flecha del top bar.
 * @param onManageAddresses callback del enlace "Gestionar".
 * @param onAddAddress callback de "Agregar nueva dirección".
 * @param onEditAddress callback del enlace "Editar" de la card seleccionada.
 * @param onContinue callback del CTA "Continuar" del bottom bar; recibe
 *   los parámetros listos para enviar al siguiente paso del checkout.
 */
@Composable
fun ShippingAddressScreen(
    modifier: Modifier = Modifier,
    addresses: List<ShippingAddress> = ShippingAddressSampleData.addresses,
    defaultAddressId: String = addresses.firstOrNull()?.id.orEmpty(),
    formattedSubtotal: String = "$146.000",
    onBack: () -> Unit = {},
    onManageAddresses: () -> Unit = {},
    onAddAddress: () -> Unit = {},
    onEditAddress: (ShippingAddress) -> Unit = {},
    onContinue: (
        selectedAddress: ShippingAddress,
        instructions: String,
        recipient: RecipientOption
    ) -> Unit = { _, _, _ -> }
) {
    var selectedAddressId by remember { mutableStateOf(defaultAddressId) }
    var instructions by remember { mutableStateOf("") }
    var recipient by remember { mutableStateOf(RecipientOption.USAR_MIS_DATOS) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.CheckoutBackground)
            .statusBarsPadding()
    ) {
        CheckoutTopBar(title = "Dirección de Envío", onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            CheckoutStepper(currentStep = 1, totalSteps = 3)

            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
                AddressesSectionHeader(onManage = onManageAddresses)

                addresses.forEach { address ->
                    AddressCard(
                        address = address,
                        selected = address.id == selectedAddressId,
                        onSelect = { selectedAddressId = address.id },
                        onEdit = { onEditAddress(address) }
                    )
                }

                AddNewAddressCard(onClick = onAddAddress)
            }

            DeliveryInstructionsField(
                value = instructions,
                onValueChange = { instructions = it }
            )

            RecipientChoiceSection(
                selected = recipient,
                onSelect = { recipient = it }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.md))
        }

        CheckoutBottomBar(
            formattedSubtotal = formattedSubtotal,
            enabled = selectedAddressId.isNotBlank(),
            onContinue = {
                val address = addresses.firstOrNull { it.id == selectedAddressId }
                if (address != null) onContinue(address, instructions, recipient)
            }
        )
    }
}

@Preview(name = "ShippingAddressScreen", widthDp = 360, heightDp = 1800)
@Composable
private fun ShippingAddressScreenPreview() {
    CafeterosTheme {
        ShippingAddressScreen()
    }
}

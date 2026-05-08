package com.cafeteros.historia.ui.features.addressbook

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
import com.cafeteros.historia.ui.features.addressbook.components.AddAddressBottomBar
import com.cafeteros.historia.ui.features.addressbook.components.AddressBookFooter
import com.cafeteros.historia.ui.features.addressbook.components.AddressBookHero
import com.cafeteros.historia.ui.features.addressbook.components.AddressBookTopBar
import com.cafeteros.historia.ui.features.addressbook.components.ManagedAddressCard
import com.cafeteros.historia.ui.features.addressbook.model.ManagedAddress
import com.cafeteros.historia.ui.features.addressbook.model.ManagedAddressSampleData
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla "Mis Direcciones": libreta de direcciones del comprador.
 *
 * Se abre desde el enlace "Gestionar" del paso 1 del checkout
 * ([com.cafeteros.historia.ui.features.checkoutshipping.ShippingAddressScreen]).
 *
 * Layout vertical:
 *  1. [AddressBookTopBar] (back · "Mis Direcciones" centrado).
 *  2. [AddressBookHero] (caption verde italic + título serif bold).
 *  3. Lista de [ManagedAddressCard] con menú kebab (predeterminada/editar/eliminar).
 *  4. [AddressBookFooter] (bloque decorativo de dos tonos + caption italic).
 *  5. [AddAddressBottomBar] (CTA dark "+ Agregar dirección").
 *
 * Estado local:
 *  - `addresses`: lista mutable que se actualiza al marcar una dirección
 *    como predeterminada (mueve el flag `isDefault`) o al eliminar una
 *    card (la quita de la lista). Cuando exista BD, este estado lo
 *    sincronizará el ViewModel con `addresses(userId, roleId)`.
 *
 * @param modifier modifier opcional.
 * @param initialAddresses direcciones iniciales del usuario.
 * @param onBack callback de la flecha del top bar.
 * @param onAddAddress callback del CTA "+ Agregar dirección".
 * @param onEditAddress callback al elegir "Editar" en el menú kebab.
 */
@Composable
fun AddressBookScreen(
    modifier: Modifier = Modifier,
    initialAddresses: List<ManagedAddress> = ManagedAddressSampleData.addresses,
    onBack: () -> Unit = {},
    onAddAddress: () -> Unit = {},
    onEditAddress: (ManagedAddress) -> Unit = {}
) {
    var addresses by remember { mutableStateOf(initialAddresses) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AddressBookBackground)
            .statusBarsPadding()
    ) {
        AddressBookTopBar(onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            AddressBookHero()

            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
                addresses.forEach { address ->
                    ManagedAddressCard(
                        address = address,
                        onSetDefault = {
                            addresses = addresses.map {
                                it.copy(isDefault = it.id == address.id)
                            }
                        },
                        onEdit = { onEditAddress(address) },
                        onDelete = {
                            addresses = addresses.filterNot { it.id == address.id }
                        }
                    )
                }
            }

            AddressBookFooter(
                modifier = Modifier.padding(top = BrandSpacing.md)
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
        }

        AddAddressBottomBar(onAddAddress = onAddAddress)
    }
}

@Preview(name = "AddressBookScreen", widthDp = 360, heightDp = 1600)
@Composable
private fun AddressBookScreenPreview() {
    CafeterosTheme {
        AddressBookScreen()
    }
}

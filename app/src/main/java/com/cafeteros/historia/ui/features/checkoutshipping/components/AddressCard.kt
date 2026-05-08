package com.cafeteros.historia.ui.features.checkoutshipping.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.checkoutshipping.model.AddressType
import com.cafeteros.historia.ui.features.checkoutshipping.model.ShippingAddress
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card de dirección seleccionable del checkout.
 *
 * Layout:
 *  - Radio button + badge `🏠 CASA` en la fila superior, link `Editar`
 *    verde alineado a la derecha (solo visible cuando la card está
 *    seleccionada).
 *  - Línea bold con la dirección principal.
 *  - Línea con la ciudad.
 *  - Línea opcional con icono de persona + contacto.
 *
 * Cuando [selected] es true, la card tiene fondo blanco, borde oscuro y
 * radio relleno. Cuando no, fondo cream sin borde y radio outline vacío.
 *
 * @param modifier modifier opcional.
 * @param address dirección a renderizar.
 * @param selected estado de selección.
 * @param onSelect callback al pulsar la card.
 * @param onEdit callback del enlace "Editar" (solo visible cuando se
 *   está seleccionada).
 */
@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    address: ShippingAddress,
    selected: Boolean,
    onSelect: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    val shape = RoundedCornerShape(14.dp)
    val containerModifier = if (selected) {
        Modifier
            .clip(shape)
            .background(BrandColors.AddressCardSelectedBackground)
            .border(BorderStroke(1.5.dp, BrandColors.AddressCardSelectedBorder), shape)
    } else {
        Modifier
            .clip(shape)
            .background(BrandColors.AddressCardUnselectedBackground)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(containerModifier)
            .clickable(onClick = onSelect)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                AddressRadio(selected = selected)
                AddressTypeBadge(type = address.type)
            }
            if (selected) {
                Text(
                    text = "Editar",
                    style = BrandTypography.AddressEditLink,
                    modifier = Modifier
                        .clickable(onClick = onEdit)
                        .padding(horizontal = BrandSpacing.xs)
                )
            }
        }

        Column(
            modifier = Modifier.padding(start = 32.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = address.addressLine, style = BrandTypography.AddressLine)
            Text(text = address.city, style = BrandTypography.AddressCity)

            if (address.contactLine != null) {
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = BrandColors.TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = address.contactLine, style = BrandTypography.AddressContact)
                }
            }
        }
    }
}

@Composable
private fun AddressRadio(modifier: Modifier = Modifier, selected: Boolean) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = if (selected) BrandColors.RadioSelected else BrandColors.RadioUnselected,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(BrandColors.RadioSelected)
            )
        }
    }
}

@Composable
private fun AddressTypeBadge(modifier: Modifier = Modifier, type: AddressType) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(BrandColors.AddressTypeBadgeBackground)
            .padding(horizontal = BrandSpacing.sm, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = type.icon,
            contentDescription = null,
            tint = BrandColors.AddressTypeBadgeIcon,
            modifier = Modifier.size(14.dp)
        )
        Text(text = type.label, style = BrandTypography.AddressTypeBadgeLabel)
    }
}

@Preview(name = "AddressCard — selected", showBackground = true, widthDp = 360)
@Composable
private fun AddressCardSelectedPreview() {
    CafeterosTheme {
        AddressCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            address = ShippingAddress(
                id = "x",
                type = AddressType.CASA,
                addressLine = "Cra 10 #42-15, Apto 502",
                city = "Bogotá, Cundinamarca",
                contactLine = "Mich Cárdenas · 300 123 4567"
            ),
            selected = true
        )
    }
}

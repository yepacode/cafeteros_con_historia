package com.cafeteros.historia.ui.features.addressbook.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.addressbook.model.ManagedAddress
import com.cafeteros.historia.ui.features.addressbook.model.ManagedAddressType
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card administrable de "Mis Direcciones".
 *
 * Layout vertical:
 *  - Fila superior: badge verde con tipo a la izquierda + menú kebab
 *    (3 puntos) a la derecha que abre un [DropdownMenu] con acciones
 *    "Establecer como predeterminada", "Editar" y "Eliminar".
 *  - Línea bold serif con la dirección principal.
 *  - Línea con la ciudad/departamento.
 *  - Fila con icono de teléfono + número.
 *  - Pill amarilla "PREDETERMINADA" (solo si [ManagedAddress.isDefault]).
 *
 * El estado de apertura del menú kebab se mantiene local a cada card —
 * abrir el menú de una card no afecta a las demás.
 *
 * @param modifier modifier opcional.
 * @param address dirección a renderizar.
 * @param onSetDefault callback al elegir "Establecer como predeterminada".
 *   Solo se invoca si la dirección NO es ya la predeterminada.
 * @param onEdit callback al elegir "Editar".
 * @param onDelete callback al elegir "Eliminar".
 */
@Composable
fun ManagedAddressCard(
    modifier: Modifier = Modifier,
    address: ManagedAddress,
    onSetDefault: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val shape = RoundedCornerShape(18.dp)
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(BrandColors.ManagedAddressCardBackground)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ManagedAddressBadge(type = address.type)

            Box {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable { menuExpanded = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Más opciones",
                        tint = BrandColors.ManagedAddressMenuIcon,
                        modifier = Modifier.size(20.dp)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    if (!address.isDefault) {
                        DropdownMenuItem(
                            text = { Text("Establecer como predeterminada") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Star,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onSetDefault()
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        }
                    )
                }
            }
        }

        Text(text = address.addressLine, style = BrandTypography.ManagedAddressLine)

        Text(text = address.city, style = BrandTypography.ManagedAddressCity)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Phone,
                contentDescription = null,
                tint = BrandColors.ManagedAddressPhoneIcon,
                modifier = Modifier.size(16.dp)
            )
            Text(text = address.phone, style = BrandTypography.ManagedAddressPhone)
        }

        if (address.isDefault) {
            DefaultAddressPill(modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Preview(name = "ManagedAddressCard — predeterminada", showBackground = true, widthDp = 360)
@Composable
private fun ManagedAddressCardDefaultPreview() {
    CafeterosTheme {
        ManagedAddressCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            address = ManagedAddress(
                id = "casa",
                type = ManagedAddressType.CASA,
                addressLine = "Cra 10 #42-15, Apto 502",
                city = "Bogotá, Cundinamarca",
                phone = "300 123 4567",
                isDefault = true
            )
        )
    }
}

@Preview(name = "ManagedAddressCard — secundaria", showBackground = true, widthDp = 360)
@Composable
private fun ManagedAddressCardSecondaryPreview() {
    CafeterosTheme {
        ManagedAddressCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            address = ManagedAddress(
                id = "oficina",
                type = ManagedAddressType.OFICINA,
                addressLine = "Calle 93 #11A-28, Edificio Ápice",
                city = "Bogotá, Cundinamarca",
                phone = "315 789 0123"
            )
        )
    }
}

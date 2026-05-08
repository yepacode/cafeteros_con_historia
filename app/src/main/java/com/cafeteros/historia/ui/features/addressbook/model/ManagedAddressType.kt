package com.cafeteros.historia.ui.features.addressbook.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Tipo de dirección dentro de la libreta "Mis Direcciones".
 *
 * Cada tipo se renderiza con una etiqueta capitalizada y un icono dentro
 * del badge verde claro de la card. Cuando exista BD, este enum se mapea
 * desde un campo string del schema de direcciones.
 *
 * @property label etiqueta visible dentro del badge ("Casa", "Oficina", "Otros").
 * @property icon icono de marca acompañando al label dentro del badge.
 */
enum class ManagedAddressType(val label: String, val icon: ImageVector) {
    CASA(label = "Casa", icon = Icons.Filled.Home),
    OFICINA(label = "Oficina", icon = Icons.Filled.Work),
    OTROS(label = "Otros", icon = Icons.Filled.Place)
}

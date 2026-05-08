package com.cafeteros.historia.ui.features.checkoutshipping.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Tipo de dirección guardada por el usuario.
 *
 * Cada tipo se renderiza con una etiqueta uppercase y un icono dentro
 * del badge de la card. Cuando exista BD, este enum se mapea desde un
 * campo string del schema de direcciones.
 */
enum class AddressType(val label: String, val icon: ImageVector) {
    CASA(label = "CASA", icon = Icons.Filled.Home),
    OFICINA(label = "OFICINA", icon = Icons.Filled.Apartment),
    OTRA(label = "OTRA", icon = Icons.Filled.Place)
}

package com.cafeteros.historia.ui.features.cart.model

/**
 * Datos de envío que aparecen en la card "Llega el martes…".
 *
 * @property arrivalLabel texto bold con la fecha estimada
 *   ("Llega el martes 23 de abril").
 * @property addressLabel línea con la dirección de envío.
 */
data class CartShipping(
    val arrivalLabel: String,
    val addressLabel: String
)

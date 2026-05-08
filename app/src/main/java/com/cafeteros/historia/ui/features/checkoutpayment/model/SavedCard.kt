package com.cafeteros.historia.ui.features.checkoutpayment.model

/**
 * Tarjeta guardada del comprador asociada a un método [PaymentMethod.TARJETA].
 *
 * Se muestra como una fila con los últimos cuatro dígitos enmascarados y la
 * marca del emisor. El check dorado de la derecha indica cuál es la tarjeta
 * activa cuando el método "Tarjeta" está seleccionado.
 *
 * Cuando exista BD, viene de `payment_cards(userId, roleId)`.
 *
 * @property id identificador estable usado como key.
 * @property brand marca del emisor ("Visa", "Mastercard"...).
 * @property last4 últimos cuatro dígitos del número de tarjeta.
 */
data class SavedCard(
    val id: String,
    val brand: String,
    val last4: String
) {
    /** Etiqueta lista para renderizar: "**** 4567 Visa". */
    val maskedLabel: String get() = "**** $last4 $brand"
}

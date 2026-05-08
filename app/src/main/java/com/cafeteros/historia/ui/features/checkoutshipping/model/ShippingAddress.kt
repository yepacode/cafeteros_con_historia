package com.cafeteros.historia.ui.features.checkoutshipping.model

/**
 * Dirección guardada del comprador, mostrada como card seleccionable
 * en el paso 1 del checkout.
 *
 * Cuando exista BD, viene de `addresses(userId, roleId)`. El
 * [contactLine] es opcional porque las direcciones secundarias (oficina,
 * otra) en el diseño no muestran contacto.
 *
 * @property id identificador estable usado como key.
 * @property type tipo de dirección — controla el badge.
 * @property addressLine línea principal bold ("Cra 10 #42-15, Apto 502").
 * @property city línea de ciudad debajo.
 * @property contactLine línea opcional con nombre y teléfono al final
 *   de la card.
 */
data class ShippingAddress(
    val id: String,
    val type: AddressType,
    val addressLine: String,
    val city: String,
    val contactLine: String? = null
)

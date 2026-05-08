package com.cafeteros.historia.ui.features.addressbook.model

/**
 * Dirección guardada del comprador, mostrada como card editable en la
 * pantalla "Mis Direcciones".
 *
 * Se diferencia de [com.cafeteros.historia.ui.features.checkoutshipping.model.ShippingAddress]
 * porque aquí la card no es seleccionable sino administrable: incluye
 * teléfono visible, marcador de predeterminada y menú contextual.
 *
 * Cuando exista BD, viene de `addresses(userId, roleId)`.
 *
 * @property id identificador estable usado como key.
 * @property type tipo de dirección — controla el badge verde claro.
 * @property addressLine línea principal serif bold ("Cra 10 #42-15, Apto 502").
 * @property city línea de ciudad/departamento debajo.
 * @property phone teléfono de contacto mostrado con icono de teléfono.
 * @property isDefault `true` si esta dirección es la predeterminada — solo
 *   una dirección puede ser predeterminada al mismo tiempo.
 */
data class ManagedAddress(
    val id: String,
    val type: ManagedAddressType,
    val addressLine: String,
    val city: String,
    val phone: String,
    val isDefault: Boolean = false
)

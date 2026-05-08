package com.cafeteros.historia.ui.features.checkoutshipping.model

/**
 * Data de muestra del paso 1 del checkout mientras no exista backend.
 *
 * Cuando exista, las direcciones vendrán de `addresses(userId, roleId)`
 * y la pantalla recibirá la lista por parámetro.
 */
object ShippingAddressSampleData {

    val addresses: List<ShippingAddress> = listOf(
        ShippingAddress(
            id = "casa",
            type = AddressType.CASA,
            addressLine = "Cra 10 #42-15, Apto 502",
            city = "Bogotá, Cundinamarca",
            contactLine = "Mich Cárdenas · 300 123 4567"
        ),
        ShippingAddress(
            id = "oficina",
            type = AddressType.OFICINA,
            addressLine = "Calle 93 #11-45, Of 301",
            city = "Bogotá"
        )
    )
}

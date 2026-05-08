package com.cafeteros.historia.ui.features.addressbook.model

/**
 * Data de muestra para la pantalla "Mis Direcciones" mientras no exista
 * backend.
 *
 * Cuando exista, las direcciones vendrán de `addresses(userId, roleId)`
 * y la pantalla las recibirá por parámetro.
 */
object ManagedAddressSampleData {

    val addresses: List<ManagedAddress> = listOf(
        ManagedAddress(
            id = "casa",
            type = ManagedAddressType.CASA,
            addressLine = "Cra 10 #42-15, Apto 502",
            city = "Bogotá, Cundinamarca",
            phone = "300 123 4567",
            isDefault = true
        ),
        ManagedAddress(
            id = "oficina",
            type = ManagedAddressType.OFICINA,
            addressLine = "Calle 93 #11A-28, Edificio Ápice",
            city = "Bogotá, Cundinamarca",
            phone = "315 789 0123"
        ),
        ManagedAddress(
            id = "otros",
            type = ManagedAddressType.OTROS,
            addressLine = "Transversal 5 #78-22, Casa 4",
            city = "Medellín, Antioquia",
            phone = "321 456 7890"
        )
    )
}

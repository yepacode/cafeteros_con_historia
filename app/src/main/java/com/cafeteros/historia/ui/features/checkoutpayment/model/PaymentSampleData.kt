package com.cafeteros.historia.ui.features.checkoutpayment.model

/**
 * Data de muestra del paso 2 del checkout mientras no exista backend.
 *
 * Cuando exista, las tarjetas guardadas vendrán de
 * `payment_cards(userId, roleId)` y la pantalla recibirá la lista por parámetro.
 */
object PaymentSampleData {

    /** Métodos disponibles — replican uno a uno el orden del diseño. */
    val methods: List<PaymentMethod> = PaymentMethod.entries

    /** Tarjetas guardadas del comprador — el diseño solo muestra una. */
    val savedCards: List<SavedCard> = listOf(
        SavedCard(id = "card-visa-4567", brand = "Visa", last4 = "4567")
    )
}

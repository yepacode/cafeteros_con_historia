package com.cafeteros.historia.ui.features.cart.model

import com.cafeteros.historia.R

/**
 * Data de muestra del carrito mientras no exista backend.
 *
 * Cuando exista, los items se hidratarán desde `CartRepository.getCart()`
 * y el resumen se calculará en la capa de dominio. La pantalla seguirá
 * recibiendo el mismo contrato de [CartCaficultorGroup] + [CartSummary].
 */
object CartSampleData {

    private val cafeHuilaPitalito = CartItem(
        id = "i1",
        name = "Café Huila Pitalito 250g",
        meta = "250g · Molido medio",
        unitPrice = 48_000,
        originalUnitPrice = 56_000,
        imageRes = R.drawable.cafe_huila_pitalito,
        quantity = 2
    )

    private val reservaDelPatron = CartItem(
        id = "i2",
        name = "Reserva del Patrón",
        meta = "500g · Grano entero",
        unitPrice = 96_000,
        imageRes = R.drawable.cafe_origen_narino,
        quantity = 1
    )

    val groups: List<CartCaficultorGroup> = listOf(
        CartCaficultorGroup(
            id = "g1",
            caficultorName = "Finca La Esperanza",
            avatarRes = R.drawable.ima_1,
            zoneLabel = "HUILA",
            items = listOf(cafeHuilaPitalito, reservaDelPatron)
        )
    )

    val shipping: CartShipping = CartShipping(
        arrivalLabel = "Llega el martes 23 de abril",
        addressLabel = "Envío a Cra 10 #42-15, Bogotá"
    )

    /** Umbral del envío gratis y descuento de muestra para el resumen. */
    const val FREE_SHIPPING_THRESHOLD: Int = 160_000
    const val SAMPLE_SHIPPING: Int = 12_000
    const val SAMPLE_DISCOUNT: Int = 10_000

    /**
     * Recomendaciones del empty state ("Recomendaciones del Origen").
     *
     * Cuando exista backend, vendrán de `recommendations(roleId)` —
     * personalizadas según el historial del comprador.
     */
    val recommendations: List<CartRecommendation> = listOf(
        CartRecommendation(
            id = "rec1",
            name = "Finca El Diviso",
            imageRes = R.drawable.cafe_origen_narino,
            metaLabel = "HUILA · LAVADO"
        ),
        CartRecommendation(
            id = "rec2",
            name = "Sierra Nevada",
            imageRes = R.drawable.cafe_sierra_nevada_premium,
            metaLabel = "ORGÁNICO · MIEL"
        )
    )
}

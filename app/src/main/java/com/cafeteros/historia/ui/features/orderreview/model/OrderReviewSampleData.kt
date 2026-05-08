package com.cafeteros.historia.ui.features.orderreview.model

/**
 * Data de muestra del paso 3 del checkout mientras no exista backend.
 *
 * Cuando se conecten BD/backend, los siguientes valores los compone el use-
 * case que arma el resumen del pedido a partir del carrito y la elección del
 * paso 2 (no se llaman desde la pantalla). Los datos aquí replican uno a uno
 * los del diseño entregado.
 */
object OrderReviewSampleData {

    /** Línea principal de la dirección de envío seleccionada en el paso 1. */
    const val shippingAddressLine: String = "Cra 10 #42-15"

    /** Línea secundaria de la dirección (ciudad y departamento). */
    const val shippingAddressSecondary: String = "Bogotá, Cundinamarca"

    /** Total de productos del carrito — se muestra en el header "Productos (N)". */
    const val totalProducts: Int = 3

    /** Listado completo de productos del pedido. El diseño solo expone el primero
     *  y oculta el resto detrás del enlace "Ver todos los productos". */
    val items: List<OrderLineItem> = listOf(
        OrderLineItem(
            id = "sierra-nevada-dark",
            name = "Sierra Nevada Dark Roast",
            unitPriceFormatted = "$48.000",
            quantity = 2,
            totalPriceFormatted = "$96.000",
            thumbLabel = "CAFÉ\nORIGEN"
        ),
        OrderLineItem(
            id = "huila-honey",
            name = "Huila Honey Process",
            unitPriceFormatted = "$28.000",
            quantity = 1,
            totalPriceFormatted = "$28.000",
            thumbLabel = "CAFÉ\nORIGEN"
        ),
        OrderLineItem(
            id = "santander-natural",
            name = "Santander Natural",
            unitPriceFormatted = "$20.000",
            quantity = 1,
            totalPriceFormatted = "$20.000",
            thumbLabel = "CAFÉ\nORIGEN"
        )
    )

    /** Resumen de costos del pedido. */
    val totals: OrderTotals = OrderTotals(
        subtotalFormatted = "$144.000",
        shippingFormatted = "$12.000",
        discountFormatted = "-$10.000",
        totalFormatted = "$146.000"
    )

    /** Rango estimado de entrega. */
    val delivery: EstimatedDelivery = EstimatedDelivery(
        rangeLabel = "Martes 23 de abril - Miércoles 24 de abril"
    )

    /** Resumen del método de pago elegido en el paso 2. */
    val payment: OrderReviewPaymentSummary = OrderReviewPaymentSummary(
        brand = "Visa",
        last4 = "4567"
    )
}

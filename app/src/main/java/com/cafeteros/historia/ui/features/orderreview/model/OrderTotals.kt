package com.cafeteros.historia.ui.features.orderreview.model

/**
 * Resumen de costos mostrado en el totals card del paso 3 del checkout.
 *
 * Todas las cifras llegan ya formateadas (`$144.000`, `-$10.000`...), de modo
 * que la pantalla no tiene que hacer cálculos. Cuando exista backend, el use-
 * case que arme el resumen del pedido los compone a partir del carrito, las
 * tarifas de envío vigentes y los descuentos aplicables.
 *
 * @property subtotalFormatted subtotal de productos antes de envío.
 * @property shippingFormatted costo de envío.
 * @property discountFormatted descuento aplicado, en negativo si reduce.
 * @property totalFormatted total final a cobrar (IVA incluido).
 */
data class OrderTotals(
    val subtotalFormatted: String,
    val shippingFormatted: String,
    val discountFormatted: String,
    val totalFormatted: String
)

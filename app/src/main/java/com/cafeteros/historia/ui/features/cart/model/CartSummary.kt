package com.cafeteros.historia.ui.features.cart.model

/**
 * Resumen de costos del carrito mostrado en la card "Subtotal / Envío /
 * Descuento / Total".
 *
 * Los valores van en pesos sin formatear; la pantalla los formatea con el
 * locale es-CO.
 *
 * @property subtotal suma de (`unitPrice * quantity`) de todos los items.
 * @property shipping costo de envío en pesos.
 * @property discount valor del cupón aplicado en pesos (positivo); se
 *   muestra precedido por un signo menos.
 * @property total subtotal + shipping − discount.
 * @property amountToFreeShipping cuánto le falta al usuario para superar
 *   el umbral de envío gratis. 0 = ya lo alcanzó.
 * @property freeShippingThreshold umbral total para envío gratis.
 */
data class CartSummary(
    val subtotal: Int,
    val shipping: Int,
    val discount: Int,
    val total: Int,
    val amountToFreeShipping: Int,
    val freeShippingThreshold: Int
) {

    /** Porcentaje de progreso hacia el envío gratis (0..1). */
    val freeShippingProgress: Float
        get() {
            if (freeShippingThreshold <= 0) return 1f
            val achieved = (freeShippingThreshold - amountToFreeShipping).coerceAtLeast(0)
            return (achieved.toFloat() / freeShippingThreshold).coerceIn(0f, 1f)
        }
}

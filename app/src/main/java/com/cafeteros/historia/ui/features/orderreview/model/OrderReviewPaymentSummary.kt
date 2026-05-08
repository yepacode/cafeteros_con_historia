package com.cafeteros.historia.ui.features.orderreview.model

/**
 * Resumen del método de pago elegido en el paso 2, mostrado en el paso 3.
 *
 * No incluye estado interactivo: el cambio de método se hace volviendo al
 * paso anterior con el enlace "Editar". Cuando exista BD, este resumen se
 * deriva de la elección guardada en el flujo de checkout.
 *
 * @property brand marca de la tarjeta o nombre del método ("Visa", "Nequi"…).
 * @property last4 últimos 4 dígitos cuando aplica (tarjeta); vacío para
 *   métodos sin tarjeta.
 */
data class OrderReviewPaymentSummary(
    val brand: String,
    val last4: String
)

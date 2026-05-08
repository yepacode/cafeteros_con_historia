package com.cafeteros.historia.ui.features.paymentsuccess.model

/**
 * Resumen del pedido recién confirmado, mostrado en el receipt card del
 * paso 4 (éxito). Todos los campos llegan ya formateados para que la
 * pantalla solo se preocupe del layout — cuando exista backend, esta data
 * se compone a partir del pedido recién creado.
 *
 * @property orderNumber identificador legible del pedido ("#OR-34521").
 * @property totalPaidFormatted total cobrado ya formateado ("$146.000").
 * @property estimatedDelivery fecha de entrega estimada formateada
 *   ("Martes 23 de abril").
 * @property paymentLabel etiqueta corta del método con el que se pagó
 *   ("Visa **** 4567").
 */
data class ConfirmedOrder(
    val orderNumber: String,
    val totalPaidFormatted: String,
    val estimatedDelivery: String,
    val paymentLabel: String
)

package com.cafeteros.historia.ui.features.paymentfailed.model

/**
 * Motivo de fallo del pago, mostrado en la card roja del paso 4 (fallo).
 *
 * El [reasonLabel] viene ya en lenguaje natural en español; el [errorCode]
 * es el código devuelto por el procesador de pagos (Visa/Mastercard/PSE)
 * que el equipo de soporte usa para diagnóstico. Cuando exista backend, esta
 * estructura se mapea desde la respuesta del gateway.
 *
 * @property reasonLabel motivo legible ("Fondos insuficientes").
 * @property errorCode código numérico estable ("51").
 */
data class PaymentFailureReason(
    val reasonLabel: String,
    val errorCode: String
)

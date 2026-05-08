package com.cafeteros.historia.ui.features.paymentfailed.model

/**
 * Data de muestra del paso 4 (fallo) mientras no exista backend.
 *
 * Cuando exista, el motivo y la lista "¿Qué puedes hacer?" se compondrán a
 * partir de la respuesta del gateway (los textos del checklist son los
 * mismos para todos los códigos según el diseño actual; si en el futuro se
 * personalizan por código, esta lista pasaría a ser dinámica).
 */
object PaymentFailedSampleData {

    /** Motivo del fallo mostrado en la card roja. */
    val reason: PaymentFailureReason = PaymentFailureReason(
        reasonLabel = "Fondos insuficientes",
        errorCode = "51"
    )

    /** Pasos sugeridos al usuario para recuperarse del fallo. */
    val whatToDo: List<String> = listOf(
        "Verifica el saldo de tu tarjeta",
        "Intenta con otro método de pago",
        "Contacta a tu banco",
        "Contáctanos si el problema persiste"
    )
}

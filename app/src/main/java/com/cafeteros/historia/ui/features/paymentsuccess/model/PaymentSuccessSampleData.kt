package com.cafeteros.historia.ui.features.paymentsuccess.model

/**
 * Data de muestra del paso 4 (éxito) mientras no exista backend.
 *
 * Replica los textos del diseño y se usa como fallback cuando la activity
 * no recibe extras del paso 3 — útil para deep-links y pruebas. Cuando
 * exista BD, el repositorio expone un equivalente a partir del pedido recién
 * creado.
 */
object PaymentSuccessSampleData {

    /** Pedido recién confirmado. */
    val order: ConfirmedOrder = ConfirmedOrder(
        orderNumber = "#OR-34521",
        totalPaidFormatted = "$146.000",
        estimatedDelivery = "Martes 23 de abril",
        paymentLabel = "Visa **** 4567"
    )

    /** Caficultores apoyados por el pedido. */
    val supportedCaficultores: List<SupportedCaficultor> = listOf(
        SupportedCaficultor(
            id = "finca-la-esperanza",
            fincaName = "Finca La Esperanza",
            ownerName = "Alberto Rodríguez",
            avatarInitials = "AR"
        ),
        SupportedCaficultor(
            id = "finca-el-paraiso",
            fincaName = "Finca El Paraíso",
            ownerName = "Elena Gómez",
            avatarInitials = "EG"
        )
    )

    /** Pasos del timeline "¿Qué sigue?". */
    val progressSteps: List<OrderProgressStep> = listOf(
        OrderProgressStep(label = "Confirmamos tu pedido", completed = true),
        OrderProgressStep(label = "Don Alberto prepara tu café", completed = false),
        OrderProgressStep(label = "Enviamos tu pedido", completed = false),
        OrderProgressStep(label = "Entregamos en tu puerta", completed = false)
    )
}

package com.cafeteros.historia.ui.features.paymentsuccess.model

/**
 * Paso del timeline "¿Qué sigue?" del paso 4 (éxito).
 *
 * El timeline muestra explícitamente al comprador el camino restante de su
 * pedido. Cuando exista backend, los pasos vendrán del estado del pedido en
 * tiempo real (`order_status_history`).
 *
 * @property label etiqueta visible del paso ("Confirmamos tu pedido").
 * @property completed `true` si el paso ya pasó (mostrado relleno en dorado
 *   con texto en bold). `false` para pasos futuros (círculo outline rosa
 *   y texto secundario).
 */
data class OrderProgressStep(
    val label: String,
    val completed: Boolean
)

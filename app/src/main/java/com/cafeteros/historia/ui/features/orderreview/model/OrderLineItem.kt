package com.cafeteros.historia.ui.features.orderreview.model

/**
 * Item del listado de productos en el paso 3 del checkout.
 *
 * Se modela ya formateado para que la pantalla solo se preocupe del layout —
 * cuando exista BD/backend, el use-case que arme el resumen del pedido será el
 * responsable de formatear los precios con la misma localización que usa el
 * carrito (es-CO, símbolo "$" antepuesto, separador de miles ".").
 *
 * @property id identificador único del producto en el carrito.
 * @property name nombre comercial del café ("Sierra Nevada Dark Roast").
 * @property unitPriceFormatted precio unitario ya formateado ("$48.000").
 * @property quantity cantidad seleccionada en el carrito.
 * @property totalPriceFormatted precio total de la línea ya formateado
 *   ("$96.000"); generalmente `unitPrice * quantity`.
 * @property thumbLabel texto a renderizar dentro del thumbnail circular del
 *   producto cuando aún no hay imagen (placeholder editorial "CAFÉ\nORIGEN").
 */
data class OrderLineItem(
    val id: String,
    val name: String,
    val unitPriceFormatted: String,
    val quantity: Int,
    val totalPriceFormatted: String,
    val thumbLabel: String
)

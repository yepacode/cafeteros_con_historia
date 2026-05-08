package com.cafeteros.historia.ui.features.orderreview.model

/**
 * Rango estimado de entrega mostrado en la sección "Entrega estimada".
 *
 * Se modela como una sola etiqueta ya formateada para no acoplar la pantalla
 * a un timezone, locale o provider concreto. Cuando exista backend la
 * componemos en el momento que se sirve el pedido.
 *
 * @property rangeLabel texto ya formateado ("Martes 23 de abril -
 *   Miércoles 24 de abril").
 */
data class EstimatedDelivery(
    val rangeLabel: String
)

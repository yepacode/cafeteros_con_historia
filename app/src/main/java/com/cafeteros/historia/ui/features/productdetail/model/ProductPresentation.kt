package com.cafeteros.historia.ui.features.productdetail.model

/**
 * Presentaciones disponibles para el producto (250g, 500g, 1kg).
 *
 * El [label] se renderiza tal cual en el chip selector y, cuando se conecten
 * los precios reales por presentación, [priceMultiplier] permite calcular el
 * precio mostrado en el bottom bar sin tocar la UI.
 *
 * @property label texto visible en el chip ("250g").
 * @property priceMultiplier factor para calcular el precio final respecto al
 *   precio base del producto.
 */
enum class ProductPresentation(val label: String, val priceMultiplier: Double) {
    SIZE_250G(label = "250g", priceMultiplier = 1.0),
    SIZE_500G(label = "500g", priceMultiplier = 1.85),
    SIZE_1KG(label = "1kg", priceMultiplier = 3.4)
}

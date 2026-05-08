package com.cafeteros.historia.ui.features.productdetail.model

/**
 * Cada par etiqueta/valor de la grilla de specs bajo la tab "Descripción".
 *
 * @property label etiqueta uppercase ("VARIEDAD").
 * @property value valor mostrado debajo ("Caturra & Castillo").
 */
data class ProductSpec(
    val label: String,
    val value: String
)

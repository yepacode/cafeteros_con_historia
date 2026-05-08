package com.cafeteros.historia.ui.features.productdetail.model

/**
 * Tabs visibles bajo el stepper de cantidad ("Descripción / Notas de cata /
 * Proceso").
 *
 * Cada tab define solo su [label] visible — el contenido se resuelve dentro
 * de [com.cafeteros.historia.ui.features.productdetail.components.ProductDetailTabContent].
 */
enum class ProductDetailTab(val label: String) {
    DESCRIPCION(label = "Descripción"),
    NOTAS_CATA(label = "Notas de cata"),
    PROCESO(label = "Proceso")
}

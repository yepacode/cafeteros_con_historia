package com.cafeteros.historia.ui.features.farmer_products.model

/**
 * Pasos del wizard de creación/edición de producto.
 *
 * Diseño de Stitch original tenía 6 pasos; aquí los compactamos a 3 para
 * reducir fricción del caficultor sin perder secciones lógicas:
 *
 *  - [Basics]: nombre, categoría, descripciones, variedades, notas de cata.
 *  - [Pricing]: peso, formato, precio, inventario, certificaciones.
 *  - [Preview]: revisión final + botón Publicar.
 */
enum class ProductCreationStep(val number: Int, val title: String) {
    Basics(number = 1, title = "Detalles del producto"),
    Pricing(number = 2, title = "Precio e inventario"),
    Preview(number = 3, title = "Revisa y publica");

    companion object {
        const val TOTAL: Int = 3
    }
}

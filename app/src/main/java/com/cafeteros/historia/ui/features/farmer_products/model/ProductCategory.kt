package com.cafeteros.historia.ui.features.farmer_products.model

/**
 * Categorías que el caficultor puede asignar a un producto al crearlo.
 * Los nombres se muestran tal cual al usuario en el dropdown.
 */
enum class ProductCategory(val label: String) {
    GREEN_BEAN(label = "Café verde"),
    ROASTED_BEAN(label = "Café en grano"),
    GROUND(label = "Café molido"),
    CAPSULES(label = "Cápsulas"),
    HONEY(label = "Miel de café"),
    OTHER(label = "Otro");

    companion object {
        val DEFAULT: ProductCategory = ROASTED_BEAN
    }
}

/** Formato físico que define cómo se entrega el café al comprador. */
enum class CoffeeFormat(val label: String) {
    WHOLE_BEAN(label = "En grano"),
    GROUND_MEDIUM(label = "Molido medio");

    companion object {
        val DEFAULT: CoffeeFormat = WHOLE_BEAN
    }
}

/** Pesos disponibles como chips en el paso de precios. */
val DEFAULT_WEIGHT_OPTIONS_GRAMS: List<Int> = listOf(250, 500, 1000)

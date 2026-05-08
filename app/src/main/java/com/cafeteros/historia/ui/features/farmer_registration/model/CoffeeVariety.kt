package com.cafeteros.historia.ui.features.farmer_registration.model

/**
 * Variedades de café cultivadas en Colombia disponibles en el selector
 * "Variedades que cultivas" del paso 2.
 *
 * Es multi-select: un caficultor puede tener varias variedades en su finca.
 * El [label] se muestra en los chips; el `name` del enum se persiste en DB
 * (fácil de mapear en el futuro a una tabla `coffee_varieties`).
 */
enum class CoffeeVariety(val label: String) {
    CATURRA("Caturra"),
    CASTILLO("Castillo"),
    COLOMBIA("Colombia"),
    TIPICA("Típica"),
    BOURBON("Bourbon"),
    GEISHA("Geisha"),
    TABI("Tabi"),
    CENICAFE_1("Cenicafé 1"),
    PACHE("Pache"),
    OTRA("Otra")
}

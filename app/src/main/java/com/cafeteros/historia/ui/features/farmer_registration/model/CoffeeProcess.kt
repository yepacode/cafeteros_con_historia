package com.cafeteros.historia.ui.features.farmer_registration.model

/**
 * Procesos de beneficio del café que el caficultor maneja en su finca.
 *
 * Multi-select: una finca puede ofrecer varios procesos a la vez.
 */
enum class CoffeeProcess(val label: String) {
    LAVADO("Lavado"),
    HONEY("Honey"),
    NATURAL("Natural"),
    ANAEROBICO("Anaeróbico"),
    FERMENTADO("Fermentado")
}

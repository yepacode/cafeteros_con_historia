package com.cafeteros.historia.ui.features.farmer_registration.model

/**
 * Método de cosecha del café. Single-select: en una finca puede haber un
 * método principal aunque ocasionalmente se combine.
 */
enum class HarvestMethod(val label: String) {
    MANUAL_SELECTIVA("Manual selectiva"),
    MANUAL_NO_SELECTIVA("Manual no selectiva"),
    MECANICA("Mecánica")
}

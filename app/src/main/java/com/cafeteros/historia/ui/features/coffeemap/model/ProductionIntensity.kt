package com.cafeteros.historia.ui.features.coffeemap.model

/**
 * Nivel de producción cafetera de una zona.
 *
 * Mapea al degradado "PRODUCCIÓN" del top-right de la pantalla:
 *  - [BAJA]  → claro
 *  - [MEDIA] → tono intermedio
 *  - [ALTA]  → oscuro
 *
 * El [weight] (0.0 – 1.0) se usa para interpolar el color del marcador o
 * para ordenar zonas por intensidad cuando se filtra el listado.
 */
enum class ProductionIntensity(val weight: Float) {
    BAJA(0.25f),
    MEDIA(0.6f),
    ALTA(1.0f)
}

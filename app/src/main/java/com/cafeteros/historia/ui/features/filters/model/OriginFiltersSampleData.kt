package com.cafeteros.historia.ui.features.filters.model

import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Data de muestra mientras no exista backend para los filtros.
 *
 * Cuando exista, las zonas y sus conteos vendrán del endpoint
 * `/zones/summary` (o equivalente), pero el contrato de la pantalla
 * permanece estable: una lista de [FilterZone] con su color de marca.
 */
object OriginFiltersSampleData {

    val zones: List<FilterZone> = listOf(
        FilterZone(name = "Santander", count = 38, accentColor = BrandColors.ZoneSantander),
        FilterZone(name = "Cundinamarca", count = 14, accentColor = BrandColors.ZoneCundinamarca),
        FilterZone(name = "Huila", count = 52, accentColor = BrandColors.ZoneHuila),
        FilterZone(name = "Nariño", count = 29, accentColor = BrandColors.ZoneEjeCafetero),
        FilterZone(name = "Eje Cafetero", count = 64, accentColor = BrandColors.ZoneEjeCafetero),
        FilterZone(name = "Sierra Nevada", count = 12, accentColor = BrandColors.ZoneSierraNevada)
    )

    /** Resultado por defecto del CTA "Ver X resultados". */
    const val INITIAL_RESULTS_COUNT: Int = 47
}

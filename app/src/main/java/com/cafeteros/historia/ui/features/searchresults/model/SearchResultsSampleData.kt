package com.cafeteros.historia.ui.features.searchresults.model

import com.cafeteros.historia.R
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Data de muestra mientras no exista backend.
 *
 * Cuando exista, los resultados vendrán de `searchProducts(query, filters,
 * roleId)` y los chips activos se hidratarán desde el `OriginFilters` que
 * el usuario aplicó.
 */
object SearchResultsSampleData {

    val activeFilters: List<SearchActiveFilter> = listOf(
        SearchActiveFilter(id = "zone", label = "Huila", accentColor = BrandColors.ZoneHuila),
        SearchActiveFilter(id = "cert", label = "Orgánico"),
        SearchActiveFilter(id = "price", label = "\$30k-\$80k")
    )

    val products: List<SearchResultProduct> = listOf(
        SearchResultProduct(
            id = "p1",
            name = "Café Bourbon Rosado",
            farmLabel = "FINCA LA ESPERANZA",
            zoneLabel = "HUILA",
            rating = 4.9,
            reviewCount = 24,
            formattedPrice = "\$54,000",
            imageRes = R.drawable.cafe_origen_narino
        ),
        SearchResultProduct(
            id = "p2",
            name = "Origen Ancestral",
            farmLabel = "DON PEDRO JIMÉNEZ",
            zoneLabel = "HUILA",
            rating = 4.8,
            reviewCount = 18,
            formattedPrice = "\$42,500",
            imageRes = R.drawable.finca_la_esperanza
        ),
        SearchResultProduct(
            id = "p3",
            name = "Reserva Especial Caturra",
            farmLabel = "FINCA SAN RAFAEL",
            zoneLabel = "HUILA",
            rating = 5.0,
            reviewCount = 32,
            formattedPrice = "\$68,000",
            imageRes = R.drawable.imag_2
        ),
        SearchResultProduct(
            id = "p4",
            name = "Cosecha de Origen",
            farmLabel = "ASOC. PRODUCTORES PITALITO",
            zoneLabel = "HUILA",
            rating = 4.7,
            reviewCount = 56,
            formattedPrice = "\$35,900",
            imageRes = R.drawable.cafe_sierra_nevada_premium
        )
    )

    /** Total que se muestra en el contador del toolbar ("47 resultados"). */
    const val TOTAL_RESULTS: Int = 47
}

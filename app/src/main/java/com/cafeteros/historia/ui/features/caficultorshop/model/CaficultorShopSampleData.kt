package com.cafeteros.historia.ui.features.caficultorshop.model

/**
 * Data de muestra mientras no exista backend.
 *
 * Cuando exista, los filtros aplicados se hidrartán desde el `OriginFilters`
 * vigente y las sugerencias populares vendrán del endpoint
 * `/suggestions/popular` (o equivalente).
 */
object CaficultorShopSampleData {

    val activeFilters: List<CaficultorShopFilter> = listOf(
        CaficultorShopFilter(id = "origen-huila", label = "Origen: Huila"),
        CaficultorShopFilter(id = "tueste-medio", label = "Tueste: Medio")
    )

    val popularSuggestions: List<PopularSuggestion> = listOf(
        PopularSuggestion(
            id = "sierra-nevada",
            title = "Sierra Nevada",
            actionLabel = "VER COLECCIÓN"
        ),
        PopularSuggestion(
            id = "cata-frutas",
            title = "Cata de Frutas",
            actionLabel = "EXPLORAR NOTAS"
        )
    )
}

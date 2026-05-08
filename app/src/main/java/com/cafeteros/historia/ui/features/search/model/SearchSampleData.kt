package com.cafeteros.historia.ui.features.search.model

import com.cafeteros.historia.R

/**
 * Data de muestra de la pantalla de búsqueda mientras no exista backend.
 *
 * Cuando el backend exista, los recientes vendrán del histórico del usuario
 * y las tendencias del endpoint `/search/trending` (o equivalente).
 */
object SearchSampleData {

    val recentSearches: List<RecentSearch> = listOf(
        RecentSearch(id = "r1", query = "Café de especialidad Huila"),
        RecentSearch(id = "r2", query = "Don Alberto Nariño"),
        RecentSearch(id = "r3", query = "Proceso Honey"),
        RecentSearch(id = "r4", query = "Sierra Nevada")
    )

    val trendingSearches: List<TrendingSearch> = listOf(
        TrendingSearch(label = "Microlotes Huila"),
        TrendingSearch(label = "Café premiado"),
        TrendingSearch(label = "Tueste oscuro"),
        TrendingSearch(label = "Geisha"),
        TrendingSearch(label = "Nariño")
    )

    val featuredCaficultores: List<FeaturedSearchCaficultor> = listOf(
        FeaturedSearchCaficultor(displayName = "Don Alberto", avatarRes = R.drawable.ima_1),
        FeaturedSearchCaficultor(displayName = "Doña Elena", avatarRes = R.drawable.imag_2),
        FeaturedSearchCaficultor(displayName = "Mateo V.", avatarRes = R.drawable.ima_1),
        FeaturedSearchCaficultor(displayName = "Clarissa", avatarRes = R.drawable.imag_2)
    )
}

package com.cafeteros.historia.ui.features.search.model

/**
 * Una entrada de la lista "Búsquedas recientes".
 *
 * @property id identificador estable usado como key de la lista — necesario
 *   para que las animaciones de borrado individuales sigan al item correcto.
 * @property query texto literal que se muestra en la fila.
 */
data class RecentSearch(
    val id: String,
    val query: String
)

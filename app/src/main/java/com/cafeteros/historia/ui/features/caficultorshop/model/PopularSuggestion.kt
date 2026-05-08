package com.cafeteros.historia.ui.features.caficultorshop.model

/**
 * Card de la fila "SUGERENCIAS POPULARES" del empty state.
 *
 * Cada card tiene un título serif itálico (ej. "Sierra Nevada") y una
 * acción uppercase debajo (ej. "VER COLECCIÓN") que lleva a la pantalla
 * destino correspondiente.
 *
 * @property id identificador estable.
 * @property title título serif italic.
 * @property actionLabel texto uppercase de la acción.
 */
data class PopularSuggestion(
    val id: String,
    val title: String,
    val actionLabel: String
)

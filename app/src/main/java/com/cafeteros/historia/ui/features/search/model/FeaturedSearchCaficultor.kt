package com.cafeteros.historia.ui.features.search.model

import androidx.annotation.DrawableRes

/**
 * Caficultor destacado mostrado como avatar circular en la fila inferior
 * de la pantalla de búsqueda.
 *
 * @property displayName nombre corto bajo el avatar ("Don Alberto").
 * @property avatarRes drawable del avatar.
 */
data class FeaturedSearchCaficultor(
    val displayName: String,
    @param:DrawableRes val avatarRes: Int
)

package com.cafeteros.historia.ui.features.caficultordetail.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Reseña que un comprador dejó sobre el café del caficultor.
 *
 * El campo [rating] es entero porque las estrellas se renderizan llenas
 * (sin medias estrellas) en este nivel; si más adelante se necesitan
 * medias estrellas, se puede pasar a Float sin romper consumidores.
 *
 * @property authorName nombre que firma la reseña ("Camila V.").
 * @property rating número de estrellas llenas (0–5).
 * @property body texto libre de la reseña.
 * @property avatarRes drawable opcional con la foto del autor.
 * @property avatarPlaceholderColor color del círculo cuando no hay foto.
 */
data class CaficultorReview(
    val authorName: String,
    val rating: Int,
    val body: String,
    @param:DrawableRes val avatarRes: Int? = null,
    val avatarPlaceholderColor: Color
)

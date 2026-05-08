package com.cafeteros.historia.ui.features.explore.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Caficultor destacado mostrado en el carrusel "Caficultores destacados".
 *
 * El [avatarRes] es opcional para permitir maquetar la pantalla sin assets
 * reales, cayendo a un círculo de [avatarPlaceholderColor].
 *
 * @property farmName nombre de la finca ("Finca La Esperanza").
 * @property location ubicación legible ("Huila • Pitalito").
 * @property rating valor del rating (0.0 – 5.0).
 * @property reviewCount número total de reseñas.
 * @property badge distintivo a mostrar en la parte inferior.
 * @property avatarRes drawable opcional con la foto del caficultor.
 * @property avatarPlaceholderColor color de fondo del avatar sin imagen.
 */
data class FeaturedCaficultor(
    val farmName: String,
    val location: String,
    val rating: Double,
    val reviewCount: Int,
    val badge: CaficultorBadge,
    @param:DrawableRes val avatarRes: Int? = null,
    val avatarPlaceholderColor: Color
)

package com.example.cafeteros.ui.features.onboarding.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Representa el contenido de una página individual del onboarding.
 *
 * El campo [imageRes] es opcional: si se provee, el componente de imagen
 * cargará el drawable real con `painterResource(imageRes)`. Si es `null`, se
 * mostrará un placeholder visual con [placeholderColor] como fondo y
 * [placeholderIcon] centrado, lo cual permite trabajar la UI completa sin
 * disponer aún de las imágenes finales.
 *
 * @property title texto del título grande de la página.
 * @property description texto descriptivo bajo el título.
 * @property imageRes id del drawable a usar como hero image. Cuando esté
 *  disponible la imagen real, basta con dejar el archivo en
 *  `app/src/main/res/drawable/` y pasar aquí `R.drawable.nombre_archivo`.
 * @property placeholderIcon ícono Material a renderizar cuando no hay imagen.
 * @property placeholderColor color de fondo del placeholder.
 * @property contentDescription descripción accesible de la imagen para lectores
 *  de pantalla. Si la imagen es decorativa, puede ser `null`.
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    @param:DrawableRes val imageRes: Int? = null,
    val placeholderIcon: ImageVector,
    val placeholderColor: Color,
    val contentDescription: String? = null
)

package com.cafeteros.historia.ui.features.farmer_onboarding.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Datos de una página individual del onboarding del caficultor.
 *
 * Es similar a [com.cafeteros.historia.ui.features.onboarding.model.OnboardingPage]
 * pero introduce el campo opcional [statsBadgeText]: cuando una página lo
 * trae, se renderiza la insignia de prueba social bajo la descripción.
 *
 * @property title título principal de la página.
 * @property description descripción bajo el título.
 * @property imageRes id del drawable real (opcional). Si es null se usa el
 *  placeholder de [placeholderIcon] + [placeholderColor].
 * @property placeholderIcon ícono Material para el placeholder.
 * @property placeholderColor color de fondo del placeholder.
 * @property contentDescription descripción accesible de la imagen para
 *  lectores de pantalla; null si es decorativa.
 * @property statsBadgeText texto opcional a renderizar en la insignia
 *  ([com.cafeteros.historia.ui.features.farmer_onboarding.components.FarmerStatsBadge]).
 *  null = no mostrar insignia.
 */
data class FarmerOnboardingPage(
    val title: String,
    val description: String,
    @param:DrawableRes val imageRes: Int? = null,
    val placeholderIcon: ImageVector,
    val placeholderColor: Color,
    val contentDescription: String? = null,
    val statsBadgeText: String? = null
)

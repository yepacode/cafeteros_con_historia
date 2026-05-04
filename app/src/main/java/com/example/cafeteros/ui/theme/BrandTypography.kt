package com.example.cafeteros.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Tokens tipográficos de la marca "Origen".
 *
 * Se usa [FontFamily.Serif] (fuente serif del sistema) para los títulos de
 * marca y [FontFamily.SansSerif] para textos de UI/lectura. Cuando exista
 * una fuente custom, basta con reemplazar la familia aquí.
 */
object BrandTypography {

    // ── Splash ──────────────────────────────────────────────────────────────

    /** Estilo del nombre de la marca ("Origen"). Tamaño grande, serif elegante. */
    val BrandTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 48.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.CreamWhite
    )

    /**
     * Estilo del tagline ("CAFÉ Y REPOSTERÍA"). Tamaño pequeño, letter-spacing
     * amplio para refuerzo visual de marca; opacidad reducida para jerarquía.
     */
    val BrandTagline: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 6.sp,
        color = BrandColors.CreamWhiteMuted
    )

    // ── Onboarding ──────────────────────────────────────────────────────────

    /** Título destacado de cada página del onboarding (sobre tarjeta blanca). */
    val OnboardingTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 30.sp,
        color = BrandColors.TextPrimary
    )

    /** Texto descriptivo bajo el título de cada página del onboarding. */
    val OnboardingDescription: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        color = BrandColors.TextSecondary
    )

    /** Texto del botón primario (Siguiente / Comenzar). */
    val PrimaryButtonLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.PrimaryButtonText
    )

    /** Estilo del botón "Saltar" sobre la imagen del onboarding. */
    val SkipButtonLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.CardBackground
    )

    /** Estilo del enlace secundario "Ya tengo cuenta" en la última página. */
    val SecondaryLinkLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.CoffeeBrown
    )
}

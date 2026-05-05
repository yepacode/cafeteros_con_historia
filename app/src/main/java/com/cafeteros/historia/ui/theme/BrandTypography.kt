package com.cafeteros.historia.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
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

    // ── Login / autenticación ───────────────────────────────────────────────

    /** Wordmark "Origen" sobre la pantalla de login. Serif itálico, escala media. */
    val Wordmark: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 32.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Título "Bienvenido de vuelta". Serif bold, jerarquía principal. */
    val LoginTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo bajo el título de login. */
    val LoginSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Etiqueta "CORREO ELECTRÓNICO" / "CONTRASEÑA". Mayúsculas, letter-spacing. */
    val FieldLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextPrimary
    )

    /** Texto del usuario y placeholder de los inputs del login. */
    val FieldText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Enlace "¿Olvidaste tu contraseña?". Verde alineado a la derecha. */
    val ForgotPasswordLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.LinkGreen
    )

    /** Texto del divider "O CONTINÚA CON". Serif itálico, sutil. */
    val DividerLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        color = BrandColors.TextSecondary
    )

    /** Etiqueta de los botones sociales (Google, Huella). */
    val SocialButtonLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Texto neutro del footer "¿No tienes cuenta?". */
    val RegisterFooterText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Acción "Regístrate" del footer. Bold + verde de marca. */
    val RegisterFooterAction: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.LinkGreen
    )

    // ── Registro: cards seleccionables, helpers, consentimiento ─────────────

    /** Título centrado del header de Register ("Crear cuenta"). Serif. */
    val ScreenTitleCentered: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo bajo el título centrado ("Únete a la comunidad..."). */
    val ScreenSubtitleCentered: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Título de las cards seleccionables ("Soy Comprador"). Serif bold. */
    val SelectableCardTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo de las cards seleccionables ("Quiero descubrir café"). */
    val SelectableCardSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Texto de ayuda bajo un campo (ej. requisitos de contraseña). */
    val FieldHelper: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Prefijo bold dentro de un input (ej. "+57" en teléfono). */
    val FieldPrefix: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Texto regular del label de un checkbox de consentimiento. */
    val ConsentText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        color = BrandColors.TextPrimary
    )

    /** Enlace embebido en un consentimiento (ej. "Términos y Condiciones"). */
    val ConsentLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        color = BrandColors.LinkGreen,
        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
    )
}

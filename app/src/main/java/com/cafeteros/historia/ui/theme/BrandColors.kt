package com.cafeteros.historia.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores de la marca "Origen".
 *
 * Centraliza los tokens de color para evitar valores mágicos repartidos en
 * los composables. Cualquier nuevo color de marca debe añadirse aquí y
 * referenciarse desde los composables o desde [CafeterosTheme].
 */
object BrandColors {

    // ── Colores de marca primarios ──────────────────────────────────────────

    /** Café oscuro tipo chocolate amargo. Fondo principal del splash. */
    val CoffeeBrown: Color = Color(0xFF3E2723)

    /** Crema cálido. Texto principal e ícono decorativo del splash. */
    val CreamWhite: Color = Color(0xFFE8DCC4)

    /**
     * Variante atenuada del crema (alpha 70%). Usado en el tagline para crear
     * jerarquía visual sin introducir un color nuevo.
     */
    val CreamWhiteMuted: Color = CreamWhite.copy(alpha = 0.7f)

    // ── Tokens para superficies tipo "card" (onboarding) ────────────────────

    /** Fondo de las tarjetas blancas del onboarding. */
    val CardBackground: Color = Color(0xFFFFFFFF)

    /** Color del texto de títulos sobre tarjetas claras. */
    val TextPrimary: Color = Color(0xFF1A1A1A)

    /** Color del texto descriptivo (menos contraste que [TextPrimary]). */
    val TextSecondary: Color = Color(0xFF6B6B6B)

    // ── Tokens para botones e indicadores ───────────────────────────────────

    /** Color de fondo del botón primario (Siguiente / Comenzar). */
    val PrimaryButton: Color = CoffeeBrown

    /** Color del texto del botón primario. */
    val PrimaryButtonText: Color = Color(0xFFFFFFFF)

    /** Color de los puntos activos del page indicator. */
    val IndicatorActive: Color = CoffeeBrown

    /** Color de los puntos inactivos del page indicator. */
    val IndicatorInactive: Color = Color(0xFFD9D2C5)

    /**
     * Fondo semi-transparente para el botón "Saltar" sobre la imagen.
     * Garantiza legibilidad sin ocultar la imagen de fondo.
     */
    val SkipButtonScrim: Color = Color(0x66000000)

    // ── Tokens del flujo de autenticación (login) ───────────────────────────

    /** Fondo crema de la pantalla de login, ligeramente cálido. */
    val AuthBackground: Color = Color(0xFFF7F1E5)

    /** Fondo gris-beige de los inputs (email, contraseña). */
    val InputBackground: Color = Color(0xFFE8E2D5)

    /** Color del placeholder y de los íconos leading dentro de los inputs. */
    val InputHint: Color = Color(0xFFA89F8E)

    /** Verde oscuro para enlaces secundarios ("Olvidaste contraseña", "Regístrate"). */
    val LinkGreen: Color = Color(0xFF1B5E20)

    /** Borde sutil de los botones sociales (Google, Huella). */
    val SocialButtonBorder: Color = Color(0xFFE5DFD2)

    /** Línea horizontal del divider "O CONTINÚA CON". */
    val DividerLine: Color = Color(0xFFD9D2C5)

    // ── Tokens de cards seleccionables (selector "Soy Comprador / Caficultor") ─

    /** Fondo de la card cuando está seleccionada (blanco puro). */
    val SelectableCardSelectedBackground: Color = CardBackground

    /** Borde de la card seleccionada (café oscuro de marca, ~2dp). */
    val SelectableCardSelectedBorder: Color = CoffeeBrown

    /** Fondo de la card no seleccionada (gris-beige sutil). */
    val SelectableCardUnselectedBackground: Color = InputBackground

    /** Borde de la card no seleccionada (transparente: solo fondo). */
    val SelectableCardUnselectedBorder: Color = Color.Transparent

    // ── Tokens de checkboxes de consentimiento ──────────────────────────────

    /** Borde del checkbox cuando está sin marcar. */
    val CheckboxBorder: Color = Color(0xFFB8AE99)

    /** Color del checkbox marcado y de su tick. */
    val CheckboxChecked: Color = CoffeeBrown

    // ── Tokens del flujo de onboarding del Caficultor ───────────────────────

    /**
     * Verde oscuro de marca caficultor (botones "Siguiente" / "Empezar mi
     * registro"). Tono inspirado en el follaje de las plantaciones.
     */
    val FarmerPrimary: Color = Color(0xFF1F4D38)

    /** Color de los puntos activos del indicador en el flujo caficultor. */
    val FarmerIndicatorActive: Color = FarmerPrimary

    /** Fondo de la insignia "+180 caficultores" — crema cálido sutil. */
    val FarmerStatsBadgeBackground: Color = Color(0xFFF3EBD8)

    /** Color del texto de la insignia "+180 caficultores". */
    val FarmerStatsBadgeText: Color = Color(0xFF3E2723)

    // ── Tokens del banner informativo amarillo (paso 2 caficultor) ───────────

    /** Fondo crema-amarillento del banner informativo. */
    val InfoBannerBackground: Color = Color(0xFFFFF6DD)

    /** Borde lateral izquierdo del banner (acento). */
    val InfoBannerAccent: Color = Color(0xFFC4A14A)

    /** Color del ícono y del CTA en el banner. */
    val InfoBannerAction: Color = Color(0xFF8C6E1F)

    // ── Tokens del banner verde de confianza (paso 4 caficultor) ────────────

    /** Fondo verde-pastel del banner de "encriptación". */
    val TrustBannerBackground: Color = Color(0xFFE8F4EC)

    /** Color del ícono e iconografía del banner verde. */
    val TrustBannerAccent: Color = FarmerPrimary

    // ── Tokens del badge "No aplica" (Cámara de Comercio) ──────────────────

    /** Fondo del chip "No aplica" cuando está activo. */
    val NotApplicableChipBackground: Color = Color(0xFFF0EDE5)

    /** Texto del chip "No aplica". */
    val NotApplicableChipText: Color = TextSecondary

    // ── Tokens del paso 5: timeline de verificación ─────────────────────────

    /** Color de un paso completado del timeline (check verde). */
    val StatusDone: Color = FarmerPrimary

    /** Color de un paso en curso (naranja-mostaza). */
    val StatusInProgress: Color = Color(0xFFD9A03C)

    /** Color de un paso pendiente (gris). */
    val StatusPending: Color = IndicatorInactive

    /** Fondo del chip "EN REVISIÓN" del hero (crema cálido). */
    val ReviewBadgeBackground: Color = FarmerStatsBadgeBackground

    /** Fondo de la card hero del paso 5 (blanco con sombra suave). */
    val HeroCardBackground: Color = CardBackground
}

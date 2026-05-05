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
}

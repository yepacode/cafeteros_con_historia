package com.example.cafeteros.ui.theme

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
}

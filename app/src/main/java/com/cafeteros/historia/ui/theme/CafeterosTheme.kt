package com.cafeteros.historia.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Theme wrapper de la app Cafeteros.
 *
 * Envuelve el contenido en [MaterialTheme] con los defaults para que los
 * composables que usen tipografía o colores del sistema reciban valores sanos
 * (incluso aunque el splash use directamente los tokens de [BrandColors]).
 *
 * @param darkTheme detecta automáticamente el modo oscuro del sistema; puede
 *  forzarse desde fuera (por ejemplo, en un `@Preview`) para validar ambos modos.
 * @param content composables a renderizar dentro del theme.
 */
@Composable
fun CafeterosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        androidx.compose.material3.darkColorScheme()
    } else {
        androidx.compose.material3.lightColorScheme()
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

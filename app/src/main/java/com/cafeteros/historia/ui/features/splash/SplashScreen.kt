package com.cafeteros.historia.ui.features.splash

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.splash.components.BrandLogo
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.delay

/**
 * Constantes textuales del splash de la marca "Origen".
 *
 * Centralizadas en un companion-style object privado para evitar magic strings
 * dispersos. Si en el futuro se internacionaliza la app, estos valores deben
 * migrarse a `strings.xml` y leerse con `stringResource`.
 */
private object SplashContent {
    const val BRAND_NAME: String = "Origen"
    const val TAGLINE: String = "CAFÉ Y REPOSTERÍA"
}

/** Duración por defecto del splash antes de invocar [onTimeout], en milisegundos. */
const val DEFAULT_SPLASH_DURATION_MS: Long = 2000L

/**
 * Pantalla de splash de la app Cafeteros.
 *
 * Muestra el logo de marca centrado sobre un fondo café oscuro. Si se
 * proporciona [onTimeout], dispara automáticamente la navegación tras
 * [durationMillis]. Cuando [onTimeout] es `null` (caso típico de previews),
 * el composable se queda renderizado indefinidamente sin efectos colaterales.
 *
 * @param modifier modifier opcional aplicado al contenedor raíz.
 * @param onTimeout callback invocado una vez tras [durationMillis]. Si es
 *  `null`, no se programa ningún efecto.
 * @param durationMillis tiempo a esperar antes de invocar [onTimeout].
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onTimeout: (() -> Unit)? = null,
    durationMillis: Long = DEFAULT_SPLASH_DURATION_MS
) {
    if (onTimeout != null) {
        LaunchedEffect(Unit) {
            delay(durationMillis)
            onTimeout()
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.CoffeeBrown),
        contentAlignment = Alignment.Center
    ) {
        BrandLogo(
            brandName = SplashContent.BRAND_NAME,
            taglineText = SplashContent.TAGLINE
        )
    }
}

/** Preview en modo claro del sistema. */
@Preview(name = "Splash – Light", showBackground = true)
@Composable
private fun SplashScreenPreviewLight() {
    CafeterosTheme(darkTheme = false) {
        SplashScreen()
    }
}

/** Preview en modo oscuro del sistema. */
@Preview(
    name = "Splash – Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SplashScreenPreviewDark() {
    CafeterosTheme(darkTheme = true) {
        SplashScreen()
    }
}

package com.cafeteros.historia.ui.features.splash

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
 * Si [onLogoLongPress] está presente, hacer **long-press** sobre el logo abre
 * la pantalla de debug navigation (gesto oculto para desarrollo).
 *
 * @param modifier modifier opcional aplicado al contenedor raíz.
 * @param onTimeout callback invocado una vez tras [durationMillis]. Si es
 *  `null`, no se programa ningún efecto.
 * @param onLogoLongPress callback al hacer long-press sobre el logo. Útil
 *  para abrir un menú de debug en builds de desarrollo.
 * @param durationMillis tiempo a esperar antes de invocar [onTimeout].
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onTimeout: (() -> Unit)? = null,
    onLogoLongPress: (() -> Unit)? = null,
    durationMillis: Long = DEFAULT_SPLASH_DURATION_MS
) {
    if (onTimeout != null) {
        LaunchedEffect(Unit) {
            delay(durationMillis)
            onTimeout()
        }
    }
    val interactionSource = remember { MutableInteractionSource() }
    val logoModifier = if (onLogoLongPress != null) {
        Modifier.combinedClickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = { /* nada en click corto */ },
            onLongClick = onLogoLongPress
        )
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.CoffeeBrown),
        contentAlignment = Alignment.Center
    ) {
        BrandLogo(
            modifier = logoModifier,
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

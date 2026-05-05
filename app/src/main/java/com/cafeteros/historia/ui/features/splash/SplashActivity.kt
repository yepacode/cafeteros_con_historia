package com.cafeteros.historia.ui.features.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.onboarding.OnboardingActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity de entrada que muestra el splash de marca antes de redirigir al
 * flujo de onboarding.
 *
 * Esta Activity es el `LAUNCHER` de la app (declarado en `AndroidManifest.xml`).
 * Se queda visible durante [DEFAULT_SPLASH_DURATION_MS] y luego lanza
 * [OnboardingActivity] cerrándose a sí misma para no dejar rastro en el back stack.
 *
 * Cuando se implemente la persistencia de "primer arranque" (DataStore),
 * esta Activity deberá saltarse el onboarding e ir directo al destino
 * correspondiente (login o home) si el usuario ya completó el flujo.
 */
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                SplashScreen(onTimeout = ::navigateToOnboarding)
            }
        }
    }

    /** Lanza [OnboardingActivity] y termina esta Activity. */
    private fun navigateToOnboarding() {
        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
    }
}

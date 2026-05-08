package com.cafeteros.historia.ui.features.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.ui.features.debug.DebugNavigationActivity
import com.cafeteros.historia.ui.features.onboarding.OnboardingActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.launch

/**
 * Activity de entrada (LAUNCHER) que muestra el splash de marca y luego
 * dirige al destino correspondiente según el estado de sesión:
 *
 *  - **Hay sesión activa** → [MainActivity] (Home), saltándose Onboarding y Login.
 *  - **Sin sesión** → [OnboardingActivity] (flujo de bienvenida).
 *
 * El splash visual se mantiene siempre por al menos [DEFAULT_SPLASH_DURATION_MS]
 * para que la marca tenga su momento, aunque la consulta de sesión sea
 * instantánea.
 *
 * **Gesto oculto de desarrollo**: hacer *long-press* sobre el logo "Origen"
 * abre [DebugNavigationActivity], una pantalla con accesos directos a todas
 * las Activities de la app (útil para QA/desarrollo). Este atajo se elimina
 * antes de publicar a producción.
 */
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                SplashScreen(
                    onTimeout = ::routeAfterSplash,
                    onLogoLongPress = ::openDebugNavigation
                )
            }
        }
    }

    /**
     * Decide a qué pantalla saltar según haya o no sesión persistida.
     */
    private fun routeAfterSplash() {
        val repository = (application as CafeterosApplication).userRepository
        lifecycleScope.launch {
            val currentUser = repository.getCurrentUser()
            val nextActivity = if (currentUser != null) {
                MainActivity::class.java
            } else {
                OnboardingActivity::class.java
            }
            startActivity(Intent(this@SplashActivity, nextActivity))
            finish()
        }
    }

    /** Abre el menú de debug sin terminar el splash. */
    private fun openDebugNavigation() {
        startActivity(Intent(this, DebugNavigationActivity::class.java))
    }
}

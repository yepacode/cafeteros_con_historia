package com.cafeteros.historia.ui.features.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.auth.BiometricAuthenticator
import com.cafeteros.historia.auth.BiometricResult
import com.cafeteros.historia.ui.features.auth.LoginActivity
import com.cafeteros.historia.ui.features.onboarding.OnboardingActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

/**
 * Activity de entrada que muestra el splash de marca y luego decide a dónde
 * navegar según tres estados de la app:
 *
 *  1. **Sesión Firebase activa + biometría habilitada** → muestra el
 *     [androidx.biometric.BiometricPrompt]. Si la huella pasa, va a
 *     [MainActivity]. Si el usuario cancela, lo dejamos en [LoginActivity]
 *     (donde podrá ingresar manualmente o salir).
 *
 *  2. **Sesión Firebase activa sin biometría** (caso raro: el usuario no
 *     habilitó biometría aún) → entra directo a [MainActivity].
 *
 *  3. **Sin sesión activa** → va a [OnboardingActivity] la primera vez, o
 *     [LoginActivity] si ya hubo un login previo (detectado por la presencia
 *     de `last_email` en DataStore).
 *
 * Extiende [FragmentActivity] porque [androidx.biometric.BiometricPrompt]
 * requiere un FragmentActivity para mostrarse correctamente.
 */
class SplashActivity : FragmentActivity() {

    private lateinit var biometricAuthenticator: BiometricAuthenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        biometricAuthenticator = BiometricAuthenticator(this)
        setContent {
            CafeterosTheme {
                SplashScreen(onTimeout = ::decideNextDestination)
            }
        }
    }

    /**
     * Lee el estado de sesión + preferencias locales y enruta en consecuencia.
     * Se invoca al terminar el tiempo del splash.
     */
    private fun decideNextDestination() {
        lifecycleScope.launch {
            val hasFirebaseSession = FirebaseAuth.getInstance().currentUser != null
            val sessionPrefs = (application as CafeterosApplication).sessionPreferences
            val biometricEnabled = sessionPrefs.isBiometricEnabled()
            val lastEmail = sessionPrefs.lastEmail()

            when {
                hasFirebaseSession && biometricEnabled -> promptBiometric()
                hasFirebaseSession -> goTo(MainActivity::class.java)
                !lastEmail.isNullOrBlank() -> goTo(LoginActivity::class.java)
                else -> goTo(OnboardingActivity::class.java)
            }
        }
    }

    /**
     * Muestra el [androidx.biometric.BiometricPrompt]. Si pasa, entra al
     * dashboard; si el usuario cancela, lo enviamos al Login (donde puede
     * decidir si escribir contraseña o salir).
     */
    private fun promptBiometric() {
        val availability = biometricAuthenticator.canAuthenticate()
        if (availability is BiometricResult.Unavailable) {
            // El dispositivo no puede autenticar biométricamente ahora
            // (sin huellas registradas o sensor inhabilitado). Caemos al
            // auto-login normal sin pedir nada extra.
            goTo(MainActivity::class.java)
            return
        }
        biometricAuthenticator.authenticate { result ->
            when (result) {
                is BiometricResult.Success -> goTo(MainActivity::class.java)
                is BiometricResult.Cancelled -> goTo(LoginActivity::class.java)
                is BiometricResult.Unavailable -> goTo(MainActivity::class.java)
                is BiometricResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    goTo(LoginActivity::class.java)
                }
            }
        }
    }

    /** Lanza la activity destino y termina esta. */
    private fun goTo(target: Class<*>) {
        startActivity(Intent(this, target))
        finish()
    }
}

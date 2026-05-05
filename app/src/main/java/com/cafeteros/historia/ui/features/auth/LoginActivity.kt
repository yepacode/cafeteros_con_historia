package com.cafeteros.historia.ui.features.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.auth.BiometricAuthenticator
import com.cafeteros.historia.auth.BiometricResult
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la pantalla de login.
 *
 * Extiende [FragmentActivity] (no [androidx.activity.ComponentActivity]) porque
 * `BiometricPrompt` requiere `FragmentActivity` para mostrarse correctamente.
 *
 * Toda la lógica de UI vive en [LoginScreen]; aquí solo se cablea la
 * navegación (al destino que corresponda) y se dispara el [BiometricAuthenticator].
 *
 * Como aún no existe backend, todos los caminos exitosos terminan en
 * [MainActivity] (home). Las acciones sin destino (Google, olvidar contraseña,
 * registro) muestran un Toast informativo.
 */
class LoginActivity : FragmentActivity() {

    private lateinit var biometricAuthenticator: BiometricAuthenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        biometricAuthenticator = BiometricAuthenticator(this)

        setContent {
            CafeterosTheme {
                LoginScreen(
                    onBack = ::finish,
                    onLogin = { _, _ -> navigateToHome() },
                    onForgotPassword = {
                        showToast("Recuperación de contraseña: próximamente")
                    },
                    onGoogleLogin = {
                        showToast("Login con Google: próximamente")
                    },
                    onFingerprintLogin = ::launchBiometric,
                    onRegister = ::navigateToRegister
                )
            }
        }
    }

    /** Lanza el [BiometricPrompt] real y enruta según el resultado. */
    private fun launchBiometric() {
        biometricAuthenticator.authenticate { result ->
            when (result) {
                is BiometricResult.Success -> navigateToHome()
                is BiometricResult.Cancelled -> Unit
                is BiometricResult.Unavailable -> showToast(result.reason)
                is BiometricResult.Error -> showToast(result.message)
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /** Abre la pantalla de Registro sin terminar la actividad de Login. */
    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

package com.cafeteros.historia.ui.features.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.auth.BiometricAuthenticator
import com.cafeteros.historia.auth.BiometricResult
import com.cafeteros.historia.ui.features.password_recovery.PasswordRecoveryActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la pantalla de Login.
 *
 * Extiende [FragmentActivity] (no [androidx.activity.ComponentActivity]) porque
 * `BiometricPrompt` requiere `FragmentActivity` para mostrarse correctamente.
 *
 * Conecta [LoginScreen] con [LoginViewModel]: pasa email/contraseña al
 * ViewModel, observa el estado y navega a Home cuando el login es exitoso.
 */
class LoginActivity : FragmentActivity() {

    private lateinit var biometricAuthenticator: BiometricAuthenticator
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        biometricAuthenticator = BiometricAuthenticator(this)

        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(state.errorMessage) {
                    state.errorMessage?.let { message ->
                        showToast(message)
                        viewModel.consumeError()
                    }
                }

                LaunchedEffect(state.loggedInSuccessfully) {
                    if (state.loggedInSuccessfully) navigateToHome()
                }

                LoginScreen(
                    onBack = ::finish,
                    onLogin = { email, password -> viewModel.login(email, password) },
                    onForgotPassword = ::navigateToPasswordRecovery,
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

    /** Abre el flujo de recuperación de contraseña sin cerrar Login. */
    private fun navigateToPasswordRecovery() {
        startActivity(Intent(this, PasswordRecoveryActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

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
 *
 * **Lógica del botón de huella:**
 *  - Solo se muestra si el ViewModel reporta `canUseBiometric=true`, lo cual
 *    sucede tras al menos un login previo exitoso con contraseña en este
 *    dispositivo (y antes de hacer logout).
 *  - Al pulsar huella, primero validamos que Firebase tenga sesión persistida
 *    (`currentUid != null`). Si sí, se lanza el `BiometricPrompt`; si la
 *    huella es correcta, se entra directo a Home. Si no hay sesión, se le
 *    avisa al usuario que escriba la contraseña.
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
                val prefs by viewModel.preferences.collectAsStateWithLifecycle()

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
                    initialEmail = prefs.lastEmail.orEmpty(),
                    showFingerprint = prefs.canUseBiometric,
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

    /**
     * Lanza el [androidx.biometric.BiometricPrompt]. Si hay credenciales
     * cifradas guardadas, la huella inicia sesión completa (lee email +
     * contraseña de la bóveda y autentica contra Firebase). Si no hay
     * credenciales (primer arranque o "olvidar dispositivo" previo), avisa
     * que primero debe iniciarse con contraseña.
     */
    private fun launchBiometric() {
        if (!viewModel.canLoginWithBiometric()) {
            showToast("Inicia con tu contraseña la primera vez. La huella servirá luego como atajo.")
            return
        }
        biometricAuthenticator.authenticate { result ->
            when (result) {
                is BiometricResult.Success -> viewModel.loginWithBiometric()
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

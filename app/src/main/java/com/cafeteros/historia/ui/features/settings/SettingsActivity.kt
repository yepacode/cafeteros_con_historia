package com.cafeteros.historia.ui.features.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.auth.BiometricAuthenticator
import com.cafeteros.historia.auth.BiometricResult
import com.cafeteros.historia.ui.features.auth.LoginActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.launch

/**
 * Activity contenedora de la pantalla de Configuración.
 *
 * Extiende [FragmentActivity] (no [androidx.activity.ComponentActivity]) porque
 * el flujo de activación de la huella levanta un `BiometricPrompt`, y este
 * componente solo se puede mostrar sobre `FragmentActivity` (lo requiere
 * AndroidX biometric).
 *
 * **Responsabilidades:**
 *  1. Consultar al [BiometricAuthenticator] si el dispositivo puede usar
 *     huella en este momento (sensor presente, huellas enroladas) y
 *     reportarlo al [SettingsViewModel] para que la UI deshabilite el
 *     Switch con un mensaje explicativo si corresponde.
 *  2. Cuando el usuario activa el Switch (false → true), lanzar el
 *     `BiometricPrompt` como verificación. **Solo si la huella se confirma**
 *     se persiste el flag en `true`. Esto evita que el usuario active la
 *     biometría sin verificar que el sensor realmente la reconoce.
 *  3. Cuando el usuario apaga el Switch (true → false), persistir el
 *     cambio sin verificación: bajar privilegios siempre está permitido
 *     dentro de una sesión activa.
 */
class SettingsActivity : FragmentActivity() {

    private lateinit var biometricAuthenticator: BiometricAuthenticator
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        biometricAuthenticator = BiometricAuthenticator(this)

        // Calculamos una sola vez la disponibilidad de biometría al entrar a
        // la pantalla. Si el usuario sale al Ajustes del sistema a enrolar
        // una huella y vuelve, perderemos esa información hasta que se
        // reabra la Activity — aceptable para el alcance académico.
        reportBiometricAvailability()

        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                SettingsScreen(
                    state = state,
                    onBack = ::finish,
                    onToggleBiometric = ::handleBiometricToggle,
                    onLogout = ::handleLogout
                )
            }
        }
    }

    /**
     * Pregunta al [BiometricAuthenticator] si el dispositivo puede usar
     * huella ahora mismo y empuja el resultado al [SettingsViewModel].
     */
    private fun reportBiometricAvailability() {
        val availability = biometricAuthenticator.canAuthenticate()
        when (availability) {
            is BiometricResult.Success ->
                viewModel.updateBiometricAvailability(available = true, reason = null)
            is BiometricResult.Unavailable ->
                viewModel.updateBiometricAvailability(available = false, reason = availability.reason)
            else ->
                viewModel.updateBiometricAvailability(available = false, reason = null)
        }
    }

    /**
     * Manejador del toggle del Switch.
     *
     * @param newValue posición a la que el usuario movió el Switch.
     *  - `true`  → primero lanza `BiometricPrompt`. Persiste solo si pasa.
     *  - `false` → persiste el apagado de inmediato.
     */
    private fun handleBiometricToggle(newValue: Boolean) {
        if (!newValue) {
            viewModel.setBiometricEnabled(false)
            showToast("Huella desactivada")
            return
        }
        biometricAuthenticator.authenticate { result ->
            when (result) {
                is BiometricResult.Success -> {
                    viewModel.setBiometricEnabled(true)
                    showToast("Huella activada")
                }
                is BiometricResult.Cancelled -> {
                    // El usuario canceló: no cambiamos el flag. El Switch
                    // visualmente vuelve a su posición anterior cuando la
                    // próxima recomposición lea el estado del DataStore.
                }
                is BiometricResult.Unavailable -> showToast(result.reason)
                is BiometricResult.Error -> showToast(result.message)
            }
        }
    }

    /**
     * Cierra la sesión del usuario y lo manda al Login. Limpia el stack de
     * Activities con FLAG_ACTIVITY_NEW_TASK + CLEAR_TASK para que no haya
     * pantalla previa a la que volver con back.
     */
    private fun handleLogout() {
        val repo = (application as CafeterosApplication).userRepository
        lifecycleScope.launch {
            repo.logout()
            val intent = Intent(this@SettingsActivity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * Entrada estándar al flujo desde cualquier pantalla. Mantiene el
         * patrón companion `start(context)` que ya usan otras Activities
         * del proyecto (`FarmerSettingsActivity`, etc.).
         */
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }
}

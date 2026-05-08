package com.cafeteros.historia.ui.features.farmer_registration

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del flujo de registro del caficultor.
 *
 * El estado vive en [FarmerRegistrationViewModel], scopeado a esta Activity.
 * Hay dos formas de salir:
 *
 *  - **Cerrar (back en el primer paso)**: simplemente termina la Activity y
 *    vuelve al onboarding caficultor.
 *  - **Finalizar al panel** (botones de [WelcomeApprovedStep]): el usuario
 *    ya quedó persistido y con sesión iniciada, así que abrimos
 *    [MainActivity] y limpiamos la pila para que el back ya no devuelva al
 *    flujo de registro.
 */
class FarmerRegistrationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                FarmerRegistrationScreen(
                    onClose = ::finish,
                    onFinishToPanel = ::navigateToPanel
                )
            }
        }
    }

    private fun navigateToPanel() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}

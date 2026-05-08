package com.cafeteros.historia.ui.features.farmer_onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.auth.LoginActivity
import com.cafeteros.historia.ui.features.farmer_registration.FarmerRegistrationActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del onboarding del caficultor.
 *
 * Navegación:
 *  - "Saltar" / "SALTAR" → cierra la Activity (vuelve a la pantalla anterior).
 *  - "Empezar mi registro" → abre [FarmerRegistrationActivity], el flujo
 *    multi-paso específico del caficultor (datos personales, finca, banco, etc.).
 *  - "Ya tengo cuenta" → abre [LoginActivity].
 */
class FarmerOnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                FarmerOnboardingScreen(
                    onSkip = ::finish,
                    onStartRegistration = ::navigateToFarmerRegistration,
                    onAlreadyHaveAccount = ::navigateToLogin
                )
            }
        }
    }

    /** Lanza el flujo multi-paso de registro del caficultor. */
    private fun navigateToFarmerRegistration() {
        startActivity(Intent(this, FarmerRegistrationActivity::class.java))
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}

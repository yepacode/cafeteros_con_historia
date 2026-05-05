package com.cafeteros.historia.ui.features.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.ui.features.auth.LoginActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del flujo de onboarding.
 *
 * Mapeo actual de los CTAs del onboarding:
 *  - "Comenzar" / "Ya tengo cuenta" → [LoginActivity] (donde el usuario
 *    arranca su sesión; mientras no exista pantalla de registro, ambos
 *    apuntan al login).
 *  - "Saltar" → [MainActivity] (continúa como invitado a la home).
 */
class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                OnboardingScreen(
                    onSkip = ::navigateToMain,
                    onFinish = ::navigateToLogin,
                    onAlreadyHaveAccount = ::navigateToLogin
                )
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

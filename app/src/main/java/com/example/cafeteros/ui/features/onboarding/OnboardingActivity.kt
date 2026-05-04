package com.example.cafeteros.ui.features.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cafeteros.MainActivity
import com.example.cafeteros.ui.theme.CafeterosTheme

/**
 * Activity contenedora del flujo de onboarding.
 *
 * Toda la navegación (Saltar, Comenzar, Ya tengo cuenta) actualmente termina
 * en [MainActivity]. Cuando existan las pantallas de Login/Registro, los
 * callbacks deberán enrutarse a esos destinos:
 *  - "Comenzar" → pantalla de registro de cuenta nueva.
 *  - "Ya tengo cuenta" → pantalla de login.
 *  - "Saltar" → continuar a la home como invitado (o login, según producto).
 */
class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                OnboardingScreen(
                    onSkip = ::navigateToMain,
                    onFinish = ::navigateToMain,
                    onAlreadyHaveAccount = ::navigateToMain
                )
            }
        }
    }

    /** Lanza [MainActivity] y termina esta Activity para sacarla del back stack. */
    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

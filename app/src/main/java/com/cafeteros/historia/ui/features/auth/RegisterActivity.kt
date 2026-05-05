package com.cafeteros.historia.ui.features.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del flujo de Registro.
 *
 * Toda la UI vive en [RegisterScreen]; aquí solo se conecta la navegación.
 *
 * Mientras no exista backend real, "Crear mi cuenta" muestra un Toast con un
 * mensaje de éxito y navega a [MainActivity] como si la cuenta se hubiese
 * creado. "Inicia sesión" del footer cierra esta Activity y vuelve al Login
 * (asumiendo que el usuario llegó aquí desde el Login).
 */
class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                RegisterScreen(
                    onBack = ::finish,
                    onCreateAccount = { data ->
                        Toast.makeText(
                            this,
                            "¡Bienvenido ${data.name}! (cuenta simulada)",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToHome()
                    },
                    onAlreadyHaveAccount = ::finish
                )
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

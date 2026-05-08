package com.cafeteros.historia.ui.features.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.cafeteros.historia.ui.features.explore.ExploreActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del flujo de Registro.
 *
 * Toda la UI vive en [RegisterScreen]; aquí solo se conecta la navegación.
 *
 * Mientras no exista backend real, "Crear mi cuenta" muestra un Toast con un
 * mensaje de éxito y enruta al destino correspondiente al rol elegido:
 *  - [UserType.COMPRADOR] → [ExploreActivity] (home de descubrimiento de café).
 *  - [UserType.CAFICULTOR] → [MainActivity] (home placeholder por ahora; aquí
 *    irá la home del vendedor cuando esa pantalla se diseñe).
 *
 * "Inicia sesión" del footer cierra esta Activity y vuelve al Login
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
                        navigateToHomeFor(data)
                    },
                    onAlreadyHaveAccount = ::finish
                )
            }
        }
    }

    private fun navigateToHomeFor(data: RegisterFormData) {
        val intent = when (data.userType) {
            UserType.COMPRADOR -> Intent(this, ExploreActivity::class.java).apply {
                putExtra(ExploreActivity.EXTRA_USER_NAME, data.name.firstNameOrFull())
                putExtra(ExploreActivity.EXTRA_ROLE_ID, data.userType.roleId)
            }
            UserType.CAFICULTOR -> Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}

/**
 * Extrae el primer nombre cuando la cadena trae varias palabras (lo típico
 * en "María González" → "María"). Si solo hay una palabra, devuelve el
 * valor completo. Útil para personalizar el saludo de la home sin saturar.
 */
private fun String.firstNameOrFull(): String =
    trim().substringBefore(' ').ifBlank { this }

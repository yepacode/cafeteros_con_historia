package com.cafeteros.historia.ui.features.password_recovery

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del flujo de recuperación de contraseña.
 *
 * Su única responsabilidad es:
 *  1. Cablear el [PasswordRecoveryFlow] dentro de [setContent].
 *  2. Cerrarse cuando el flujo termina (vía ::finish).
 *
 * Toda la UI y la lógica de pasos vive en composables; aquí no hay estado.
 */
class PasswordRecoveryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                PasswordRecoveryFlow(
                    onClose = ::finish,
                    onContactSupport = ::showSupportPlaceholder
                )
            }
        }
    }

    /** Mientras no exista un canal de soporte real, mostramos un toast. */
    private fun showSupportPlaceholder() {
        Toast.makeText(
            this,
            "Soporte técnico: próximamente",
            Toast.LENGTH_SHORT
        ).show()
    }
}

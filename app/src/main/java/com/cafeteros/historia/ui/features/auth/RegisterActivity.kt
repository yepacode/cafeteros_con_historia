package com.cafeteros.historia.ui.features.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.cafeteros.historia.ui.features.farmer_onboarding.FarmerOnboardingActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del flujo de Registro.
 *
 * Conecta [RegisterScreen] con [RegisterViewModel]: pasa los datos del
 * formulario al ViewModel y reacciona al estado para mostrar errores
 * (Toast) o navegar a Home cuando el registro tiene éxito.
 *
 * Acepta un extra opcional [EXTRA_INITIAL_USER_TYPE] (nombre del enum
 * [UserType]) que pre-selecciona el selector de tipo de usuario al entrar.
 * Útil cuando el usuario llega aquí desde un onboarding específico de rol
 * (ej. el flujo del caficultor).
 */
class RegisterActivity : ComponentActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val initialUserType = intent
            ?.getStringExtra(EXTRA_INITIAL_USER_TYPE)
            ?.let { runCatching { UserType.valueOf(it) }.getOrNull() }
            ?: UserType.COMPRADOR

        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(state.errorMessage) {
                    state.errorMessage?.let { message ->
                        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
                        viewModel.consumeError()
                    }
                }

                LaunchedEffect(state.registeredSuccessfully) {
                    if (state.registeredSuccessfully) {
                        navigateToHome()
                    }
                }

                RegisterScreen(
                    initialUserType = initialUserType,
                    onBack = ::finish,
                    onCreateAccount = { data ->
                        viewModel.register(
                            email = data.email,
                            name = data.name,
                            phone = data.phone,
                            password = data.password,
                            userType = data.userType
                        )
                    },
                    onAlreadyHaveAccount = ::finish,
                    onChooseFarmerFlow = ::navigateToFarmerOnboarding
                )
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /**
     * Abre el onboarding del caficultor (3 pantallas verdes) que a su vez
     * inicia el flujo extendido de 5 pasos para el registro de vendedor.
     */
    private fun navigateToFarmerOnboarding() {
        startActivity(Intent(this, FarmerOnboardingActivity::class.java))
        finish()
    }

    companion object {
        /**
         * Clave del extra del Intent que pre-selecciona el tipo de usuario al
         * entrar. Su valor debe ser el `name` de un valor del enum [UserType].
         */
        const val EXTRA_INITIAL_USER_TYPE: String = "extra_initial_user_type"
    }
}

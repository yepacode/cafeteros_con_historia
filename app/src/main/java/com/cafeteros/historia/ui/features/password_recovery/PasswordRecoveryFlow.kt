package com.cafeteros.historia.ui.features.password_recovery

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cafeteros.historia.ui.features.password_recovery.model.PasswordRecoveryStep
import com.cafeteros.historia.ui.features.password_recovery.screens.CodeVerificationScreen
import com.cafeteros.historia.ui.features.password_recovery.screens.EmailEntryScreen
import com.cafeteros.historia.ui.features.password_recovery.screens.NewPasswordScreen
import com.cafeteros.historia.ui.features.password_recovery.screens.PasswordUpdatedScreen
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Código OTP "mock" que la app acepta como válido mientras no exista backend.
 *
 * En la integración real, esta constante desaparece: el código se genera en
 * el servidor, se envía por email/SMS y se verifica vía endpoint REST.
 */
private const val MOCK_VALID_OTP_CODE: String = "1234"

/**
 * Composable raíz del flujo completo de recuperación de contraseña.
 *
 * Mantiene una pequeña máquina de estados sobre [PasswordRecoveryStep] y
 * delega la persistencia (verificar email, actualizar contraseña) a
 * [PasswordRecoveryViewModel].
 *
 * Se mantienen los `// TODO backend:` para los puntos donde, cuando exista
 * un servidor real, hay que reemplazar la lógica local (envío de OTP,
 * verificación de código).
 *
 * @param modifier modifier opcional aplicado a la pantalla activa.
 * @param onClose callback que la Activity debe usar para cerrarse.
 * @param onContactSupport callback del enlace "Contactar a soporte técnico".
 */
@Composable
fun PasswordRecoveryFlow(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onContactSupport: () -> Unit = {}
) {
    val viewModel: PasswordRecoveryViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var currentStep: PasswordRecoveryStep by rememberSaveable(
        stateSaver = PasswordRecoveryStepSaver
    ) { mutableStateOf(PasswordRecoveryStep.EmailEntry) }

    var email by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.emailErrorMessage) {
        uiState.emailErrorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.consumeEmailError()
        }
    }

    LaunchedEffect(uiState.passwordErrorMessage) {
        uiState.passwordErrorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.consumePasswordError()
        }
    }

    AnimatedContent(
        targetState = currentStep,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "passwordRecoveryStep"
    ) { step ->
        when (step) {
            PasswordRecoveryStep.EmailEntry -> EmailEntryScreen(
                modifier = modifier,
                onBack = onClose,
                onSendCode = { enteredEmail ->
                    email = enteredEmail
                    // TODO backend: solicitar al servidor el envío del código a `enteredEmail`.
                    viewModel.checkEmailAndAdvance(enteredEmail) {
                        currentStep = PasswordRecoveryStep.CodeVerification
                    }
                },
                onAlreadyRemember = onClose
            )

            PasswordRecoveryStep.CodeVerification -> CodeVerificationScreen(
                modifier = modifier,
                email = email,
                onBack = { currentStep = PasswordRecoveryStep.EmailEntry },
                onVerifyCode = { code ->
                    // TODO backend: validar el código con el servidor en vez de comparar local.
                    if (code == MOCK_VALID_OTP_CODE) {
                        currentStep = PasswordRecoveryStep.NewPassword
                    } else {
                        Toast.makeText(
                            context,
                            "Código incorrecto. Pista temporal: $MOCK_VALID_OTP_CODE",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                onResendCode = {
                    // TODO backend: pedir al servidor reenviar el código a `email`.
                }
            )

            PasswordRecoveryStep.NewPassword -> NewPasswordScreen(
                modifier = modifier,
                onBack = { currentStep = PasswordRecoveryStep.CodeVerification },
                onSavePassword = { newPassword ->
                    viewModel.savePassword(email = email, newPassword = newPassword) {
                        currentStep = PasswordRecoveryStep.Success
                    }
                }
            )

            PasswordRecoveryStep.Success -> PasswordUpdatedScreen(
                modifier = modifier,
                onLogin = onClose,
                onContactSupport = onContactSupport
            )
        }
    }
}

/**
 * Saver para que [PasswordRecoveryStep] sobreviva a recreaciones de la
 * Activity (ej. rotación de pantalla) usando [rememberSaveable].
 */
private val PasswordRecoveryStepSaver: androidx.compose.runtime.saveable.Saver<PasswordRecoveryStep, String> =
    androidx.compose.runtime.saveable.Saver(
        save = { step -> step::class.simpleName ?: "EmailEntry" },
        restore = { name ->
            when (name) {
                "EmailEntry" -> PasswordRecoveryStep.EmailEntry
                "CodeVerification" -> PasswordRecoveryStep.CodeVerification
                "NewPassword" -> PasswordRecoveryStep.NewPassword
                "Success" -> PasswordRecoveryStep.Success
                else -> PasswordRecoveryStep.EmailEntry
            }
        }
    )

@Preview(name = "PasswordRecoveryFlow", widthDp = 360, heightDp = 800)
@Composable
private fun PasswordRecoveryFlowPreview() {
    CafeterosTheme {
        PasswordRecoveryFlow(onClose = {})
    }
}

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
import com.cafeteros.historia.ui.features.password_recovery.screens.EmailEntryScreen
import com.cafeteros.historia.ui.features.password_recovery.screens.PasswordUpdatedScreen
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Composable raíz del flujo de recuperación de contraseña.
 *
 * Con Firebase Auth el flujo se reduce a 2 pantallas: el usuario escribe su
 * correo, Firebase le envía un link de restablecimiento, y mostramos una
 * pantalla de confirmación. El reseteo en sí lo realiza el usuario en la
 * página de Firebase a la que llega el link.
 *
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

    var showSuccess by rememberSaveable { mutableStateOf(false) }

    // Cuando el correo se envía correctamente, avanzamos a la pantalla final.
    LaunchedEffect(uiState.wasSent) {
        if (uiState.wasSent) showSuccess = true
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.consumeError()
        }
    }

    AnimatedContent(
        targetState = showSuccess,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "passwordRecoveryStep"
    ) { success ->
        if (success) {
            PasswordUpdatedScreen(
                modifier = modifier,
                onLogin = onClose,
                onContactSupport = onContactSupport
            )
        } else {
            EmailEntryScreen(
                modifier = modifier,
                onBack = onClose,
                onSendCode = { enteredEmail ->
                    viewModel.sendRecoveryEmail(enteredEmail)
                },
                onAlreadyRemember = onClose
            )
        }
    }
}

@Preview(name = "PasswordRecoveryFlow", widthDp = 360, heightDp = 800)
@Composable
private fun PasswordRecoveryFlowPreview() {
    CafeterosTheme {
        PasswordRecoveryFlow(onClose = {})
    }
}

package com.cafeteros.historia.ui.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.features.auth.components.AuthHeader
import com.cafeteros.historia.ui.features.auth.components.AuthPrimaryButton
import com.cafeteros.historia.ui.features.auth.components.ForgotPasswordLink
import com.cafeteros.historia.ui.features.auth.components.LoginEmailField
import com.cafeteros.historia.ui.features.auth.components.LoginPasswordField
import com.cafeteros.historia.ui.features.auth.components.OrContinueDivider
import com.cafeteros.historia.ui.features.auth.components.RegisterFooter
import com.cafeteros.historia.ui.features.auth.components.SocialAuthButton
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla raíz del flujo de login.
 *
 * Es stateful en cuanto a los inputs (email, contraseña, visibilidad), pero
 * delega toda la navegación y los efectos al padre vía callbacks.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param onBack callback de la flecha de regreso (típ. cerrar Activity).
 * @param onLogin callback de "Iniciar sesión" — recibe email + contraseña.
 * @param onForgotPassword callback de "¿Olvidaste tu contraseña?".
 * @param onGoogleLogin callback del botón Google.
 * @param onFingerprintLogin callback del botón Huella; aquí se debe lanzar
 *  el `BiometricPrompt` desde la Activity.
 * @param onRegister callback del enlace "Regístrate" del footer.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    initialEmail: String = "",
    showFingerprint: Boolean = false,
    onBack: () -> Unit = {},
    onLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onFingerprintLogin: () -> Unit = {},
    onRegister: () -> Unit = {}
) {
    // Email pre-llenado: el StateFlow del ViewModel emite asíncronamente, por
    // lo que `initialEmail` llega null al primer render y con valor real
    // después. Un LaunchedEffect copia el valor en cuanto está disponible,
    // sin sobreescribir lo que el usuario escribió manualmente.
    var email by remember { mutableStateOf("") }
    LaunchedEffect(initialEmail) {
        if (initialEmail.isNotBlank() && email.isBlank()) {
            email = initialEmail
        }
    }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        AuthHeader(
            title = "Bienvenido de vuelta",
            subtitle = "Inicia sesión para seguir explorando",
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            LoginEmailField(
                value = email,
                onValueChange = { email = it }
            )

            LoginPasswordField(
                value = password,
                onValueChange = { password = it },
                isVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible }
            )

            ForgotPasswordLink(onClick = onForgotPassword)

            AuthPrimaryButton(
                label = "Iniciar sesión",
                onClick = { onLogin(email, password) }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            OrContinueDivider()
            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            SocialAuthButtonsRow(
                onGoogleClick = onGoogleLogin,
                onFingerprintClick = onFingerprintLogin,
                showFingerprint = showFingerprint
            )

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
            RegisterFooter(onRegisterClick = onRegister)
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

/**
 * Fila de botones sociales. La huella sólo aparece si [showFingerprint] es
 * true, lo cual indica que ya hubo un login con contraseña en este
 * dispositivo y por lo tanto Firebase tiene una sesión que la huella puede
 * desbloquear.
 */
@Composable
private fun SocialAuthButtonsRow(
    onGoogleClick: () -> Unit,
    onFingerprintClick: () -> Unit,
    showFingerprint: Boolean
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        SocialAuthButton(
            modifier = Modifier.weight(1f),
            onClick = onGoogleClick,
            label = "Google",
            iconRes = R.drawable.ic_google_g
        )
        if (showFingerprint) {
            SocialAuthButton(
                modifier = Modifier.weight(1f),
                onClick = onFingerprintClick,
                label = "Huella",
                iconVector = Icons.Outlined.Fingerprint
            )
        }
    }
}

@Preview(name = "LoginScreen", widthDp = 360, heightDp = 800)
@Composable
private fun LoginScreenPreview() {
    CafeterosTheme {
        LoginScreen()
    }
}

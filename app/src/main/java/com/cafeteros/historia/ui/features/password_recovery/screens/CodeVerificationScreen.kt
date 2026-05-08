package com.cafeteros.historia.ui.features.password_recovery.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.auth.components.AuthHeader
import com.cafeteros.historia.ui.features.auth.components.AuthPrimaryButton
import com.cafeteros.historia.ui.features.password_recovery.components.OTP_LENGTH
import com.cafeteros.historia.ui.features.password_recovery.components.OtpInputField
import com.cafeteros.historia.ui.features.password_recovery.components.RecoveryStepper
import com.cafeteros.historia.ui.features.password_recovery.components.ResendCodeButton
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Paso 2 del flujo: la usuaria ingresa el código OTP de [OTP_LENGTH] dígitos.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param email correo al que se envió el código (se muestra en pantalla).
 * @param onBack callback de la flecha de retorno.
 * @param onVerifyCode callback al pulsar "Verificar código"; recibe el código completo.
 * @param onResendCode callback al pulsar "Reenviar" cuando el cooldown llega a cero.
 */
@Composable
fun CodeVerificationScreen(
    modifier: Modifier = Modifier,
    email: String,
    onBack: () -> Unit = {},
    onVerifyCode: (code: String) -> Unit = {},
    onResendCode: () -> Unit = {}
) {
    var code by remember { mutableStateOf("") }
    val isCodeComplete = code.length == OTP_LENGTH

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        AuthHeader(
            title = "Recuperar acceso",
            subtitle = "Verificación en dos pasos",
            onBackClick = onBack
        )
        RecoveryStepper(currentStep = 2)
        Spacer(modifier = Modifier.height(BrandSpacing.md))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            HeadingBlock(email = email)
            OtpInputField(value = code, onValueChange = { code = it })
            ResendCodeButton(onResendClick = onResendCode)
            AuthPrimaryButton(
                label = "Verificar código",
                onClick = { onVerifyCode(code) },
                enabled = isCodeComplete
            )
        }
    }
}

@Composable
private fun HeadingBlock(email: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ingresa el código",
            style = BrandTypography.LoginTitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Te enviamos un código de $OTP_LENGTH dígitos a $email",
            style = BrandTypography.LoginSubtitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "CodeVerificationScreen", widthDp = 360, heightDp = 800)
@Composable
private fun CodeVerificationScreenPreview() {
    CafeterosTheme {
        CodeVerificationScreen(email = "tu@correo.com")
    }
}

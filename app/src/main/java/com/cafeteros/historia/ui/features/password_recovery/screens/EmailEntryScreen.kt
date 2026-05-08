package com.cafeteros.historia.ui.features.password_recovery.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.auth.components.AuthHeader
import com.cafeteros.historia.ui.features.auth.components.AuthPrimaryButton
import com.cafeteros.historia.ui.features.auth.components.AuthTextField
import com.cafeteros.historia.ui.features.password_recovery.components.RecoveryStepper
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Paso 1 del flujo: la usuaria ingresa su correo para recibir el código.
 *
 * Stateless en cuanto a UI; gestiona internamente solo el valor del email.
 * Toda la lógica de envío y navegación se delega al padre.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param onBack callback de la flecha de retorno (cierra el flujo).
 * @param onSendCode callback al pulsar "Enviar código"; recibe el email.
 * @param onAlreadyRemember callback del enlace "Recordé mi contraseña".
 */
@Composable
fun EmailEntryScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSendCode: (email: String) -> Unit = {},
    onAlreadyRemember: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    val isEmailValid = email.contains("@") && email.contains(".")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        AuthHeader(
            title = "Recuperar acceso",
            subtitle = "Te ayudamos a volver a tu cuenta",
            onBackClick = onBack
        )
        RecoveryStepper(currentStep = 1)
        Spacer(modifier = Modifier.height(BrandSpacing.md))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            EnvelopeIcon()
            HeadingBlock()
            AuthTextField(
                label = "CORREO ELECTRÓNICO",
                placeholder = "tu@correo.com",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )
            AuthPrimaryButton(
                label = "Enviar código",
                onClick = { onSendCode(email.trim()) },
                enabled = isEmailValid
            )
            TextButton(onClick = onAlreadyRemember) {
                Text(
                    text = "Recordé mi contraseña",
                    style = BrandTypography.BrandTagline.copy(
                        color = BrandColors.LinkGreen,
                        letterSpacing = 0.sp,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
private fun EnvelopeIcon() {
    Box(
        modifier = Modifier
            .size(72.dp)
            .background(color = BrandColors.InputBackground, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.MailOutline,
            contentDescription = null,
            tint = BrandColors.CoffeeBrown,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
private fun HeadingBlock() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¿Olvidaste tu contraseña?",
            style = BrandTypography.LoginTitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Ingresa tu correo y te enviaremos un código para restablecerla.",
            style = BrandTypography.LoginSubtitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "EmailEntryScreen", widthDp = 360, heightDp = 800)
@Composable
private fun EmailEntryScreenPreview() {
    CafeterosTheme {
        EmailEntryScreen()
    }
}

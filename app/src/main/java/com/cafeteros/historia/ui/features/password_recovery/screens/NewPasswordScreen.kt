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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.auth.components.AuthHeader
import com.cafeteros.historia.ui.features.auth.components.AuthPrimaryButton
import com.cafeteros.historia.ui.features.auth.components.AuthTextField
import com.cafeteros.historia.ui.features.password_recovery.components.PasswordRequirementsHint
import com.cafeteros.historia.ui.features.password_recovery.components.RecoveryStepper
import com.cafeteros.historia.ui.features.password_recovery.components.isPasswordValid
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Paso 3 del flujo: la usuaria define su nueva contraseña.
 *
 * Habilita "Guardar contraseña" únicamente cuando:
 *  - La contraseña cumple todos los requisitos ([isPasswordValid]).
 *  - La confirmación coincide exactamente con la nueva contraseña.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param onBack callback de la flecha de retorno.
 * @param onSavePassword callback al pulsar "Guardar contraseña"; recibe la
 *  nueva contraseña ya validada.
 */
@Composable
fun NewPasswordScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSavePassword: (newPassword: String) -> Unit = {}
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isNewVisible by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }

    val passwordsMatch = newPassword.isNotEmpty() && newPassword == confirmPassword
    val canSave = isPasswordValid(newPassword) && passwordsMatch

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        AuthHeader(
            title = "Nueva contraseña",
            subtitle = "Casi terminamos",
            onBackClick = onBack
        )
        RecoveryStepper(currentStep = 3)
        Spacer(modifier = Modifier.height(BrandSpacing.md))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            HeadingBlock()

            AuthTextField(
                label = "NUEVA CONTRASEÑA",
                placeholder = "••••••••",
                value = newPassword,
                onValueChange = { newPassword = it },
                visualTransformation = visualTransformationFor(isNewVisible),
                trailing = {
                    VisibilityToggleIcon(
                        isVisible = isNewVisible,
                        onToggle = { isNewVisible = !isNewVisible }
                    )
                }
            )

            PasswordRequirementsHint(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BrandSpacing.sm),
                password = newPassword
            )

            AuthTextField(
                label = "CONFIRMAR CONTRASEÑA",
                placeholder = "••••••••",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                visualTransformation = visualTransformationFor(isConfirmVisible),
                helperText = if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                    "Las contraseñas no coinciden"
                } else null,
                trailing = {
                    VisibilityToggleIcon(
                        isVisible = isConfirmVisible,
                        onToggle = { isConfirmVisible = !isConfirmVisible }
                    )
                },
                keyboardType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            AuthPrimaryButton(
                label = "Guardar contraseña",
                onClick = { onSavePassword(newPassword) },
                enabled = canSave
            )
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
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
            text = "Crea tu nueva contraseña",
            style = BrandTypography.LoginTitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Asegura tu cuenta con una combinación fuerte y fácil de recordar.",
            style = BrandTypography.LoginSubtitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun VisibilityToggleIcon(isVisible: Boolean, onToggle: () -> Unit) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (isVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
            contentDescription = if (isVisible) "Ocultar contraseña" else "Mostrar contraseña",
            tint = BrandColors.InputHint
        )
    }
}

private fun visualTransformationFor(isVisible: Boolean): VisualTransformation =
    if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

@Preview(name = "NewPasswordScreen", widthDp = 360, heightDp = 800)
@Composable
private fun NewPasswordScreenPreview() {
    CafeterosTheme {
        NewPasswordScreen()
    }
}

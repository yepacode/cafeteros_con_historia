package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Campo de correo con botón "VERIFICAR" a la derecha que dispara un flujo
 * de OTP. Cuando el correo ya fue verificado, muestra un check verde en
 * lugar del botón.
 *
 * @param modifier modifier opcional.
 * @param email correo actual.
 * @param onEmailChange callback al escribir.
 * @param isVerified true si el correo ya pasó por OTP exitoso.
 * @param onRequestVerification callback al pulsar "VERIFICAR" (genera y
 *  "envía" el OTP). Debe devolver el código generado en modo demo.
 * @param onVerifyOtp callback al confirmar el OTP escrito por el usuario.
 *  Recibe el código y devuelve true si fue válido.
 */
@Composable
fun EmailVerifyField(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    isVerified: Boolean,
    onRequestVerification: () -> String,
    onVerifyOtp: (String) -> Boolean,
    onShowDemoCode: (String) -> Unit
) {
    var showOtpDialog by remember { mutableStateOf(false) }
    var otpError by remember { mutableStateOf<String?>(null) }

    LabeledField(modifier = modifier, label = "CORREO ELECTRÓNICO") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = email,
                onValueChange = onEmailChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "ejemplo@correo.com",
                        style = TextStyle(color = BrandColors.InputHint)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BrandColors.InputBackground,
                    unfocusedContainerColor = BrandColors.InputBackground,
                    disabledContainerColor = BrandColors.InputBackground,
                    focusedTextColor = BrandColors.TextPrimary,
                    unfocusedTextColor = BrandColors.TextPrimary,
                    disabledTextColor = BrandColors.TextPrimary,
                    cursorColor = BrandColors.CoffeeBrown,
                    focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )

            if (isVerified) {
                VerifiedBadge()
            } else {
                VerifyButton(
                    enabled = email.isValidEmailLite(),
                    onClick = {
                        val generatedCode = onRequestVerification()
                        onShowDemoCode(generatedCode)
                        otpError = null
                        showOtpDialog = true
                    }
                )
            }
        }
    }

    if (showOtpDialog) {
        OtpVerificationDialog(
            email = email,
            errorMessage = otpError,
            onDismiss = { showOtpDialog = false },
            onConfirm = { typedCode ->
                if (onVerifyOtp(typedCode)) {
                    showOtpDialog = false
                    otpError = null
                } else {
                    otpError = "Código incorrecto. Intenta de nuevo."
                }
            }
        )
    }
}

/** Botón "VERIFICAR" cuando aún no se valida el correo. */
@Composable
private fun VerifyButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandColors.FarmerStatsBadgeBackground,
            contentColor = BrandColors.TextPrimary,
            disabledContainerColor = BrandColors.InputBackground,
            disabledContentColor = BrandColors.InputHint
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = BrandSpacing.md,
            vertical = 14.dp
        )
    ) {
        Text(
            text = "VERIFICAR",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        )
    }
}

/** Badge de check verde cuando el correo ya fue verificado. */
@Composable
private fun VerifiedBadge() {
    Box(
        modifier = Modifier
            .background(
                color = BrandColors.FarmerPrimary,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = BrandSpacing.md, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = "Correo verificado",
            tint = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Diálogo donde el usuario escribe el OTP de 6 dígitos que recibió por correo.
 *
 * @param email correo al que se "envió" el código (mostrado al usuario).
 * @param errorMessage mensaje de error a mostrar si el código fue incorrecto.
 * @param onDismiss callback al cerrar/cancelar.
 * @param onConfirm callback al pulsar Confirmar; recibe el código tipeado.
 */
@Composable
private fun OtpVerificationDialog(
    email: String,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var typed by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Verifica tu correo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                Text(
                    text = "Enviamos un código de 6 dígitos a $email. " +
                            "Escríbelo aquí abajo.",
                    style = TextStyle(fontSize = 14.sp)
                )
                TextField(
                    value = typed,
                    onValueChange = { newValue ->
                        typed = newValue.filter { it.isDigit() }.take(6)
                    },
                    placeholder = { Text("000000") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    isError = errorMessage != null,
                    supportingText = errorMessage?.let { { Text(it) } }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(typed) },
                enabled = typed.length == 6
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

/** Validador "ligero" — basta para gating del botón VERIFICAR. */
private fun String.isValidEmailLite(): Boolean =
    isNotBlank() && contains("@") && substringAfter("@").contains(".")

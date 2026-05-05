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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.auth.components.AlreadyHaveAccountFooter
import com.cafeteros.historia.ui.features.auth.components.AuthPrimaryButton
import com.cafeteros.historia.ui.features.auth.components.AuthTextField
import com.cafeteros.historia.ui.features.auth.components.ConsentCheckbox
import com.cafeteros.historia.ui.features.auth.components.RegisterHeader
import com.cafeteros.historia.ui.features.auth.components.RegisterPasswordField
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.cafeteros.historia.ui.features.auth.components.UserTypeSelector
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Datos capturados por el formulario de Registro.
 *
 * Se modela como data class para que [onCreateAccount] reciba un objeto
 * cohesivo en vez de seis parámetros sueltos.
 */
data class RegisterFormData(
    val userType: UserType,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val acceptedMarketing: Boolean
)

/**
 * Pantalla raíz del flujo de Registro.
 *
 * Mantiene el estado completo del formulario: tipo de usuario, nombre, email,
 * teléfono, contraseña, confirmación, visibilidad de contraseña, aceptación
 * de términos y aceptación de marketing. Toda la navegación y los efectos
 * salen vía callbacks.
 *
 * Validación mínima implementada (gating del botón "Crear mi cuenta"):
 *  - Nombre, email, teléfono y contraseña no vacíos.
 *  - Contraseña con mínimo 8 caracteres.
 *  - Las dos contraseñas coinciden.
 *  - Términos y Condiciones aceptados.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param onBack callback de la flecha de regreso.
 * @param onCreateAccount callback al pulsar "Crear mi cuenta" cuando el form
 *  es válido. Recibe los datos en un [RegisterFormData].
 * @param onAlreadyHaveAccount callback de "Inicia sesión" del footer.
 */
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onCreateAccount: (RegisterFormData) -> Unit = {},
    onAlreadyHaveAccount: () -> Unit = {}
) {
    var userType by remember { mutableStateOf(UserType.COMPRADOR) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var acceptedMarketing by remember { mutableStateOf(false) }

    val passwordsMatch = password.isNotEmpty() && password == confirmPassword
    val isValid = name.isNotBlank() &&
            email.isNotBlank() &&
            phone.isNotBlank() &&
            password.length >= 8 &&
            passwordsMatch &&
            acceptedTerms

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        RegisterHeader(
            title = "Crear cuenta",
            subtitle = "Únete a la comunidad cafetera colombiana",
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            UserTypeSelector(
                selected = userType,
                onSelect = { userType = it }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.xs))

            AuthTextField(
                label = "NOMBRE COMPLETO",
                placeholder = "Ej: María González",
                value = name,
                onValueChange = { name = it }
            )

            AuthTextField(
                label = "CORREO ELECTRÓNICO",
                placeholder = "tu@correo.com",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )

            AuthTextField(
                label = "TELÉFONO CELULAR",
                placeholder = "300 123 4567",
                value = phone,
                onValueChange = { phone = it },
                prefix = "+57",
                keyboardType = KeyboardType.Phone
            )

            RegisterPasswordField(
                label = "CONTRASEÑA",
                value = password,
                onValueChange = { password = it },
                isVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible },
                helperText = "Mínimo 8 caracteres, 1 mayúscula y 1 número"
            )

            RegisterPasswordField(
                label = "CONFIRMAR CONTRASEÑA",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                isVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible },
                showVisibilityToggle = false
            )

            ConsentCheckbox(
                checked = acceptedTerms,
                onCheckedChange = { acceptedTerms = it },
                label = termsAndConditionsLabel()
            )

            ConsentCheckbox(
                checked = acceptedMarketing,
                onCheckedChange = { acceptedMarketing = it },
                label = AnnotatedString(
                    "Quiero recibir novedades y promociones de caficultores"
                )
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            AuthPrimaryButton(
                label = "Crear mi cuenta",
                enabled = isValid,
                onClick = {
                    onCreateAccount(
                        RegisterFormData(
                            userType = userType,
                            name = name.trim(),
                            email = email.trim(),
                            phone = phone.trim(),
                            password = password,
                            acceptedMarketing = acceptedMarketing
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            AlreadyHaveAccountFooter(onLoginClick = onAlreadyHaveAccount)

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

/** Construye el label del checkbox de Términos con los dos enlaces estilizados. */
private fun termsAndConditionsLabel(): AnnotatedString = buildAnnotatedString {
    append("Acepto los ")
    pushStyle(BrandTypography.ConsentLink.toSpanStyle())
    append("Términos y Condiciones")
    pop()
    append(" y la ")
    pushStyle(BrandTypography.ConsentLink.toSpanStyle())
    append("Política de Tratamiento de Datos")
    pop()
}

@Preview(name = "RegisterScreen", widthDp = 360, heightDp = 1100)
@Composable
private fun RegisterScreenPreview() {
    CafeterosTheme {
        RegisterScreen()
    }
}

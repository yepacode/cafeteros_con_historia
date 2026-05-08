package com.cafeteros.historia.ui.features.auth

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 * Datos capturados por el formulario de Registro (solo se usa para
 * compradores; los caficultores van por el flujo multi-paso dedicado).
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
 * Comportamiento por tipo de usuario:
 *  - **COMPRADOR**: muestra el formulario corto (nombre, email, teléfono,
 *    contraseña) y registra directamente.
 *  - **CAFICULTOR**: el formulario corto se oculta y se muestra una tarjeta
 *    explicativa con un CTA "Empezar registro de caficultor", que dispara
 *    [onChooseFarmerFlow]. Esto desvía al flujo extendido de 5 pasos
 *    (Datos personales → Finca → Banco → Verificación → Estado).
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param initialUserType tipo preseleccionado al abrir la pantalla.
 * @param onBack callback de la flecha de regreso.
 * @param onCreateAccount callback al pulsar "Crear mi cuenta" cuando el form
 *  es válido (solo aplica para compradores).
 * @param onAlreadyHaveAccount callback de "Inicia sesión" del footer.
 * @param onChooseFarmerFlow callback cuando el usuario selecciona caficultor
 *  y pulsa el CTA de la tarjeta — la Activity debe abrir el onboarding del
 *  caficultor.
 */
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    initialUserType: UserType = UserType.COMPRADOR,
    onBack: () -> Unit = {},
    onCreateAccount: (RegisterFormData) -> Unit = {},
    onAlreadyHaveAccount: () -> Unit = {},
    onChooseFarmerFlow: () -> Unit = {}
) {
    var userType by remember { mutableStateOf(initialUserType) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var acceptedMarketing by remember { mutableStateOf(false) }

    val passwordsMatch = password.isNotEmpty() && password == confirmPassword
    val isFormValid = name.isNotBlank() &&
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

            // Comprador: form corto.
            // Caficultor: card que invita al flujo extendido.
            if (userType == UserType.COMPRADOR) {
                BuyerForm(
                    name = name,
                    onNameChange = { name = it },
                    email = email,
                    onEmailChange = { email = it },
                    phone = phone,
                    onPhoneChange = { phone = it },
                    password = password,
                    onPasswordChange = { password = it },
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = { confirmPassword = it },
                    passwordVisible = passwordVisible,
                    onTogglePasswordVisible = { passwordVisible = !passwordVisible },
                    acceptedTerms = acceptedTerms,
                    onAcceptedTermsChange = { acceptedTerms = it },
                    acceptedMarketing = acceptedMarketing,
                    onAcceptedMarketingChange = { acceptedMarketing = it },
                    isValid = isFormValid,
                    onSubmit = {
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
            } else {
                FarmerFlowCallout(onStartFarmerFlow = onChooseFarmerFlow)
            }

            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            AlreadyHaveAccountFooter(onLoginClick = onAlreadyHaveAccount)

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

/** Formulario corto para compradores (mismo de antes, extraído por claridad). */
@Composable
private fun BuyerForm(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisible: () -> Unit,
    acceptedTerms: Boolean,
    onAcceptedTermsChange: (Boolean) -> Unit,
    acceptedMarketing: Boolean,
    onAcceptedMarketingChange: (Boolean) -> Unit,
    isValid: Boolean,
    onSubmit: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
        AuthTextField(
            label = "NOMBRE COMPLETO",
            placeholder = "Ej: María González",
            value = name,
            onValueChange = onNameChange
        )
        AuthTextField(
            label = "CORREO ELECTRÓNICO",
            placeholder = "tu@correo.com",
            value = email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email
        )
        AuthTextField(
            label = "TELÉFONO CELULAR",
            placeholder = "300 123 4567",
            value = phone,
            onValueChange = onPhoneChange,
            prefix = "+57",
            keyboardType = KeyboardType.Phone
        )
        RegisterPasswordField(
            label = "CONTRASEÑA",
            value = password,
            onValueChange = onPasswordChange,
            isVisible = passwordVisible,
            onVisibilityToggle = onTogglePasswordVisible,
            helperText = "Mínimo 8 caracteres, 1 mayúscula y 1 número"
        )
        RegisterPasswordField(
            label = "CONFIRMAR CONTRASEÑA",
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            isVisible = passwordVisible,
            onVisibilityToggle = onTogglePasswordVisible,
            showVisibilityToggle = false
        )
        ConsentCheckbox(
            checked = acceptedTerms,
            onCheckedChange = onAcceptedTermsChange,
            label = termsAndConditionsLabel()
        )
        ConsentCheckbox(
            checked = acceptedMarketing,
            onCheckedChange = onAcceptedMarketingChange,
            label = AnnotatedString(
                "Quiero recibir novedades y promociones de caficultores"
            )
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        AuthPrimaryButton(
            label = "Crear mi cuenta",
            enabled = isValid,
            onClick = onSubmit
        )
    }
}

/**
 * Tarjeta verde que se muestra cuando el usuario selecciona "Caficultor".
 * Le explica que su flujo es distinto y lo lleva al onboarding extendido.
 */
@Composable
private fun FarmerFlowCallout(
    modifier: Modifier = Modifier,
    onStartFarmerFlow: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.TrustBannerBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = BrandColors.FarmerPrimary, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Eco,
                contentDescription = null,
                tint = BrandColors.PrimaryButtonText,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = "Como caficultor te tenemos un programa especial",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "El registro toma cerca de 10 minutos: validamos tu identidad, " +
                    "tu finca y tu cuenta bancaria para que recibas pagos seguros.",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = BrandColors.TextSecondary
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(BrandSpacing.xs))
        AuthPrimaryButton(
            label = "Empezar registro de caficultor",
            enabled = true,
            onClick = onStartFarmerFlow
        )
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

@Preview(name = "Register – Comprador", widthDp = 360, heightDp = 1100)
@Composable
private fun RegisterBuyerPreview() {
    CafeterosTheme {
        RegisterScreen(initialUserType = UserType.COMPRADOR)
    }
}

@Preview(name = "Register – Caficultor (callout)", widthDp = 360, heightDp = 720)
@Composable
private fun RegisterFarmerPreview() {
    CafeterosTheme {
        RegisterScreen(initialUserType = UserType.CAFICULTOR)
    }
}

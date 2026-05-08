package com.cafeteros.historia.ui.features.farmer_registration.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_registration.components.DocumentTypeTabs
import com.cafeteros.historia.ui.features.farmer_registration.components.EmailVerifyField
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledDateField
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledDropdown
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledTextField
import com.cafeteros.historia.ui.features.farmer_registration.components.PhotoUploadSlot
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationActionBar
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationStepHeader
import com.cafeteros.historia.ui.features.farmer_registration.components.SectionHeader
import com.cafeteros.historia.ui.features.farmer_registration.data.ColombianGeography
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationFormState
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationStep
import com.cafeteros.historia.ui.features.farmer_registration.model.Gender
import com.cafeteros.historia.ui.features.farmer_registration.model.PhotoSlot
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Paso 1 del registro del caficultor: "Tus datos personales".
 *
 * Es stateless: recibe el [state] y los callbacks; toda la lógica vive en
 * el [com.cafeteros.historia.ui.features.farmer_registration.FarmerRegistrationViewModel].
 */
@Composable
fun PersonalDataStep(
    modifier: Modifier = Modifier,
    state: FarmerRegistrationFormState,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onIdentificationTypeChange: (com.cafeteros.historia.ui.features.farmer_registration.model.IdentificationType) -> Unit,
    onDocumentNumberChange: (String) -> Unit,
    onDocumentIssuedAtChange: (Long) -> Unit,
    onFirstNamesChange: (String) -> Unit,
    onLastNamesChange: (String) -> Unit,
    onBirthDateChange: (Long) -> Unit,
    onGenderChange: (Gender) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onRequestEmailOtp: () -> String,
    onVerifyEmailOtp: (String) -> Boolean,
    onShowDemoOtpToast: (String) -> Unit,
    onDepartmentChange: (String?) -> Unit,
    onCityChange: (String?) -> Unit,
    onResidenceAddressChange: (String) -> Unit,
    onPhotoSelected: (PhotoSlot, android.net.Uri) -> Unit
) {
    val cityOptions = ColombianGeography.citiesFor(state.departmentCode)
    val selectedDepartmentLabel = ColombianGeography.departmentLabel(state.departmentCode)
    val selectedCityLabel = ColombianGeography.cityLabel(state.departmentCode, state.cityCode)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RegistrationStepHeader(
                currentStepNumber = FarmerRegistrationStep.PersonalData.number,
                totalSteps = FarmerRegistrationStep.TOTAL,
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = BrandSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                Spacer(modifier = Modifier.height(BrandSpacing.sm))

                StepTitleAndIntro()

                // ── IDENTIFICACIÓN ──
                SectionHeader(text = "IDENTIFICACIÓN")
                DocumentTypeTabs(
                    selected = state.identificationType,
                    onSelect = onIdentificationTypeChange
                )
                LabeledTextField(
                    label = "NÚMERO DE DOCUMENTO",
                    value = state.documentNumber,
                    onValueChange = onDocumentNumberChange,
                    placeholder = "Ej. 1023456789",
                    keyboardType = KeyboardType.Number
                )
                LabeledDateField(
                    label = "FECHA DE EXPEDICIÓN",
                    epochMillis = state.documentIssuedAtEpochMillis,
                    onDateSelected = onDocumentIssuedAtChange
                )

                // ── INFORMACIÓN CIVIL ──
                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionHeader(text = "INFORMACIÓN CIVIL")
                LabeledTextField(
                    label = "NOMBRES",
                    value = state.firstNames,
                    onValueChange = onFirstNamesChange,
                    placeholder = "Tus nombres"
                )
                LabeledTextField(
                    label = "APELLIDOS",
                    value = state.lastNames,
                    onValueChange = onLastNamesChange,
                    placeholder = "Tus apellidos"
                )
                LabeledDateField(
                    label = "FECHA DE NACIMIENTO",
                    epochMillis = state.birthDateEpochMillis,
                    onDateSelected = onBirthDateChange
                )
                LabeledDropdown(
                    label = "GÉNERO",
                    selectedLabel = state.gender?.label,
                    placeholder = "Seleccionar",
                    options = Gender.entries.map { it.name to it.label },
                    onSelect = { key, _ -> onGenderChange(Gender.valueOf(key)) }
                )

                // ── CONTACTO ──
                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionHeader(text = "CONTACTO")
                LabeledTextField(
                    label = "TELÉFONO MÓVIL",
                    value = state.phoneNumber,
                    onValueChange = onPhoneChange,
                    placeholder = "300 000 0000",
                    prefix = "+57",
                    keyboardType = KeyboardType.Phone
                )
                EmailVerifyField(
                    email = state.email,
                    onEmailChange = onEmailChange,
                    isVerified = state.emailVerified,
                    onRequestVerification = onRequestEmailOtp,
                    onVerifyOtp = onVerifyEmailOtp,
                    onShowDemoCode = onShowDemoOtpToast
                )

                // ── RESIDENCIA ACTUAL ──
                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionHeader(text = "RESIDENCIA ACTUAL")
                LabeledDropdown(
                    label = "DEPARTAMENTO",
                    selectedLabel = selectedDepartmentLabel,
                    placeholder = "Seleccionar depto",
                    options = ColombianGeography.departments,
                    onSelect = { key, _ -> onDepartmentChange(key) }
                )
                LabeledDropdown(
                    label = "MUNICIPIO / CIUDAD",
                    selectedLabel = selectedCityLabel,
                    placeholder = if (state.departmentCode == null) {
                        "Selecciona un depto primero"
                    } else {
                        "Seleccionar ciudad"
                    },
                    options = cityOptions,
                    onSelect = { key, _ -> onCityChange(key) }
                )
                LabeledTextField(
                    label = "DIRECCIÓN DE RESIDENCIA",
                    value = state.residenceAddress,
                    onValueChange = onResidenceAddressChange,
                    placeholder = "Barrio, vereda o finca…"
                )

                // ── DOCUMENTACIÓN FOTOGRÁFICA ──
                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionHeader(text = "DOCUMENTACIÓN FOTOGRÁFICA")
                PhotoUploadSlot(
                    title = "CÉDULA FRONTAL",
                    helperText = "PNG, JPG hasta 5 MB",
                    photoUri = state.documentFrontPhotoUri,
                    onPhotoSelected = { uri ->
                        onPhotoSelected(PhotoSlot.DOCUMENT_FRONT, uri)
                    }
                )
                PhotoUploadSlot(
                    title = "CÉDULA POSTERIOR",
                    helperText = "PNG, JPG hasta 5 MB",
                    photoUri = state.documentBackPhotoUri,
                    onPhotoSelected = { uri ->
                        onPhotoSelected(PhotoSlot.DOCUMENT_BACK, uri)
                    }
                )
                PhotoUploadSlot(
                    title = "SELFIE CON CÉDULA",
                    helperText = "Asegura buena luz",
                    photoUri = state.selfieWithDocumentUri,
                    onPhotoSelected = { uri ->
                        onPhotoSelected(PhotoSlot.SELFIE_WITH_DOCUMENT, uri)
                    }
                )

                Spacer(modifier = Modifier.height(BrandSpacing.lg))
            }

            RegistrationActionBar(
                onBackClick = onBack,
                onContinueClick = onContinue,
                // Modo desarrollo del front: el botón siempre está activo para
                // poder navegar entre los 5 pasos sin frenos. Restaurar
                // `state.canSubmitPersonalData()` cuando se quiera enforce.
                continueEnabled = true
            )
        }
    }
}

/** Título grande "Tus datos personales" + intro corta. */
@Composable
private fun StepTitleAndIntro() {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)) {
        Text(
            text = "Tus datos personales",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
        Text(
            text = "Comencemos con la información básica para validar tu identidad " +
                    "como caficultor profesional.",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = BrandColors.TextSecondary
            )
        )
    }
}

/**
 * Validación de "Continuar" para el paso 1.
 *
 * **Modo testing**: hoy solo exige los campos esenciales (nombres, apellidos
 * y un correo con `@`) para facilitar las pruebas del flujo. Cuando el
 * formulario esté pulido, restaurar la validación completa que comprobaba
 * documento, fechas, género, dirección y `emailVerified`. La función
 * preserva en comentarios la versión estricta para no perder la lista.
 */
private fun FarmerRegistrationFormState.canSubmitPersonalData(): Boolean {
    // Versión estricta (la queremos cuando vayamos a producción):
    //   documentNumber.isNotBlank() &&
    //           documentIssuedAtEpochMillis != null &&
    //           firstNames.isNotBlank() &&
    //           lastNames.isNotBlank() &&
    //           birthDateEpochMillis != null &&
    //           gender != null &&
    //           phoneNumber.length >= 7 &&
    //           email.isNotBlank() &&
    //           emailVerified &&
    //           departmentCode != null &&
    //           cityCode != null &&
    //           residenceAddress.isNotBlank()
    return firstNames.isNotBlank() &&
            lastNames.isNotBlank() &&
            email.contains('@')
}

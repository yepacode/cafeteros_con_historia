package com.cafeteros.historia.ui.features.farmer_registration

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cafeteros.historia.data.repository.RegisterResult
import com.cafeteros.historia.ui.features.farmer_registration.data.ColombianGeography
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationStep
import com.cafeteros.historia.ui.features.farmer_registration.steps.FarmDetailsStep
import com.cafeteros.historia.ui.features.farmer_registration.steps.PersonalDataStep
import com.cafeteros.historia.ui.features.farmer_registration.steps.PlaceholderStep
import com.cafeteros.historia.ui.features.farmer_registration.steps.VerificationStatusStep
import com.cafeteros.historia.ui.features.farmer_registration.steps.VerificationStep
import com.cafeteros.historia.ui.features.farmer_registration.steps.WelcomeApprovedStep
import kotlinx.coroutines.launch

/**
 * Pantalla raíz del flujo de registro del caficultor.
 *
 * Maneja la transición animada entre los 5 pasos. Solo el paso 1 está
 * implementado; los demás muestran [PlaceholderStep] hasta que se diseñen.
 *
 * @param modifier modifier opcional.
 * @param onClose callback para cerrar el flujo (ej. al pulsar atrás en el
 *  primer paso). Generalmente devuelve al usuario al onboarding caficultor.
 * @param onFinishToPanel callback al completar el flujo (botones "Ir a mi
 *  panel" / "Explorar la app primero" de [WelcomeApprovedStep]). Debe
 *  navegar al panel del usuario (MainActivity).
 */
@Composable
fun FarmerRegistrationScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onFinishToPanel: () -> Unit
) {
    val viewModel: FarmerRegistrationViewModel = viewModel()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val handleBack: () -> Unit = {
        val moved = viewModel.goToPreviousStep()
        if (!moved) onClose()
    }

    /**
     * Continuar del paso 4 (Verificación documental): persiste el usuario en
     * la base local con datos mínimos (correo, nombre, teléfono, rol) y
     * recién entonces avanza a la pantalla "Estamos revisando tu finca".
     * Si el correo ya existe, lo informamos por Toast y NO avanzamos.
     */
    val handleVerificationContinue: () -> Unit = {
        scope.launch {
            when (val result = viewModel.registerCurrentUser()) {
                is RegisterResult.Success -> viewModel.goToNextStep()
                RegisterResult.EmailAlreadyExists -> Toast.makeText(
                    context,
                    "Ese correo ya tiene una cuenta. Inicia sesión.",
                    Toast.LENGTH_LONG
                ).show()
                RegisterResult.InvalidEmail -> Toast.makeText(
                    context,
                    "El correo tiene un formato inválido.",
                    Toast.LENGTH_LONG
                ).show()
                RegisterResult.WeakPassword -> Toast.makeText(
                    context,
                    "La contraseña debe tener al menos 6 caracteres.",
                    Toast.LENGTH_LONG
                ).show()
                is RegisterResult.UnknownError -> Toast.makeText(
                    context,
                    "No se pudo crear la cuenta: ${result.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // NOTA: el BackHandler estaba interceptando el back del sistema, pero
    // parece estar causando que ciertos clicks cierren la Activity. Lo
    // dejamos desactivado mientras se diagnostica. Re-activar cuando se
    // confirme que no causa regresiones.

    AnimatedContent(
        targetState = currentStep,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "farmerRegistrationStep",
        modifier = modifier
    ) { step ->
        when (step) {
            FarmerRegistrationStep.PersonalData -> PersonalDataStep(
                state = formState,
                onBack = handleBack,
                onContinue = viewModel::goToNextStep,
                onIdentificationTypeChange = viewModel::setIdentificationType,
                onDocumentNumberChange = viewModel::setDocumentNumber,
                onDocumentIssuedAtChange = viewModel::setDocumentIssuedAt,
                onFirstNamesChange = viewModel::setFirstNames,
                onLastNamesChange = viewModel::setLastNames,
                onBirthDateChange = viewModel::setBirthDate,
                onGenderChange = viewModel::setGender,
                onPhoneChange = viewModel::setPhoneNumber,
                onEmailChange = viewModel::setEmail,
                onRequestEmailOtp = viewModel::requestEmailOtp,
                onVerifyEmailOtp = viewModel::verifyEmailOtp,
                onShowDemoOtpToast = { code ->
                    // Modo demo: mostramos el código por Toast porque no hay
                    // backend real que envíe el correo. Reemplazar por una
                    // llamada al servidor cuando exista.
                    Toast.makeText(
                        context,
                        "Código demo enviado: $code",
                        Toast.LENGTH_LONG
                    ).show()
                },
                onDepartmentChange = viewModel::setDepartmentCode,
                onCityChange = viewModel::setCityCode,
                onResidenceAddressChange = viewModel::setResidenceAddress,
                onPhotoSelected = viewModel::setPhotoForSlot
            )

            FarmerRegistrationStep.VerificationStatus -> VerificationStatusStep(
                userFirstName = formState.firstNames
                    .trim()
                    .takeIf { it.isNotBlank() }
                    ?.substringBefore(' '),
                onContactSupport = {
                    // TODO: abrir línea de soporte (Intent.ACTION_DIAL al 018000-ORIGEN
                    // o pantalla de chat cuando exista).
                },
                onLogout = onClose,
                onAdvanceDemo = viewModel::goToNextStep
            )

            FarmerRegistrationStep.Verification -> VerificationStep(
                state = formState,
                onBack = handleBack,
                onContinue = handleVerificationContinue,
                onMandatoryDocumentPicked = { type, uri ->
                    viewModel.setMandatoryDocument(type, uri)
                },
                onToggleChamberOfCommerceNotApplicable = viewModel::toggleChamberOfCommerceNotApplicable,
                onOptionalDocumentPicked = { type, uri ->
                    viewModel.setOptionalDocument(type, uri)
                }
            )

            FarmerRegistrationStep.FarmDetails -> FarmDetailsStep(
                state = formState,
                onBack = handleBack,
                onContinue = viewModel::goToNextStep,
                onChangeLocation = handleBack,
                onFarmNameChange = viewModel::setFarmName,
                onYearsExperienceChange = viewModel::setYearsOfCoffeeExperience,
                onAltitudeChange = viewModel::setAltitudeMasl,
                onTotalAreaChange = viewModel::setTotalAreaHectares,
                onCultivatedAreaChange = viewModel::setCultivatedAreaHectares,
                onAnnualProductionChange = viewModel::setEstimatedAnnualProductionKg,
                onToggleVariety = viewModel::toggleCultivatedVariety,
                onToggleProcess = viewModel::toggleProcess,
                onSelectHarvestMethod = viewModel::setHarvestMethod,
                onHasAwardsChange = viewModel::setHasAwards
            )

            FarmerRegistrationStep.Approved -> WelcomeApprovedStep(
                userFirstName = formState.firstNames
                    .trim()
                    .takeIf { it.isNotBlank() }
                    ?.substringBefore(' '),
                farmName = formState.farmName,
                regionLabel = ColombianGeography.departmentLabel(formState.departmentCode),
                onGoToPanel = onFinishToPanel,
                onExplore = onFinishToPanel
            )

            else -> PlaceholderStep(
                step = step,
                onBack = handleBack,
                onContinue = viewModel::goToNextStep
            )
        }
    }
}

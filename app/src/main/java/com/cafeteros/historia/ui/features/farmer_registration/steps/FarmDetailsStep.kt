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
import com.cafeteros.historia.ui.features.farmer_registration.components.InfoBanner
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledSwitch
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledTextField
import com.cafeteros.historia.ui.features.farmer_registration.components.MultiSelectChips
import com.cafeteros.historia.ui.features.farmer_registration.components.RadioCardOptionList
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationActionBar
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationStepHeader
import com.cafeteros.historia.ui.features.farmer_registration.components.SectionHeader
import com.cafeteros.historia.ui.features.farmer_registration.data.ColombianGeography
import com.cafeteros.historia.ui.features.farmer_registration.model.CoffeeProcess
import com.cafeteros.historia.ui.features.farmer_registration.model.CoffeeVariety
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationFormState
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationStep
import com.cafeteros.historia.ui.features.farmer_registration.model.HarvestMethod
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Paso 2 del registro del caficultor: "Cuéntanos sobre tu finca".
 *
 * Captura datos públicos del perfil del caficultor: nombre comercial,
 * experiencia, altitud, áreas, producción, variedades cultivadas,
 * procesos, método de cosecha y reconocimientos.
 *
 * El banner informativo lee la ubicación capturada en el paso 1
 * ([FarmerRegistrationFormState.departmentCode]) y la muestra como "zona
 * cafetera detectada". El CTA "CAMBIAR UBICACIÓN" navega de vuelta al paso 1.
 */
@Composable
fun FarmDetailsStep(
    modifier: Modifier = Modifier,
    state: FarmerRegistrationFormState,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onChangeLocation: () -> Unit,
    onFarmNameChange: (String) -> Unit,
    onYearsExperienceChange: (String) -> Unit,
    onAltitudeChange: (String) -> Unit,
    onTotalAreaChange: (String) -> Unit,
    onCultivatedAreaChange: (String) -> Unit,
    onAnnualProductionChange: (String) -> Unit,
    onToggleVariety: (CoffeeVariety) -> Unit,
    onToggleProcess: (CoffeeProcess) -> Unit,
    onSelectHarvestMethod: (HarvestMethod) -> Unit,
    onHasAwardsChange: (Boolean) -> Unit
) {
    val coffeeRegionLabel = ColombianGeography.departmentLabel(state.departmentCode)
        ?: "tu región"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RegistrationStepHeader(
                currentStepNumber = FarmerRegistrationStep.FarmDetails.number,
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

                LabeledTextField(
                    label = "NOMBRE DE LA FINCA",
                    value = state.farmName,
                    onValueChange = onFarmNameChange,
                    placeholder = "Ej. Finca La Esperanza",
                    helperText = "Este es el nombre que verán los compradores."
                )

                LabeledTextField(
                    label = "EXPERIENCIA CAFETERA",
                    value = state.yearsOfCoffeeExperience,
                    onValueChange = onYearsExperienceChange,
                    placeholder = "10",
                    keyboardType = KeyboardType.Number,
                    suffix = "años"
                )

                LabeledTextField(
                    label = "ALTITUD (MSNM)",
                    value = state.altitudeMasl,
                    onValueChange = onAltitudeChange,
                    placeholder = "1850",
                    keyboardType = KeyboardType.Number,
                    suffix = "msnm"
                )

                LabeledTextField(
                    label = "ÁREA TOTAL",
                    value = state.totalAreaHectares,
                    onValueChange = onTotalAreaChange,
                    placeholder = "5.5",
                    keyboardType = KeyboardType.Decimal,
                    suffix = "hectáreas"
                )

                LabeledTextField(
                    label = "ÁREA CULTIVADA",
                    value = state.cultivatedAreaHectares,
                    onValueChange = onCultivatedAreaChange,
                    placeholder = "4.0",
                    keyboardType = KeyboardType.Decimal,
                    suffix = "hectáreas"
                )

                LabeledTextField(
                    label = "PRODUCCIÓN ANUAL ESTIMADA",
                    value = state.estimatedAnnualProductionKg,
                    onValueChange = onAnnualProductionChange,
                    placeholder = "2500",
                    keyboardType = KeyboardType.Number,
                    suffix = "kilos",
                    helperText = "Referencia: Café pergamino seco."
                )

                InfoBanner(
                    leadingText = "Según tu ubicación registrada, tu café pertenece a la zona cafetera de:",
                    highlight = coffeeRegionLabel,
                    actionLabel = "CAMBIAR UBICACIÓN",
                    onActionClick = onChangeLocation
                )

                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionHeader(text = "VARIEDADES QUE CULTIVAS")
                MultiSelectChips(
                    options = CoffeeVariety.entries,
                    selected = state.cultivatedVarieties,
                    onToggle = onToggleVariety,
                    optionLabel = { it.label }
                )

                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionHeader(text = "PROCESOS QUE MANEJAS")
                MultiSelectChips(
                    options = CoffeeProcess.entries,
                    selected = state.processes,
                    onToggle = onToggleProcess,
                    optionLabel = { it.label }
                )

                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionHeader(text = "MÉTODO DE COSECHA")
                RadioCardOptionList(
                    options = HarvestMethod.entries,
                    selected = state.harvestMethod,
                    onSelect = onSelectHarvestMethod,
                    optionLabel = { it.label }
                )

                Spacer(modifier = Modifier.height(BrandSpacing.md))
                LabeledSwitch(
                    title = "¿Reconocimientos o premios?",
                    description = "Si tu café ha sido galardonado en Taza de la Excelencia u otros.",
                    checked = state.hasAwards,
                    onCheckedChange = onHasAwardsChange
                )

                Spacer(modifier = Modifier.height(BrandSpacing.lg))
            }

            RegistrationActionBar(
                onBackClick = onBack,
                onContinueClick = onContinue,
                // Modo desarrollo del front: siempre activo para permitir
                // navegar el flujo sin frenos. Restaurar
                // `state.canSubmitFarmDetails()` cuando se quiera enforce.
                continueEnabled = true
            )
        }
    }
}

/** Título grande "Cuéntanos sobre tu finca" + intro corta. */
@Composable
private fun StepTitleAndIntro() {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)) {
        Text(
            text = "Cuéntanos sobre tu finca",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                color = BrandColors.TextPrimary
            )
        )
        Text(
            text = "Estos datos aparecerán en tu perfil público para conectar " +
                    "con compradores.",
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
 * Validación mínima para habilitar "Continuar":
 *  - Nombre, experiencia, altitud, áreas y producción no vacíos.
 *  - Al menos 1 variedad y 1 proceso seleccionados.
 *  - Método de cosecha elegido.
 *
 * No exige reconocimientos (es un toggle opcional).
 */
private fun FarmerRegistrationFormState.canSubmitFarmDetails(): Boolean =
    farmName.isNotBlank() &&
            yearsOfCoffeeExperience.isNotBlank() &&
            altitudeMasl.isNotBlank() &&
            totalAreaHectares.isNotBlank() &&
            cultivatedAreaHectares.isNotBlank() &&
            estimatedAnnualProductionKg.isNotBlank() &&
            cultivatedVarieties.isNotEmpty() &&
            processes.isNotEmpty() &&
            harvestMethod != null

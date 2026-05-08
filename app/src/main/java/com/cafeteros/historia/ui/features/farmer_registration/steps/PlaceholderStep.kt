package com.cafeteros.historia.ui.features.farmer_registration.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationActionBar
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationStepHeader
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationStep
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography

/**
 * Placeholder visible para los pasos 2–5 del registro mientras se diseñan.
 *
 * Mantiene el header y la action bar para que la navegación entre pasos
 * funcione (Atrás / Continuar). El contenido principal es una breve pantalla
 * "Próximamente" con el título del paso.
 *
 * @param modifier modifier opcional.
 * @param step paso al que corresponde este placeholder.
 * @param onBack callback de Atrás.
 * @param onContinue callback de Continuar.
 */
@Composable
fun PlaceholderStep(
    modifier: Modifier = Modifier,
    step: FarmerRegistrationStep,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RegistrationStepHeader(
                currentStepNumber = step.number,
                totalSteps = FarmerRegistrationStep.TOTAL,
                onBack = onBack
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(BrandSpacing.lg),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Construction,
                        contentDescription = null,
                        tint = BrandColors.TextSecondary,
                        modifier = Modifier.padding(BrandSpacing.md)
                    )
                    Text(
                        text = step.title,
                        style = BrandTypography.OnboardingTitle,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Próximamente — paso ${step.number} de ${FarmerRegistrationStep.TOTAL}",
                        style = BrandTypography.OnboardingDescription,
                        textAlign = TextAlign.Center
                    )
                }
            }

            val isLast = step == FarmerRegistrationStep.VerificationStatus
            RegistrationActionBar(
                onBackClick = onBack,
                onContinueClick = onContinue,
                continueLabel = if (isLast) "Finalizar" else "Continuar"
            )
        }
    }
}

package com.cafeteros.historia.ui.features.farmer_registration.steps

import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_registration.components.HelpFooter
import com.cafeteros.historia.ui.features.farmer_registration.components.MandatoryDocumentCard
import com.cafeteros.historia.ui.features.farmer_registration.components.OptionalDocumentRow
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationActionBar
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationStepHeader
import com.cafeteros.historia.ui.features.farmer_registration.components.SectionHeader
import com.cafeteros.historia.ui.features.farmer_registration.components.TrustBanner
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationFormState
import com.cafeteros.historia.ui.features.farmer_registration.model.FarmerRegistrationStep
import com.cafeteros.historia.ui.features.farmer_registration.model.MandatoryDocumentType
import com.cafeteros.historia.ui.features.farmer_registration.model.OptionalDocumentType
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Paso 4 del registro del caficultor: "Verifica tu condición de caficultor".
 *
 * Captura los documentos legales y opcionales que el equipo de Origen usará
 * para validar la finca y emitir factura. Los obligatorios bloquean el
 * "Continuar" (excepto Cámara de Comercio si se marca "No aplica"); los
 * opcionales no son requeridos pero se persisten igual.
 */
@Composable
fun VerificationStep(
    modifier: Modifier = Modifier,
    state: FarmerRegistrationFormState,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onMandatoryDocumentPicked: (MandatoryDocumentType, Uri) -> Unit,
    onToggleChamberOfCommerceNotApplicable: () -> Unit,
    onOptionalDocumentPicked: (OptionalDocumentType, Uri) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RegistrationStepHeader(
                currentStepNumber = FarmerRegistrationStep.Verification.number,
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

                TrustBanner()

                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionHeader(text = "OBLIGATORIOS")

                MandatoryDocumentCard(
                    icon = Icons.Outlined.Article,
                    title = MandatoryDocumentType.RUT.title,
                    description = MandatoryDocumentType.RUT.description,
                    uri = state.mandatoryDocuments[MandatoryDocumentType.RUT],
                    onPicked = { uri ->
                        onMandatoryDocumentPicked(MandatoryDocumentType.RUT, uri)
                    }
                )

                MandatoryDocumentCard(
                    icon = Icons.Outlined.Apartment,
                    title = MandatoryDocumentType.CHAMBER_OF_COMMERCE.title,
                    description = MandatoryDocumentType.CHAMBER_OF_COMMERCE.description,
                    uri = state.mandatoryDocuments[MandatoryDocumentType.CHAMBER_OF_COMMERCE],
                    notApplicable = state.chamberOfCommerceNotApplicable,
                    showNotApplicableToggle = true,
                    onPicked = { uri ->
                        onMandatoryDocumentPicked(MandatoryDocumentType.CHAMBER_OF_COMMERCE, uri)
                    },
                    onToggleNotApplicable = onToggleChamberOfCommerceNotApplicable
                )

                MandatoryDocumentCard(
                    icon = Icons.Outlined.Description,
                    title = MandatoryDocumentType.PROPERTY_DEED.title,
                    description = MandatoryDocumentType.PROPERTY_DEED.description,
                    uri = state.mandatoryDocuments[MandatoryDocumentType.PROPERTY_DEED],
                    onPicked = { uri ->
                        onMandatoryDocumentPicked(MandatoryDocumentType.PROPERTY_DEED, uri)
                    }
                )

                MandatoryDocumentCard(
                    icon = Icons.Outlined.Coffee,
                    title = MandatoryDocumentType.FNC_CERTIFICATE.title,
                    description = MandatoryDocumentType.FNC_CERTIFICATE.description,
                    uri = state.mandatoryDocuments[MandatoryDocumentType.FNC_CERTIFICATE],
                    onPicked = { uri ->
                        onMandatoryDocumentPicked(MandatoryDocumentType.FNC_CERTIFICATE, uri)
                    }
                )

                Spacer(modifier = Modifier.height(BrandSpacing.sm))
                SectionHeader(text = "OPCIONALES PERO SUMAN")

                OptionalDocumentRow(
                    icon = Icons.Outlined.MilitaryTech,
                    label = OptionalDocumentType.QUALITY_CERTIFICATIONS.title,
                    uri = state.optionalDocuments[OptionalDocumentType.QUALITY_CERTIFICATIONS],
                    onPicked = { uri ->
                        onOptionalDocumentPicked(OptionalDocumentType.QUALITY_CERTIFICATIONS, uri)
                    }
                )

                OptionalDocumentRow(
                    icon = Icons.Outlined.Insights,
                    label = OptionalDocumentType.QUALITY_ANALYSIS.title,
                    uri = state.optionalDocuments[OptionalDocumentType.QUALITY_ANALYSIS],
                    onPicked = { uri ->
                        onOptionalDocumentPicked(OptionalDocumentType.QUALITY_ANALYSIS, uri)
                    }
                )

                OptionalDocumentRow(
                    icon = Icons.Outlined.EmojiEvents,
                    label = OptionalDocumentType.AWARDS_RECOGNITIONS.title,
                    uri = state.optionalDocuments[OptionalDocumentType.AWARDS_RECOGNITIONS],
                    onPicked = { uri ->
                        onOptionalDocumentPicked(OptionalDocumentType.AWARDS_RECOGNITIONS, uri)
                    }
                )

                Spacer(modifier = Modifier.height(BrandSpacing.md))
                HelpFooter()

                Spacer(modifier = Modifier.height(BrandSpacing.lg))
            }

            RegistrationActionBar(
                onBackClick = onBack,
                onContinueClick = onContinue,
                // Modo desarrollo del front: siempre activo. Restaurar
                // `state.canSubmitVerification()` cuando se quiera enforce
                // (RUT + Tradición + FNC + Cámara o "No aplica").
                continueEnabled = true
            )
        }
    }
}

/** Título grande "Verifica tu condición de caficultor" + intro. */
@Composable
private fun StepTitleAndIntro() {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)) {
        Text(
            text = "Verifica tu condición de caficultor",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                color = BrandColors.TextPrimary
            )
        )
        Text(
            text = "Estos documentos nos ayudan a validar tu finca y generar tu " +
                    "factura. Toda tu información es confidencial.",
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
 * Validación de "Continuar":
 *  - RUT, Certificado de Tradición y Certificado FNC obligatorios.
 *  - Cámara de Comercio: subido o marcado "No aplica".
 */
private fun FarmerRegistrationFormState.canSubmitVerification(): Boolean {
    val rut = mandatoryDocuments.containsKey(MandatoryDocumentType.RUT)
    val chamber = mandatoryDocuments.containsKey(MandatoryDocumentType.CHAMBER_OF_COMMERCE) ||
            chamberOfCommerceNotApplicable
    val deed = mandatoryDocuments.containsKey(MandatoryDocumentType.PROPERTY_DEED)
    val fnc = mandatoryDocuments.containsKey(MandatoryDocumentType.FNC_CERTIFICATE)
    return rut && chamber && deed && fnc
}

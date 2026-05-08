package com.cafeteros.historia.ui.features.farmer_products.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_products.components.WizardHeader
import com.cafeteros.historia.ui.features.farmer_products.model.CoffeeFormat
import com.cafeteros.historia.ui.features.farmer_products.model.DEFAULT_WEIGHT_OPTIONS_GRAMS
import com.cafeteros.historia.ui.features.farmer_products.model.ProductCreationStep
import com.cafeteros.historia.ui.features.farmer_products.model.ProductFormState
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledTextField
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationActionBar
import com.cafeteros.historia.ui.features.farmer_registration.components.SectionHeader
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Paso 2 del wizard: precio, formato, peso, stock y certificación.
 *
 * Compacta los pasos 3 (presentaciones+precio) y 5 (inventario) del Stitch
 * en una sola pantalla, usando una sola variante por producto. Si en el
 * futuro se necesita matriz de variantes (ej. 250g/500g/1kg en grano y
 * molido al mismo tiempo), este paso se rediseña — hoy mantenemos simple.
 */
@Composable
fun ProductPricingStep(
    modifier: Modifier = Modifier,
    state: ProductFormState,
    isEditing: Boolean,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onFormatChange: (CoffeeFormat) -> Unit,
    onWeightChange: (Int) -> Unit,
    onPriceChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onOrganicChange: (Boolean) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth().background(BrandColors.AuthBackground)) {
        WizardHeader(
            title = if (isEditing) "Editar producto" else "Nuevo producto",
            currentStep = ProductCreationStep.Pricing,
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

            SectionHeader(text = "PRESENTACIÓN")
            FormatSelector(selected = state.format, onSelect = onFormatChange)

            Spacer(modifier = Modifier.height(BrandSpacing.xs))
            SectionHeader(text = "PESO POR UNIDAD")
            WeightSelector(
                options = DEFAULT_WEIGHT_OPTIONS_GRAMS,
                selectedGrams = state.weightGrams,
                onSelect = onWeightChange
            )

            Spacer(modifier = Modifier.height(BrandSpacing.xs))
            SectionHeader(text = "PRECIO E INVENTARIO")
            LabeledTextField(
                label = "PRECIO POR UNIDAD",
                value = state.priceCopInput,
                onValueChange = onPriceChange,
                placeholder = "48000",
                keyboardType = KeyboardType.Number,
                prefix = "COP $"
            )
            LabeledTextField(
                label = "STOCK DISPONIBLE",
                value = state.stockUnitsInput,
                onValueChange = onStockChange,
                placeholder = "0",
                keyboardType = KeyboardType.Number,
                suffix = "unidades"
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            SectionHeader(text = "CERTIFICACIONES")
            OrganicCertificationRow(
                checked = state.isOrganic,
                onCheckedChange = onOrganicChange
            )

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }

        RegistrationActionBar(
            onBackClick = onBack,
            onContinueClick = onContinue,
            continueEnabled = true,
            continueLabel = "Siguiente"
        )
    }
}

/** Selector binario "En grano" / "Molido medio". */
@Composable
private fun FormatSelector(selected: CoffeeFormat, onSelect: (CoffeeFormat) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        CoffeeFormat.entries.forEach { format ->
            FormatChip(
                modifier = Modifier.weight(1f),
                label = format.label,
                isSelected = format == selected,
                onClick = { onSelect(format) }
            )
        }
    }
}

@Composable
private fun FormatChip(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = if (isSelected) BrandColors.FarmerPrimary else BrandColors.InputBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) BrandColors.PrimaryButtonText else BrandColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/** Selector de peso entre los 3 valores predeterminados (250 / 500 / 1000g). */
@Composable
private fun WeightSelector(
    options: List<Int>,
    selectedGrams: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        options.forEach { grams ->
            FormatChip(
                modifier = Modifier.weight(1f),
                label = formatGramsLabel(grams),
                isSelected = grams == selectedGrams,
                onClick = { onSelect(grams) }
            )
        }
    }
}

/** "1000" → "1kg", "250" → "250g". */
private fun formatGramsLabel(grams: Int): String =
    if (grams >= 1000 && grams % 1000 == 0) "${grams / 1000}kg" else "${grams}g"

/** Checkbox single-line "Orgánico". Se puede ampliar a más certificaciones después. */
@Composable
private fun OrganicCertificationRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = BrandSpacing.md, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = BrandColors.FarmerPrimary,
                uncheckedColor = BrandColors.CheckboxBorder
            )
        )
        Text(
            text = "Orgánico",
            color = BrandColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

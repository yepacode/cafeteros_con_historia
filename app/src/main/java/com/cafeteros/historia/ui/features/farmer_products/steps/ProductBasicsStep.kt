package com.cafeteros.historia.ui.features.farmer_products.steps

import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_products.components.WizardHeader
import com.cafeteros.historia.ui.features.farmer_products.model.ProductCategory
import com.cafeteros.historia.ui.features.farmer_products.model.ProductCreationStep
import com.cafeteros.historia.ui.features.farmer_products.model.ProductFormState
import com.cafeteros.historia.ui.features.farmer_products.model.SUGGESTED_TASTING_NOTES
import com.cafeteros.historia.ui.features.farmer_products.model.SUGGESTED_VARIETIES
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledDropdown
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledTextField
import com.cafeteros.historia.ui.features.farmer_registration.components.MultiSelectChips
import com.cafeteros.historia.ui.features.farmer_registration.components.PhotoUploadSlot
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationActionBar
import com.cafeteros.historia.ui.features.farmer_registration.components.SectionHeader
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Paso 1 del wizard: detalles del producto.
 *
 * Combina el "Detalles del Origen" (paso 1 Stitch) y "Detalles del café"
 * (paso 2 Stitch) en una sola pantalla scrollable. Captura nombre,
 * categoría, descripciones, tags, variedades y notas de cata.
 */
@Composable
fun ProductBasicsStep(
    modifier: Modifier = Modifier,
    state: ProductFormState,
    isEditing: Boolean,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onPhotoSelected: (Uri) -> Unit,
    onNameChange: (String) -> Unit,
    onCategoryChange: (ProductCategory) -> Unit,
    onShortDescriptionChange: (String) -> Unit,
    onFullDescriptionChange: (String) -> Unit,
    onTagInputChange: (String) -> Unit,
    onTagSubmit: () -> Unit,
    onTagRemove: (String) -> Unit,
    onToggleVariety: (String) -> Unit,
    onToggleTastingNote: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth().background(BrandColors.AuthBackground)) {
        WizardHeader(
            title = if (isEditing) "Editar producto" else "Nuevo producto",
            currentStep = ProductCreationStep.Basics,
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

            SectionHeader(text = "FOTO PRINCIPAL")
            PhotoUploadSlot(
                title = "FOTO DEL PRODUCTO",
                helperText = "PNG o JPG. Mínimo 1200×1200 para verse nítida.",
                icon = Icons.Outlined.PhotoCamera,
                photoUri = state.photoUri,
                onPhotoSelected = onPhotoSelected
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            SectionHeader(text = "INFORMACIÓN BÁSICA")
            LabeledTextField(
                label = "NOMBRE DEL PRODUCTO",
                value = state.name,
                onValueChange = onNameChange,
                placeholder = "Ej: Café Huila Pitalito 250g",
                helperText = "Sé específico. Incluye zona y presentación."
            )
            LabeledDropdown(
                label = "CATEGORÍA",
                selectedLabel = state.category.label,
                placeholder = "Seleccionar categoría",
                options = ProductCategory.entries.map { it.name to it.label },
                onSelect = { key, _ -> onCategoryChange(ProductCategory.valueOf(key)) }
            )

            LabeledTextField(
                label = "DESCRIPCIÓN CORTA",
                value = state.shortDescription,
                onValueChange = onShortDescriptionChange,
                placeholder = "En pocas palabras, ¿qué hace especial a este café?"
            )
            LabeledTextField(
                label = "DESCRIPCIÓN COMPLETA",
                value = state.fullDescription,
                onValueChange = onFullDescriptionChange,
                placeholder = "Cuenta el origen, el proceso, las notas, el tueste…"
            )

            // Tags como chips removibles + input para añadir uno nuevo.
            SectionHeader(text = "PALABRAS CLAVE (TAGS)")
            LabeledTextField(
                label = "AÑADIR TAG",
                value = state.tagsInput,
                onValueChange = onTagInputChange,
                placeholder = "Ej: chocolate, microlote, orgánico…",
                helperText = "Pulsa Enter para añadir cada tag."
            )
            // TODO: capturar Enter del soft keyboard para llamar onTagSubmit.
            // Mientras tanto exponemos un botón explícito.
            if (state.tagsInput.isNotBlank()) {
                Text(
                    text = "+ Añadir \"${state.tagsInput.trim()}\"",
                    modifier = Modifier
                        .clickable(onClick = onTagSubmit)
                        .padding(vertical = 4.dp),
                    color = BrandColors.FarmerPrimary,
                    fontSize = 13.sp
                )
            }
            if (state.tags.isNotEmpty()) TagsRow(tags = state.tags, onRemove = onTagRemove)

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            SectionHeader(text = "VARIEDAD DEL CAFÉ")
            MultiSelectChips(
                options = SUGGESTED_VARIETIES,
                selected = state.varietyChips,
                onToggle = onToggleVariety,
                optionLabel = { it }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            SectionHeader(text = "NOTAS DE CATA")
            MultiSelectChips(
                options = SUGGESTED_TASTING_NOTES,
                selected = state.tastingNotes,
                onToggle = onToggleTastingNote,
                optionLabel = { it }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }

        RegistrationActionBar(
            onBackClick = onBack,
            onContinueClick = onContinue,
            // Demo: siempre habilitado. La validación dura está en
            // ProductFormState.canPublish() y se enforza en el paso 3.
            continueEnabled = true,
            continueLabel = "Siguiente"
        )
    }
}

/** Render simple de los tags ya añadidos como pequeños chips con botón de quitar. */
@Composable
private fun TagsRow(tags: List<String>, onRemove: (String) -> Unit) {
    val chunked = tags.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        chunked.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .background(
                                color = BrandColors.FarmerStatsBadgeBackground,
                                shape = RoundedCornerShape(50)
                            )
                            .clickable { onRemove(tag) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "#$tag",
                                color = BrandColors.TextPrimary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(0.dp))
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Quitar tag",
                                tint = BrandColors.TextSecondary,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .height(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

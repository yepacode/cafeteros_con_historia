package com.cafeteros.historia.ui.features.farmer_products.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cafeteros.historia.ui.features.farmer_products.components.WizardHeader
import com.cafeteros.historia.ui.features.farmer_products.model.ProductCreationStep
import com.cafeteros.historia.ui.features.farmer_products.model.ProductFormState
import com.cafeteros.historia.ui.features.farmer_registration.components.RegistrationActionBar
import com.cafeteros.historia.ui.features.farmer_registration.components.SectionHeader
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Paso 3 del wizard: revisión y publicación. Muestra una tarjeta con la
 * vista previa de cómo se verá el producto en el marketplace, una checklist
 * de calidad y el botón final "Publicar producto".
 *
 * El botón solo se habilita si [ProductFormState.canPublish] devuelve true
 * (nombre + precio mínimos). Lo demás es opcional para no bloquear el demo.
 */
@Composable
fun ProductPreviewStep(
    modifier: Modifier = Modifier,
    state: ProductFormState,
    isEditing: Boolean,
    onBack: () -> Unit,
    onPublish: () -> Unit
) {
    val priceFilled = (state.priceCopInput.toIntOrNull() ?: 0) > 0
    val checks: List<Pair<String, Boolean>> = listOf(
        "Foto del producto" to (state.photoUri != null),
        "Nombre del producto" to state.name.isNotBlank(),
        "Precio definido" to priceFilled,
        "Descripción" to state.shortDescription.isNotBlank(),
        "Variedad seleccionada" to state.varietyChips.isNotEmpty(),
        "Notas de cata" to state.tastingNotes.isNotEmpty()
    )

    Column(modifier = modifier.fillMaxWidth().background(BrandColors.AuthBackground)) {
        WizardHeader(
            title = if (isEditing) "Editar producto" else "Nuevo producto",
            currentStep = ProductCreationStep.Preview,
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

            Text(
                text = if (isEditing) "Revisa los cambios" else "Revisa y publica",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = "Así se verá tu producto en el marketplace.",
                color = BrandColors.TextSecondary,
                fontSize = 13.sp
            )

            ProductPreviewCard(state = state)

            Spacer(modifier = Modifier.height(BrandSpacing.xs))
            SectionHeader(text = "CHECKLIST DE CALIDAD")
            QualityChecklist(items = checks)

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }

        RegistrationActionBar(
            onBackClick = onBack,
            onContinueClick = onPublish,
            continueEnabled = state.canPublish(),
            continueLabel = if (isEditing) "Guardar cambios" else "Publicar producto"
        )
    }
}

/** Tarjeta tipo "ficha" del producto: imagen placeholder + nombre + precio + descripción. */
@Composable
private fun ProductPreviewCard(state: ProductFormState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(16.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (state.photoUri != null) {
            AsyncImage(
                model = state.photoUri,
                contentDescription = state.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
            )
        } else {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(BrandColors.InputBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📷",
                    fontSize = 36.sp
                )
            }
        }
        Text(
            text = state.name.ifBlank { "(Sin nombre)" },
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
        val price = state.priceCopInput.toIntOrNull() ?: 0
        Text(
            text = if (price > 0) "$" + "%,d".format(price).replace(',', '.') + " COP" else "Precio pendiente",
            color = BrandColors.FarmerPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        if (state.shortDescription.isNotBlank()) {
            Text(
                text = state.shortDescription,
                color = BrandColors.TextSecondary,
                fontSize = 13.sp
            )
        }
        if (state.tastingNotes.isNotEmpty()) {
            Text(
                text = "Notas: " + state.tastingNotes.joinToString(", "),
                color = BrandColors.TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

/**
 * Lista de checkpoints; cada uno muestra un check verde si está cumplido o
 * un círculo gris si todavía falta. Es informativa: no bloquea el envío
 * (la validación dura está en `state.canPublish()`).
 */
@Composable
private fun QualityChecklist(items: List<Pair<String, Boolean>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEach { (label, ok) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (ok) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = BrandColors.FarmerPrimary,
                        modifier = Modifier.height(18.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = BrandColors.IndicatorInactive,
                        modifier = Modifier.height(18.dp)
                    )
                }
                Text(
                    text = label,
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 13.sp,
                    color = if (ok) BrandColors.TextPrimary else BrandColors.TextSecondary
                )
            }
        }
    }
}

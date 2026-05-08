package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Selector multi-opción tipo "chips" con layout en cuadrícula fija (filas de
 * hasta 3 chips). Antes usaba `FlowRow` (ExperimentalLayoutApi) pero esa API
 * estaba causando `NoSuchMethodError` en runtime por desync entre la firma
 * compilada y la empaquetada en el APK con AGP 9.x.
 *
 * El estilo de cada chip varía según [isSelected]:
 *  - Seleccionado: fondo verde de marca, texto blanco.
 *  - No seleccionado: fondo gris, texto oscuro.
 *
 * @param T tipo de las opciones (típicamente un enum).
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param options opciones a mostrar.
 * @param selected conjunto de opciones actualmente seleccionadas.
 * @param onToggle callback al tocar un chip (alterna su selección).
 * @param optionLabel función para obtener la etiqueta visible de cada opción.
 */
@Composable
fun <T> MultiSelectChips(
    modifier: Modifier = Modifier,
    options: List<T>,
    selected: Set<T>,
    onToggle: (T) -> Unit,
    optionLabel: (T) -> String
) {
    val chipsPerRow = 3
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.chunked(chipsPerRow).forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowOptions.forEach { option ->
                    SelectableChip(
                        label = optionLabel(option),
                        isSelected = option in selected,
                        onClick = { onToggle(option) }
                    )
                }
            }
        }
    }
}

/** Chip individual con dos estados visuales (seleccionado vs no). */
@Composable
private fun SelectableChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        BrandColors.FarmerPrimary
    } else {
        BrandColors.InputBackground
    }
    val textColor = if (isSelected) {
        BrandColors.PrimaryButtonText
    } else {
        BrandColors.TextPrimary
    }
    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor
            )
        )
    }
}

@Preview(name = "MultiSelectChips", widthDp = 360)
@Composable
private fun MultiSelectChipsPreview() {
    MultiSelectChips(
        options = listOf("Caturra", "Castillo", "Colombia", "Típica", "Bourbon"),
        selected = setOf("Caturra", "Colombia"),
        onToggle = {},
        optionLabel = { it }
    )
}

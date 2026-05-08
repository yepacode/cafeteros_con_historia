package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
 * Lista vertical de opciones single-select tipo "tarjeta con radio".
 *
 * Cada opción es una tarjeta clickeable con un radio button a la izquierda;
 * la opción seleccionada muestra un punto interno verde.
 *
 * @param T tipo de las opciones.
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param options lista de opciones.
 * @param selected opción actualmente seleccionada o null.
 * @param onSelect callback al elegir una opción.
 * @param optionLabel función para extraer la etiqueta visible de cada opción.
 */
@Composable
fun <T> RadioCardOptionList(
    modifier: Modifier = Modifier,
    options: List<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    optionLabel: (T) -> String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            RadioCardOption(
                label = optionLabel(option),
                isSelected = option == selected,
                onClick = { onSelect(option) }
            )
        }
    }
}

/** Tarjeta individual: radio button + label. */
@Composable
private fun RadioCardOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) BrandColors.FarmerPrimary else BrandColors.InputBackground
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) BrandColors.CardBackground else BrandColors.InputBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioCircle(isSelected = isSelected)
        Text(
            text = label,
            modifier = Modifier.padding(start = 12.dp),
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = BrandColors.TextPrimary
            )
        )
    }
}

/** Círculo del radio: relleno cuando está seleccionado, vacío cuando no. */
@Composable
private fun RadioCircle(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .border(
                width = 1.5.dp,
                color = if (isSelected) BrandColors.FarmerPrimary else BrandColors.InputHint,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color = BrandColors.FarmerPrimary, shape = CircleShape)
            )
        }
    }
}

@Preview(name = "RadioCardOptionList", widthDp = 360)
@Composable
private fun RadioCardOptionListPreview() {
    RadioCardOptionList(
        options = listOf("Manual selectiva", "Manual no selectiva", "Mecánica"),
        selected = "Manual selectiva",
        onSelect = {},
        optionLabel = { it }
    )
}

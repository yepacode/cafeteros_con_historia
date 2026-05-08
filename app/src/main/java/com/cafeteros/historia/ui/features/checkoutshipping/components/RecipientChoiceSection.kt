package com.cafeteros.historia.ui.features.checkoutshipping.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.checkoutshipping.model.RecipientOption
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bloque "¿A quién avisamos cuando llegue?" con un título serif bold y
 * dos opciones excluyentes.
 *
 * Sigue el diseño tal cual: la opción seleccionada se renderiza con un
 * **cuadrado** redondeado oscuro con check ✓ blanco; la NO seleccionada
 * con un **círculo** outline vacío. Ambas son mutuamente excluyentes
 * (radio-style) aunque visualmente difieren.
 *
 * @param modifier modifier opcional.
 * @param selected opción activa.
 * @param onSelect callback al elegir una opción.
 */
@Composable
fun RecipientChoiceSection(
    modifier: Modifier = Modifier,
    selected: RecipientOption,
    onSelect: (RecipientOption) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(
            text = "¿A quién avisamos cuando llegue?",
            style = BrandTypography.CheckoutSectionTitle
        )
        Column {
            RecipientOption.entries.forEach { option ->
                RecipientRow(
                    option = option,
                    selected = option == selected,
                    onClick = { onSelect(option) }
                )
            }
        }
    }
}

@Composable
private fun RecipientRow(
    modifier: Modifier = Modifier,
    option: RecipientOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        if (selected) {
            CheckedSquare()
        } else {
            UncheckedCircle()
        }
        Text(text = option.label, style = BrandTypography.RecipientOptionLabel)
    }
}

@Composable
private fun CheckedSquare(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(22.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(BrandColors.RecipientCheckedBackground),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = BrandColors.RecipientCheckedIcon,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun UncheckedCircle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(22.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = BrandColors.RecipientUncheckedBorder,
                shape = CircleShape
            )
    )
}

@Preview(name = "RecipientChoiceSection", showBackground = true, widthDp = 360)
@Composable
private fun RecipientChoiceSectionPreview() {
    CafeterosTheme {
        RecipientChoiceSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            selected = RecipientOption.USAR_MIS_DATOS,
            onSelect = {}
        )
    }
}

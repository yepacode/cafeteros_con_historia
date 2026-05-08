package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Una fila tipo "label + radio button derecho" reutilizable por las
 * secciones "PROCESO" y "ORDENAR POR".
 *
 * El radio button es un círculo con borde gris cuando no está marcado y
 * un círculo con borde oscuro + punto interno cuando lo está. Es un dibujo
 * propio, no `RadioButton` de Material 3, para coincidir con el espesor
 * y los colores del diseño.
 *
 * @param modifier modifier opcional.
 * @param label texto a la izquierda.
 * @param selected estado del radio.
 * @param onClick callback al pulsar la fila completa.
 */
@Composable
fun RadioOptionRow(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = BrandTypography.FilterListItem)
        RadioCircle(selected = selected)
    }
}

@Composable
private fun RadioCircle(modifier: Modifier = Modifier, selected: Boolean) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = if (selected) BrandColors.RadioSelected else BrandColors.RadioUnselected,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(BrandColors.RadioSelected)
            )
        }
    }
}

@Preview(name = "RadioOptionRow", showBackground = true, widthDp = 320)
@Composable
private fun RadioOptionRowPreview() {
    CafeterosTheme {
        Box(modifier = Modifier.padding(BrandSpacing.md)) {
            RadioOptionRow(label = "Todos", selected = true, onClick = {})
        }
    }
}

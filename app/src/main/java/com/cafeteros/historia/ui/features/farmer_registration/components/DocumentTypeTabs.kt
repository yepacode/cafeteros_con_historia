package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.cafeteros.historia.ui.features.farmer_registration.model.IdentificationType
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Selector segmentado de tipo de documento (C.C. / C.E. / P.P.T.).
 *
 * Estilo "pill" con fondo gris claro; la pestaña activa se resalta con fondo
 * blanco y borde sutil. Solo permite una selección a la vez.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param selected tipo de documento actualmente seleccionado.
 * @param onSelect callback cuando el usuario elige otro tipo.
 */
@Composable
fun DocumentTypeTabs(
    modifier: Modifier = Modifier,
    selected: IdentificationType,
    onSelect: (IdentificationType) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.InputBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        IdentificationType.entries.forEach { type ->
            DocumentTypeTab(
                modifier = Modifier.weight(1f),
                label = type.label,
                isSelected = type == selected,
                onClick = { onSelect(type) }
            )
        }
    }
}

@Composable
private fun DocumentTypeTab(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = if (isSelected) BrandColors.CardBackground else androidx.compose.ui.graphics.Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) BrandColors.TextPrimary else BrandColors.TextSecondary
            )
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun DocumentTypeTabsPreview() {
    DocumentTypeTabs(selected = IdentificationType.CC, onSelect = {})
}

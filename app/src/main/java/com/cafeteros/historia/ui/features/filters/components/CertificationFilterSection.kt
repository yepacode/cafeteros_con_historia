package com.cafeteros.historia.ui.features.filters.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.filters.model.FilterCertification
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "CERTIFICACIONES": label + lista vertical con un checkbox por
 * certificación. Selección múltiple — la pantalla guarda el conjunto.
 *
 * El checkbox es un cuadrado redondeado: sin marcar muestra solo borde
 * gris, marcado muestra fondo verde con check blanco.
 *
 * @param modifier modifier opcional.
 * @param selected conjunto de certificaciones marcadas.
 * @param onToggle callback al pulsar un item.
 */
@Composable
fun CertificationFilterSection(
    modifier: Modifier = Modifier,
    selected: Set<FilterCertification>,
    onToggle: (FilterCertification) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        FilterSectionLabel(text = "CERTIFICACIONES")
        FilterCertification.entries.forEach { cert ->
            CertificationRow(
                certification = cert,
                checked = cert in selected,
                onClick = { onToggle(cert) }
            )
        }
    }
}

@Composable
private fun CertificationRow(
    modifier: Modifier = Modifier,
    certification: FilterCertification,
    checked: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        CertificationCheckbox(checked = checked)
        Text(text = certification.label, style = BrandTypography.CertificationItem)
    }
}

@Composable
private fun CertificationCheckbox(modifier: Modifier = Modifier, checked: Boolean) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(shape)
            .then(
                if (checked) {
                    Modifier.background(BrandColors.CertificationChecked)
                } else {
                    Modifier
                        .background(BrandColors.CardBackground)
                        .border(width = 1.5.dp, color = BrandColors.CertificationUnchecked, shape = shape)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(name = "CertificationFilterSection", showBackground = true, widthDp = 360)
@Composable
private fun CertificationFilterSectionPreview() {
    CafeterosTheme {
        CertificationFilterSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            selected = setOf(FilterCertification.COMERCIO_JUSTO),
            onToggle = {}
        )
    }
}

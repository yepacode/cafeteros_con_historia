package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Sección "ENVÍO": label + dos toggles apilados.
 *
 *  - "Envío gratis" — restringe a productos con envío gratis (>$150k).
 *  - "Envío express" — restringe a productos con entrega < 24h.
 *
 * El switch es un dibujo casero (no `Switch` de Material) para coincidir
 * exactamente con los colores y dimensiones del diseño.
 *
 * @param modifier modifier opcional.
 * @param freeShipping estado del toggle "Envío gratis".
 * @param expressShipping estado del toggle "Envío express".
 * @param onFreeShippingChange callback al alternar el primer toggle.
 * @param onExpressShippingChange callback al alternar el segundo.
 */
@Composable
fun ShippingFilterSection(
    modifier: Modifier = Modifier,
    freeShipping: Boolean,
    expressShipping: Boolean,
    onFreeShippingChange: (Boolean) -> Unit,
    onExpressShippingChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        FilterSectionLabel(text = "ENVÍO")
        ShippingToggleRow(
            title = "Envío gratis",
            subtitle = "Para compras superiores a \$150k",
            checked = freeShipping,
            onCheckedChange = onFreeShippingChange
        )
        ShippingToggleRow(
            title = "Envío express",
            subtitle = "Entrega en menos de 24 horas",
            checked = expressShipping,
            onCheckedChange = onExpressShippingChange
        )
    }
}

@Composable
private fun ShippingToggleRow(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = title, style = BrandTypography.ShippingToggleTitle)
            Text(text = subtitle, style = BrandTypography.ShippingToggleSubtitle)
        }
        BrandSwitch(checked = checked)
    }
}

@Composable
private fun BrandSwitch(modifier: Modifier = Modifier, checked: Boolean) {
    val trackShape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .width(44.dp)
            .height(24.dp)
            .clip(trackShape)
            .background(if (checked) BrandColors.SwitchTrackOn else BrandColors.SwitchTrackOff),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(BrandColors.SwitchThumb)
        )
    }
}

@Preview(name = "ShippingFilterSection", showBackground = true, widthDp = 360)
@Composable
private fun ShippingFilterSectionPreview() {
    CafeterosTheme {
        ShippingFilterSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            freeShipping = true,
            expressShipping = false,
            onFreeShippingChange = {},
            onExpressShippingChange = {}
        )
    }
}

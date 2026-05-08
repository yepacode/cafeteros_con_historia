package com.cafeteros.historia.ui.features.filters.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Sección "RANGO DE PRECIO" del filtro:
 *  - Header con label + valor verde "$X - $Y".
 *  - [RangeSlider] de Material 3 con dos thumbs y la pista activa oscura.
 *  - Inputs MIN / MAX read-only que reflejan los valores actuales del slider.
 *
 * Los inputs son sólo de lectura — la fuente de verdad es el slider. Cuando
 * exista entrada manual (ej. teclado numérico), se reemplazará el `Text`
 * de cada caja por un `BasicTextField` que actualice [onValueChange].
 *
 * @param modifier modifier opcional.
 * @param valueRange rango total permitido (límites duros del slider).
 * @param value rango activo seleccionado.
 * @param onValueChange callback al mover los thumbs del slider.
 */
@Composable
fun PriceRangeSection(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 10_000f..150_000f,
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        FilterSectionLabel(
            text = "RANGO DE PRECIO",
            trailing = {
                Text(
                    text = "${formatPrice(value.start)} - ${formatPrice(value.endInclusive)}",
                    style = BrandTypography.PriceRangeValue
                )
            }
        )

        RangeSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = BrandColors.PriceSliderThumb,
                activeTrackColor = BrandColors.PriceSliderActiveTrack,
                inactiveTrackColor = BrandColors.PriceSliderTrack
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            PriceField(
                modifier = Modifier.weight(1f),
                caption = "MIN",
                value = formatPriceShort(value.start)
            )
            PriceField(
                modifier = Modifier.weight(1f),
                caption = "MAX",
                value = formatPriceShort(value.endInclusive)
            )
        }
    }
}

@Composable
private fun PriceField(
    modifier: Modifier = Modifier,
    caption: String,
    value: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        Text(text = caption, style = BrandTypography.PriceFieldCaption)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(BrandColors.PriceInputBackground)
                .padding(horizontal = BrandSpacing.md, vertical = 14.dp)
        ) {
            Text(text = value, style = BrandTypography.PriceFieldValue)
        }
    }
}

private fun formatPrice(value: Float): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "CO"))
    return "$" + formatter.format(value.toLong())
}

private fun formatPriceShort(value: Float): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "CO"))
    return formatter.format(value.toLong())
}

@Preview(name = "PriceRangeSection", showBackground = true, widthDp = 360)
@Composable
private fun PriceRangeSectionPreview() {
    CafeterosTheme {
        PriceRangeSection(
            modifier = Modifier.padding(BrandSpacing.lg),
            value = 30_000f..80_000f,
            onValueChange = {}
        )
    }
}

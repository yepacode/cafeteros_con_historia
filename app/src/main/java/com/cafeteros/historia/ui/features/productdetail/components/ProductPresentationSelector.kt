package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.productdetail.model.ProductPresentation
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Selector horizontal de presentación con tres chips (250g / 500g / 1kg).
 *
 * El chip seleccionado se renderiza con fondo café oscuro y texto blanco;
 * los demás con fondo blanco crema y borde gris suave.
 *
 * @param modifier modifier opcional.
 * @param options lista de presentaciones disponibles para este producto.
 * @param selected presentación seleccionada actualmente.
 * @param onSelect callback al pulsar un chip.
 */
@Composable
fun ProductPresentationSelector(
    modifier: Modifier = Modifier,
    options: List<ProductPresentation>,
    selected: ProductPresentation,
    onSelect: (ProductPresentation) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(text = "PRESENTACIÓN", style = BrandTypography.ProductSectionLabel)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            options.forEach { option ->
                PresentationChip(
                    label = option.label,
                    selected = option == selected,
                    onClick = { onSelect(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PresentationChip(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val baseModifier = if (selected) {
        Modifier
            .clip(shape)
            .background(BrandColors.PresentationChipSelectedBackground)
    } else {
        Modifier
            .clip(shape)
            .background(BrandColors.PresentationChipUnselectedBackground)
            .border(
                BorderStroke(1.dp, BrandColors.PresentationChipUnselectedBorder),
                shape
            )
    }

    Box(
        modifier = modifier
            .then(baseModifier)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = if (selected) BrandTypography.PresentationChipSelected
            else BrandTypography.PresentationChipUnselected
        )
    }
}

@Preview(name = "ProductPresentationSelector", showBackground = true, widthDp = 360)
@Composable
private fun ProductPresentationSelectorPreview() {
    CafeterosTheme {
        ProductPresentationSelector(
            modifier = Modifier.padding(BrandSpacing.lg),
            options = ProductPresentation.entries,
            selected = ProductPresentation.SIZE_250G,
            onSelect = {}
        )
    }
}

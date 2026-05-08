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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.productdetail.model.ProductRoast
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Dropdown reutilizable para los selectores "TUESTE" y "TIPO".
 *
 * Pinta una etiqueta uppercase encima y, debajo, una pill rectangular con
 * el [selectedLabel] a la izquierda y un chevron a la derecha. Al pulsar
 * la pill se despliega un [DropdownMenu] estándar con las [options] y se
 * dispara [onSelect] al elegir una.
 *
 * Es genérico (`<T>`) para servir tanto a [ProductRoast] como a
 * [com.cafeteros.historia.ui.features.productdetail.model.ProductGrindType]
 * sin duplicar código. El llamador provee [labelOf] para extraer el texto
 * visible de cada opción.
 *
 * @param modifier modifier opcional (controla el ancho cuando se usa en grid).
 * @param label etiqueta uppercase encima del campo ("TUESTE").
 * @param options lista de opciones.
 * @param selected opción seleccionada actualmente.
 * @param labelOf función que devuelve el texto visible de cada opción.
 * @param onSelect callback al elegir una opción.
 */
@Composable
fun <T> ProductOptionDropdown(
    modifier: Modifier = Modifier,
    label: String,
    options: List<T>,
    selected: T,
    labelOf: (T) -> String,
    onSelect: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(text = label, style = BrandTypography.ProductSectionLabel)

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(BrandColors.OptionFieldBackground)
                    .border(BorderStroke(1.dp, BrandColors.OptionFieldBorder), shape)
                    .clickable { expanded = true }
                    .padding(horizontal = BrandSpacing.md, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = labelOf(selected),
                    style = BrandTypography.OptionFieldValue
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Abrir opciones",
                    tint = BrandColors.TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(BrandColors.OptionFieldBackground)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = labelOf(option), style = BrandTypography.OptionFieldValue)
                        },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(name = "ProductOptionDropdown", showBackground = true, widthDp = 200)
@Composable
private fun ProductOptionDropdownPreview() {
    CafeterosTheme {
        ProductOptionDropdown(
            modifier = Modifier
                .width(180.dp)
                .padding(BrandSpacing.md),
            label = "TUESTE",
            options = ProductRoast.entries,
            selected = ProductRoast.MEDIO,
            labelOf = { it.label },
            onSelect = {}
        )
    }
}

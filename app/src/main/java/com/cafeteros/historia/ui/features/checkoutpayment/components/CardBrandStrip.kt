package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
 * Tira horizontal con las marcas de tarjeta soportadas (Visa, Mastercard,
 * Amex). En el diseño aparecen como tres "chips" pequeños grises debajo del
 * título "Tarjeta de crédito/débito".
 *
 * Como solo estamos haciendo el front, cada marca se renderiza como un
 * pill plano con el nombre uppercase. Cuando exista BD/SDK de pasarela
 * estos placeholders se sustituyen por los logos oficiales.
 *
 * @param modifier modifier opcional.
 * @param brands marcas a mostrar en orden.
 */
@Composable
fun CardBrandStrip(
    modifier: Modifier = Modifier,
    brands: List<String> = listOf("VISA", "MC", "AMEX")
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        brands.forEach { brand ->
            BrandChip(brand = brand)
        }
    }
}

@Composable
private fun BrandChip(modifier: Modifier = Modifier, brand: String) {
    Box(
        modifier = modifier
            .height(20.dp)
            .width(34.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(BrandColors.CardBrandChipBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(text = brand, style = BrandTypography.CardBrandChipLabel)
    }
}

@Preview(name = "CardBrandStrip", showBackground = true, widthDp = 360)
@Composable
private fun CardBrandStripPreview() {
    CafeterosTheme {
        CardBrandStrip(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

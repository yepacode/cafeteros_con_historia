package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pill pequeña con una nota de sabor ("Chocolate amargo", "Cuerpo fuerte"…).
 *
 * Es solo presentacional: fondo beige, texto sans-serif, esquinas
 * redondeadas. Si en el futuro se hace clickable para filtrar por nota,
 * basta con agregar un `clickable` modifier desde el call site.
 */
@Composable
fun FlavorTagPill(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        text = label,
        style = BrandTypography.FlavorTagText,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BrandColors.FlavorTagBackground)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Preview(name = "FlavorTagPill", showBackground = true)
@Composable
private fun FlavorTagPillPreview() {
    CafeterosTheme {
        FlavorTagPill(label = "Chocolate amargo")
    }
}

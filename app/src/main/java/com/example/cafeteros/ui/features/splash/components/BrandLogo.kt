package com.example.cafeteros.ui.features.splash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cafeteros.ui.theme.BrandColors
import com.example.cafeteros.ui.theme.BrandSpacing
import com.example.cafeteros.ui.theme.BrandTypography

/**
 * Logo composicional de la marca: ícono + nombre + tagline.
 *
 * Composición vertical centrada que combina los tres elementos visuales que
 * representan la identidad de "Origen". Es stateless y reutilizable en
 * cualquier pantalla (no solo el splash).
 *
 * @param modifier modifier opcional para el contenedor.
 * @param brandName nombre de la marca a mostrar (ej. "Origen").
 * @param taglineText subtítulo bajo el nombre (ej. "CAFÉ Y REPOSTERÍA").
 */
@Composable
fun BrandLogo(
    modifier: Modifier = Modifier,
    brandName: String,
    taglineText: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BrandIcon()
        Spacer(modifier = Modifier.height(BrandSpacing.md))
        Text(
            text = brandName,
            style = BrandTypography.BrandTitle
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Text(
            text = taglineText,
            style = BrandTypography.BrandTagline
        )
    }
}

/** Preview del logo sobre el fondo café de marca. */
@Preview(name = "BrandLogo – sobre fondo de marca", showBackground = false)
@Composable
private fun BrandLogoPreview() {
    BrandLogo(
        modifier = Modifier
            .background(BrandColors.CoffeeBrown)
            .padding(BrandSpacing.xl),
        brandName = "Origen",
        taglineText = "CAFÉ Y REPOSTERÍA"
    )
}

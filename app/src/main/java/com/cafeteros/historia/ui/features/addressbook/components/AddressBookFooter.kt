package com.cafeteros.historia.ui.features.addressbook.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Footer decorativo bajo la lista de direcciones.
 *
 * Visual fiel al diseño:
 *  - Bloque rectangular dividido horizontalmente en dos colores
 *    (mitad superior crema más claro, mitad inferior crema medio) — sirve
 *    de placeholder para una futura ilustración real.
 *  - Caption italic centrado bajo el bloque.
 *
 * El bloque se renderiza con dos `Box` apilados verticalmente para que el
 * usuario vea claramente las dos bandas del diseño aún antes de tener
 * el asset definitivo.
 *
 * @param modifier modifier opcional.
 * @param caption texto editorial italic mostrado bajo la imagen.
 */
@Composable
fun AddressBookFooter(
    modifier: Modifier = Modifier,
    caption: String = "Nuestros caficultores llegan a cada rincón."
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Column(
            modifier = Modifier
                .width(170.dp)
                .height(110.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(BrandColors.AddressBookFooterImageTop)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(BrandColors.AddressBookFooterImageBottom)
            )
        }
        Text(
            text = caption,
            style = BrandTypography.AddressBookFooterCaption
        )
    }
}

@Preview(name = "AddressBookFooter", showBackground = true, widthDp = 360)
@Composable
private fun AddressBookFooterPreview() {
    CafeterosTheme {
        AddressBookFooter(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

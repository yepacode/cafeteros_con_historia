package com.cafeteros.historia.ui.features.addressbook.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pill amarilla "PREDETERMINADA" que aparece dentro de la card cuando la
 * dirección es la predeterminada del usuario.
 *
 * @param modifier modifier opcional.
 */
@Composable
fun DefaultAddressPill(
    modifier: Modifier = Modifier
) {
    Text(
        text = "PREDETERMINADA",
        style = BrandTypography.DefaultAddressPill,
        modifier = modifier
            .background(
                color = BrandColors.DefaultAddressPillBackground,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    )
}

@Preview(name = "DefaultAddressPill", showBackground = true, widthDp = 200)
@Composable
private fun DefaultAddressPillPreview() {
    CafeterosTheme {
        DefaultAddressPill(modifier = Modifier.padding(16.dp))
    }
}

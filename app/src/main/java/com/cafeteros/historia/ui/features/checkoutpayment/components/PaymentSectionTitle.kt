package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Título italic serif de la sección de métodos de pago
 * ("Selecciona cómo deseas pagar"), tomado del diseño tal cual.
 *
 * @param modifier modifier opcional.
 * @param text texto a renderizar.
 */
@Composable
fun PaymentSectionTitle(
    modifier: Modifier = Modifier,
    text: String = "Selecciona cómo deseas pagar"
) {
    Text(
        text = text,
        style = BrandTypography.PaymentSectionTitle,
        modifier = modifier
    )
}

@Preview(name = "PaymentSectionTitle", showBackground = true, widthDp = 360)
@Composable
private fun PaymentSectionTitlePreview() {
    CafeterosTheme {
        PaymentSectionTitle(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

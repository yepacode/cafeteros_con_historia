package com.cafeteros.historia.ui.features.checkoutshipping.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Header de la sección "Mis direcciones" con título serif bold a la
 * izquierda y enlace verde subrayado "Gestionar" a la derecha.
 *
 * @param modifier modifier opcional.
 * @param onManage callback del enlace "Gestionar".
 */
@Composable
fun AddressesSectionHeader(
    modifier: Modifier = Modifier,
    onManage: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Mis direcciones", style = BrandTypography.CheckoutSectionTitle)
        Text(
            text = "Gestionar",
            style = BrandTypography.AddressManageLink,
            modifier = Modifier
                .clickable(onClick = onManage)
                .padding(vertical = 4.dp)
        )
    }
}

@Preview(name = "AddressesSectionHeader", showBackground = true, widthDp = 360)
@Composable
private fun AddressesSectionHeaderPreview() {
    CafeterosTheme {
        AddressesSectionHeader(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

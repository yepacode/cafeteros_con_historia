package com.cafeteros.historia.ui.features.addressbook.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bloque hero superior de "Mis Direcciones".
 *
 * Layout vertical:
 *  - Caption italic verde "Logística del Origen".
 *  - Título serif bold grande "Gestiona tus puntos de entrega.".
 *
 * @param modifier modifier opcional.
 * @param caption caption editorial italic.
 * @param title título serif bold del hero.
 */
@Composable
fun AddressBookHero(
    modifier: Modifier = Modifier,
    caption: String = "Logística del Origen",
    title: String = "Gestiona tus puntos de entrega."
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = caption, style = BrandTypography.AddressBookEditorialCaption)
        Text(text = title, style = BrandTypography.AddressBookHeroTitle)
    }
}

@Preview(name = "AddressBookHero", showBackground = true, widthDp = 360)
@Composable
private fun AddressBookHeroPreview() {
    CafeterosTheme {
        AddressBookHero(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

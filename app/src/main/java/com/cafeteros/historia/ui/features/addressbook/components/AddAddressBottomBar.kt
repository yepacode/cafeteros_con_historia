package com.cafeteros.historia.ui.features.addressbook.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
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
 * Bottom bar fijo de "Mis Direcciones".
 *
 * Visual: contenedor con fondo crema y un único CTA ancho oscuro
 * "+ Agregar dirección".
 *
 * @param modifier modifier opcional.
 * @param onAddAddress callback al pulsar el CTA.
 */
@Composable
fun AddAddressBottomBar(
    modifier: Modifier = Modifier,
    onAddAddress: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.AddressBookBackground)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.md)
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(BrandColors.AddAddressCtaBackground)
                .clickable(onClick = onAddAddress)
                .padding(horizontal = BrandSpacing.lg)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = BrandColors.AddAddressCtaText,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Agregar dirección",
                style = BrandTypography.AddAddressCtaLabel,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Preview(name = "AddAddressBottomBar", showBackground = true, widthDp = 360)
@Composable
private fun AddAddressBottomBarPreview() {
    CafeterosTheme {
        AddAddressBottomBar()
    }
}

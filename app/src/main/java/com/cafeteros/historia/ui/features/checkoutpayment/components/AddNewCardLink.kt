package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
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
 * Enlace verde "+ Agregar nueva tarjeta" que aparece al final del bloque
 * expandido de tarjetas. Al pulsarse, abrirá el sheet de captura de tarjeta
 * (TODO cuando exista esa pantalla).
 *
 * @param modifier modifier opcional.
 * @param onClick callback al pulsar el enlace.
 */
@Composable
fun AddNewCardLink(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = BrandColors.AddNewCardLink,
            modifier = Modifier.size(16.dp)
        )
        Text(text = "Agregar nueva tarjeta", style = BrandTypography.AddNewCardLabel)
    }
}

@Preview(name = "AddNewCardLink", showBackground = true, widthDp = 360)
@Composable
private fun AddNewCardLinkPreview() {
    CafeterosTheme {
        AddNewCardLink(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

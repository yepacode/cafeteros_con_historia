package com.cafeteros.historia.ui.features.addressbook.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.addressbook.model.ManagedAddressType
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Badge verde claro con icono + label de la categoría de la dirección.
 *
 * Visual: pill redondeada con fondo verde claro, icono pequeño verde
 * profundo a la izquierda y label capitalizado al lado.
 *
 * @param modifier modifier opcional.
 * @param type tipo de dirección a mostrar.
 */
@Composable
fun ManagedAddressBadge(
    modifier: Modifier = Modifier,
    type: ManagedAddressType
) {
    Row(
        modifier = modifier
            .background(
                color = BrandColors.ManagedAddressBadgeBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = type.icon,
            contentDescription = null,
            tint = BrandColors.ManagedAddressBadgeIcon,
            modifier = Modifier.size(14.dp)
        )
        Text(text = type.label, style = BrandTypography.ManagedAddressBadgeLabel)
    }
}

@Preview(name = "ManagedAddressBadge — todos", showBackground = true, widthDp = 360)
@Composable
private fun ManagedAddressBadgePreview() {
    CafeterosTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ManagedAddressType.entries.forEach { ManagedAddressBadge(type = it) }
        }
    }
}

package com.cafeteros.historia.ui.features.addressbook.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
 * Top bar de la pantalla "Mis Direcciones".
 *
 * Layout: flecha back a la izquierda + título serif **centrado** ("Mis
 * Direcciones"). Sin acciones a la derecha.
 *
 * Se centra el título usando un `Box` con `Alignment.Center` mientras la
 * flecha queda alineada en `CenterStart`. Esto evita que el título se
 * corra cuando la flecha es más ancha que el espacio reservado a la
 * derecha (no hay acción simétrica).
 *
 * @param modifier modifier opcional.
 * @param title título serif centrado ("Mis Direcciones").
 * @param onBack callback de la flecha.
 */
@Composable
fun AddressBookTopBar(
    modifier: Modifier = Modifier,
    title: String = "Mis Direcciones",
    onBack: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = BrandColors.TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = title,
            style = BrandTypography.AddressBookTopBarTitle,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(name = "AddressBookTopBar", showBackground = true, widthDp = 360)
@Composable
private fun AddressBookTopBarPreview() {
    CafeterosTheme {
        AddressBookTopBar()
    }
}

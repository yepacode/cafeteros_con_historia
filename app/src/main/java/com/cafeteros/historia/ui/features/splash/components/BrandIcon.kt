package com.cafeteros.historia.ui.features.splash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Ícono decorativo de marca para el splash.
 *
 * Usa el ícono `Outlined.Coffee` de Material Icons como placeholder hasta
 * disponer del logo definitivo de "Origen". Es un elemento puramente
 * decorativo: el `contentDescription` es `null` porque el texto "Origen"
 * que aparece debajo (en [BrandLogo]) ya transmite la identidad de marca a
 * lectores de pantalla — añadir descripción aquí duplicaría información.
 *
 * @param modifier modifier opcional aplicado al ícono.
 * @param tint color del trazo del ícono. Por defecto, crema de marca.
 * @param size tamaño del ícono. Por defecto 32.dp (escala moderada).
 */
@Composable
fun BrandIcon(
    modifier: Modifier = Modifier,
    tint: Color = BrandColors.CreamWhite,
    size: Dp = 32.dp
) {
    Icon(
        imageVector = Icons.Outlined.Coffee,
        contentDescription = null,
        tint = tint,
        modifier = modifier.size(size)
    )
}

/** Preview aislado del ícono sobre el fondo café de marca. */
@Preview(name = "BrandIcon – sobre fondo de marca", showBackground = false)
@Composable
private fun BrandIconPreview() {
    BrandIcon(
        modifier = Modifier
            .background(BrandColors.CoffeeBrown)
            .padding(BrandSpacing.lg)
    )
}

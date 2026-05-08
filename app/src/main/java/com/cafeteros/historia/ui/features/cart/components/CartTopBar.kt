package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
 * Top bar del carrito.
 *
 *  - Flecha back a la izquierda.
 *  - Título "Tu Selección" centrado serif.
 *  - Texto "Limpiar" alineado a la derecha.
 *
 * @param modifier modifier opcional.
 * @param onBack callback de la flecha.
 * @param onClear callback del enlace "Limpiar".
 */
@Composable
fun CartTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onClear: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
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
                text = "Limpiar",
                style = BrandTypography.CartClearAction,
                modifier = Modifier
                    .clickable(onClick = onClear)
                    .padding(horizontal = BrandSpacing.xs, vertical = BrandSpacing.sm)
            )
        }

        Text(text = "Tu Selección", style = BrandTypography.CartTopBarTitle)
    }
}

@Preview(name = "CartTopBar", showBackground = true, widthDp = 360)
@Composable
private fun CartTopBarPreview() {
    CafeterosTheme {
        CartTopBar()
    }
}

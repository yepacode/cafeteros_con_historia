package com.cafeteros.historia.ui.features.caficultorshop.components

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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ShoppingBag
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
 * Top bar de la pantalla "el caficultor".
 *
 * Layout en una fila:
 *  - Hamburguesa a la izquierda (abre el menú lateral).
 *  - Wordmark serif itálico "el caficultor" centrado.
 *  - Bolsa de compras a la derecha (acceso al carrito).
 *
 * @param modifier modifier opcional.
 * @param onMenuClick callback de la hamburguesa.
 * @param onCartClick callback de la bolsa de compras.
 */
@Composable
fun CaficultorShopTopBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
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
            CircularIconButton(
                contentDescription = "Abrir menú",
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null,
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
            CircularIconButton(
                contentDescription = "Abrir carrito",
                onClick = onCartClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingBag,
                    contentDescription = null,
                    tint = BrandColors.TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Text(text = "el caficultor", style = BrandTypography.CaficultorShopWordmark)
    }
}

@Composable
private fun CircularIconButton(
    modifier: Modifier = Modifier,
    contentDescription: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick, onClickLabel = contentDescription),
        contentAlignment = Alignment.Center,
        content = { content() }
    )
}

@Preview(name = "CaficultorShopTopBar", showBackground = true, widthDp = 360)
@Composable
private fun CaficultorShopTopBarPreview() {
    CafeterosTheme {
        CaficultorShopTopBar()
    }
}

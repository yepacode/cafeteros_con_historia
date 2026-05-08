package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.caficultordetail.model.CaficultorProduct
import com.cafeteros.historia.ui.features.explore.components.FavoriteToggleButton
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card de producto en la grilla del detalle del caficultor.
 *
 * Visual:
 *  - Foto cuadrada arriba con un corazón flotante en la esquina superior
 *    derecha (reusa [FavoriteToggleButton] del feature de exploración).
 *  - Bloque blanco abajo con nombre serif, peso pequeño en gris y, en una
 *    fila al final, precio café oscuro a la izquierda y botón "+" a la
 *    derecha para agregar al carrito.
 *
 * @param modifier modifier opcional.
 * @param product datos a renderizar.
 * @param initiallyFavorite estado inicial del corazón.
 * @param onClick callback al pulsar la card (excluyendo corazón y +).
 * @param onAddToCart callback al pulsar el botón "+".
 */
@Composable
fun CaficultorProductCard(
    modifier: Modifier = Modifier,
    product: CaficultorProduct,
    initiallyFavorite: Boolean = false,
    onClick: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {
    var isFavorite by remember { mutableStateOf(initiallyFavorite) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.CardBackground)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(product.placeholderColor)
        ) {
            if (product.imageRes != null) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Coffee,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            FavoriteToggleButton(
                isFavorite = isFavorite,
                onToggle = { isFavorite = !isFavorite },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(BrandSpacing.sm)
            )
        }

        Column(
            modifier = Modifier.padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = product.name, style = BrandTypography.CaficultorProductName)
            Text(text = product.weight, style = BrandTypography.CaficultorProductWeight)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = product.formattedPrice, style = BrandTypography.CaficultorProductPrice)
                AddToCartButton(onClick = onAddToCart)
            }
        }
    }
}

@Preview(name = "CaficultorProductCard", showBackground = true, widthDp = 180)
@Composable
private fun CaficultorProductCardPreview() {
    CafeterosTheme {
        CaficultorProductCard(
            product = CaficultorProduct(
                name = "Café Huila Pitalito",
                weight = "250g",
                formattedPrice = "$48.000",
                placeholderColor = Color(0xFFE8DCC4)
            )
        )
    }
}

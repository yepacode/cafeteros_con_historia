package com.cafeteros.historia.ui.features.cart.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.features.cart.model.CartItem
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card de un item del carrito.
 *
 * Layout horizontal:
 *  - Imagen cuadrada del producto a la izquierda.
 *  - Bloque central con nombre, meta (peso · tipo), precio + tachado.
 *  - Trash arriba a la derecha + stepper pill abajo a la derecha.
 *
 * @param modifier modifier opcional.
 * @param item item a renderizar.
 * @param formattedPrice precio actual ya formateado ("$48.000").
 * @param formattedOriginalPrice precio anterior ya formateado o null.
 * @param onQuantityChange callback al mover el stepper.
 * @param onRemove callback al pulsar el trash.
 */
@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    item: CartItem,
    formattedPrice: String,
    formattedOriginalPrice: String? = null,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.CartItemCardBackground)
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrandColors.CartItemImageBackground)
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = item.name,
                style = BrandTypography.CartItemName,
                maxLines = 2
            )
            Text(text = item.meta, style = BrandTypography.CartItemMeta)
            // Precios apilados: el principal arriba (dorado) y, si existe,
            // el original tachado debajo. Se renderiza así (no en una Row)
            // para que el tachado no se trunque cuando el ancho de la
            // columna central queda apretado entre la imagen y el stepper.
            Column(
                modifier = Modifier.padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = formattedPrice,
                    style = BrandTypography.CartItemPrice,
                    maxLines = 1,
                    softWrap = false
                )
                if (formattedOriginalPrice != null) {
                    Text(
                        text = formattedOriginalPrice,
                        style = BrandTypography.CartItemOriginalPrice,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clickable(onClick = onRemove, onClickLabel = "Eliminar item"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = null,
                    tint = BrandColors.CartItemDeleteIcon,
                    modifier = Modifier.size(20.dp)
                )
            }
            CartItemStepper(
                quantity = item.quantity,
                onQuantityChange = onQuantityChange
            )
        }
    }
}

@Preview(name = "CartItemCard", showBackground = true, widthDp = 360)
@Composable
private fun CartItemCardPreview() {
    CafeterosTheme {
        CartItemCard(
            modifier = Modifier.padding(BrandSpacing.md),
            item = CartItem(
                id = "x",
                name = "Café Huila Pitalito 250g",
                meta = "250g · Molido medio",
                unitPrice = 48_000,
                originalUnitPrice = 56_000,
                imageRes = R.drawable.cafe_huila_pitalito,
                quantity = 2
            ),
            formattedPrice = "$48.000",
            formattedOriginalPrice = "$56.000",
            onQuantityChange = {},
            onRemove = {}
        )
    }
}

package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.cafeteros.historia.ui.features.explore.model.CoffeeProduct
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card de producto en la sección "Lo más pedido esta semana".
 *
 * Apila imagen cuadrada arriba (con botón de favorito sobrepuesto en la
 * esquina superior derecha) y debajo nombre del producto, finca y precio.
 *
 * El estado del favorito se gestiona internamente con [remember] cuando no
 * se inyecta uno desde fuera; cuando exista lista de favoritos persistente,
 * conviene migrar a un estado controlado y eliminar este `remember`.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param product datos a renderizar.
 * @param initiallyFavorite valor inicial del corazón.
 * @param onClick callback al pulsar la card (excluyendo el corazón).
 */
@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: CoffeeProduct,
    initiallyFavorite: Boolean = false,
    onClick: () -> Unit = {}
) {
    var isFavorite by remember { mutableStateOf(initiallyFavorite) }

    Column(
        modifier = modifier
            .width(170.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.CardBackground)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(product.placeholderColor)
        ) {
            when {
                // 1) Foto real desde Firestore (Base64) si vino del ViewModel.
                product.imageBase64 != null -> {
                    com.cafeteros.historia.ui.components.Base64Image(
                        base64 = product.imageBase64,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // 2) Fallback: drawable mock si la card es de sample data.
                product.imageRes != null -> {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // 3) Sin foto: icono decorativo sobre el color de la card.
                else -> {
                    Icon(
                        imageVector = Icons.Outlined.Coffee,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(56.dp)
                    )
                }
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
            modifier = Modifier.padding(
                horizontal = BrandSpacing.md,
                vertical = BrandSpacing.sm
            ),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = product.name,
                style = BrandTypography.ProductName
            )
            Text(
                text = product.farmName,
                style = BrandTypography.ProductFarm
            )
            Text(
                text = product.formattedPrice,
                style = BrandTypography.ProductPrice,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(name = "ProductCard", showBackground = true, widthDp = 200)
@Composable
private fun ProductCardPreview() {
    CafeterosTheme {
        ProductCard(
            product = CoffeeProduct(
                name = "Café Huila Pitalito",
                farmName = "Finca La Esperanza",
                formattedPrice = "$48.000",
                placeholderColor = Color(0xFFE8DCC4)
            )
        )
    }
}

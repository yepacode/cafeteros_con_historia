package com.cafeteros.historia.ui.features.farmer_products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.ui.components.Base64Image
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Lista de productos del caficultor. Si la lista está vacía, muestra el
 * estado "Aún no tienes productos" inline; si hay productos, los pinta
 * como tarjetas tocables (cada toque abre la edición).
 *
 * La pantalla es **stateless**: recibe la lista vía [products] y delega
 * acciones al contenedor. El observador de Firestore vive en
 * [ProductListViewModel].
 *
 * @param products productos del caficultor logueado, observados desde el
 *  repositorio. Vacía mientras carga la primera emisión de Firestore.
 * @param onBack cierra la activity.
 * @param onCreateProduct abre el wizard en modo creación.
 * @param onEditProduct abre el wizard en modo edición para el producto dado.
 */
@Composable
fun ProductListScreen(
    products: List<Product>,
    onBack: () -> Unit,
    onCreateProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ListHeader(productCount = products.size, onBack = onBack)

            if (products.isEmpty()) {
                EmptyProducts(modifier = Modifier.weight(1f), onCreateFirst = onCreateProduct)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = BrandSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
                    contentPadding = PaddingValues(
                        top = BrandSpacing.sm,
                        bottom = 96.dp
                    )
                ) {
                    items(products) { product ->
                        ProductRow(product = product, onClick = { onEditProduct(product) })
                    }
                }
            }
        }

        // FAB "+ Nuevo producto" — solo se muestra cuando ya hay al menos
        // uno en la lista (el estado vacío ya tiene su propio botón).
        if (products.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(BrandSpacing.lg)
                    .size(56.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape)
                    .clickable(onClick = onCreateProduct),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuevo producto",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

/** Header con flecha atrás + título "Mis Cosechas" + contador de productos. */
@Composable
private fun ListHeader(productCount: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Volver",
                tint = BrandColors.TextPrimary
            )
        }
        Text(
            text = "Mis Cosechas",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = BrandColors.TextPrimary
            )
        )
        if (productCount > 0) {
            Text(
                text = "$productCount producto${if (productCount == 1) "" else "s"}",
                modifier = Modifier.padding(end = BrandSpacing.md),
                color = BrandColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/** Estado vacío con ilustración placeholder + CTA "Crear mi primer producto". */
@Composable
private fun EmptyProducts(
    modifier: Modifier = Modifier,
    onCreateFirst: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(BrandColors.InputBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                tint = BrandColors.FarmerPrimary,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Aún no tienes productos",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Publica tu primer café en 5 minutos",
            color = BrandColors.TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        Box(
            modifier = Modifier
                .background(BrandColors.FarmerPrimary, RoundedCornerShape(8.dp))
                .clickable(onClick = onCreateFirst)
                .padding(horizontal = BrandSpacing.lg, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Crear mi primer producto",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Tarjeta de producto en la lista: foto (Base64) + nombre + precio + estado.
 * Tap → abre edición.
 */
@Composable
private fun ProductRow(product: Product, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Base64Image(
            base64 = product.imageBase64,
            contentDescription = product.name,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
            Text(
                text = if (product.isPaused) "PAUSADO" else "ACTIVO",
                color = if (product.isPaused) BrandColors.TextSecondary else BrandColors.FarmerPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Text(
                text = product.name,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = "$" + "%,d".format(product.priceCop).replace(',', '.') +
                        " · Stock " + product.stockUnits,
                color = BrandColors.TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

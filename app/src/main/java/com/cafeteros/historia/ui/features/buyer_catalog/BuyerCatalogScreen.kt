package com.cafeteros.historia.ui.features.buyer_catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
 * Catálogo del comprador. Stateless: recibe la lista de productos del
 * contenedor y delega la compra vía [onBuy].
 *
 * Cada card muestra: foto, nombre, descripción corta, precio, stock
 * disponible, y botón "Comprar 1" (deshabilitado mientras se procesa otra
 * compra o si el producto está agotado).
 */
@Composable
fun BuyerCatalogScreen(
    products: List<Product>,
    isBuying: Boolean,
    onOpenCart: () -> Unit,
    onOpenMyPurchases: () -> Unit,
    onOpenMessages: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenProduct: (Product) -> Unit,
    onContact: (Product) -> Unit,
    onBuy: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        TopBar(
            productCount = products.size,
            onOpenCart = onOpenCart,
            onOpenMyPurchases = onOpenMyPurchases,
            onOpenMessages = onOpenMessages,
            onOpenSettings = onOpenSettings
        )
        // Fila de accesos rápidos: búsqueda + mapa cafetero. Más
        // descubrible que esconderlos en el top bar saturado.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.xs),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            QuickAction(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Search,
                label = "Buscar café",
                onClick = onOpenSearch
            )
            QuickAction(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Map,
                label = "Mapa cafetero",
                onClick = onOpenMap
            )
        }

        if (products.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = BrandSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = BrandSpacing.sm,
                    bottom = BrandSpacing.lg
                )
            ) {
                items(products) { product ->
                    ProductBuyCard(
                        product = product,
                        isBuying = isBuying,
                        onOpen = { onOpenProduct(product) },
                        onBuy = { onBuy(product) },
                        onContact = { onContact(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    productCount: Int,
    onOpenCart: () -> Unit,
    onOpenMyPurchases: () -> Unit,
    onOpenMessages: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = BrandSpacing.md)) {
            Text(
                text = "Catálogo de Cafés",
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
                    text = "$productCount café${if (productCount == 1) "" else "s"} disponibles",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
        IconButton(onClick = onOpenCart) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = "Carrito",
                tint = BrandColors.TextPrimary
            )
        }
        IconButton(onClick = onOpenMessages) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Mensajes",
                tint = BrandColors.TextPrimary
            )
        }
        IconButton(onClick = onOpenMyPurchases) {
            Icon(
                imageVector = Icons.Outlined.Receipt,
                contentDescription = "Mis pedidos",
                tint = BrandColors.TextPrimary
            )
        }
        IconButton(onClick = onOpenSettings) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Configuración",
                tint = BrandColors.TextPrimary
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
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
                imageVector = Icons.Outlined.LocalCafe,
                contentDescription = null,
                tint = BrandColors.CoffeeBrown,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Aún no hay cafés publicados",
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
            text = "Los caficultores están preparando sus cosechas. Vuelve pronto.",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Card grande de un producto: foto a la izquierda, datos a la derecha,
 * botón "Comprar 1" abajo. Si el stock es 0, el botón queda
 * deshabilitado con texto "Agotado".
 */
/**
 * Botón pill con ícono + texto. Se usa para "Buscar café" y "Mapa
 * cafetero" debajo del top bar — accesos descubribles sin saturar la
 * barra superior.
 */
@Composable
private fun QuickAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(BrandColors.CardBackground, RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandColors.FarmerPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = label,
            color = BrandColors.TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ProductBuyCard(
    product: Product,
    isBuying: Boolean,
    onOpen: () -> Unit,
    onBuy: () -> Unit,
    onContact: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onOpen)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Base64Image(
                base64 = product.imageBase64,
                contentDescription = product.name,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = product.name,
                    color = BrandColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    maxLines = 2
                )
                if (product.shortDescription.isNotBlank()) {
                    Text(
                        text = product.shortDescription,
                        color = BrandColors.TextSecondary,
                        fontSize = 11.sp,
                        maxLines = 2
                    )
                }
                Text(
                    text = "${product.weightGrams}g · ${product.category.label}",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$" + "%,d".format(product.priceCop).replace(',', '.') + " COP",
                    color = BrandColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        val outOfStock = product.stockUnits <= 0
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            // Botón secundario: contactar al caficultor por chat.
            androidx.compose.material3.OutlinedButton(
                onClick = onContact,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = null,
                    tint = BrandColors.FarmerPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "Contactar",
                    color = BrandColors.FarmerPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Button(
                onClick = onBuy,
                enabled = !isBuying && !outOfStock,
                modifier = Modifier
                    .weight(1.4f)
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.CoffeeBrown,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = when {
                        outOfStock -> "Agotado"
                        isBuying -> "Procesando…"
                        else -> "Comprar 1"
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        if (!outOfStock) {
            Text(
                text = "Stock disponible: ${product.stockUnits}",
                color = BrandColors.TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

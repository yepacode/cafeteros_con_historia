package com.cafeteros.historia.ui.features.cart

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Remove
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
import com.cafeteros.historia.ui.components.Base64Image
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

@Composable
fun CartScreen(
    state: CartUiState,
    onBack: () -> Unit,
    onInc: (productId: String) -> Unit,
    onDec: (productId: String) -> Unit,
    onRemove: (productId: String) -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)) {
        TopBar(itemCount = state.lines.size, onBack = onBack)

        if (state.lines.isEmpty()) {
            EmptyCart(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = BrandSpacing.lg,
                    vertical = BrandSpacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                items(state.lines) { line ->
                    CartLineRow(
                        line = line,
                        onInc = { onInc(line.productId) },
                        onDec = { onDec(line.productId) },
                        onRemove = { onRemove(line.productId) }
                    )
                }
            }
            CheckoutBar(totalCop = state.totalCop, onCheckout = onCheckout)
        }
    }
}

@Composable
private fun TopBar(itemCount: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Mi Carrito",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = BrandColors.TextPrimary
                )
            )
            if (itemCount > 0) {
                Text(
                    text = "$itemCount producto${if (itemCount == 1) "" else "s"}",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun EmptyCart(modifier: Modifier = Modifier) {
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
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = null,
                tint = BrandColors.CoffeeBrown,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Tu carrito está vacío",
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
            text = "Vuelve al catálogo y agrega productos para continuar.",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CartLineRow(
    line: CartLine,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit
) {
    val product = line.product
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Base64Image(
            base64 = product?.imageBase64,
            contentDescription = product?.name,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = BrandSpacing.sm)) {
            Text(
                text = product?.name ?: "(Producto eliminado)",
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "$" + "%,d".format(line.subtotalCop).replace(',', '.'),
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .background(BrandColors.InputBackground, RoundedCornerShape(50))
                    .padding(horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDec, enabled = line.quantity > 1) {
                    Icon(
                        imageVector = Icons.Outlined.Remove,
                        contentDescription = "Disminuir",
                        tint = if (line.quantity > 1) BrandColors.TextPrimary else BrandColors.TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = line.quantity.toString(),
                    modifier = Modifier.padding(horizontal = 6.dp),
                    color = BrandColors.TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onInc,
                    enabled = product != null && line.quantity < (product.stockUnits)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Aumentar",
                        tint = BrandColors.TextPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Quitar del carrito",
                tint = BrandColors.TextSecondary
            )
        }
    }
}

@Composable
private fun CheckoutBar(totalCop: Long, onCheckout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground)
            .systemBarsPadding()
            .padding(BrandSpacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "TOTAL",
                    color = BrandColors.TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "$" + "%,d".format(totalCop).replace(',', '.'),
                    color = BrandColors.TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }
            Button(
                onClick = onCheckout,
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.CoffeeBrown,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Continuar al pago", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

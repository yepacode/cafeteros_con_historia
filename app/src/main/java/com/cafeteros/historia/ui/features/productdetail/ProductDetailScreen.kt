package com.cafeteros.historia.ui.features.productdetail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.ui.components.Base64Image
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Pantalla de detalle del producto vista por el comprador.
 *
 * Estructura:
 *  - Hero: foto del producto (Base64) + flecha atrás superpuesta.
 *  - Sección principal: nombre, categoría, precio, descripción.
 *  - Sección caficultor: avatar + nombre + región + botones contactar/ver.
 *  - Detalles: presentación, formato, variedad, notas, certificación.
 *  - Selector de cantidad (+/-).
 *  - Bottom bar: "Agregar al carrito" + "Comprar ahora".
 */
@Composable
fun ProductDetailScreen(
    state: ProductDetailUiState,
    onBack: () -> Unit,
    onIncQty: () -> Unit,
    onDecQty: () -> Unit,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
    onOpenCaficultor: () -> Unit,
    onContactCaficultor: () -> Unit
) {
    val product = state.product
    if (product == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BrandColors.AuthBackground)
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Cargando producto…", color = BrandColors.TextSecondary)
        }
        return
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)) {
                Base64Image(
                    base64 = product.imageBase64,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(BrandSpacing.sm)
                        .size(40.dp)
                        .background(BrandColors.HeroOverlayButtonBackground, CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Volver",
                        tint = BrandColors.TextPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(BrandSpacing.lg)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                MainSection(product = product)
                CaficultorSection(
                    farm = state.farm,
                    caficultorName = state.caficultorName,
                    onOpenCaficultor = onOpenCaficultor,
                    onContact = onContactCaficultor
                )
                ExtraInfoSection(product = product)
                QuantitySection(
                    quantity = state.quantity,
                    maxStock = product.stockUnits,
                    onInc = onIncQty,
                    onDec = onDecQty
                )
                Spacer(modifier = Modifier.height(BrandSpacing.lg))
            }
        }

        BottomActionBar(
            stockUnits = product.stockUnits,
            onAddToCart = onAddToCart,
            onBuyNow = onBuyNow
        )
    }
}

@Composable
private fun MainSection(product: Product) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = product.category.label.uppercase(),
            color = BrandColors.TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = product.name,
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary,
                lineHeight = 30.sp
            )
        )
        Text(
            text = "$" + "%,d".format(product.priceCop).replace(',', '.') + " COP",
            color = BrandColors.TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        val stockColor = if (product.stockUnits > 0)
            BrandColors.FarmerPrimary else Color(0xFFB23A3A)
        Text(
            text = if (product.stockUnits > 0)
                "✓ ${product.stockUnits} unidades disponibles"
            else "✗ Agotado",
            color = stockColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        if (product.shortDescription.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.shortDescription,
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 19.sp
            )
        }
        if (product.fullDescription.isNotBlank()) {
            Text(
                text = product.fullDescription,
                color = BrandColors.TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun CaficultorSection(
    farm: FarmProfile?,
    caficultorName: String,
    onOpenCaficultor: () -> Unit,
    onContact: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val avatar = farm?.farmerPhotoBase64
        if (avatar != null) {
            Base64Image(
                base64 = avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = BrandSpacing.sm)) {
            Text(
                text = "VENDIDO POR",
                color = BrandColors.TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Text(
                text = caficultorName,
                color = BrandColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            )
            if (!farm?.region.isNullOrBlank()) {
                Text(
                    text = "📍 ${farm?.region}",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
        IconButton(onClick = onContact) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Contactar",
                tint = BrandColors.FarmerPrimary
            )
        }
        Text(
            text = "Ver →",
            modifier = Modifier.clickable(onClick = onOpenCaficultor),
            color = BrandColors.FarmerPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ExtraInfoSection(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "DETALLES",
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        InfoRow(label = "Presentación", value = "${product.weightGrams}g")
        InfoRow(label = "Formato", value = product.format.label)
        if (product.varietyChips.isNotEmpty()) {
            InfoRow(label = "Variedad", value = product.varietyChips.joinToString(", "))
        }
        if (product.tastingNotes.isNotEmpty()) {
            InfoRow(label = "Notas de cata", value = product.tastingNotes.joinToString(", "))
        }
        if (product.isOrganic) {
            InfoRow(label = "Certificación", value = "🌱 Orgánico")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = BrandColors.TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = BrandColors.TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1.5f)
        )
    }
}

@Composable
private fun QuantitySection(
    quantity: Int,
    maxStock: Int,
    onInc: () -> Unit,
    onDec: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Cantidad",
            modifier = Modifier.weight(1f),
            color = BrandColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier
                .background(BrandColors.InputBackground, RoundedCornerShape(50))
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDec,
                enabled = quantity > 1
            ) {
                Icon(
                    imageVector = Icons.Outlined.Remove,
                    contentDescription = "Disminuir",
                    tint = if (quantity > 1) BrandColors.TextPrimary else BrandColors.TextSecondary
                )
            }
            Text(
                text = quantity.toString(),
                modifier = Modifier.padding(horizontal = 12.dp),
                color = BrandColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = onInc,
                enabled = quantity < maxStock
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Aumentar",
                    tint = if (quantity < maxStock) BrandColors.TextPrimary else BrandColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun BottomActionBar(
    stockUnits: Int,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit
) {
    val outOfStock = stockUnits <= 0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground)
            .systemBarsPadding()
            .padding(BrandSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        OutlinedButton(
            onClick = onAddToCart,
            enabled = !outOfStock,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(
                text = "Agregar",
                color = if (outOfStock) BrandColors.TextSecondary else BrandColors.CoffeeBrown,
                fontWeight = FontWeight.SemiBold
            )
        }
        Button(
            onClick = onBuyNow,
            enabled = !outOfStock,
            modifier = Modifier
                .weight(1.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.CoffeeBrown,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (outOfStock) "Agotado" else "Comprar ahora",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

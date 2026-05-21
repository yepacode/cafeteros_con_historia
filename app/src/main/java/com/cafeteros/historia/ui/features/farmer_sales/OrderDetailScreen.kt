package com.cafeteros.historia.ui.features.farmer_sales

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderItem
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Detalle de un pedido visto por el caficultor.
 *
 * Muestra comprador, dirección, líneas del pedido, total y la barra de
 * acciones de transición de estado. La barra ofrece el siguiente paso
 * lógico (PENDING → ACCEPTED → PACKED → SHIPPED → DELIVERED) y la opción
 * de cancelar mientras el pedido no esté entregado.
 *
 * @param order pedido cargado por el ViewModel; null mientras carga.
 * @param isUpdating true mientras se persiste un cambio de estado (botones
 *  quedan deshabilitados).
 * @param onBack cierra la activity.
 * @param onChangeStatus dispara la transición al estado pasado.
 */
@Composable
fun OrderDetailScreen(
    order: Order?,
    isUpdating: Boolean,
    onBack: () -> Unit,
    onChangeStatus: (OrderStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        TopBar(orderId = order?.id, onBack = onBack)

        if (order == null) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Cargando pedido…", color = BrandColors.TextSecondary)
            }
            return@Column
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            StatusSection(order = order)
            BuyerSection(order = order)
            ItemsSection(items = order.items)
            TotalSection(order = order)
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }

        ActionBar(
            currentStatus = order.status,
            isUpdating = isUpdating,
            onChangeStatus = onChangeStatus
        )
    }
}

@Composable
private fun TopBar(orderId: String?, onBack: () -> Unit) {
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Detalle del pedido",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            if (orderId != null) {
                Text(
                    text = "#${orderId.take(8).uppercase()}",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
private fun StatusSection(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "ESTADO ACTUAL",
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Text(
            text = order.status.label,
            color = statusColor(order.status),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

@Composable
private fun BuyerSection(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "COMPRADOR",
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Text(
            text = order.compradorName.ifBlank { "—" },
            color = BrandColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Serif
        )
        if (order.shippingAddress.isNotBlank()) {
            Text(
                text = "📍 ${order.shippingAddress}",
                color = BrandColors.TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ItemsSection(items: List<OrderItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "PRODUCTOS (${items.size})",
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        items.forEachIndexed { index, item ->
            if (index > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(BrandColors.DividerLine)
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.productName,
                        color = BrandColors.TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${item.quantity} × $" +
                            "%,d".format(item.unitPriceCop).replace(',', '.'),
                        color = BrandColors.TextSecondary,
                        fontSize = 11.sp
                    )
                }
                Text(
                    text = "$" + "%,d".format(item.subtotalCop).replace(',', '.'),
                    color = BrandColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TotalSection(order: Order) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CoffeeBrown, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "TOTAL",
            color = BrandColors.CreamWhiteMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = "$" + "%,d".format(order.totalCop).replace(',', '.'),
            color = BrandColors.CreamWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

/**
 * Barra inferior con la transición al siguiente estado (acción primaria)
 * y la opción de cancelar (acción secundaria). Si el pedido ya está
 * entregado o cancelado, solo muestra el mensaje informativo.
 */
@Composable
private fun ActionBar(
    currentStatus: OrderStatus,
    isUpdating: Boolean,
    onChangeStatus: (OrderStatus) -> Unit
) {
    val nextStatus = nextStep(currentStatus)
    val canCancel = currentStatus !in setOf(OrderStatus.DELIVERED, OrderStatus.CANCELLED)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.AuthBackground)
            .padding(BrandSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (canCancel) {
            OutlinedButton(
                onClick = { onChangeStatus(OrderStatus.CANCELLED) },
                enabled = !isUpdating,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cancelar", color = Color(0xFFB23A3A))
            }
        }
        if (nextStatus != null) {
            Button(
                onClick = { onChangeStatus(nextStatus) },
                enabled = !isUpdating,
                modifier = Modifier.weight(if (canCancel) 1f else 2f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.FarmerPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Marcar como ${nextStatus.label.lowercase()}",
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else if (!canCancel) {
            Text(
                text = if (currentStatus == OrderStatus.DELIVERED)
                    "✅ Pedido entregado"
                else "Pedido cancelado",
                modifier = Modifier.weight(1f),
                color = BrandColors.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/** Devuelve el siguiente estado natural en la línea de tiempo, o null si terminó. */
private fun nextStep(current: OrderStatus): OrderStatus? = when (current) {
    OrderStatus.PENDING -> OrderStatus.ACCEPTED
    OrderStatus.ACCEPTED -> OrderStatus.PACKED
    OrderStatus.PACKED -> OrderStatus.SHIPPED
    OrderStatus.SHIPPED -> OrderStatus.DELIVERED
    OrderStatus.DELIVERED, OrderStatus.CANCELLED -> null
}

private fun statusColor(status: OrderStatus): Color = when (status) {
    OrderStatus.PENDING -> Color(0xFF8C6E1F)
    OrderStatus.ACCEPTED, OrderStatus.PACKED, OrderStatus.DELIVERED -> BrandColors.FarmerPrimary
    OrderStatus.SHIPPED -> BrandColors.CoffeeBrown
    OrderStatus.CANCELLED -> Color(0xFFC62828)
}

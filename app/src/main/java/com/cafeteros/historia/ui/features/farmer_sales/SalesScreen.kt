package com.cafeteros.historia.ui.features.farmer_sales

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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pantalla "Mis ventas" del caficultor.
 *
 * Stateless: recibe la lista de pedidos del contenedor ([SalesActivity])
 * y delega el tap para abrir el detalle. Cuando no hay pedidos, muestra un
 * estado vacío explicativo para que el caficultor entienda qué pasará
 * cuando un comprador haga una compra.
 *
 * @param orders pedidos donde el caficultor logueado es el vendedor.
 * @param onBack callback para cerrar la activity.
 * @param onOrderTap callback al tocar un pedido (abre detalle por id).
 */
@Composable
fun SalesScreen(
    orders: List<Order>,
    onBack: () -> Unit,
    onOrderTap: (orderId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        TopBar(orderCount = orders.size, onBack = onBack)

        if (orders.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = BrandSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = BrandSpacing.sm,
                    bottom = BrandSpacing.lg
                )
            ) {
                items(orders) { order ->
                    OrderCard(order = order, onClick = { onOrderTap(order.id) })
                }
            }
        }
    }
}

@Composable
private fun TopBar(orderCount: Int, onBack: () -> Unit) {
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
            text = "Mis Ventas",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = BrandColors.TextPrimary
            )
        )
        if (orderCount > 0) {
            Text(
                text = "$orderCount pedido${if (orderCount == 1) "" else "s"}",
                modifier = Modifier.padding(end = BrandSpacing.md),
                color = BrandColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(BrandSpacing.lg),
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
            text = "Aún no tienes pedidos",
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
            text = "Cuando un comprador haga una compra de alguno de tus productos, " +
                "verás el pedido aquí para procesarlo.",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

/**
 * Card resumen de un pedido: estado + comprador + total + items.
 * Tap → abre el detalle ([OrderDetailActivity]).
 */
@Composable
private fun OrderCard(order: Order, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusBadge(status = order.status)
            Text(
                text = formatRelativeDate(order.createdAtEpochMillis),
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
        }
        Text(
            text = order.compradorName.ifBlank { "Comprador" },
            color = BrandColors.TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = "${order.totalUnits} unidad${if (order.totalUnits == 1) "" else "es"} · " +
                "${order.items.size} producto${if (order.items.size == 1) "" else "s"}",
            color = BrandColors.TextSecondary,
            fontSize = 12.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$" + "%,d".format(order.totalCop).replace(',', '.'),
                color = BrandColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "Ver detalle ›",
                color = BrandColors.FarmerPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/** Badge pequeño con el estado del pedido, color por categoría. */
@Composable
private fun StatusBadge(status: OrderStatus) {
    val (bg, fg) = when (status) {
        OrderStatus.PENDING -> Color(0xFFFFF1B8) to Color(0xFF8C6E1F)
        OrderStatus.ACCEPTED -> Color(0xFFE8F4EC) to BrandColors.FarmerPrimary
        OrderStatus.PACKED -> Color(0xFFE8F4EC) to BrandColors.FarmerPrimary
        OrderStatus.SHIPPED -> Color(0xFFEDE6D7) to BrandColors.CoffeeBrown
        OrderStatus.DELIVERED -> Color(0xFFD9EAD3) to BrandColors.FarmerPrimary
        OrderStatus.CANCELLED -> Color(0xFFFCE4E6) to Color(0xFFC62828)
    }
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = status.label.uppercase(),
            color = fg,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

/**
 * Formatea una fecha epoch como "hoy 10:45", "ayer 16:30" o "21 abr 2026".
 * Útil para que la card no muestre timestamps largos.
 */
private fun formatRelativeDate(epochMillis: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - epochMillis
    val oneDay = 24L * 60 * 60 * 1000
    val timeFmt = SimpleDateFormat("HH:mm", Locale("es", "CO"))
    val dateFmt = SimpleDateFormat("dd MMM yyyy", Locale("es", "CO"))
    return when {
        diff < oneDay -> "Hoy ${timeFmt.format(Date(epochMillis))}"
        diff < 2 * oneDay -> "Ayer ${timeFmt.format(Date(epochMillis))}"
        else -> dateFmt.format(Date(epochMillis))
    }
}

package com.cafeteros.historia.ui.features.buyer_catalog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Activity contenedora de "Mis pedidos" del comprador.
 *
 * Muestra el historial de pedidos del usuario logueado (como comprador),
 * con su estado actualizado en tiempo real a medida que el caficultor lo
 * procesa.
 */
class MyPurchasesActivity : ComponentActivity() {

    private val viewModel: MyPurchasesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val orders by viewModel.orders.collectAsStateWithLifecycle()
                MyPurchasesScreen(
                    orders = orders,
                    onBack = ::finish,
                    onLeaveReview = { orderId ->
                        LeaveReviewActivity.start(this, orderId)
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MyPurchasesActivity::class.java))
        }
    }
}

@Composable
private fun MyPurchasesScreen(
    orders: List<Order>,
    onBack: () -> Unit,
    onLeaveReview: (orderId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
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
                text = "Mis Pedidos",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = BrandColors.TextPrimary
                )
            )
            if (orders.isNotEmpty()) {
                Text(
                    text = "${orders.size}",
                    modifier = Modifier.padding(end = BrandSpacing.md),
                    color = BrandColors.TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        if (orders.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
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
                        imageVector = Icons.Outlined.Receipt,
                        contentDescription = null,
                        tint = BrandColors.CoffeeBrown,
                        modifier = Modifier.size(56.dp)
                    )
                }
                Spacer(modifier = Modifier.height(BrandSpacing.lg))
                Text(
                    text = "Aún no has comprado",
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
                    text = "Cuando hagas tu primera compra, verás aquí el estado del pedido.",
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
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
                    PurchaseCard(
                        order = order,
                        onLeaveReview = { onLeaveReview(order.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PurchaseCard(order: Order, onLeaveReview: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
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
        order.items.forEach { item ->
            Text(
                text = "${item.quantity}× ${item.productName}",
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = "$" + "%,d".format(order.totalCop).replace(',', '.'),
                color = BrandColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
        }
        // Si el pedido ya está entregado, mostrar acción para dejar reseña.
        // Si ya reseñó, LeaveReviewActivity le mostrará un mensaje al entrar.
        if (order.status == OrderStatus.DELIVERED) {
            Spacer(modifier = Modifier.height(BrandSpacing.xs))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(8.dp))
                    .clickable(onClick = onLeaveReview)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⭐ Dejar reseña",
                    color = Color(0xFF8C6E1F),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

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

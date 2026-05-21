package com.cafeteros.historia.ui.features.farmer_wallet

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

/** Filtros para el historial de movimientos. */
private enum class TxFilter(val label: String) {
    ALL("Todos"),
    DELIVERED("Entregados"),
    PENDING("En proceso")
}

/**
 * Activity de "Historial completo" de la billetera. Lista TODOS los
 * movimientos (sin truncar a 5 como hace WalletScreen). Reutiliza el
 * [WalletViewModel] porque la fuente de datos es la misma — los pedidos
 * del caficultor.
 */
class TransactionHistoryActivity : ComponentActivity() {

    private val viewModel: WalletViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                TransactionHistoryScreen(
                    movements = state.movements,
                    onBack = ::finish
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, TransactionHistoryActivity::class.java))
        }
    }
}

@Composable
private fun TransactionHistoryScreen(
    movements: List<Order>,
    onBack: () -> Unit
) {
    var filter by remember { mutableStateOf(TxFilter.ALL) }
    val filtered = remember(movements, filter) {
        when (filter) {
            TxFilter.ALL -> movements
            TxFilter.DELIVERED -> movements.filter { it.status == OrderStatus.DELIVERED }
            TxFilter.PENDING -> movements.filter { it.status != OrderStatus.DELIVERED }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        // Top bar
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
                text = "Historial",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
        }

        // Filtros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.xs),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            TxFilter.entries.forEach { f ->
                val active = f == filter
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (active) BrandColors.CoffeeBrown else BrandColors.CardBackground,
                            RoundedCornerShape(50)
                        )
                        .clickable { filter = f }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = f.label,
                        color = if (active) BrandColors.PrimaryButtonText else BrandColors.TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.sm))

        if (filtered.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = BrandSpacing.lg,
                    vertical = BrandSpacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                items(filtered) { order ->
                    HistoryRow(order = order)
                }
            }
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
                .size(96.dp)
                .background(BrandColors.InputBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Receipt,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.md))
        Text(
            text = "Sin movimientos",
            color = BrandColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun HistoryRow(order: Order) {
    val isDelivered = order.status == OrderStatus.DELIVERED
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Venta #${order.id.take(8).uppercase()}",
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${order.compradorName.ifBlank { "Comprador" }} · ${
                    formatDate(order.createdAtEpochMillis)
                }",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = order.status.label,
                color = if (isDelivered) BrandColors.FarmerPrimary else BrandColors.TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = (if (isDelivered) "+" else "") +
                "$" + "%,d".format(order.totalCop).replace(',', '.'),
            color = if (isDelivered) BrandColors.FarmerPrimary else BrandColors.TextSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

private fun formatDate(epochMillis: Long): String {
    val fmt = SimpleDateFormat("dd MMM yyyy", Locale("es", "CO"))
    return fmt.format(Date(epochMillis))
}

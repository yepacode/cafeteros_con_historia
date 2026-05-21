package com.cafeteros.historia.ui.features.farmer_wallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

/**
 * Activity contenedora de la billetera del caficultor. Usa
 * [WalletViewModel] que deriva el state desde los pedidos del caficultor
 * en Firestore (no hay una colección `/wallets` aparte — sería redundante).
 *
 * El "retiro" está deshabilitado en MVP porque no hay pasarela de pagos
 * conectada (ePayco/Mercado Pago); al pulsarlo muestra Toast informativo.
 */
class WalletActivity : ComponentActivity() {

    private val viewModel: WalletViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                WalletScreen(
                    state = state,
                    onBack = ::finish,
                    onWithdraw = {
                        Toast.makeText(
                            this,
                            "Retiros: próximamente con integración ePayco",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onOpenHistory = { TransactionHistoryActivity.start(this) }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, WalletActivity::class.java))
        }
    }
}

/**
 * Pantalla de billetera. Stateless: recibe el [state] derivado de pedidos
 * en `WalletViewModel`.
 */
@Composable
fun WalletScreen(
    state: WalletUiState,
    onBack: () -> Unit,
    onWithdraw: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = BrandSpacing.lg)
    ) {
        TopBar(onBack = onBack)

        Spacer(modifier = Modifier.height(BrandSpacing.sm))

        BalanceCard(state = state)

        Spacer(modifier = Modifier.height(BrandSpacing.md))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            MetricTile(
                modifier = Modifier.weight(1f),
                label = "ESTE MES",
                value = formatCop(state.monthRevenueCop)
            )
            MetricTile(
                modifier = Modifier.weight(1f),
                label = "PEDIDOS ENTREGADOS",
                value = state.deliveredCount.toString()
            )
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))

        Button(
            onClick = onWithdraw,
            modifier = Modifier
                .padding(horizontal = BrandSpacing.lg)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = state.availableCop > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.FarmerPrimary,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Outlined.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Retirar a mi cuenta", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        // Movimientos recientes
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.lg),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Movimientos recientes",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = "Ver todos",
                modifier = Modifier.clickable(onClick = onOpenHistory),
                color = BrandColors.FarmerPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(BrandSpacing.sm))

        if (state.movements.isEmpty()) {
            EmptyMovements()
        } else {
            Column(
                modifier = Modifier.padding(horizontal = BrandSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                state.movements.take(5).forEach { order ->
                    MovementRow(order = order)
                }
            }
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
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
            text = "Billetera",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
    }
}

@Composable
private fun BalanceCard(state: WalletUiState) {
    Column(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg)
            .fillMaxWidth()
            .background(BrandColors.CoffeeBrown, RoundedCornerShape(16.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "SALDO DISPONIBLE",
            color = BrandColors.CreamWhiteMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = formatCop(state.availableCop),
            color = BrandColors.CreamWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        if (state.pendingCop > 0) {
            Text(
                text = "+ ${formatCop(state.pendingCop)} pendientes de entrega",
                color = BrandColors.CreamWhiteMuted,
                fontSize = 12.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

@Composable
private fun MetricTile(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            color = BrandColors.TextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Text(
            text = value,
            color = BrandColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

@Composable
private fun EmptyMovements() {
    Column(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg)
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier.size(64.dp).background(BrandColors.InputBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = "Aún sin movimientos",
            color = BrandColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Cuando vendas tu primer café, lo verás aquí.",
            color = BrandColors.TextSecondary,
            fontSize = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

/** Fila de movimiento: una venta entregada o en proceso. */
@Composable
private fun MovementRow(order: Order) {
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
                    formatRelativeDate(order.createdAtEpochMillis)
                }",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = if (isDelivered) "✓ Entregado" else "⏳ ${order.status.label}",
                color = if (isDelivered) BrandColors.FarmerPrimary else BrandColors.TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = (if (isDelivered) "+" else "") + formatCop(order.totalCop.toLong()),
            color = if (isDelivered) BrandColors.FarmerPrimary else BrandColors.TextSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

private fun formatCop(amount: Long): String =
    "$" + "%,d".format(amount).replace(',', '.')

private fun formatRelativeDate(epochMillis: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - epochMillis
    val oneDay = 24L * 60 * 60 * 1000
    val dateFmt = SimpleDateFormat("dd MMM", Locale("es", "CO"))
    return when {
        diff < oneDay -> "Hoy"
        diff < 2 * oneDay -> "Ayer"
        else -> dateFmt.format(Date(epochMillis))
    }
}

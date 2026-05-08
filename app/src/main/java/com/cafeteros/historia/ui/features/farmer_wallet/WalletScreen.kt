package com.cafeteros.historia.ui.features.farmer_wallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

// ── Datos mock del feature ──────────────────────────────────────────────────

internal data class MockMovement(
    val id: String,
    val type: MovementType,
    val title: String,
    val subtitle: String,
    val amount: Int
)

internal enum class MovementType { INCOME, OUTCOME, PENDING }

internal val MOCK_MOVEMENTS: List<MockMovement> = listOf(
    MockMovement("M1", MovementType.INCOME, "+\$126.720", "Venta #OR-34521 · Hoy", 126_720),
    MockMovement("M2", MovementType.OUTCOME, "-\$500.000", "Retiro a Bancolombia · Ayer", -500_000),
    MockMovement("M3", MovementType.PENDING, "+\$245.000", "Venta #OR-34519 · Pendiente", 245_000)
)

class WalletActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                WalletScreen(
                    onBack = ::finish,
                    onWithdraw = { WithdrawActivity.start(this) },
                    onOpenHistory = { TransactionHistoryActivity.start(this) },
                    onMovementTap = { id -> TransactionDetailActivity.start(this, id) }
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

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onWithdraw: () -> Unit,
    onOpenHistory: () -> Unit,
    onMovementTap: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = BrandSpacing.lg)
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
                text = "Billetera",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .padding(end = BrandSpacing.sm)
                    .size(32.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape)
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            BalanceCard(available = 4_820_500, pending = 680_000)

            ActionsRow(
                onWithdraw = onWithdraw,
                onHistory = onOpenHistory,
                onInvoices = { /* TODO: pantalla facturas */ }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                MetricMini(
                    modifier = Modifier.weight(1f),
                    label = "Este mes",
                    value = "\$4.850k",
                    delta = "+18%"
                )
                MetricMini(
                    modifier = Modifier.weight(1f),
                    label = "Ventas netas",
                    value = "\$4.268k",
                    delta = null
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                MetricMini(
                    modifier = Modifier.weight(1f),
                    label = "Retenciones",
                    value = "\$42.000",
                    delta = null
                )
                MetricMini(
                    modifier = Modifier.weight(1f),
                    label = "Retiros",
                    value = "\$3.200k",
                    delta = null
                )
            }

            // Banner amarillo: pendientes por liberar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                    .clickable { /* TODO: detalle pendientes */ }
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = Color(0xFF8C6E1F),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "\$680.000 en 3 pedidos",
                        color = BrandColors.TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Próxima liberación en 2 días",
                        color = BrandColors.TextSecondary,
                        fontSize = 11.sp
                    )
                }
                Text(
                    text = "Ver detalle",
                    color = Color(0xFF8C6E1F),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Movimientos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Movimientos",
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
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            MOCK_MOVEMENTS.forEach { mov ->
                MovementRow(movement = mov, onClick = { onMovementTap(mov.id) })
            }

            // Cuenta vinculada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFFFD23F), RoundedCornerShape(6.dp))
                )
                Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
                    Text(text = "**** 8754", color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "Cuenta vinculada", color = BrandColors.TextSecondary, fontSize = 11.sp)
                }
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(BrandColors.FarmerPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }

            // Información tributaria
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Receipt, contentDescription = null, tint = BrandColors.TextPrimary, modifier = Modifier.size(20.dp))
                Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
                    Text(text = "Información tributaria", color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "RUT actualizado ✓", color = BrandColors.TextSecondary, fontSize = 11.sp)
                }
                Text(text = "EDITAR", color = BrandColors.FarmerPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
        }
    }
}

@Composable
private fun BalanceCard(available: Int, pending: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CoffeeBrown, RoundedCornerShape(16.dp))
            .padding(BrandSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SALDO DISPONIBLE",
                color = BrandColors.CreamWhiteMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Icon(Icons.Outlined.Visibility, contentDescription = null, tint = BrandColors.CreamWhiteMuted, modifier = Modifier.size(16.dp))
        }
        Text(
            text = "\$" + "%,d".format(available).replace(',', '.'),
            color = BrandColors.CreamWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = "Pendiente por liberar: \$" + "%,d".format(pending).replace(',', '.'),
            color = BrandColors.CreamWhiteMuted,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun ActionsRow(
    onWithdraw: () -> Unit,
    onHistory: () -> Unit,
    onInvoices: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        ActionPill(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.AccountBalanceWallet,
            label = "Retirar",
            background = Color(0xFFC9A24A),
            contentColor = Color.White,
            onClick = onWithdraw
        )
        ActionPill(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.History,
            label = "Historial",
            background = BrandColors.CardBackground,
            contentColor = BrandColors.TextPrimary,
            onClick = onHistory
        )
        ActionPill(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Receipt,
            label = "Facturas",
            background = BrandColors.CardBackground,
            contentColor = BrandColors.TextPrimary,
            onClick = onInvoices
        )
    }
}

@Composable
private fun ActionPill(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    background: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(background, RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.size(6.dp))
        Text(text = label, color = contentColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MetricMini(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    delta: String?
) {
    Column(
        modifier = modifier
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = label, color = BrandColors.TextSecondary, fontSize = 11.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                color = BrandColors.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            if (delta != null) {
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = delta,
                    color = BrandColors.FarmerPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun MovementRow(movement: MockMovement, onClick: () -> Unit) {
    val (iconBg, iconTint, icon) = when (movement.type) {
        MovementType.INCOME -> Triple(Color(0xFFE8F4EC), BrandColors.FarmerPrimary, Icons.Outlined.Add)
        MovementType.OUTCOME -> Triple(Color(0xFFF6D9D2), Color(0xFFB23A3A), Icons.Outlined.Remove)
        MovementType.PENDING -> Triple(Color(0xFFFFF1B8), Color(0xFF8C6E1F), Icons.Outlined.AccessTime)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(36.dp).background(iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp)) }
        Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
            Text(text = movement.title, color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = movement.subtitle, color = BrandColors.TextSecondary, fontSize = 11.sp)
        }
        Text(text = "›", color = BrandColors.TextSecondary, fontSize = 18.sp)
    }
}

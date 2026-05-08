package com.cafeteros.historia.ui.features.farmer_wallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ShoppingBag
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

private enum class TxFilter(val label: String) {
    ALL("Todos"), SALES("Ventas"), WITHDRAWALS("Retiros"), COMMISSIONS("Comisiones")
}

private enum class TxKind { SALE, COMMISSION, WITHDRAWAL, WITHHOLDING }
private enum class TxStatus(val label: String, val bg: Color, val fg: Color) {
    LIBERATED("LIBERADO", Color(0xFFE8F4EC), Color(0xFF1F4D38)),
    PENDING("PENDIENTE", Color(0xFFFFF1B8), Color(0xFF8C6E1F)),
    PROCESSED("PROCESADO", Color(0xFFE8E2D5), Color(0xFF1A1A1A)),
    SUCCESSFUL("EXITOSO", Color(0xFFE8F4EC), Color(0xFF1F4D38))
}

private data class TxRow(
    val id: String,
    val title: String,
    val subtitle: String,
    val time: String,
    val amount: Int,
    val kind: TxKind,
    val status: TxStatus
)

private data class DayGroup(val dateLabel: String, val netLabel: String, val items: List<TxRow>)

private val MOCK_HISTORY: List<DayGroup> = listOf(
    DayGroup(
        dateLabel = "Hoy, 21 de abril",
        netLabel = "\$126.720 neto",
        items = listOf(
            TxRow("M1", "Venta #OR-34521", "3 productos · María G.", "10:45 AM", 126_720, TxKind.SALE, TxStatus.LIBERATED),
            TxRow("M1c", "Comisión Origen", "Uso de plataforma", "10:45 AM", -17_280, TxKind.COMMISSION, TxStatus.PROCESSED)
        )
    ),
    DayGroup(
        dateLabel = "Ayer, 20 de abril",
        netLabel = "\$241.500 neto",
        items = listOf(
            TxRow("M3", "Venta #OR-34519", "Pendiente liberación", "04:30 PM", 245_000, TxKind.SALE, TxStatus.PENDING),
            TxRow("M2", "Retiro a Bancolombia", "Cuenta ****4521", "11:15 AM", -500_000, TxKind.WITHDRAWAL, TxStatus.SUCCESSFUL),
            TxRow("M4", "Retención ICA", "Impuesto territorial", "11:00 AM", -3_500, TxKind.WITHHOLDING, TxStatus.PROCESSED)
        )
    )
)

class TransactionHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                TransactionHistoryScreen(
                    onBack = ::finish,
                    onTxTap = { id -> TransactionDetailActivity.start(this, id) },
                    onExport = { format ->
                        Toast.makeText(this, "Próximamente: exportar $format", Toast.LENGTH_SHORT).show()
                    }
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
fun TransactionHistoryScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onTxTap: (String) -> Unit,
    onExport: (String) -> Unit
) {
    var activeFilter by remember { mutableStateOf(TxFilter.ALL) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp)) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver", tint = BrandColors.TextPrimary)
                }
                Text(
                    text = "Historial",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                )
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Outlined.FilterList, contentDescription = "Filtros", tint = BrandColors.TextPrimary)
                }
            }

            // Banner amarillo: período + balance
            Column(
                modifier = Modifier
                    .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.xs)
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Del 1 al 21 abril", color = Color(0xFF8C6E1F), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "📅", fontSize = 14.sp)
                }
                Text(text = "BALANCE DEL PERÍODO", color = Color(0xFF8C6E1F), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                Text(text = "+\$4.268.000", color = BrandColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
            }

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            // Filter tabs
            Row(
                modifier = Modifier.padding(horizontal = BrandSpacing.lg),
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                TxFilter.entries.forEach { f ->
                    val active = f == activeFilter
                    Box(
                        modifier = Modifier
                            .background(
                                if (active) BrandColors.CoffeeBrown else BrandColors.CardBackground,
                                RoundedCornerShape(50)
                            )
                            .clickable { activeFilter = f }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
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
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = BrandSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                MOCK_HISTORY.forEach { group ->
                    val visible = group.items.filter { matchesFilter(it.kind, activeFilter) }
                    if (visible.isEmpty()) return@forEach

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = group.dateLabel,
                                color = BrandColors.TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(text = group.netLabel, color = BrandColors.FarmerPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    items(visible) { tx -> TxItem(tx = tx, onClick = { onTxTap(tx.id) }) }
                }
                item { Spacer(modifier = Modifier.height(BrandSpacing.lg)) }
            }
        }

        // Banner inferior: exportar período
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(BrandSpacing.md)
                .background(BrandColors.CoffeeBrown, RoundedCornerShape(50))
                .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.IosShare, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Exportar período", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            ExportFormatChip(label = "PDF", onClick = { onExport("PDF") })
            Spacer(modifier = Modifier.size(6.dp))
            ExportFormatChip(label = "XLS", onClick = { onExport("XLS") })
            Spacer(modifier = Modifier.size(6.dp))
            ExportFormatChip(label = "CSV", onClick = { onExport("CSV") })
        }
    }
}

private fun matchesFilter(kind: TxKind, f: TxFilter): Boolean = when (f) {
    TxFilter.ALL -> true
    TxFilter.SALES -> kind == TxKind.SALE
    TxFilter.WITHDRAWALS -> kind == TxKind.WITHDRAWAL
    TxFilter.COMMISSIONS -> kind == TxKind.COMMISSION || kind == TxKind.WITHHOLDING
}

@Composable
private fun TxItem(tx: TxRow, onClick: () -> Unit) {
    val (iconBg, icon) = iconForKind(tx.kind)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(36.dp).background(iconBg, CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
            Text(text = tx.title, color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(text = tx.subtitle, color = BrandColors.TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .background(tx.status.bg, RoundedCornerShape(50))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = tx.status.label, color = tx.status.fg, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            val sign = if (tx.amount >= 0) "+" else "-"
            Text(
                text = "$sign\$" + "%,d".format(kotlin.math.abs(tx.amount)).replace(',', '.'),
                color = if (tx.amount >= 0) BrandColors.FarmerPrimary else Color(0xFFB23A3A),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = tx.time, color = BrandColors.TextSecondary, fontSize = 10.sp)
        }
    }
}

private fun iconForKind(kind: TxKind): Pair<Color, ImageVector> = when (kind) {
    TxKind.SALE -> Color(0xFF1F4D38) to Icons.Outlined.ShoppingBag
    TxKind.COMMISSION -> Color(0xFFB23A3A) to Icons.Outlined.Percent
    TxKind.WITHDRAWAL -> Color(0xFF8C6E1F) to Icons.Outlined.AccountBalance
    TxKind.WITHHOLDING -> Color(0xFF6B6B6B) to Icons.Outlined.Description
}

@Composable
private fun ExportFormatChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

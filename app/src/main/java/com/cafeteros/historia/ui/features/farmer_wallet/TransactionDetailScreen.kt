package com.cafeteros.historia.ui.features.farmer_wallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

class TransactionDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val txId = intent.getStringExtra(EXTRA_TX_ID) ?: "—"
        setContent {
            CafeterosTheme {
                TransactionDetailScreen(
                    txId = txId,
                    onBack = ::finish,
                    onAction = { Toast.makeText(this, "Próximamente: $it", Toast.LENGTH_SHORT).show() }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_TX_ID = "extra_tx_id"
        fun start(context: Context, txId: String) {
            context.startActivity(Intent(context, TransactionDetailActivity::class.java).putExtra(EXTRA_TX_ID, txId))
        }
    }
}

/** Detalle de una transacción de venta (datos mock). El parámetro `txId` se
 *  usa solo en el título; el resto del contenido es estático por ahora. */
@Composable
fun TransactionDetailScreen(
    modifier: Modifier = Modifier,
    txId: String,
    onBack: () -> Unit,
    onAction: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver", tint = BrandColors.TextPrimary)
            }
            Text(
                text = "Detalle de Venta",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
            IconButton(onClick = { onAction("Más opciones") }) {
                Icon(Icons.Outlined.MoreVert, contentDescription = "Más", tint = BrandColors.TextPrimary)
            }
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

            // Hero: check + monto
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(60.dp).background(BrandColors.FarmerPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp)) }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "+\$126.720",
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                )
                Text(text = "Venta #OR-34521 confirmada", color = BrandColors.TextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE8F4EC), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(text = "✓ LIBERADO", color = BrandColors.FarmerPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                }
            }

            // Tabla de detalles
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetaRow("TIPO", "Venta")
                MetaRow("FECHA", "21 abril 2026, 10:45 AM")
                MetaRow("PEDIDO", "#OR-34521")
                MetaRow("COMPRADOR", "María González")
                MetaRow("PRODUCTOS", "3 items")
                MetaRow("ID TRANSACCIÓN", "TX-08374625")
            }

            // Desglose
            Text(
                text = "Desglose",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BreakdownLine("Monto del pedido", 144_000)
                BreakdownLine("Descuento aplicado", 0)
                BreakdownLine("Subtotal", 144_000)
                BreakdownLine("Envío (lo paga comprador)", 12_000)
                BreakdownLine("Total pagado", 146_000, isBold = true)
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
                BreakdownLine("Subtotal de tus productos", 144_000, small = true)
                BreakdownLine("Comisión Origen 12%", -17_280, valueColor = Color(0xFFB23A3A), small = true)
                BreakdownLine("Retención ICA", 0, small = true)
                BreakdownLine("Retención IVA", 0, small = true)
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Tu ingreso neto", color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "\$126.720",
                        color = BrandColors.FarmerPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "📅 Liberado en tu saldo el 29 abril",
                color = BrandColors.TextSecondary,
                fontSize = 12.sp
            )

            // Factura electrónica
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Description, contentDescription = null, tint = BrandColors.TextPrimary, modifier = Modifier.size(20.dp))
                Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
                    Text(text = "Factura electrónica PDF", color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "Disponible para descarga", color = BrandColors.TextSecondary, fontSize = 11.sp)
                }
                TextButton(onClick = { onAction("Ver en DIAN") }) {
                    Text(text = "Ver en DIAN", color = BrandColors.FarmerPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            OutlinedButton(
                onClick = { onAction("Ver pedido original") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "VER PEDIDO ORIGINAL", color = BrandColors.TextPrimary, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
            }

            TextButton(onClick = { onAction("Reportar problema") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Reportar un problema con esta transacción", color = Color(0xFFB23A3A), fontSize = 12.sp, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun MetaRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = BrandColors.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        Text(text = value, color = BrandColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun BreakdownLine(
    label: String,
    value: Int,
    isBold: Boolean = false,
    small: Boolean = false,
    valueColor: Color = BrandColors.TextPrimary
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = label,
            color = if (small) BrandColors.TextSecondary else BrandColors.TextPrimary,
            fontSize = if (small) 12.sp else 13.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        val sign = if (value < 0) "-" else if (small || value == 0) "" else ""
        val abs = kotlin.math.abs(value)
        Text(
            text = if (value < 0) "-\$" + "%,d".format(abs).replace(',', '.')
                else "\$" + "%,d".format(abs).replace(',', '.'),
            color = valueColor,
            fontSize = if (small) 12.sp else 13.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium
        )
    }
}

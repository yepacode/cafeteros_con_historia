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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

// ── Modelos del detalle (mock, reemplazar con backend) ──────────────────────

private data class OrderProduct(
    val name: String,
    val variant: String,
    val quantity: Int
)

private data class FinancialBreakdown(
    val subtotal: Int,
    val shipping: Int,
    val discount: Int,
    val total: Int,
    val originCommission: Int,
    val icaWithholding: Int,
    val netEarnings: Int
)

private data class CarrierInfo(
    val name: String,
    val service: String,
    val estimatedDelivery: String
)

private data class OrderDetailMock(
    val orderNumber: String,
    val statusLabel: String,
    val deadlineRelative: String,
    val customerName: String,
    val customerPreviousOrders: Int,
    val customerVerified: Boolean,
    val shippingAddress: String,
    val shippingCity: String,
    val products: List<OrderProduct>,
    val financials: FinancialBreakdown,
    val carrier: CarrierInfo
)

private val MOCK_ORDER_DETAIL = OrderDetailMock(
    orderNumber = "OR-34521",
    statusLabel = "NUEVO PEDIDO",
    deadlineRelative = "Recibido hace 30 min · Empaca antes de mañana 9 AM",
    customerName = "María González",
    customerPreviousOrders = 5,
    customerVerified = true,
    shippingAddress = "Carrera 7 # 71-21, Torre B, Apto 902",
    shippingCity = "Bogotá, Cundinamarca · 110221",
    products = listOf(
        OrderProduct("Café Huila Pitalito 250g", "Molienda media · Lavado", 2),
        OrderProduct("Filtros de Papel V60", "Pack x100 unidades", 1)
    ),
    financials = FinancialBreakdown(
        subtotal = 144_000,
        shipping = 12_000,
        discount = -10_000,
        total = 146_000,
        originCommission = -17_520,
        icaWithholding = -1_760,
        netEarnings = 126_720
    ),
    carrier = CarrierInfo(
        name = "Coordinadora",
        service = "Entrega red nodos",
        estimatedDelivery = "Jueves 23 de abril"
    )
)

private val PACKING_CHECKLIST: List<String> = listOf(
    "Calidad de producto revisada",
    "Bolsa sellada al vacío"
)

/**
 * Detalle de un pedido del caficultor. Compone todas las secciones del
 * diseño Stitch: banner de urgencia, datos del cliente, dirección,
 * productos, breakdown financiero, logística y checklist de empaque.
 *
 * Por ahora `orderNumber` solo se usa en el título — los datos vienen del
 * mock [MOCK_ORDER_DETAIL] porque la fuente real (API/Room) aún no existe.
 *
 * @param orderNumber número del pedido (ej. "OR-34521") para mostrar en
 *  el título de la pantalla.
 * @param onBack flecha atrás del top bar.
 * @param onAction callback genérico de las acciones (Marcar empacado,
 *  enviar mensaje, llamar, generar guía, imprimir ficha, ayuda). Hoy
 *  muestra Toast "Próximamente".
 */
@Composable
fun OrderDetailScreen(
    modifier: Modifier = Modifier,
    orderNumber: String,
    onBack: () -> Unit,
    onAction: (String) -> Unit
) {
    val detail = MOCK_ORDER_DETAIL.copy(orderNumber = orderNumber)
    var checkedItems by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        TopBar(
            orderNumber = detail.orderNumber,
            onBack = onBack,
            onShare = { onAction("Compartir") },
            onMore = { onAction("Más opciones") }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.xs))

            UrgencyBanner(
                statusLabel = detail.statusLabel,
                deadline = detail.deadlineRelative,
                onMarkPacked = { onAction("Marcar como empacado") }
            )

            CustomerSection(
                name = detail.customerName,
                previousOrders = detail.customerPreviousOrders,
                isVerified = detail.customerVerified,
                onMessage = { onAction("Enviar mensaje") },
                onCall = { onAction("Llamar") }
            )

            ShippingAddressSection(
                address = detail.shippingAddress,
                city = detail.shippingCity,
                onCopy = { onAction("Copiar dirección") }
            )

            SectionTitle("Productos")
            detail.products.forEach { product -> ProductRow(product = product) }

            SectionTitle("Resumen financiero")
            FinancialBreakdownCard(financials = detail.financials)

            SectionTitle("Logística")
            CarrierCard(carrier = detail.carrier)

            SectionTitle("Lista de empaque ✓")
            PackingChecklistCard(
                items = PACKING_CHECKLIST,
                checkedItems = checkedItems,
                onToggle = { item ->
                    checkedItems = if (item in checkedItems) checkedItems - item
                        else checkedItems + item
                }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            BottomActions(
                onGenerateGuide = { onAction("Generar guía de envío") },
                onPrintTicket = { onAction("Imprimir ficha") },
                onHelp = { onAction("Ayuda") }
            )
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

// ── Top bar ─────────────────────────────────────────────────────────────────

@Composable
private fun TopBar(
    orderNumber: String,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onMore: () -> Unit
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
            text = "Pedido #$orderNumber",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
        IconButton(onClick = onShare) {
            Icon(Icons.Outlined.Share, contentDescription = "Compartir", tint = BrandColors.TextPrimary)
        }
        IconButton(onClick = onMore) {
            Icon(Icons.Outlined.MoreVert, contentDescription = "Más", tint = BrandColors.TextPrimary)
        }
    }
}

// ── Banner amarillo de urgencia ─────────────────────────────────────────────

@Composable
private fun UrgencyBanner(
    statusLabel: String,
    deadline: String,
    onMarkPacked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFC9A24A), RoundedCornerShape(16.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "🟡 $statusLabel",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }
        Text(
            text = deadline,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp
        )
        Button(
            onClick = onMarkPacked,
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.FarmerPrimary,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Marcar como empacado", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
    }
}

// ── Sección del cliente ─────────────────────────────────────────────────────

@Composable
private fun CustomerSection(
    name: String,
    previousOrders: Int,
    isVerified: Boolean,
    onMessage: () -> Unit,
    onCall: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(BrandColors.FarmerPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = name,
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
        if (isVerified) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Comprador verificado",
                    color = BrandColors.TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.size(4.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = BrandColors.FarmerPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Text(
            text = "$previousOrders pedidos anteriores",
            color = BrandColors.TextSecondary,
            fontSize = 12.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            OutlinedButton(
                onClick = onMessage,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.size(6.dp))
                Text("Enviar mensaje", fontSize = 12.sp, color = BrandColors.TextPrimary)
            }
            OutlinedButton(
                onClick = onCall,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Outlined.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.size(6.dp))
                Text("Llamar", fontSize = 12.sp, color = BrandColors.TextPrimary)
            }
        }
    }
}

// ── Dirección de envío ──────────────────────────────────────────────────────

@Composable
private fun ShippingAddressSection(
    address: String,
    city: String,
    onCopy: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Dirección de envío",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Row(
                modifier = Modifier.clickable(onClick = onCopy),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = null,
                    tint = BrandColors.FarmerPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "Copiar",
                    color = BrandColors.FarmerPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Text(text = address, color = BrandColors.TextPrimary, fontSize = 14.sp)
        Text(text = city, color = BrandColors.TextSecondary, fontSize = 12.sp)
        // Placeholder de mapa: la integración con Google Maps llegará después.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color(0xFFE8E2D5), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = BrandColors.FarmerPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// ── Productos ───────────────────────────────────────────────────────────────

@Composable
private fun ProductRow(product: OrderProduct) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(BrandColors.InputBackground, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) { Text(text = "📷", fontSize = 18.sp) }
        Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
            Text(
                text = product.name,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = product.variant.uppercase(),
                color = BrandColors.TextSecondary,
                fontSize = 10.sp,
                letterSpacing = 0.8.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "×${product.quantity}",
            color = BrandColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Breakdown financiero ────────────────────────────────────────────────────

@Composable
private fun FinancialBreakdownCard(financials: FinancialBreakdown) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BreakdownRow(label = "Subtotal", value = financials.subtotal)
        BreakdownRow(label = "Envío", value = financials.shipping)
        BreakdownRow(
            label = "Descuento aplicado",
            value = financials.discount,
            valueColor = Color(0xFFB23A3A)
        )
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
        BreakdownRow(
            label = "Total Pedido",
            value = financials.total,
            isBold = true
        )
        Spacer(modifier = Modifier.height(2.dp))
        BreakdownRow(
            label = "Comisión Origen (12%)",
            value = financials.originCommission,
            labelColor = BrandColors.TextSecondary,
            valueColor = BrandColors.TextSecondary,
            small = true
        )
        BreakdownRow(
            label = "Retención ICA",
            value = financials.icaWithholding,
            labelColor = BrandColors.TextSecondary,
            valueColor = BrandColors.TextSecondary,
            small = true
        )
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "TU GANANCIA NETA:",
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = formatCop(financials.netEarnings),
                color = BrandColors.FarmerPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BreakdownRow(
    label: String,
    value: Int,
    labelColor: Color = BrandColors.TextPrimary,
    valueColor: Color = BrandColors.TextPrimary,
    isBold: Boolean = false,
    small: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = labelColor,
            fontSize = if (small) 12.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = formatCop(value),
            color = valueColor,
            fontSize = if (small) 12.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium
        )
    }
}

// ── Logística ───────────────────────────────────────────────────────────────

@Composable
private fun CarrierCard(carrier: CarrierInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(BrandColors.InputBackground, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.LocalShipping,
                contentDescription = null,
                tint = BrandColors.FarmerPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(modifier = Modifier.weight(1f).padding(start = BrandSpacing.sm)) {
            Text(
                text = carrier.name,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = "${carrier.service} · ${carrier.estimatedDelivery}",
                color = BrandColors.TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

// ── Lista de empaque (checklist) ────────────────────────────────────────────

@Composable
private fun PackingChecklistCard(
    items: List<String>,
    checkedItems: Set<String>,
    onToggle: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEach { item ->
            val checked = item in checkedItems
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle(item) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (checked) Icons.Default.Check
                        else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (checked) BrandColors.FarmerPrimary else BrandColors.IndicatorInactive,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = item,
                    color = BrandColors.TextPrimary,
                    fontSize = 13.sp,
                    textDecoration = if (checked)
                        androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
            }
        }
    }
}

// ── Botones del fondo ───────────────────────────────────────────────────────

@Composable
private fun BottomActions(
    onGenerateGuide: () -> Unit,
    onPrintTicket: () -> Unit,
    onHelp: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onGenerateGuide,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.FarmerPrimary,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Outlined.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text("Generar guía de envío", fontWeight = FontWeight.SemiBold)
        }
        OutlinedButton(
            onClick = onPrintTicket,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Outlined.Print, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Imprimir ficha",
                color = BrandColors.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
        TextButton(onClick = onHelp) {
            Text(
                text = "¿Necesitas ayuda?",
                color = BrandColors.TextSecondary,
                fontSize = 13.sp
            )
        }
    }
}

// ── Helpers ─────────────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandColors.TextPrimary
        )
    )
}

/** Formatea un entero como moneda colombiana: -10000 → "-$10.000". */
private fun formatCop(value: Int): String {
    val sign = if (value < 0) "-" else ""
    val absValue = kotlin.math.abs(value)
    return "$sign$" + "%,d".format(absValue).replace(',', '.')
}

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

// ── Modelos del feature ─────────────────────────────────────────────────────
// Mock-only mientras no exista tabla `orders`. Cuando llegue el backend, esta
// sección se reemplaza por una entidad Room (o data class del API) con la
// misma forma — los composables no cambian.

private enum class OrderStatus(val label: String) {
    NEW("Nuevos"),
    TO_PACK("Por empacar"),
    DISPATCHED("Despachados")
}

private data class OrderItem(
    val productName: String,
    val quantity: Int,
    val priceCop: Int
)

private data class MockOrder(
    val orderNumber: String,
    val status: OrderStatus,
    val placedAtRelative: String,
    val customerName: String,
    val customerCity: String,
    val items: List<OrderItem>,
    val isCritical: Boolean = false
) {
    val totalCop: Int get() = items.sumOf { it.priceCop * it.quantity }
    val productCount: Int get() = items.size
    val unitsCount: Int get() = items.sumOf { it.quantity }
}

private val MOCK_ORDERS: List<MockOrder> = listOf(
    MockOrder(
        orderNumber = "OR-34521",
        status = OrderStatus.NEW,
        placedAtRelative = "Hoy, 10:45 AM · hace 30 min",
        customerName = "María G.",
        customerCity = "Bogotá, DC",
        items = listOf(
            OrderItem("Café Huila 250g", quantity = 2, priceCop = 42_000),
            OrderItem("Café Nariño 500g", quantity = 1, priceCop = 62_000)
        )
    ),
    MockOrder(
        orderNumber = "OR-34520",
        status = OrderStatus.TO_PACK,
        placedAtRelative = "Ayer, 4:20 PM",
        customerName = "Juan P.",
        customerCity = "Medellín, ANT",
        items = listOf(
            OrderItem("Café Sierra Nevada 500g", quantity = 1, priceCop = 52_000)
        )
    ),
    MockOrder(
        orderNumber = "OR-34515",
        status = OrderStatus.NEW,
        placedAtRelative = "Hace 2 días · Crítico",
        customerName = "Carolina V.",
        customerCity = "Cali, VAC",
        items = listOf(
            OrderItem("Café Geisha 250g", quantity = 1, priceCop = 80_000),
            OrderItem("Café Huila 250g", quantity = 1, priceCop = 42_000),
            OrderItem("Café Tabi 500g", quantity = 1, priceCop = 88_000)
        ),
        isCritical = true
    ),
    MockOrder(
        orderNumber = "OR-34509",
        status = OrderStatus.DISPATCHED,
        placedAtRelative = "Lun, 8:10 AM",
        customerName = "Diego R.",
        customerCity = "Manizales, CAL",
        items = listOf(
            OrderItem("Café Caturra 250g", quantity = 4, priceCop = 38_000)
        )
    )
)

/** Métricas agregadas que muestra el header (mock por ahora). */
private data class SalesMetrics(val today: Int, val week: Int, val month: Int)

private val MOCK_METRICS = SalesMetrics(today = 245_000, week = 1_240_000, month = 4_850_000)

/**
 * Pantalla "Mis Ventas": resumen del día / semana / mes, tabs por estado
 * del pedido, buscador y lista de órdenes con CTA según estado.
 *
 * Es stateless excepto por el filtro activo y el texto del buscador, que
 * viven en `remember` porque son UI-state puro (no dato de dominio).
 *
 * @param onBack flecha atrás del header.
 * @param onOrderTap callback al tocar un pedido (futura pantalla de detalle).
 * @param onPrimaryAction callback del botón principal de cada card. Recibe
 *  el `orderNumber` y la acción semántica ("Aceptar pedido", "Confirmar
 *  despacho", "Ver detalle"). Hoy muestra Toast "Próximamente".
 */
@Composable
fun SalesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onOrderTap: (String) -> Unit,
    onPrimaryAction: (orderNumber: String, action: String) -> Unit
) {
    var activeTab by remember { mutableStateOf(OrderStatus.NEW) }
    var searchQuery by remember { mutableStateOf("") }

    val newCount = MOCK_ORDERS.count { it.status == OrderStatus.NEW }
    val toPackCount = MOCK_ORDERS.count { it.status == OrderStatus.TO_PACK }
    val dispatchedCount = MOCK_ORDERS.count { it.status == OrderStatus.DISPATCHED }

    val filtered = MOCK_ORDERS
        .filter { it.status == activeTab }
        .filter {
            searchQuery.isBlank() ||
                    it.orderNumber.contains(searchQuery, ignoreCase = true) ||
                    it.customerName.contains(searchQuery, ignoreCase = true) ||
                    it.items.any { item -> item.productName.contains(searchQuery, ignoreCase = true) }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        SalesTopBar(onBack = onBack)
        MetricsRow(metrics = MOCK_METRICS)

        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        StatusTabs(
            active = activeTab,
            counts = mapOf(
                OrderStatus.NEW to newCount,
                OrderStatus.TO_PACK to toPackCount,
                OrderStatus.DISPATCHED to dispatchedCount
            ),
            onSelect = { activeTab = it }
        )

        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        SearchField(query = searchQuery, onQueryChange = { searchQuery = it })

        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        if (filtered.isEmpty()) {
            EmptyOrdersState(activeTab = activeTab, modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = BrandSpacing.lg,
                    vertical = BrandSpacing.xs
                ),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                items(filtered) { order ->
                    OrderCard(
                        order = order,
                        onTap = { onOrderTap(order.orderNumber) },
                        onPrimaryAction = { actionLabel ->
                            onPrimaryAction(order.orderNumber, actionLabel)
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(BrandSpacing.lg)) }
            }
        }
    }
}

// ── Top bar (menú + título + filtro) ────────────────────────────────────────

@Composable
private fun SalesTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Outlined.Menu,
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
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = { /* Filtro: pendiente */ }) {
            Icon(
                imageVector = Icons.Outlined.FilterList,
                contentDescription = "Filtros",
                tint = BrandColors.TextPrimary
            )
        }
    }
}

// ── Métricas: HOY / SEMANA / MES ────────────────────────────────────────────

@Composable
private fun MetricsRow(metrics: SalesMetrics) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MetricColumn(label = "HOY", value = metrics.today)
        MetricColumn(label = "SEMANA", value = metrics.week)
        MetricColumn(label = "MES", value = metrics.month)
    }
}

@Composable
private fun MetricColumn(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = label,
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = formatCop(value),
            color = BrandColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )
    }
}

// ── Tabs por estado del pedido ──────────────────────────────────────────────

@Composable
private fun StatusTabs(
    active: OrderStatus,
    counts: Map<OrderStatus, Int>,
    onSelect: (OrderStatus) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.lg),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        OrderStatus.entries.forEach { status ->
            val isActive = status == active
            val count = counts[status] ?: 0
            Box(
                modifier = Modifier
                    .background(
                        color = if (isActive) BrandColors.CoffeeBrown else BrandColors.CardBackground,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { onSelect(status) }
                    .padding(horizontal = BrandSpacing.md, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = status.label,
                        color = if (isActive) BrandColors.PrimaryButtonText else BrandColors.TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (count > 0 && status != OrderStatus.DISPATCHED) {
                        Spacer(modifier = Modifier.size(6.dp))
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(Color(0xFFC9A24A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = count.toString(),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Buscador ────────────────────────────────────────────────────────────────

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.lg)
            .background(BrandColors.CardBackground, RoundedCornerShape(50))
            .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = BrandColors.TextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = "Buscar por # de pedido, producto o comprador",
                    color = BrandColors.InputHint,
                    fontSize = 13.sp
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = BrandColors.TextPrimary,
                    fontSize = 13.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ── Card de un pedido ───────────────────────────────────────────────────────

@Composable
private fun OrderCard(
    order: MockOrder,
    onTap: () -> Unit,
    onPrimaryAction: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(16.dp))
            .clickable(onClick = onTap)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header: # de pedido + badge del estado
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "#${order.orderNumber}",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            StatusBadge(status = order.status, isCritical = order.isCritical)
        }

        // Timestamp con icono (warning si es crítico, reloj si no)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (order.isCritical) Icons.Outlined.WarningAmber
                    else Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = if (order.isCritical) Color(0xFFB23A3A) else BrandColors.TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = order.placedAtRelative,
                color = if (order.isCritical) Color(0xFFB23A3A) else BrandColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = if (order.isCritical) FontWeight.SemiBold else FontWeight.Normal
            )
        }

        // Cliente
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "${order.customerName} · ${order.customerCity}",
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Resumen + items
        Text(
            text = "${order.productCount} ${if (order.productCount == 1) "PRODUCTO" else "PRODUCTOS"} · " +
                    "${order.unitsCount} ${if (order.unitsCount == 1) "UNIDAD" else "UNIDADES"}",
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        order.items.forEach { item ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "${item.productName} ×${item.quantity}",
                    color = BrandColors.TextPrimary,
                    fontSize = 13.sp
                )
                Text(
                    text = formatCop(item.priceCop * item.quantity),
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))

        // Total + CTA
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "TOTAL",
                    color = BrandColors.TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = formatCop(order.totalCop),
                    color = BrandColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            PrimaryActionButton(status = order.status, onClick = onPrimaryAction)
        }
    }
}

@Composable
private fun StatusBadge(status: OrderStatus, isCritical: Boolean) {
    val (background, contentColor, label) = when {
        status == OrderStatus.NEW && isCritical -> Triple(
            Color(0xFFF6D9D2), Color(0xFFB23A3A), "CRÍTICO"
        )
        status == OrderStatus.NEW -> Triple(
            Color(0xFFFFF1B8), Color(0xFF8C6E1F), "NUEVO"
        )
        status == OrderStatus.TO_PACK -> Triple(
            Color(0xFFE8F4EC), BrandColors.FarmerPrimary, "POR EMPACAR"
        )
        else -> Triple(
            BrandColors.IndicatorInactive, BrandColors.TextPrimary, "DESPACHADO"
        )
    }
    Box(
        modifier = Modifier
            .background(background, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = contentColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun PrimaryActionButton(status: OrderStatus, onClick: (String) -> Unit) {
    val (label, container) = when (status) {
        OrderStatus.NEW -> "Aceptar pedido" to BrandColors.FarmerPrimary
        OrderStatus.TO_PACK -> "Confirmar despacho" to Color(0xFFC9A24A)
        OrderStatus.DISPATCHED -> "Ver detalle" to BrandColors.CoffeeBrown
    }
    Button(
        onClick = { onClick(label) },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = Color.White
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 14.dp,
            vertical = 10.dp
        )
    ) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ── Estado vacío del filtro ─────────────────────────────────────────────────

@Composable
private fun EmptyOrdersState(activeTab: OrderStatus, modifier: Modifier = Modifier) {
    val msg = when (activeTab) {
        OrderStatus.NEW -> "No hay pedidos nuevos. Cuando un comprador haga un pedido, aparecerá aquí."
        OrderStatus.TO_PACK -> "No tienes pedidos por empacar."
        OrderStatus.DISPATCHED -> "Aún no has despachado pedidos."
    }
    Box(
        modifier = modifier.fillMaxWidth().padding(BrandSpacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = msg,
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ── Helper de formateo de moneda ────────────────────────────────────────────

/** Formatea un entero como moneda colombiana: 1240000 → "$1.240.000". */
private fun formatCop(value: Int): String =
    "$" + "%,d".format(value).replace(',', '.')

@Preview(name = "Sales", widthDp = 360, heightDp = 900)
@Composable
private fun SalesScreenPreview() {
    SalesScreen(onBack = {}, onOrderTap = {}, onPrimaryAction = { _, _ -> })
}

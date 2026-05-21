package com.cafeteros.historia.ui.features.farmer_home

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.Product
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

// ── Datos mock ──────────────────────────────────────────────────────────────
// TODO backend: estos valores vendrán del API/BD cuando exista. Se mantienen
// como constantes simples para que el panel se vea poblado en demo.

private data class MockOrder(
    val orderId: String,
    val product: String,
    val customerName: String,
    val customerCity: String,
    val deadline: String
)

private data class MockLowStock(val product: String, val unitsLeft: Int)

private val MOCK_ORDERS_TO_PACK: List<MockOrder> = listOf(
    MockOrder(
        orderId = "OR-34521",
        product = "Café Geisha Honey",
        customerName = "María G.",
        customerCity = "Bogotá",
        deadline = "MAÑANA 9AM"
    ),
    MockOrder(
        orderId = "OR-34508",
        product = "Pack Selección",
        customerName = "Carlos R.",
        customerCity = "Medellín",
        deadline = "EN 2 DÍAS"
    )
)

private val MOCK_LOW_STOCK: List<MockLowStock> = listOf(
    MockLowStock(product = "Café Huila 250g", unitsLeft = 3),
    MockLowStock(product = "Miel de Café 500ml", unitsLeft = 1)
)

/** Días de la semana abreviados con altura relativa (0..1) de las ventas. */
private val WEEK_SALES_BARS: List<Pair<String, Float>> = listOf(
    "L" to 0.35f,
    "M" to 0.50f,
    "M" to 0.75f,
    "J" to 0.40f,
    "V" to 0.55f,
    "S" to 0.95f,
    "D" to 0.30f
)

/**
 * Panel principal del caficultor (post-aprobación). Replica el diseño de
 * Stitch con datos mock hasta que exista el backend que provea pedidos,
 * inventario y métricas reales.
 *
 * @param userFirstName primer nombre del caficultor (header).
 * @param farmName nombre comercial de la finca para mostrar en el header.
 *  Si es null, se usa un placeholder neutro.
 * @param onLogout callback al tocar el avatar (por ahora la única vía para
 *  cerrar sesión hasta que exista pantalla de Perfil real).
 * @param onCreateProduct callback de "+ Nuevo producto" (Quick Action).
 * @param onOpenProductsList callback de la tab "PRODUCTOS" del bottom nav.
 * @param onOpenInventory callback de "Inventario" (Quick Action) y
 *  "Reabastecer" del card de stock bajo.
 * @param onOpenSales callback de la tab "VENTAS" (bottom nav) y del Quick
 *  Action "Gestionar pedidos" — abren la pantalla de Mis Ventas.
 * @param onOpenNotifications callback al tocar la campana del header — hoy
 *  abre la pantalla de "Nueva venta" para demo.
 * @param onOpenWallet callback del Quick Action "Billetera" — abre el
 *  flujo de billetera (saldo, retiros, historial).
 * @param onOpenStats callback al tocar el card "Tus ventas esta semana"
 *  — abre la pantalla de estadísticas.
 * @param onOpenProfile callback de la tab "PERFIL" del bottom nav.
 * @param onOpenInbox callback del Quick Action "Mensajes".
 * @param onOpenReviews callback del Quick Action "Reseñas".
 * @param onSectionTap callback genérico para acciones todavía no
 *  implementadas. Muestra Toast "Próximamente: $sección".
 */
@Composable
fun FarmerHomeScreen(
    modifier: Modifier = Modifier,
    @Suppress("UNUSED_PARAMETER") userFirstName: String? = null,
    @Suppress("UNUSED_PARAMETER") farmName: String? = null,
    onLogout: () -> Unit,
    onCreateProduct: () -> Unit,
    onOpenProductsList: () -> Unit,
    onOpenInventory: () -> Unit,
    onOpenSales: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenWallet: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenInbox: () -> Unit,
    onOpenReviews: () -> Unit,
    onSectionTap: (String) -> Unit
) {
    // El state real ahora viene del VM (User + Farm + productos en Firestore).
    // Los params `userFirstName` y `farmName` se ignoran — se mantienen en la
    // firma sólo para no romper a callers existentes mientras se migra.
    val viewModel: FarmerHomeViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize().background(BrandColors.AuthBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            FarmerHomeHeader(
                userFirstName = state.firstName.takeIf { it.isNotBlank() },
                farmName = state.farmName.takeIf { it.isNotBlank() },
                notificationCount = state.lowStock.size + state.outOfStockCount,
                onAvatarClick = onLogout,
                onBellClick = onOpenNotifications
            )
            Greeting()
            TodaySummaryCard(state = state)
            if (state.pendingOrders.isNotEmpty()) {
                OrdersAlertCard(
                    pendingCount = state.pendingOrders.size,
                    onClick = onOpenSales
                )
            }
            QuickActionsGrid(
                onCreateProduct = onCreateProduct,
                onOpenInventory = onOpenInventory,
                onOpenSales = onOpenSales,
                onOpenWallet = onOpenWallet,
                onOpenInbox = onOpenInbox,
                onOpenReviews = onOpenReviews,
                onAction = onSectionTap
            )
            if (state.pendingOrders.isNotEmpty()) {
                OrdersToPackSection(
                    pendingOrders = state.pendingOrders,
                    onSeeAll = onOpenSales
                )
            }
            LowStockSection(lowStock = state.lowStock, onRestock = onOpenInventory)
            WeekSalesChart(onTap = onOpenStats)
            TipOfDayCard()
        }

        FarmerBottomNav(
            modifier = Modifier.align(Alignment.BottomCenter),
            onTabSales = onOpenSales,
            onTabProducts = onOpenProductsList,
            onTabProfile = onOpenProfile,
            onTab = onSectionTap
        )
    }
}

// ── Header (avatar + nombre + finca + campana) ──────────────────────────────

@Composable
private fun FarmerHomeHeader(
    userFirstName: String?,
    farmName: String?,
    notificationCount: Int,
    onAvatarClick: () -> Unit,
    onBellClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(BrandColors.FarmerPrimary)
                .clickable(onClick = onAvatarClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Tocar para cerrar sesión",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.width(BrandSpacing.sm))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hola, ${userFirstName ?: "Caficultor"}",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = farmName?.uppercase() ?: "TU FINCA",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp,
                    color = BrandColors.TextSecondary
                )
            )
        }

        Box(modifier = Modifier.size(40.dp).clickable(onClick = onBellClick)) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notificaciones",
                tint = BrandColors.TextPrimary,
                modifier = Modifier.align(Alignment.Center).size(24.dp)
            )
            if (notificationCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(18.dp)
                        .background(Color(0xFFD64545), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = notificationCount.toString(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ── Saludo "Buenos días" + fecha ────────────────────────────────────────────

@Composable
private fun Greeting() {
    Column(modifier = Modifier.padding(horizontal = BrandSpacing.lg)) {
        Text(
            text = "Buenos días ☀️",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
        // TODO backend: fecha dinámica con LocalDate.now().format(...)
        Text(
            text = "Hoy es martes, 21 de abril de 2026",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp,
                color = BrandColors.TextSecondary
            )
        )
    }
}

// ── Card oscuro: resumen del día ────────────────────────────────────────────

@Composable
private fun TodaySummaryCard(state: FarmerHomeUiState) {
    Column(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg)
            .fillMaxWidth()
            .background(BrandColors.CoffeeBrown, RoundedCornerShape(16.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(
            text = "TU INVENTARIO HOY",
            color = BrandColors.CreamWhiteMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SummaryStat(
                label = "VALOR",
                value = "$" + "%,d".format(state.inventoryValueCop).replace(',', '.')
            )
            SummaryStat(
                label = "PRODUCTOS",
                value = state.activeProductsCount.toString()
            )
            SummaryStat(
                label = "STOCK",
                value = state.totalStockUnits.toString()
            )
        }
    }
}

@Composable
private fun SummaryStat(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = BrandColors.CreamWhiteMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = BrandColors.CreamWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Alerta amarilla "Tienes pedidos por empacar" ────────────────────────────

@Composable
private fun OrdersAlertCard(pendingCount: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg)
            .fillMaxWidth()
            .background(BrandColors.InfoBannerBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.WarningAmber,
            contentDescription = null,
            tint = BrandColors.InfoBannerAction,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Tienes $pendingCount pedido${if (pendingCount == 1) "" else "s"} por procesar",
            modifier = Modifier.weight(1f),
            fontSize = 13.sp,
            color = BrandColors.TextPrimary
        )
        Text(
            text = "Ver",
            color = BrandColors.InfoBannerAction,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

// ── Grid 2x2 de acciones rápidas ────────────────────────────────────────────

@Composable
private fun QuickActionsGrid(
    onCreateProduct: () -> Unit,
    onOpenInventory: () -> Unit,
    onOpenSales: () -> Unit,
    onOpenWallet: () -> Unit,
    onOpenInbox: () -> Unit,
    onOpenReviews: () -> Unit,
    onAction: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = BrandSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
            QuickAction(
                modifier = Modifier.weight(1f),
                title = "+ Nuevo\nproducto",
                icon = Icons.Outlined.Inventory2,
                background = BrandColors.FarmerPrimary,
                contentColor = Color.White,
                onClick = onCreateProduct
            )
            QuickAction(
                modifier = Modifier.weight(1f),
                title = "Gestionar\npedidos",
                icon = Icons.Outlined.Receipt,
                background = Color(0xFFC9A24A),
                contentColor = Color.White,
                onClick = onOpenSales
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
            QuickAction(
                modifier = Modifier.weight(1f),
                title = "Inventario",
                icon = Icons.Outlined.Inventory,
                background = BrandColors.IndicatorInactive,
                contentColor = BrandColors.TextPrimary,
                onClick = onOpenInventory
            )
            QuickAction(
                modifier = Modifier.weight(1f),
                title = "Billetera",
                icon = Icons.Outlined.AccountBalanceWallet,
                background = Color(0xFFD9E8F0),
                contentColor = BrandColors.TextPrimary,
                onClick = onOpenWallet
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
            QuickAction(
                modifier = Modifier.weight(1f),
                title = "Mensajes",
                icon = Icons.Outlined.ChatBubbleOutline,
                background = Color(0xFFC2EBD3),
                contentColor = BrandColors.TextPrimary,
                badge = 3,
                onClick = onOpenInbox
            )
            QuickAction(
                modifier = Modifier.weight(1f),
                title = "Reseñas",
                icon = Icons.Outlined.Star,
                background = Color(0xFFFFE9A8),
                contentColor = BrandColors.TextPrimary,
                onClick = onOpenReviews
            )
        }
    }
}

@Composable
private fun QuickAction(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    background: Color,
    contentColor: Color,
    badge: Int = 0,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(110.dp)
            .background(background, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(24.dp))
        Text(
            text = title,
            modifier = Modifier.align(Alignment.BottomStart),
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 18.sp
        )
        if (badge > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(22.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badge.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Sección "Pedidos por empacar" con lista horizontal ──────────────────────

@Composable
private fun OrdersToPackSection(pendingOrders: List<Order>, onSeeAll: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.lg),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pedidos por empacar (${pendingOrders.size})",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = "Ver todos",
                modifier = Modifier.clickable(onClick = onSeeAll),
                color = BrandColors.FarmerPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        LazyRow(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = BrandSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            items(pendingOrders) { order -> RealOrderCard(order = order) }
        }
    }
}

/** Card horizontal de un pedido real. Reemplaza el MockOrder original. */
@Composable
private fun RealOrderCard(order: Order) {
    Column(
        modifier = Modifier
            .width(280.dp)
            .background(BrandColors.CardBackground, RoundedCornerShape(16.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "ORDEN #${order.id.take(8).uppercase()}",
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Text(
            text = order.items.firstOrNull()?.productName ?: "Pedido",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
        if (order.items.size > 1) {
            Text(
                text = "+ ${order.items.size - 1} producto${if (order.items.size - 1 == 1) "" else "s"} más",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = order.compradorName.ifBlank { "Comprador" },
                color = BrandColors.TextSecondary,
                fontSize = 13.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.InfoBannerBackground, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Text(
                text = "📦 ESTADO: ${order.status.label.uppercase()} · $" +
                    "%,d".format(order.totalCop).replace(',', '.'),
                color = BrandColors.InfoBannerAction,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// ── Productos con stock bajo ────────────────────────────────────────────────

@Composable
private fun LowStockSection(lowStock: List<Product>, onRestock: () -> Unit) {
    if (lowStock.isEmpty()) return
    Column(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg)
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(16.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Productos con stock bajo ⚠️",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandColors.TextPrimary
        )
        lowStock.forEachIndexed { index, product ->
            if (index > 0) {
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = product.name, fontSize = 14.sp, color = BrandColors.TextPrimary)
                    val units = product.stockUnits
                    Text(
                        text = "Solo queda${if (units == 1) "" else "n"} $units unidad${if (units == 1) "" else "es"}",
                        color = Color(0xFFB23A3A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "Reabastecer",
                    modifier = Modifier.clickable(onClick = onRestock),
                    color = BrandColors.FarmerPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ── Chart simple de ventas semanal ──────────────────────────────────────────

@Composable
private fun WeekSalesChart(onTap: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg)
            .clickable(onClick = onTap),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        Text(
            text = "Tus ventas esta semana",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
        Text(
            text = "\$1.240.000 en 7 días · +12%",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(16.dp))
                .padding(BrandSpacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WEEK_SALES_BARS.forEachIndexed { index, (label, ratio) ->
                    val isHighlighted = ratio == WEEK_SALES_BARS.maxOf { it.second }
                    val isMidWeek = index == 2
                    val barColor = when {
                        isHighlighted -> Color(0xFFC9A24A)
                        isMidWeek -> BrandColors.FarmerPrimary
                        else -> BrandColors.IndicatorInactive
                    }
                    Column(
                        modifier = Modifier.width(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height((120 * ratio).dp)
                                .background(barColor, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        )
                        Text(
                            text = label,
                            color = BrandColors.TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// ── Card "Consejo de hoy" ───────────────────────────────────────────────────

@Composable
private fun TipOfDayCard() {
    Row(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg)
            .fillMaxWidth()
            .background(BrandColors.FarmerStatsBadgeBackground, RoundedCornerShape(16.dp))
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFC9A24A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Consejo de hoy",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
            Text(
                text = "Las fotos con luz natural se venden 40% más. Prueba tomar fotos de tus granos cerca de una ventana al amanecer.",
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

// ── Bottom navigation ───────────────────────────────────────────────────────

@Composable
private fun FarmerBottomNav(
    modifier: Modifier = Modifier,
    onTabSales: () -> Unit,
    onTabProducts: () -> Unit,
    onTabProfile: () -> Unit,
    onTab: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(label = "INICIO", icon = Icons.Outlined.Home, active = true, onClick = { onTab("Inicio") })
        BottomNavItem(label = "VENTAS", icon = Icons.Outlined.ShoppingBag, active = false, onClick = onTabSales)
        BottomNavItem(label = "PRODUCTOS", icon = Icons.Outlined.Inventory, active = false, onClick = onTabProducts)
        BottomNavItem(label = "PERFIL", icon = Icons.Outlined.AccountCircle, active = false, onClick = onTabProfile)
    }
}

@Composable
private fun BottomNavItem(label: String, icon: ImageVector, active: Boolean, onClick: () -> Unit) {
    val tint = if (active) BrandColors.TextPrimary else BrandColors.TextSecondary
    Column(
        modifier = Modifier.clickable(onClick = onClick).padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
        Text(text = label, color = tint, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp)
        Box(
            modifier = Modifier
                .size(width = 22.dp, height = 2.dp)
                .background(if (active) BrandColors.TextPrimary else Color.Transparent)
        )
    }
}

@Preview(name = "FarmerHome", widthDp = 360, heightDp = 1100)
@Composable
private fun FarmerHomeScreenPreview() {
    FarmerHomeScreen(
        userFirstName = "Don Alberto",
        farmName = "Finca La Esperanza",
        onLogout = {},
        onCreateProduct = {},
        onOpenProductsList = {},
        onOpenInventory = {},
        onOpenSales = {},
        onOpenNotifications = {},
        onOpenWallet = {},
        onOpenStats = {},
        onOpenProfile = {},
        onOpenInbox = {},
        onOpenReviews = {},
        onSectionTap = {}
    )
}

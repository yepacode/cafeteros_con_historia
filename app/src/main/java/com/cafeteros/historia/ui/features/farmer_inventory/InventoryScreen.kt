package com.cafeteros.historia.ui.features.farmer_inventory

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
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cafeteros.historia.ui.features.farmer_products.model.Product
import com.cafeteros.historia.ui.features.farmer_products.model.ProductsStore
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

// ── Constantes y modelos auxiliares ─────────────────────────────────────────

/** Umbral por debajo del cual un producto se considera "stock bajo". */
private const val LOW_STOCK_THRESHOLD: Int = 10

/** Filtros de la lista (tabs sobre el listado). */
private enum class InventoryFilter(val label: String) {
    ALL("Todos"),
    LOW_STOCK("Stock bajo"),
    OUT_OF_STOCK("Agotados")
}

/** Movimiento mock — en producción vendrá de la tabla `inventory_movements`. */
private data class MockMovement(
    val productName: String,
    val date: String,
    val reason: String,
    val delta: Int
)

private val MOCK_RECENT_MOVEMENTS: List<MockMovement> = listOf(
    MockMovement(
        productName = "Café Huila",
        date = "21 ABR",
        reason = "VENTA",
        delta = -1
    ),
    MockMovement(
        productName = "Café Santander",
        date = "20 ABR",
        reason = "AJUSTE MANUAL",
        delta = -10
    )
)

/**
 * Pantalla de gestión de inventario del caficultor. Lee el estado de
 * [ProductsStore] reactivamente y calcula sus métricas en vivo.
 *
 * Diferencias con [com.cafeteros.historia.ui.features.farmer_products.ProductListScreen]:
 *  - Se enfoca en **gestión de stock** (steppers +/-, alertas, movimientos)
 *    en vez de catálogo público.
 *  - Permite filtrar por estado del stock (Todos / Bajo / Agotados).
 *  - Modifica el `stockUnits` de los productos directamente vía
 *    [ProductsStore.update].
 *
 * @param onBack flecha atrás del header — termina la activity.
 * @param onProductTap callback al tocar un producto (abre edición completa).
 * @param onMassAction callback genérico para "Exportar/Importar CSV", aún
 *  no implementadas. Muestra Toast "Próximamente".
 */
@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onProductTap: (Product) -> Unit,
    onMassAction: (String) -> Unit
) {
    val products by ProductsStore.productsFlow.collectAsStateWithLifecycle()
    var activeFilter by remember { mutableStateOf(InventoryFilter.ALL) }
    var notifyOnLowStock by remember { mutableStateOf(true) }
    var pauseWhenOutOfStock by remember { mutableStateOf(false) }

    val totalUnits = products.sumOf { it.stockUnits }
    val lowStockCount = products.count { it.stockUnits in 1..LOW_STOCK_THRESHOLD }
    val outOfStockCount = products.count { it.stockUnits == 0 }

    val filteredProducts = when (activeFilter) {
        InventoryFilter.ALL -> products
        InventoryFilter.LOW_STOCK -> products.filter { it.stockUnits in 1..LOW_STOCK_THRESHOLD }
        InventoryFilter.OUT_OF_STOCK -> products.filter { it.stockUnits == 0 }
    }.sortedBy { it.stockUnits }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        InventoryHeader(onBack = onBack)

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = BrandSpacing.lg,
                vertical = BrandSpacing.sm
            ),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            item {
                MetricsGrid(
                    totalUnits = totalUnits,
                    productsCount = products.size,
                    lowStockCount = lowStockCount,
                    outOfStockCount = outOfStockCount
                )
            }
            item {
                FiltersTabs(active = activeFilter, onSelect = { activeFilter = it })
            }
            if (filteredProducts.isEmpty()) {
                item { EmptyFilterState(filter = activeFilter) }
            } else {
                items(filteredProducts) { product ->
                    InventoryRow(
                        product = product,
                        onTap = { onProductTap(product) },
                        onIncrement = {
                            ProductsStore.update(product.copy(stockUnits = product.stockUnits + 1))
                        },
                        onDecrement = {
                            if (product.stockUnits > 0) {
                                ProductsStore.update(product.copy(stockUnits = product.stockUnits - 1))
                            }
                        }
                    )
                }
            }
            item {
                MassActionsSection(onAction = onMassAction)
            }
            item {
                RecentMovementsSection(movements = MOCK_RECENT_MOVEMENTS)
            }
            item {
                AlertsSection(
                    notifyOnLowStock = notifyOnLowStock,
                    onNotifyChange = { notifyOnLowStock = it },
                    pauseWhenOutOfStock = pauseWhenOutOfStock,
                    onPauseChange = { pauseWhenOutOfStock = it }
                )
            }
            item { Spacer(modifier = Modifier.height(BrandSpacing.lg)) }
        }
    }
}

// ── Header ──────────────────────────────────────────────────────────────────

@Composable
private fun InventoryHeader(onBack: () -> Unit) {
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
            text = "Inventario",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = BrandColors.TextPrimary
            )
        )
        // Avatar decorativo del header (sin acción por ahora).
        Box(
            modifier = Modifier
                .padding(end = BrandSpacing.sm)
                .size(36.dp)
                .background(BrandColors.FarmerPrimary, CircleShape)
        )
    }
}

// ── Grid 2x2 de métricas ────────────────────────────────────────────────────

@Composable
private fun MetricsGrid(
    totalUnits: Int,
    productsCount: Int,
    lowStockCount: Int,
    outOfStockCount: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
            MetricTile(
                modifier = Modifier.weight(1f),
                value = totalUnits.toString(),
                label = "UNIDADES EN STOCK",
                background = BrandColors.IndicatorInactive,
                contentColor = BrandColors.TextPrimary
            )
            MetricTile(
                modifier = Modifier.weight(1f),
                value = productsCount.toString(),
                label = "PRODUCTOS",
                background = Color(0xFFC9A24A),
                contentColor = Color.White
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
            MetricTile(
                modifier = Modifier.weight(1f),
                value = lowStockCount.toString(),
                label = "STOCK BAJO",
                background = Color(0xFFF6D9D2),
                contentColor = Color(0xFFB23A3A)
            )
            MetricTile(
                modifier = Modifier.weight(1f),
                value = outOfStockCount.toString(),
                label = "AGOTADOS",
                background = BrandColors.InputBackground,
                contentColor = BrandColors.TextPrimary
            )
        }
    }
}

@Composable
private fun MetricTile(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    background: Color,
    contentColor: Color
) {
    Column(
        modifier = modifier
            .height(80.dp)
            .background(background, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = value,
            color = contentColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = label,
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

// ── Filtros (tabs) ──────────────────────────────────────────────────────────

@Composable
private fun FiltersTabs(active: InventoryFilter, onSelect: (InventoryFilter) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        InventoryFilter.entries.forEach { filter ->
            val isActive = filter == active
            Box(
                modifier = Modifier
                    .background(
                        color = if (isActive) BrandColors.CoffeeBrown else BrandColors.CardBackground,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { onSelect(filter) }
                    .padding(horizontal = BrandSpacing.md, vertical = 8.dp)
            ) {
                Text(
                    text = filter.label,
                    color = if (isActive) BrandColors.PrimaryButtonText else BrandColors.TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ── Card de un producto en la lista de inventario ───────────────────────────

@Composable
private fun InventoryRow(
    product: Product,
    onTap: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val isLow = product.stockUnits in 1..LOW_STOCK_THRESHOLD
    val isOut = product.stockUnits == 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (product.photoUri != null) {
            AsyncImage(
                model = product.photoUri,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BrandColors.InputBackground, RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(BrandColors.InputBackground, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) { Text(text = "📷", fontSize = 18.sp) }
        }

        Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
            Text(
                text = product.name,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                ),
                maxLines = 1
            )
            Text(
                text = formatVariantLine(product),
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
            if (isOut) {
                Text(
                    text = "AGOTADO",
                    color = Color(0xFFB23A3A),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            } else if (isLow) {
                Text(
                    text = "▲ BAJO",
                    color = Color(0xFFB23A3A),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
        }

        StockStepper(
            value = product.stockUnits,
            onDecrement = onDecrement,
            onIncrement = onIncrement
        )
    }
}

/** Variante "250g · Molido" mostrada como subtítulo del producto. */
private fun formatVariantLine(product: Product): String {
    val weight = if (product.weightGrams >= 1000) "${product.weightGrams / 1000}kg"
        else "${product.weightGrams}g"
    return "$weight · ${product.format.label}"
}

@Composable
private fun StockStepper(value: Int, onDecrement: () -> Unit, onIncrement: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(BrandColors.AuthBackground, RoundedCornerShape(50))
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        StepperButton(symbol = "−", enabled = value > 0, onClick = onDecrement)
        Text(
            text = "%02d".format(value),
            modifier = Modifier.padding(horizontal = 10.dp),
            color = BrandColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        StepperButton(symbol = "+", enabled = true, onClick = onIncrement)
    }
}

@Composable
private fun StepperButton(symbol: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(
                color = if (enabled) BrandColors.CardBackground else BrandColors.InputBackground,
                shape = CircleShape
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = if (enabled) BrandColors.TextPrimary else BrandColors.TextSecondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Estado vacío del filtro ─────────────────────────────────────────────────

@Composable
private fun EmptyFilterState(filter: InventoryFilter) {
    val message = when (filter) {
        InventoryFilter.ALL -> "Aún no tienes productos. Crea uno para empezar a gestionar tu inventario."
        InventoryFilter.LOW_STOCK -> "Ningún producto está en stock bajo. ¡Bien hecho!"
        InventoryFilter.OUT_OF_STOCK -> "Ningún producto está agotado."
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// ── Acciones masivas (Exportar / Importar CSV) ──────────────────────────────

@Composable
private fun MassActionsSection(onAction: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        Text(
            text = "🟡 Acciones masivas",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
        Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
            MassActionTile(
                modifier = Modifier.weight(1f),
                icon = androidx.compose.material.icons.Icons.Outlined.Upload,
                label = "Exportar (CSV)",
                onClick = { onAction("Exportar CSV") }
            )
            MassActionTile(
                modifier = Modifier.weight(1f),
                icon = androidx.compose.material.icons.Icons.Outlined.Download,
                label = "Importar (CSV)",
                onClick = { onAction("Importar CSV") }
            )
        }
    }
}

@Composable
private fun MassActionTile(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = BrandSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = BrandColors.TextPrimary)
        Text(
            text = label,
            color = BrandColors.TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── Movimientos recientes ───────────────────────────────────────────────────

@Composable
private fun RecentMovementsSection(movements: List<MockMovement>) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        Text(
            text = "Movimientos recientes",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            movements.forEachIndexed { index, m ->
                if (index > 0) {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = m.productName,
                            color = BrandColors.TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${m.date} · ${m.reason}",
                            color = BrandColors.TextSecondary,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = if (m.delta > 0) "+${m.delta}" else m.delta.toString(),
                        color = if (m.delta < 0) Color(0xFFB23A3A) else BrandColors.FarmerPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ── Alertas automáticas ─────────────────────────────────────────────────────

@Composable
private fun AlertsSection(
    notifyOnLowStock: Boolean,
    onNotifyChange: (Boolean) -> Unit,
    pauseWhenOutOfStock: Boolean,
    onPauseChange: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
        Text(
            text = "🔔 Alertas automáticas",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(horizontal = BrandSpacing.md, vertical = 6.dp)
        ) {
            AlertSwitchRow(
                label = "Notificarme stock bajo",
                checked = notifyOnLowStock,
                onCheckedChange = onNotifyChange
            )
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
            AlertSwitchRow(
                label = "Pausar agotados",
                checked = pauseWhenOutOfStock,
                onCheckedChange = onPauseChange
            )
        }
    }
}

@Composable
private fun AlertSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = BrandColors.TextPrimary,
            fontSize = 14.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFC9A24A),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = BrandColors.IndicatorInactive
            )
        )
    }
}

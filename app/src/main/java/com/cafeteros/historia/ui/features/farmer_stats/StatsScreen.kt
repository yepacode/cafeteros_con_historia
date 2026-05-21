package com.cafeteros.historia.ui.features.farmer_stats

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

private enum class StatsRange(val label: String) {
    SEVEN_DAYS("7 días"),
    THIRTY_DAYS("30 días"),
    THREE_MONTHS("3 meses")
}

private data class TopProduct(val name: String, val sales: String, val percent: Float)

private val MOCK_TOP_PRODUCTS = listOf(
    TopProduct("Reserva El Húmedo", "\$1.7M", 1.0f),
    TopProduct("Crema Suave", "\$815k", 0.65f),
    TopProduct("Geisha Honey", "\$643k", 0.50f),
    TopProduct("Bourbon Reserva", "\$415k", 0.32f)
)

private val MOCK_DAILY_INCOME: List<Pair<String, Float>> = listOf(
    "L" to 0.30f, "M" to 0.55f, "M" to 0.75f, "J" to 0.45f,
    "V" to 0.60f, "S" to 0.95f, "D" to 0.40f
)

class StatsActivity : ComponentActivity() {

    private val viewModel: StatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                StatsScreen(
                    state = state,
                    onBack = ::finish,
                    onExport = {
                        Toast.makeText(this, "Próximamente: exportar PDF", Toast.LENGTH_SHORT).show()
                    },
                    onOpenReviews = {
                        com.cafeteros.historia.ui.features.farmer_reviews.ReviewsActivity.start(this)
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, StatsActivity::class.java))
        }
    }
}

@Composable
fun StatsScreen(
    state: StatsUiState,
    onBack: () -> Unit,
    onExport: () -> Unit,
    onOpenReviews: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var range by remember { mutableStateOf(StatsRange.THIRTY_DAYS) }

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
                text = "Estadísticas",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            // Range tabs
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                StatsRange.entries.forEach { r ->
                    val active = r == range
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (active) BrandColors.CoffeeBrown else BrandColors.CardBackground, RoundedCornerShape(50))
                            .clickable { range = r }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = r.label,
                            color = if (active) BrandColors.PrimaryButtonText else BrandColors.TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Métricas 2x2 — valores reales del inventario actual del
            // caficultor. Los conceptos "ventas / pedidos / reseñas"
            // aparecerán cuando se conecte el módulo de pedidos.
            val inventoryValueFmt = "$" + "%,d".format(state.inventoryValueCop).replace(',', '.')
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                MetricBigTile(
                    modifier = Modifier.weight(1f),
                    label = "VALOR INVENTARIO",
                    value = inventoryValueFmt,
                    delta = null
                )
                MetricBigTile(
                    modifier = Modifier.weight(1f),
                    label = "PRODUCTOS ACTIVOS",
                    value = state.activeProductCount.toString(),
                    delta = if (state.pausedProductCount > 0)
                        "${state.pausedProductCount} pausados" else null
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                MetricBigTile(
                    modifier = Modifier.weight(1f),
                    label = "STOCK TOTAL",
                    value = "${state.totalStockUnits} uds",
                    delta = if (state.outOfStockCount > 0)
                        "${state.outOfStockCount} agotados" else null
                )
                MetricBigTile(
                    modifier = Modifier.weight(1f),
                    label = "PEDIDOS",
                    value = "0",
                    delta = "Aún sin pedidos"
                )
            }

            // Insignia
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🏅", fontSize = 28.sp)
                Spacer(modifier = Modifier.size(BrandSpacing.sm))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Insignia de Origen", color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Tu producto más vendido es Café Huila Pitalito.",
                        color = BrandColors.TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            // Ingresos diarios
            SectionTitle("Ingresos diarios")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val maxRatio = MOCK_DAILY_INCOME.maxOf { it.second }
                    MOCK_DAILY_INCOME.forEach { (label, ratio) ->
                        val highlighted = ratio == maxRatio
                        Column(
                            modifier = Modifier.width(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height((100 * ratio).dp)
                                    .background(
                                        if (highlighted) Color(0xFFC9A24A) else BrandColors.IndicatorInactive,
                                        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                    )
                            )
                            Text(text = label, color = BrandColors.TextSecondary, fontSize = 10.sp)
                        }
                    }
                }
            }

            // Productos destacados — top por valor de inventario
            SectionTitle("Tus productos por valor de inventario")
            if (state.topByValue.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                        .padding(BrandSpacing.lg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no tienes productos publicados.",
                        color = BrandColors.TextSecondary,
                        fontSize = 12.sp
                    )
                }
            } else {
                val maxValue = state.topByValue.maxOf { it.priceCop.toLong() * it.stockUnits }
                    .coerceAtLeast(1L)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                        .padding(BrandSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.topByValue.forEach { product ->
                        val productValue = product.priceCop.toLong() * product.stockUnits
                        val ratio = (productValue.toFloat() / maxValue.toFloat())
                            .coerceIn(0.05f, 1f)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = product.name,
                                    color = BrandColors.TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "$" + "%,d".format(productValue).replace(',', '.'),
                                    color = BrandColors.TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .background(
                                        BrandColors.IndicatorInactive,
                                        RoundedCornerShape(3.dp)
                                    )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(ratio)
                                        .height(6.dp)
                                        .background(
                                            BrandColors.FarmerPrimary,
                                            RoundedCornerShape(3.dp)
                                        )
                                )
                            }
                            Text(
                                text = "${product.stockUnits} uds · $" +
                                    "%,d".format(product.priceCop).replace(',', '.') + " c/u",
                                color = BrandColors.TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Embudo de conversión
            SectionTitle("Embudo de conversión")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FunnelStep(label = "VISITAS", value = "1.247", percent = 1.0f)
                FunnelStep(label = "INGRESARON AL PRODUCTO", value = "380", percent = 0.30f)
                FunnelStep(label = "COMPRAS", value = "47", percent = 0.04f)
            }

            // Compradores recurrentes
            SectionTitle("Compradores recurrentes")
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                CustomerChip(name = "María G.", count = 5)
                CustomerChip(name = "Carlos R.", count = 3)
            }

            // Origen de los pedidos (placeholder de mapa)
            SectionTitle("Origen de los pedidos")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🗺️", fontSize = 48.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RegionStat(label = "Bogotá", percent = "45%")
                RegionStat(label = "Medellín", percent = "25%")
                RegionStat(label = "Otras", percent = "30%")
            }

            // Tendencia de reseñas
            SectionTitle("Tendencia de reseñas")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .clickable(onClick = onOpenReviews)
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReviewBar(stars = 5, percent = 0.80f)
                ReviewBar(stars = 4, percent = 0.15f)
                ReviewBar(stars = 3, percent = 0.05f)
                Text(
                    text = "💬 \"Excelente aroma y tueste\"",
                    color = BrandColors.TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            Button(
                onClick = onExport,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.CoffeeBrown,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Outlined.IosShare, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Exportar reporte PDF", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
    )
}

@Composable
private fun MetricBigTile(
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
        Text(text = label, color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        Text(
            text = value,
            color = BrandColors.TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        if (delta != null) {
            Text(text = delta, color = BrandColors.FarmerPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FunnelStep(label: String, value: String, percent: Float) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, color = BrandColors.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Text(text = value, color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(BrandColors.IndicatorInactive, RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent)
                    .height(8.dp)
                    .background(BrandColors.FarmerPrimary, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
private fun CustomerChip(name: String, count: Int) {
    Row(
        modifier = Modifier
            .background(BrandColors.CardBackground, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(24.dp).background(BrandColors.FarmerPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = name, color = BrandColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "(${count}× compras)", color = BrandColors.TextSecondary, fontSize = 10.sp)
    }
}

@Composable
private fun RegionStat(label: String, percent: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = percent, color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = BrandColors.TextSecondary, fontSize = 11.sp)
    }
}

@Composable
private fun ReviewBar(stars: Int, percent: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.width(64.dp)) {
            repeat(stars) {
                Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFC9A24A), modifier = Modifier.size(12.dp))
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .background(BrandColors.IndicatorInactive, RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent)
                    .height(6.dp)
                    .background(BrandColors.FarmerPrimary, RoundedCornerShape(3.dp))
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "${(percent * 100).toInt()}%", color = BrandColors.TextSecondary, fontSize = 11.sp, modifier = Modifier.width(32.dp))
    }
}

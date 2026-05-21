package com.cafeteros.historia.ui.features.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Activity de reporte de ventas (rol Administrador).
 *
 * Renderiza:
 *  - KPIs de ventas (total, ticket promedio, pedidos).
 *  - Gráfico de barras semanal dibujado a mano con [Canvas] de Compose.
 *  - Tabla "Top productos del periodo" con barra de progreso por unidades vendidas.
 *
 * Datos mock por ahora; cuando exista backend, se reemplaza por un repositorio
 * `SalesReportRepository` con la firma final.
 */
class SalesReportActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                SalesReportScreen(onBack = ::finish)
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SalesReportActivity::class.java))
        }
    }
}

private data class WeeklySalesPoint(val dayLabel: String, val salesCop: Long)
private data class TopProductRow(val name: String, val unitsSold: Int, val revenueCop: Long)

private val MockWeeklySales = listOf(
    WeeklySalesPoint("Lun", 320_000L),
    WeeklySalesPoint("Mar", 480_000L),
    WeeklySalesPoint("Mié", 410_000L),
    WeeklySalesPoint("Jue", 760_000L),
    WeeklySalesPoint("Vie", 920_000L),
    WeeklySalesPoint("Sáb", 1_240_000L),
    WeeklySalesPoint("Dom", 720_000L)
)

private val MockTopProducts = listOf(
    TopProductRow("Geisha Honey 250 g", 38, 2_964_000L),
    TopProductRow("Bourbon Lavado 500 g", 27, 1_674_000L),
    TopProductRow("Pack Selección 3×250 g", 14, 2_310_000L),
    TopProductRow("Castillo Anaeróbico 1 kg", 9, 1_305_000L),
    TopProductRow("Caturra Natural 250 g", 22, 1_056_000L)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SalesReportScreen(onBack: () -> Unit) {
    val totalSales = MockWeeklySales.sumOf { it.salesCop }
    val totalOrders = 84
    val avgTicket = if (totalOrders > 0) totalSales / totalOrders else 0L

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Reporte de ventas",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandColors.CoffeeBrown,
                    titleContentColor = BrandColors.CreamWhite,
                    navigationIconContentColor = BrandColors.CreamWhite
                )
            )
        },
        containerColor = BrandColors.AuthBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SectionHeader("Resumen del periodo") }
            item {
                SalesKpiRow(
                    totalSales = totalSales,
                    avgTicket = avgTicket,
                    totalOrders = totalOrders
                )
            }
            item { SectionHeader("Ventas por día (semana actual)") }
            item { WeeklySalesChart(points = MockWeeklySales) }
            item { SectionHeader("Top productos del periodo") }
            items(MockTopProducts) { row ->
                TopProductCard(
                    row = row,
                    maxUnits = MockTopProducts.maxOf { it.unitsSold }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextSecondary,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun SalesKpiRow(totalSales: Long, avgTicket: Long, totalOrders: Int) {
    val pesoFmt = remember {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).apply { maximumFractionDigits = 0 }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SalesKpiCard(
            modifier = Modifier.weight(1f),
            value = pesoFmt.format(totalSales),
            label = "Total semana",
            accent = BrandColors.ForestGreen
        )
        SalesKpiCard(
            modifier = Modifier.weight(1f),
            value = pesoFmt.format(avgTicket),
            label = "Ticket promedio",
            accent = BrandColors.RatingStar
        )
        SalesKpiCard(
            modifier = Modifier.weight(1f),
            value = totalOrders.toString(),
            label = "Pedidos",
            accent = BrandColors.CoffeeBrown
        )
    }
}

@Composable
private fun SalesKpiCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    accent: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = BrandColors.CardBackground,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = value,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = BrandColors.TextPrimary,
                maxLines = 1
            )
            Text(
                text = label,
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                color = BrandColors.TextSecondary
            )
        }
    }
}

/**
 * Gráfico de barras vertical dibujado con [Canvas] de Compose.
 *
 * El alto de cada barra es proporcional a `salesCop / maxValue`. El ancho se
 * reparte uniformemente con un padding horizontal de 8 dp entre barras. Las
 * etiquetas de día se renderizan debajo del canvas en un [Row] paralelo.
 */
@Composable
private fun WeeklySalesChart(points: List<WeeklySalesPoint>) {
    val maxValue = points.maxOfOrNull { it.salesCop } ?: 0L

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BrandColors.CardBackground,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                if (maxValue == 0L) return@Canvas

                val barCount = points.size
                val gapPx = 12.dp.toPx()
                val totalGapPx = gapPx * (barCount - 1)
                val barWidth = (size.width - totalGapPx) / barCount
                val maxBarHeight = size.height

                points.forEachIndexed { index, point ->
                    val ratio = point.salesCop.toFloat() / maxValue.toFloat()
                    val barHeight = maxBarHeight * ratio
                    val x = index * (barWidth + gapPx)
                    val y = maxBarHeight - barHeight
                    drawRoundRect(
                        color = BrandColors.CoffeeBrown,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                    )
                }

                // Línea base del eje X
                drawLine(
                    color = BrandColors.IndicatorInactive,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            Spacer(Modifier.size(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                points.forEach { point ->
                    Text(
                        text = point.dayLabel,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 11.sp,
                        color = BrandColors.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun TopProductCard(row: TopProductRow, maxUnits: Int) {
    val ratio = if (maxUnits == 0) 0f else row.unitsSold.toFloat() / maxUnits
    val pesoFmt = remember {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).apply { maximumFractionDigits = 0 }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = BrandColors.CardBackground,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = row.name,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = BrandColors.TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = pesoFmt.format(row.revenueCop),
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = BrandColors.CoffeeBrown
                )
            }
            Spacer(Modifier.size(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        BrandColors.IndicatorInactive,
                        shape = RoundedCornerShape(3.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = ratio)
                        .height(6.dp)
                        .background(BrandColors.RatingStar, shape = RoundedCornerShape(3.dp))
                )
            }
            Spacer(Modifier.size(4.dp))
            Text(
                text = "${row.unitsSold} unidades vendidas",
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.sp,
                color = BrandColors.TextSecondary
            )
        }
    }
}

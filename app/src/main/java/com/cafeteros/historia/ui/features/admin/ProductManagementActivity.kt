package com.cafeteros.historia.ui.features.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.PauseCircleOutline
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * Activity de gestión de productos (rol Administrador).
 *
 * Muestra el catálogo global con filtro por estado (Activos / Pausados / Todos)
 * y permite pausar o reactivar publicaciones reportadas.
 *
 * Por ahora consume un dataset mock en memoria; cuando se persista `Product`
 * en Room, se reemplaza por un `ViewModel + Flow` análogo a [UserManagementViewModel].
 */
class ProductManagementActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                ProductManagementScreen(onBack = ::finish)
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ProductManagementActivity::class.java))
        }
    }
}

private data class AdminProductRow(
    val id: String,
    val name: String,
    val seller: String,
    val region: String,
    val priceCop: Int,
    val stockUnits: Int,
    val isPaused: Boolean,
    val isReported: Boolean
)

private val MockAdminProducts: List<AdminProductRow> = listOf(
    AdminProductRow("p-1", "Geisha Honey 250 g", "Finca La Esperanza", "Huila", 78_000, 24, false, false),
    AdminProductRow("p-2", "Bourbon Lavado 500 g", "Finca El Diviso", "Nariño", 62_000, 15, false, true),
    AdminProductRow("p-3", "Caturra Natural 250 g", "Finca Los Pinos", "Cundinamarca", 48_000, 0, true, false),
    AdminProductRow("p-4", "Castillo Anaeróbico 1 kg", "Finca Buenavista", "Sierra Nevada", 145_000, 8, false, false),
    AdminProductRow("p-5", "Típica Honey 500 g", "Finca La Pradera", "Eje Cafetero", 58_000, 12, false, true),
    AdminProductRow("p-6", "Pack Selección 3×250 g", "Origen Curado", "Multi-origen", 165_000, 6, true, false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductManagementScreen(onBack: () -> Unit) {
    var statusFilter by remember { mutableStateOf<StatusFilter>(StatusFilter.All) }
    var products by remember { mutableStateOf(MockAdminProducts) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Gestión de productos",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            StatusFilterRow(selected = statusFilter, onChange = { statusFilter = it })
            val filtered = remember(products, statusFilter) {
                when (statusFilter) {
                    StatusFilter.All -> products
                    StatusFilter.Active -> products.filter { !it.isPaused }
                    StatusFilter.Paused -> products.filter { it.isPaused }
                    StatusFilter.Reported -> products.filter { it.isReported }
                }
            }
            Text(
                text = "${filtered.size} productos",
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp,
                color = BrandColors.TextSecondary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered, key = { it.id }) { product ->
                    AdminProductCard(
                        product = product,
                        onTogglePause = {
                            products = products.map {
                                if (it.id == product.id) it.copy(isPaused = !it.isPaused) else it
                            }
                        }
                    )
                }
            }
        }
    }
}

private enum class StatusFilter { All, Active, Paused, Reported }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusFilterRow(
    selected: StatusFilter,
    onChange: (StatusFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatusChip("Todos", selected == StatusFilter.All) { onChange(StatusFilter.All) }
        StatusChip("Activos", selected == StatusFilter.Active) { onChange(StatusFilter.Active) }
        StatusChip("Pausados", selected == StatusFilter.Paused) { onChange(StatusFilter.Paused) }
        StatusChip("Reportados", selected == StatusFilter.Reported) { onChange(StatusFilter.Reported) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BrandColors.CoffeeBrown,
            selectedLabelColor = BrandColors.CreamWhite
        )
    )
}

@Composable
private fun AdminProductCard(
    product: AdminProductRow,
    onTogglePause: () -> Unit
) {
    val priceFormatted = remember(product.priceCop) {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"))
            .apply { maximumFractionDigits = 0 }
            .format(product.priceCop)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = BrandColors.CardBackground,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = BrandColors.CoffeeBrown.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Coffee,
                    contentDescription = null,
                    tint = BrandColors.CoffeeBrown,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = BrandColors.TextPrimary
                )
                Text(
                    text = "${product.seller} · ${product.region}",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = BrandColors.TextSecondary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = priceFormatted,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = BrandColors.CoffeeBrown
                    )
                    Text(
                        text = "  ·  Stock ${product.stockUnits}",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        color = BrandColors.TextSecondary
                    )
                }
                Spacer(Modifier.size(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (product.isPaused) Badge("Pausado", Color(0xFF6B6B6B))
                    if (product.isReported) Badge("Reportado", Color(0xFFC62828))
                    if (!product.isPaused && !product.isReported) Badge("Activo", BrandColors.ForestGreen)
                }
            }
            IconButton(onClick = onTogglePause) {
                Icon(
                    imageVector = if (product.isPaused)
                        Icons.Outlined.PlayCircleOutline
                    else
                        Icons.Outlined.PauseCircleOutline,
                    contentDescription = if (product.isPaused) "Reactivar" else "Pausar",
                    tint = BrandColors.CoffeeBrown
                )
            }
        }
    }
}

@Composable
private fun Badge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            fontFamily = FontFamily.SansSerif,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

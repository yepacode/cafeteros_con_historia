package com.cafeteros.historia.ui.features.searchresults

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.model.ProductCategory
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.ui.components.Base64Image
import com.cafeteros.historia.ui.features.filters.OriginFiltersActivity
import com.cafeteros.historia.ui.features.productdetail.ProductDetailActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/** Filtros del comprador aplicados a la lista de productos. */
data class AppliedFilters(
    val priceMinCop: Int = 0,
    val priceMaxCop: Int = 0,
    val categories: Set<ProductCategory> = emptySet(),
    val regions: Set<String> = emptySet(),
    val onlyOrganic: Boolean = false
) {
    val activeCount: Int get() = listOf(
        priceMinCop > 0 || priceMaxCop > 0,
        categories.isNotEmpty(),
        regions.isNotEmpty(),
        onlyOrganic
    ).count { it }
}

/**
 * Combina productos + fincas con [AppliedFilters] para producir la lista
 * que ve el comprador en SearchResults. La región se obtiene del
 * [FarmProfile] del caficultor del producto.
 */
class SearchResultsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val productRepository: ProductRepository = app.productRepository
    private val farmRepository: FarmRepository = app.farmRepository

    private val _filters = MutableStateFlow(AppliedFilters())
    val filters: StateFlow<AppliedFilters> = _filters

    val results: StateFlow<List<Product>> = combine(
        productRepository.observeActive(),
        farmRepository.observeAllFarms(),
        _filters
    ) { products, farms, filters ->
        val farmsByUid = farms.associateBy { it.caficultorUid }
        products.filter { product ->
            applyFilters(product, farmsByUid[product.caficultorUid], filters)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun setFilters(applied: AppliedFilters) {
        _filters.value = applied
    }

    /**
     * Aplica todos los criterios de filtro. Cada criterio actúa como AND:
     * el producto pasa si TODOS los activos lo aceptan. Los criterios
     * vacíos no filtran (priceMin=0, lista vacía de categorías, etc.).
     */
    private fun applyFilters(
        product: Product,
        farm: FarmProfile?,
        filters: AppliedFilters
    ): Boolean {
        if (filters.priceMinCop > 0 && product.priceCop < filters.priceMinCop) return false
        if (filters.priceMaxCop > 0 && product.priceCop > filters.priceMaxCop) return false
        if (filters.categories.isNotEmpty() && product.category !in filters.categories) return false
        if (filters.regions.isNotEmpty()) {
            val productRegion = farm?.region.orEmpty()
            if (productRegion !in filters.regions) return false
        }
        if (filters.onlyOrganic && !product.isOrganic) return false
        return true
    }
}

/**
 * Activity de resultados de búsqueda filtrada. Recibe filtros como extras
 * del intent (los pone [OriginFiltersActivity] al aplicar), y los pasa
 * al ViewModel que combina con la lista de productos en tiempo real.
 */
class SearchResultsActivity : ComponentActivity() {

    private val viewModel: SearchResultsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val applied = readFiltersFromIntent()

        setContent {
            CafeterosTheme {
                val results by viewModel.results.collectAsStateWithLifecycle()
                val filters by viewModel.filters.collectAsStateWithLifecycle()

                LaunchedEffect(applied) {
                    viewModel.setFilters(applied)
                }

                SearchResultsScreen(
                    results = results,
                    filters = filters,
                    onBack = ::finish,
                    onProductTap = { product ->
                        ProductDetailActivity.start(this, product.id)
                    },
                    onChangeFilters = { OriginFiltersActivity.start(this); finish() }
                )
            }
        }
    }

    private fun readFiltersFromIntent(): AppliedFilters {
        val cats = intent.getStringArrayExtra(EXTRA_CATEGORIES).orEmpty()
            .mapNotNull { name -> runCatching { ProductCategory.valueOf(name) }.getOrNull() }
            .toSet()
        val regs = intent.getStringArrayExtra(EXTRA_REGIONS).orEmpty().toSet()
        return AppliedFilters(
            priceMinCop = intent.getIntExtra(EXTRA_PRICE_MIN, 0),
            priceMaxCop = intent.getIntExtra(EXTRA_PRICE_MAX, 0),
            categories = cats,
            regions = regs,
            onlyOrganic = intent.getBooleanExtra(EXTRA_ONLY_ORGANIC, false)
        )
    }

    companion object {
        private const val EXTRA_PRICE_MIN = "extra_price_min"
        private const val EXTRA_PRICE_MAX = "extra_price_max"
        private const val EXTRA_CATEGORIES = "extra_categories"
        private const val EXTRA_REGIONS = "extra_regions"
        private const val EXTRA_ONLY_ORGANIC = "extra_only_organic"

        fun start(
            context: Context,
            priceMin: Int = 0,
            priceMax: Int = 0,
            categories: List<String> = emptyList(),
            regions: Set<String> = emptySet(),
            onlyOrganic: Boolean = false
        ) {
            val intent = Intent(context, SearchResultsActivity::class.java).apply {
                putExtra(EXTRA_PRICE_MIN, priceMin)
                putExtra(EXTRA_PRICE_MAX, priceMax)
                putExtra(EXTRA_CATEGORIES, categories.toTypedArray())
                putExtra(EXTRA_REGIONS, regions.toTypedArray())
                putExtra(EXTRA_ONLY_ORGANIC, onlyOrganic)
            }
            context.startActivity(intent)
        }
    }
}

@Composable
private fun SearchResultsScreen(
    results: List<Product>,
    filters: AppliedFilters,
    onBack: () -> Unit,
    onProductTap: (Product) -> Unit,
    onChangeFilters: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)
        .systemBarsPadding()) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Resultados",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandColors.TextPrimary
                    )
                )
                Text(
                    text = "${results.size} producto${if (results.size == 1) "" else "s"}" +
                        if (filters.activeCount > 0) " · ${filters.activeCount} filtro${if (filters.activeCount == 1) "" else "s"}" else "",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
            IconButton(onClick = onChangeFilters) {
                Icon(
                    imageVector = Icons.Outlined.FilterList,
                    contentDescription = "Cambiar filtros",
                    tint = if (filters.activeCount > 0) BrandColors.FarmerPrimary
                    else BrandColors.TextPrimary
                )
            }
        }

        if (results.isEmpty()) {
            EmptyResults(modifier = Modifier.weight(1f), onChangeFilters = onChangeFilters)
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = BrandSpacing.lg,
                    vertical = BrandSpacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                items(results) { product ->
                    ResultRow(product = product, onTap = { onProductTap(product) })
                }
            }
        }
    }
}

@Composable
private fun EmptyResults(modifier: Modifier = Modifier, onChangeFilters: () -> Unit) {
    Column(
        modifier = modifier.fillMaxWidth().padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(BrandColors.InputBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.SentimentDissatisfied,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Sin resultados",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Prueba con menos filtros o cambia el rango de precio.",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Box(
            modifier = Modifier
                .background(BrandColors.CoffeeBrown, RoundedCornerShape(8.dp))
                .clickable(onClick = onChangeFilters)
                .padding(horizontal = BrandSpacing.lg, vertical = 12.dp)
        ) {
            Text(
                text = "Cambiar filtros",
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ResultRow(product: Product, onTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Base64Image(
            base64 = product.imageBase64,
            contentDescription = product.name,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = BrandSpacing.sm)) {
            Text(
                text = product.name,
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "${product.weightGrams}g · ${product.category.label}" +
                    if (product.isOrganic) " · 🌱" else "",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = "$" + "%,d".format(product.priceCop).replace(',', '.'),
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(text = "›", color = BrandColors.TextSecondary, fontSize = 22.sp)
    }
}

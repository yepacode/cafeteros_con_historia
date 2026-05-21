package com.cafeteros.historia.ui.features.search

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.ui.components.Base64Image
import com.cafeteros.historia.ui.features.productdetail.ProductDetailActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel del buscador. Observa todos los productos activos y los
 * filtra en cliente por una query escrita por el usuario. Para volúmenes
 * típicos (decenas de productos) es más simple que orquestar consultas a
 * Firestore con substring matching.
 */
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository =
        (application as CafeterosApplication).productRepository

    /** Lista base de productos activos, sin filtrar. */
    val allProducts: StateFlow<List<Product>> = productRepository.observeActive()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}

/**
 * Activity de búsqueda del comprador. Filtra productos por nombre,
 * descripción, categoría o variedad — match case-insensitive en cliente.
 */
class SearchActivity : ComponentActivity() {

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val products by viewModel.allProducts.collectAsStateWithLifecycle()
                SearchScreen(
                    products = products,
                    onBack = ::finish,
                    onProductTap = { product ->
                        ProductDetailActivity.start(this, product.id)
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    }
}

@Composable
private fun SearchScreen(
    products: List<Product>,
    onBack: () -> Unit,
    onProductTap: (Product) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(products, query) {
        if (query.isBlank()) products
        else products.filter { it.matchesQuery(query) }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)
        .systemBarsPadding()) {
        // Barra superior con campo de búsqueda inline
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
            SearchField(
                value = query,
                onValueChange = { query = it },
                onClear = { query = "" },
                modifier = Modifier.weight(1f)
            )
        }

        if (filtered.isEmpty()) {
            EmptyResults(modifier = Modifier.weight(1f), hasQuery = query.isNotBlank())
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = BrandSpacing.lg,
                    vertical = BrandSpacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                items(filtered) { product ->
                    SearchResultRow(product = product, onTap = { onProductTap(product) })
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(BrandColors.InputBackground, RoundedCornerShape(50))
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
            if (value.isBlank()) {
                Text(
                    text = "Buscar café, variedad, región…",
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = BrandColors.TextPrimary,
                    fontSize = 13.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (value.isNotBlank()) {
            IconButton(onClick = onClear, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Limpiar",
                    tint = BrandColors.TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyResults(modifier: Modifier = Modifier, hasQuery: Boolean) {
    Column(
        modifier = modifier.fillMaxWidth().padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(BrandColors.InputBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(44.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.md))
        Text(
            text = if (hasQuery) "Sin resultados" else "Empieza a buscar",
            color = BrandColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (hasQuery) "Prueba con otro término."
            else "Escribe el nombre del café, una variedad o una región.",
            color = BrandColors.TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchResultRow(product: Product, onTap: () -> Unit) {
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
                .size(64.dp)
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
                text = "${product.weightGrams}g · ${product.category.label}",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = "$" + "%,d".format(product.priceCop).replace(',', '.'),
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(text = "›", color = BrandColors.TextSecondary, fontSize = 22.sp)
    }
}

/**
 * Match case-insensitive contra los campos textuales del producto.
 * Incluye nombre, descripciones, categoría, formato, variedad y notas de
 * cata para que el buscador acepte queries diversas ("geisha", "miel",
 * "honey", "lavado", "1kg" e incluso "huila" si está en el nombre).
 */
private fun Product.matchesQuery(query: String): Boolean {
    val q = query.trim().lowercase()
    val haystack = buildString {
        append(name).append(' ')
        append(shortDescription).append(' ')
        append(fullDescription).append(' ')
        append(category.label).append(' ')
        append(format.label).append(' ')
        append(varietyChips.joinToString(" ")).append(' ')
        append(tastingNotes.joinToString(" "))
    }.lowercase()
    return q in haystack
}

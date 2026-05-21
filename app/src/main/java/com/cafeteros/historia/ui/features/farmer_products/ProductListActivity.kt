package com.cafeteros.historia.ui.features.farmer_products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la lista de productos del caficultor. Cablea las
 * acciones de la pantalla a las activities correspondientes:
 *
 *  - "+ Nuevo producto" / FAB → [ProductCreateActivity] en modo creación.
 *  - Toque en una tarjeta → [ProductCreateActivity] en modo edición.
 *  - Flecha atrás → cierra esta activity.
 *
 * Los datos se observan reactivamente vía [ProductListViewModel] que tira
 * de `productRepository.observeMyProducts(currentUid)`.
 */
class ProductListActivity : ComponentActivity() {

    private val viewModel: ProductListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val products by viewModel.products.collectAsStateWithLifecycle()
                val toast by viewModel.toast.collectAsStateWithLifecycle()

                LaunchedEffect(toast) {
                    toast?.let {
                        Toast.makeText(this@ProductListActivity, it, Toast.LENGTH_SHORT).show()
                        viewModel.consumeToast()
                    }
                }

                ProductListScreen(
                    products = products,
                    onBack = ::finish,
                    onCreateProduct = { ProductCreateActivity.startNew(this) },
                    onEditProduct = { product ->
                        ProductCreateActivity.startEdit(this, product.id)
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ProductListActivity::class.java))
        }
    }
}

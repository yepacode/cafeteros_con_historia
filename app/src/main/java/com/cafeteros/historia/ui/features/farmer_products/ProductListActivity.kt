package com.cafeteros.historia.ui.features.farmer_products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la lista de productos del caficultor. Cablea las
 * acciones de la pantalla a las activities correspondientes:
 *
 *  - "+ Nuevo producto" / FAB → [ProductCreateActivity] en modo creación.
 *  - Toque en una tarjeta → [ProductCreateActivity] en modo edición.
 *  - Flecha atrás → cierra esta activity.
 */
class ProductListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                ProductListScreen(
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

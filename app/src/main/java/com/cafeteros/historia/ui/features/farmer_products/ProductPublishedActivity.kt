package com.cafeteros.historia.ui.features.farmer_products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.MainActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity de confirmación post-publicación. Muestra
 * [ProductPublishedScreen] y cablea sus 3 CTAs:
 *
 *  - Ver mi producto: vuelve a [ProductListActivity] (donde aparecerá
 *    arriba en la lista, ya que la store inserta al inicio).
 *  - Agregar otro producto: abre un [ProductCreateActivity] limpio.
 *  - Ir al panel de inicio: vuelve a [MainActivity] limpiando la pila.
 */
class ProductPublishedActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val productName = intent.getStringExtra(EXTRA_PRODUCT_NAME).orEmpty()

        setContent {
            CafeterosTheme {
                ProductPublishedScreen(
                    productName = productName,
                    onViewProduct = ::goToList,
                    onAddAnother = {
                        ProductCreateActivity.startNew(this)
                        finish()
                    },
                    onGoToPanel = ::goToPanel
                )
            }
        }
    }

    private fun goToList() {
        startActivity(Intent(this, ProductListActivity::class.java))
        finish()
    }

    private fun goToPanel() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    companion object {
        private const val EXTRA_PRODUCT_ID = "extra_product_id"
        private const val EXTRA_PRODUCT_NAME = "extra_product_name"

        fun start(context: Context, productId: String, productName: String) {
            val intent = Intent(context, ProductPublishedActivity::class.java)
                .putExtra(EXTRA_PRODUCT_ID, productId)
                .putExtra(EXTRA_PRODUCT_NAME, productName)
            context.startActivity(intent)
        }
    }
}

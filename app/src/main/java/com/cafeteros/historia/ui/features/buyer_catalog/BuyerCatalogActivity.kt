package com.cafeteros.historia.ui.features.buyer_catalog

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
import com.cafeteros.historia.ui.features.cart.CartActivity
import com.cafeteros.historia.ui.features.coffeemap.CoffeeMapActivity
import com.cafeteros.historia.ui.features.farmer_messages.ChatActivity
import com.cafeteros.historia.ui.features.farmer_messages.InboxActivity
import com.cafeteros.historia.ui.features.productdetail.ProductDetailActivity
import com.cafeteros.historia.ui.features.search.SearchActivity
import com.cafeteros.historia.ui.features.settings.SettingsActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del catálogo del comprador (MVP simplificado).
 *
 * Lista todos los productos activos de todos los caficultores y permite
 * al comprador hacer una compra inmediata de 1 unidad por toque. Cuando
 * el flow de carrito + checkout completo se conecte a Firestore, esta
 * activity puede eliminarse o convertirse en un atajo.
 */
class BuyerCatalogActivity : ComponentActivity() {

    private val viewModel: BuyerCatalogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val products by viewModel.products.collectAsStateWithLifecycle()
                val isBuying by viewModel.isBuying.collectAsStateWithLifecycle()
                val toast by viewModel.toast.collectAsStateWithLifecycle()

                LaunchedEffect(toast) {
                    toast?.let {
                        Toast.makeText(this@BuyerCatalogActivity, it, Toast.LENGTH_LONG).show()
                        viewModel.consumeToast()
                    }
                }

                BuyerCatalogScreen(
                    products = products,
                    isBuying = isBuying,
                    onOpenCart = { CartActivity.start(this) },
                    onOpenMyPurchases = { MyPurchasesActivity.start(this) },
                    onOpenMessages = { InboxActivity.start(this) },
                    onOpenSettings = { SettingsActivity.start(this) },
                    onOpenSearch = { SearchActivity.start(this) },
                    onOpenMap = { CoffeeMapActivity.start(this) },
                    onOpenProduct = { product ->
                        ProductDetailActivity.start(this, product.id)
                    },
                    onContact = { product ->
                        ChatActivity.start(
                            context = this,
                            conversationId = null,
                            partnerUid = product.caficultorUid,
                            partnerName = "Caficultor"
                        )
                    },
                    onBuy = viewModel::buyOne
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, BuyerCatalogActivity::class.java))
        }
    }
}

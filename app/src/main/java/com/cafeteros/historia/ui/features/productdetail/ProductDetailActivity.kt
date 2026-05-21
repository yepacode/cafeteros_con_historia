package com.cafeteros.historia.ui.features.productdetail

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
import com.cafeteros.historia.ui.features.caficultordetail.CaficultorDetailActivity
import com.cafeteros.historia.ui.features.farmer_messages.ChatActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del detalle del producto vista por el comprador.
 *
 * Recibe `productId` como extra de intent. Carga el producto desde
 * Firestore vía [ProductDetailViewModel], junto con el perfil del
 * caficultor dueño. Las acciones (agregar al carrito, comprar, contactar)
 * delegan en el ViewModel; navegaciones a perfil/chat se cablean acá.
 */
class ProductDetailActivity : ComponentActivity() {

    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val productId = intent.getStringExtra(EXTRA_PRODUCT_ID).orEmpty()

        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(productId) {
                    if (productId.isNotBlank()) viewModel.load(productId)
                }

                LaunchedEffect(state.outcome) {
                    when (val o = state.outcome) {
                        is ActionOutcome.AddedToCart -> {
                            Toast.makeText(
                                this@ProductDetailActivity,
                                "${o.productName} agregado al carrito",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.consumeOutcome()
                        }
                        is ActionOutcome.OrderPlaced -> {
                            Toast.makeText(
                                this@ProductDetailActivity,
                                "¡Pedido realizado!",
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.consumeOutcome()
                            finish()
                        }
                        is ActionOutcome.Error -> {
                            Toast.makeText(
                                this@ProductDetailActivity,
                                o.message,
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.consumeOutcome()
                        }
                        null -> Unit
                    }
                }

                ProductDetailScreen(
                    state = state,
                    onBack = ::finish,
                    onIncQty = { viewModel.setQuantity(state.quantity + 1) },
                    onDecQty = { viewModel.setQuantity(state.quantity - 1) },
                    onAddToCart = viewModel::addToCart,
                    onBuyNow = viewModel::buyNow,
                    onOpenCaficultor = {
                        state.product?.caficultorUid?.takeIf { it.isNotBlank() }?.let { uid ->
                            CaficultorDetailActivity.start(this, uid)
                        }
                    },
                    onContactCaficultor = {
                        state.product?.caficultorUid?.takeIf { it.isNotBlank() }?.let { uid ->
                            ChatActivity.start(
                                context = this,
                                conversationId = null,
                                partnerUid = uid,
                                partnerName = state.caficultorName
                            )
                        }
                    }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_PRODUCT_ID = "extra_product_id"

        fun start(context: Context, productId: String) {
            context.startActivity(
                Intent(context, ProductDetailActivity::class.java)
                    .putExtra(EXTRA_PRODUCT_ID, productId)
            )
        }
    }
}

package com.cafeteros.historia.ui.features.farmer_sales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de [SalesScreen]. Observa los pedidos del caficultor
 * vía [SalesViewModel] y los pasa a la pantalla. El tap en un pedido abre
 * el detalle ([OrderDetailActivity]) donde se actualiza el estado.
 */
class SalesActivity : ComponentActivity() {

    private val viewModel: SalesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val orders by viewModel.orders.collectAsStateWithLifecycle()
                SalesScreen(
                    orders = orders,
                    onBack = ::finish,
                    onOrderTap = { orderId ->
                        OrderDetailActivity.start(this, orderId)
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SalesActivity::class.java))
        }
    }
}

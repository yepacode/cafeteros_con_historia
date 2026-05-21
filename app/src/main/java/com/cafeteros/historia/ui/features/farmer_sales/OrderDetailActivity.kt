package com.cafeteros.historia.ui.features.farmer_sales

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
 * Activity contenedora del detalle de un pedido del caficultor.
 *
 * Recibe el `orderId` por intent extra; lo carga vía
 * [OrderDetailViewModel] (que tira de Firestore) y lo pasa al
 * [OrderDetailScreen]. Las acciones de cambio de estado pasan por el VM
 * y se reflejan al instante.
 */
class OrderDetailActivity : ComponentActivity() {

    private val viewModel: OrderDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val orderId = intent.getStringExtra(EXTRA_ORDER_ID) ?: ""

        setContent {
            CafeterosTheme {
                val order by viewModel.order.collectAsStateWithLifecycle()
                val isUpdating by viewModel.isUpdating.collectAsStateWithLifecycle()
                val toast by viewModel.toast.collectAsStateWithLifecycle()

                LaunchedEffect(orderId) {
                    if (orderId.isNotBlank()) viewModel.load(orderId)
                }

                LaunchedEffect(toast) {
                    toast?.let {
                        Toast.makeText(this@OrderDetailActivity, it, Toast.LENGTH_SHORT).show()
                        viewModel.consumeToast()
                    }
                }

                OrderDetailScreen(
                    order = order,
                    isUpdating = isUpdating,
                    onBack = ::finish,
                    onChangeStatus = viewModel::changeStatus
                )
            }
        }
    }

    companion object {
        private const val EXTRA_ORDER_ID = "extra_order_id"

        fun start(context: Context, orderId: String) {
            val intent = Intent(context, OrderDetailActivity::class.java)
                .putExtra(EXTRA_ORDER_ID, orderId)
            context.startActivity(intent)
        }
    }
}

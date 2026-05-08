package com.cafeteros.historia.ui.features.farmer_sales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del detalle de un pedido.
 *
 * Recibe el `orderNumber` por intent extra para mostrarlo en el título.
 * Mientras no exista backend, todas las acciones (Marcar empacado,
 * mensaje, llamar, generar guía, imprimir) caen en Toast "Próximamente".
 */
class OrderDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val orderNumber = intent.getStringExtra(EXTRA_ORDER_NUMBER) ?: "—"

        setContent {
            CafeterosTheme {
                OrderDetailScreen(
                    orderNumber = orderNumber,
                    onBack = ::finish,
                    onAction = { actionLabel ->
                        when (actionLabel) {
                            "Marcar como empacado" ->
                                UpdateOrderStatusActivity.start(this, orderNumber)
                            "Generar guía de envío" ->
                                GenerateShippingGuideActivity.start(this, orderNumber)
                            else -> Toast.makeText(
                                this,
                                "Próximamente: $actionLabel",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_ORDER_NUMBER = "extra_order_number"

        fun start(context: Context, orderNumber: String) {
            val intent = Intent(context, OrderDetailActivity::class.java)
                .putExtra(EXTRA_ORDER_NUMBER, orderNumber)
            context.startActivity(intent)
        }
    }
}

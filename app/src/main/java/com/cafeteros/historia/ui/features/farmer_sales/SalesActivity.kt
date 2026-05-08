package com.cafeteros.historia.ui.features.farmer_sales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de [SalesScreen]. Por ahora todas las acciones
 * (toque en pedido, "Aceptar pedido", "Confirmar despacho", "Ver detalle")
 * muestran Toast "Próximamente" porque las pantallas de detalle aún no
 * existen. Cuando se diseñen, basta con cambiar los callbacks acá.
 */
class SalesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                SalesScreen(
                    onBack = ::finish,
                    onOrderTap = { orderNumber ->
                        OrderDetailActivity.start(this, orderNumber)
                    },
                    onPrimaryAction = { orderNumber, action ->
                        // Las acciones rápidas de la lista (Aceptar / Confirmar
                        // despacho / Ver detalle) abren el detalle del pedido,
                        // que es donde el caficultor termina de procesarlo.
                        OrderDetailActivity.start(this, orderNumber)
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

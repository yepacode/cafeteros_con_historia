package com.cafeteros.historia.ui.features.farmer_inventory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.farmer_products.ProductCreateActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de [InventoryScreen]. Cablea las acciones de la
 * pantalla a las activities correspondientes:
 *
 *  - Toque en un producto → abre [ProductCreateActivity] en modo edición.
 *  - Acciones masivas (Exportar/Importar CSV) → Toast "Próximamente".
 *  - Flecha atrás → cierra esta activity.
 */
class InventoryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                InventoryScreen(
                    onBack = ::finish,
                    onProductTap = { product ->
                        ProductCreateActivity.startEdit(this, product.id)
                    },
                    onMassAction = { actionLabel ->
                        Toast.makeText(
                            this,
                            "Próximamente: $actionLabel",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, InventoryActivity::class.java))
        }
    }
}

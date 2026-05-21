package com.cafeteros.historia.ui.features.farmer_inventory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.ui.features.farmer_products.ProductCreateActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de [InventoryScreen]. Observa los productos del
 * caficultor vía [InventoryViewModel] y cablea las acciones a Firestore.
 *
 *  - Toque en un producto → abre [ProductCreateActivity] en modo edición.
 *  - Steppers +/− del stock → llaman a `viewModel.updateStock` que persiste
 *    el nuevo valor en Firestore.
 *  - Acciones masivas (Exportar/Importar CSV) → Toast "Próximamente".
 *  - Flecha atrás → cierra esta activity.
 */
class InventoryActivity : ComponentActivity() {

    private val viewModel: InventoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val products by viewModel.products.collectAsStateWithLifecycle()

                InventoryScreen(
                    products = products,
                    onBack = ::finish,
                    onProductTap = { product ->
                        ProductCreateActivity.startEdit(this, product.id)
                    },
                    onUpdateStock = viewModel::updateStock,
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

package com.cafeteros.historia.ui.features.farmer_products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del wizard de creación/edición de producto.
 *
 * Soporta dos modos:
 *  - **Crear**: se abre con [start] (sin id) y entra al wizard en blanco.
 *  - **Editar**: se abre con [start] pasando un `productId` existente; el
 *    [ProductCreateViewModel] precarga el formulario con sus datos.
 *
 * Al pulsar "Publicar" / "Guardar cambios", se persiste en
 * [com.cafeteros.historia.ui.features.farmer_products.model.ProductsStore]
 * y se navega a [ProductPublishedActivity] (solo en modo crear) o se cierra
 * y vuelve a la pantalla anterior (en modo editar).
 */
class ProductCreateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val editingProductId: String? = intent.getStringExtra(EXTRA_PRODUCT_ID)
        val isEditing = editingProductId != null

        setContent {
            CafeterosTheme {
                ProductCreateScreen(
                    editingProductId = editingProductId,
                    onClose = ::finish,
                    onPublished = { product ->
                        if (isEditing) {
                            finish()
                        } else {
                            ProductPublishedActivity.start(this, product.id, product.name)
                            finish()
                        }
                    }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_PRODUCT_ID = "extra_product_id"

        /** Abre el wizard en modo creación (sin id). */
        fun startNew(context: Context) {
            context.startActivity(Intent(context, ProductCreateActivity::class.java))
        }

        /** Abre el wizard en modo edición precargando el producto existente. */
        fun startEdit(context: Context, productId: String) {
            val intent = Intent(context, ProductCreateActivity::class.java)
                .putExtra(EXTRA_PRODUCT_ID, productId)
            context.startActivity(intent)
        }
    }
}

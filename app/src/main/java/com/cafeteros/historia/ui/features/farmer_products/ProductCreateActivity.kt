package com.cafeteros.historia.ui.features.farmer_products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del wizard de creación/edición de producto.
 *
 * Soporta dos modos:
 *  - **Crear**: se abre con [startNew] y entra al wizard en blanco.
 *  - **Editar**: se abre con [startEdit] pasando un `productId` existente;
 *    el [ProductCreateViewModel] precarga el formulario desde Firestore.
 *
 * Al publicar con éxito navega a [ProductPublishedActivity] (modo crear) o
 * cierra la activity (modo editar). En caso de error muestra un Toast con
 * el mensaje devuelto por el repositorio.
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
                    onPublished = { productId, productName ->
                        if (isEditing) {
                            Toast.makeText(
                                this,
                                "Cambios guardados",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            ProductPublishedActivity.start(this, productId, productName)
                            finish()
                        }
                    },
                    onPublishError = { message ->
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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

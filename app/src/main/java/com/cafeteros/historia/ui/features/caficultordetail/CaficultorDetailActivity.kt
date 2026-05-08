package com.cafeteros.historia.ui.features.caficultordetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.caficultordetail.model.CaficultorDetailSampleData
import com.cafeteros.historia.ui.features.productdetail.ProductDetailActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del detalle del caficultor.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_CAFICULTOR_NAME]: identificador legible del caficultor o de su
 *    finca ("Don Alberto", "Finca La Esperanza"). Se usa para resolver el
 *    [com.cafeteros.historia.ui.features.caficultordetail.model.CaficultorProfile]
 *    desde [CaficultorDetailSampleData.forName]. Si no llega o no matchea,
 *    cae a Don Alberto por default.
 *  - [EXTRA_ROLE_ID]: identificador del rol activo (Comprador = 1). Se
 *    propaga desde [com.cafeteros.historia.ui.features.explore.ExploreActivity]
 *    para mantener la cadena del rol cuando se conecte la BD.
 *
 * Toda la UI vive en [CaficultorDetailScreen]; aquí solo se conecta la
 * navegación via Toasts hasta que existan las pantallas reales (chat,
 * productos completos, reseñas completas, reproductor de video, etc.).
 */
class CaficultorDetailActivity : ComponentActivity() {

    /** Rol del usuario activo, leído del Intent. */
    private var currentRoleId: Int = ROLE_ID_COMPRADOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val caficultorName = intent.getStringExtra(EXTRA_CAFICULTOR_NAME)
            ?.takeIf { it.isNotBlank() }
            ?: DEFAULT_CAFICULTOR_NAME
        currentRoleId = intent.getIntExtra(EXTRA_ROLE_ID, ROLE_ID_COMPRADOR)

        Log.d(
            TAG,
            "CaficultorDetailActivity iniciada para caficultor='$caficultorName', " +
                    "roleId=$currentRoleId"
        )

        val profile = CaficultorDetailSampleData.forName(caficultorName)

        setContent {
            CafeterosTheme {
                CaficultorDetailScreen(
                    profile = profile,
                    onBack = ::finish,
                    onShare = { toast("Compartir ${profile.farmName}") },
                    onProductClick = { openProductDetail(it.name, profile.displayShortName) },
                    onAddToCart = { toast("${it.name} agregado al carrito") },
                    onSeeAllProducts = { toast("Ver todos los productos de ${profile.displayShortName}") },
                    onSeeAllReviews = { toast("Ver todas las reseñas") },
                    onSendMessage = { toast("Mensaje a ${profile.displayShortName}") },
                    onPlayVideo = { toast("Reproducir video") },
                    onReadFullStory = { toast("Leer historia completa") }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Abre el detalle de producto preservando el rol activo y propagando el
     * caficultor dueño para que el botón "Ver perfil →" pueda devolver al
     * usuario al perfil correcto.
     */
    private fun openProductDetail(productName: String, producerShortName: String) {
        val intent = Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(ProductDetailActivity.EXTRA_PRODUCT_NAME, productName)
            putExtra(ProductDetailActivity.EXTRA_PRODUCER_NAME, producerShortName)
            putExtra(ProductDetailActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG: String = "CaficultorDetailActivity"

        /** Clave del extra que transporta el nombre del caficultor a mostrar. */
        const val EXTRA_CAFICULTOR_NAME: String = "extra_caficultor_name"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /**
         * Identificador del rol "Comprador". Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1

        private const val DEFAULT_CAFICULTOR_NAME: String = "Don Alberto"
    }
}

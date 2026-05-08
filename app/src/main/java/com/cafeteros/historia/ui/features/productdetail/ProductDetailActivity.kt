package com.cafeteros.historia.ui.features.productdetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.caficultordetail.CaficultorDetailActivity
import com.cafeteros.historia.ui.features.productdetail.model.ProductDetailSampleData
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del detalle de producto.
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * el caficultor tiene su propia experiencia y nunca aterriza aquí. El detalle
 * de producto, el botón "Agregar al carrito" y la pill del productor solo
 * tienen sentido para el comprador que está descubriendo café.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_PRODUCT_NAME]: identificador legible del producto a mostrar.
 *    Hoy se usa para resolver el [com.cafeteros.historia.ui.features.productdetail.model.ProductDetail]
 *    desde [ProductDetailSampleData.forName]. Si no llega, cae al producto
 *    por default ("Café Huila Pitalito").
 *  - [EXTRA_PRODUCER_NAME]: identificador del caficultor dueño del producto.
 *    Se propaga a [CaficultorDetailActivity] cuando el usuario pulsa
 *    "Ver perfil →" para reabrir el perfil correcto.
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat. El campo se guarda en
 *    [currentRoleId] para que, cuando la base de datos esté disponible,
 *    los casos de uso (agregar al carrito, registrar pedido, calcular
 *    envío por usuario, etc.) lo tomen de aquí sin tener que volver a
 *    inferirlo.
 *
 * Toda la UI vive en [ProductDetailScreen]; aquí solo conectamos los
 * callbacks vía Toasts e Intents mientras el carrito y la página de perfil
 * del caficultor son las únicas pantallas reales accesibles.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. `addToCart(productId, presentation, roast, grind, quantity, roleId = currentRoleId)`
 *     ya está listo para parametrizarse con [currentRoleId].
 */
class ProductDetailActivity : ComponentActivity() {

    /**
     * Rol del usuario activo. Se inicializa desde el Intent extra y se
     * fuerza a [ROLE_ID_COMPRADOR] cuando llega un valor inesperado, porque
     * esta pantalla solo tiene sentido para compradores. Mientras no exista
     * BD, se conserva en memoria por el ciclo de vida de la Activity.
     */
    private var currentRoleId: Int = ROLE_ID_COMPRADOR
    private var producerName: String = DEFAULT_PRODUCER_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val productName = intent.getStringExtra(EXTRA_PRODUCT_NAME)
            ?.takeIf { it.isNotBlank() }
            ?: DEFAULT_PRODUCT_NAME
        producerName = intent.getStringExtra(EXTRA_PRODUCER_NAME)
            ?.takeIf { it.isNotBlank() }
            ?: DEFAULT_PRODUCER_NAME

        val incomingRoleId = intent.getIntExtra(EXTRA_ROLE_ID, ROLE_ID_COMPRADOR)
        currentRoleId = if (incomingRoleId == ROLE_ID_COMPRADOR) {
            incomingRoleId
        } else {
            Log.w(
                TAG,
                "Rol $incomingRoleId no soportado en ProductDetailActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        Log.d(
            TAG,
            "ProductDetailActivity iniciada para product='$productName', " +
                    "producer='$producerName', roleId=$currentRoleId"
        )

        val product = ProductDetailSampleData.forName(productName)

        setContent {
            CafeterosTheme {
                ProductDetailScreen(
                    product = product,
                    cartItemCount = 3,
                    onBack = ::finish,
                    onShare = { toast("Compartir ${product.name}") },
                    onCart = { toast("Carrito (3)") },
                    onViewProducerProfile = { openCaficultorDetail(producerName) },
                    onAddToCart = { presentation, roast, grind, quantity ->
                        toast(
                            "Agregado: ${quantity}x ${presentation.label} · " +
                                    "${roast.label} · ${grind.label}"
                        )
                    }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Abre el detalle del caficultor cuando el usuario pulsa "Ver perfil →"
     * desde la pill del productor. Reusa la activity ya existente para
     * mantener una sola pantalla de perfil de caficultor en toda la app.
     */
    private fun openCaficultorDetail(name: String) {
        val intent = Intent(this, CaficultorDetailActivity::class.java).apply {
            putExtra(CaficultorDetailActivity.EXTRA_CAFICULTOR_NAME, name)
            putExtra(CaficultorDetailActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG: String = "ProductDetailActivity"

        /** Clave del extra que transporta el nombre del producto. */
        const val EXTRA_PRODUCT_NAME: String = "extra_product_name"

        /** Clave del extra que transporta el nombre del caficultor dueño. */
        const val EXTRA_PRODUCER_NAME: String = "extra_producer_name"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /**
         * Identificador del rol "Comprador". Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1

        private const val DEFAULT_PRODUCT_NAME: String = "Café Huila Pitalito"
        private const val DEFAULT_PRODUCER_NAME: String = "Don Alberto"
    }
}

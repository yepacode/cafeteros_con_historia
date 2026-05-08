package com.cafeteros.historia.ui.features.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.checkoutshipping.ShippingAddressActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la pantalla "Tu Selección" (carrito).
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * solo el comprador acumula items en su bolsa y avanza a checkout.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat. El campo se guarda en
 *    [currentRoleId] para que, cuando la base de datos esté disponible,
 *    los casos de uso (`getCart(userId, roleId = ...)`,
 *    `updateQuantity(itemId, qty, roleId = ...)`,
 *    `applyCoupon(code, roleId = ...)`,
 *    `checkout(cart, roleId = ...)`) lo tomen de aquí.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. Las llamadas tipo `checkout(cart, roleId = currentRoleId)` ya están
 *     listas para parametrizarse con [currentRoleId].
 *
 * Toda la UI vive en [CartScreen]; aquí solo conectamos los callbacks
 * (back, cambiar dirección, checkout, seguir comprando) con Toasts/finish
 * mientras los demás flujos no existen aún.
 */
class CartActivity : ComponentActivity() {

    /**
     * Rol del usuario activo. Se inicializa desde el Intent extra y se
     * fuerza a [ROLE_ID_COMPRADOR] cuando llega un valor inesperado.
     */
    private var currentRoleId: Int = ROLE_ID_COMPRADOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val incomingRoleId = intent.getIntExtra(EXTRA_ROLE_ID, ROLE_ID_COMPRADOR)
        currentRoleId = if (incomingRoleId == ROLE_ID_COMPRADOR) {
            incomingRoleId
        } else {
            Log.w(
                TAG,
                "Rol $incomingRoleId no soportado en CartActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        Log.d(TAG, "CartActivity iniciada con roleId=$currentRoleId")

        setContent {
            CafeterosTheme {
                CartScreen(
                    onBack = ::finish,
                    onChangeShipping = { toast("Cambiar dirección próximamente") },
                    onCheckout = ::openShippingAddress,
                    onContinueShopping = ::finish,
                    onExploreCoffee = ::finish,
                    onRecommendationClick = { recommendation ->
                        toast("${recommendation.name} próximamente")
                    },
                    onTabSelected = { tab -> Log.d(TAG, "Tab seleccionado: $tab") }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Abre el paso 1 del checkout ("Dirección de Envío") propagando el
     * total formateado del carrito como subtotal del bottom bar y el rol
     * activo.
     */
    private fun openShippingAddress(formattedTotal: String) {
        val intent = Intent(this, ShippingAddressActivity::class.java).apply {
            putExtra(ShippingAddressActivity.EXTRA_FORMATTED_SUBTOTAL, formattedTotal)
            putExtra(ShippingAddressActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG: String = "CartActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1
    }
}

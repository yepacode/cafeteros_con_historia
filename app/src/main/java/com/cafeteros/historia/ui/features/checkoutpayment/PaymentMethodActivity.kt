package com.cafeteros.historia.ui.features.checkoutpayment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.orderreview.OrderReviewActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del paso 2 del checkout: "Método de pago".
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * solo el comprador completa el flujo de compra. Es el segundo paso del
 * stepper de 3 pasos (el siguiente — resumen del pedido — no existe aún).
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat.
 *  - [EXTRA_FORMATTED_TOTAL]: total a pagar ya formateado para mostrarse
 *    en el bottom bar fijo. Cuando exista BD, este valor lo recalcula la
 *    pantalla a partir del cart real cargado por `getCart(roleId)`.
 *  - [EXTRA_SHIPPING_ADDRESS_LINE]: línea ya formateada con la dirección
 *    seleccionada en el paso 1 ("Cra 10 #42-15, Bogotá").
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. `getPaymentMethods(roleId = currentRoleId)` carga los métodos
 *     habilitados, `getSavedCards(userId, roleId = currentRoleId)` las
 *     tarjetas guardadas, y `confirmPayment(method, cardId, roleId =
 *     currentRoleId)` avanza al paso 3.
 *
 * Toda la UI vive en [PaymentMethodScreen]; aquí solo conectamos los
 * callbacks (back, cambiar dirección, agregar tarjeta, revisar pedido)
 * con Toasts/finish mientras los demás flujos no existen.
 */
class PaymentMethodActivity : ComponentActivity() {

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
                "Rol $incomingRoleId no soportado en PaymentMethodActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        val formattedTotal = intent.getStringExtra(EXTRA_FORMATTED_TOTAL)
            ?.takeIf { it.isNotBlank() }
            ?: DEFAULT_TOTAL

        val shippingAddressLine = intent.getStringExtra(EXTRA_SHIPPING_ADDRESS_LINE)
            ?.takeIf { it.isNotBlank() }
            ?: DEFAULT_SHIPPING_ADDRESS_LINE

        Log.d(
            TAG,
            "PaymentMethodActivity iniciada con roleId=$currentRoleId, " +
                    "total=$formattedTotal, shipping=$shippingAddressLine"
        )

        setContent {
            CafeterosTheme {
                PaymentMethodScreen(
                    shippingAddressLine = shippingAddressLine,
                    formattedTotal = formattedTotal,
                    onBack = ::finish,
                    onOpenCart = { toast("Carrito próximamente") },
                    onChangeAddress = ::finish,
                    onAddCard = { toast("Agregar tarjeta próximamente") },
                    onReview = { method, card ->
                        Log.d(
                            TAG,
                            "Revisar pedido con method=${method.code}, " +
                                    "cardId=${card?.id ?: "-"}, roleId=$currentRoleId"
                        )
                        val paymentLabel = card?.let { "${it.brand} **** ${it.last4}" }
                            ?: method.title
                        val intent = Intent(this, OrderReviewActivity::class.java).apply {
                            putExtra(OrderReviewActivity.EXTRA_ROLE_ID, currentRoleId)
                            putExtra(OrderReviewActivity.EXTRA_FORMATTED_TOTAL, formattedTotal)
                            putExtra(
                                OrderReviewActivity.EXTRA_SHIPPING_ADDRESS_LINE,
                                shippingAddressLine
                            )
                            putExtra(
                                OrderReviewActivity.EXTRA_PAYMENT_METHOD_LABEL,
                                paymentLabel
                            )
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG: String = "PaymentMethodActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /** Clave del extra con el total a pagar ya formateado. */
        const val EXTRA_FORMATTED_TOTAL: String = "extra_formatted_total"

        /** Clave del extra con la dirección de envío ya formateada. */
        const val EXTRA_SHIPPING_ADDRESS_LINE: String = "extra_shipping_address_line"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1

        private const val DEFAULT_TOTAL: String = "$146.000"
        private const val DEFAULT_SHIPPING_ADDRESS_LINE: String = "Cra 10 #42-15, Bogotá"
    }
}

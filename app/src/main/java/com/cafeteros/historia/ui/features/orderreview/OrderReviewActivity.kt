package com.cafeteros.historia.ui.features.orderreview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.checkoutpayment.MockPaymentGateway
import com.cafeteros.historia.ui.features.orderreview.model.OrderReviewSampleData
import com.cafeteros.historia.ui.features.paymentfailed.PaymentFailedActivity
import com.cafeteros.historia.ui.features.paymentsuccess.PaymentSuccessActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del paso 3 del checkout: "Revisar pedido".
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * solo el comprador completa el flujo de compra. Es el último paso del
 * stepper de 3 pasos antes de la confirmación.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat.
 *  - [EXTRA_FORMATTED_TOTAL]: total a pagar ya formateado para mostrarse
 *    en el bottom bar fijo. Cuando exista BD, este valor lo recalcula la
 *    pantalla a partir del cart real cargado por `getCart(roleId)`.
 *  - [EXTRA_SHIPPING_ADDRESS_LINE]: línea principal de la dirección
 *    seleccionada en el paso 1 ("Cra 10 #42-15").
 *  - [EXTRA_SHIPPING_ADDRESS_SECONDARY]: ciudad y departamento.
 *  - [EXTRA_PAYMENT_METHOD_LABEL]: etiqueta corta del método elegido en el
 *    paso 2 ("Visa **** 4567"); si llega vacío, se usa el resumen sample.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. `getOrderReview(cartId, addressId, paymentChoice, roleId =
 *     currentRoleId)` arma el resumen completo (items, totals, delivery
 *     estimate) y `confirmOrder(orderId, roleId = currentRoleId)` cierra
 *     el flujo.
 *
 * Toda la UI vive en [OrderReviewScreen]; aquí solo conectamos los
 * callbacks (back, editar dirección/pago, ver todos los productos,
 * confirmar) con Toasts/finish mientras los demás flujos no existen.
 */
class OrderReviewActivity : ComponentActivity() {

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
                "Rol $incomingRoleId no soportado en OrderReviewActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        val shippingAddressLine = intent.getStringExtra(EXTRA_SHIPPING_ADDRESS_LINE)
            ?.takeIf { it.isNotBlank() }
            ?: OrderReviewSampleData.shippingAddressLine

        val shippingAddressSecondary = intent.getStringExtra(EXTRA_SHIPPING_ADDRESS_SECONDARY)
            ?.takeIf { it.isNotBlank() }
            ?: OrderReviewSampleData.shippingAddressSecondary

        val paymentMethodLabel = intent.getStringExtra(EXTRA_PAYMENT_METHOD_LABEL)
            ?.takeIf { it.isNotBlank() }

        val incomingTotal = intent.getStringExtra(EXTRA_FORMATTED_TOTAL)
            ?.takeIf { it.isNotBlank() }

        val totals = if (incomingTotal != null) {
            OrderReviewSampleData.totals.copy(totalFormatted = incomingTotal)
        } else {
            OrderReviewSampleData.totals
        }

        Log.d(
            TAG,
            "OrderReviewActivity iniciada con roleId=$currentRoleId, " +
                    "total=${totals.totalFormatted}, shipping=$shippingAddressLine, " +
                    "paymentLabel=${paymentMethodLabel ?: "-"}"
        )

        setContent {
            CafeterosTheme {
                OrderReviewScreen(
                    shippingAddressLine = shippingAddressLine,
                    shippingAddressSecondary = shippingAddressSecondary,
                    totals = totals,
                    onBack = ::finish,
                    onEditAddress = ::finish,
                    onEditPayment = ::finish,
                    onViewAllProducts = { toast("Ver productos próximamente") },
                    onConfirm = { confirmedTotals ->
                        val gatewayResult = MockPaymentGateway.simulatePayment()
                        Log.d(
                            TAG,
                            "Confirmar pedido total=${confirmedTotals.totalFormatted}, " +
                                    "roleId=$currentRoleId, gatewayResult=$gatewayResult"
                        )
                        val nextIntent = when (gatewayResult) {
                            MockPaymentGateway.Result.SUCCESS ->
                                Intent(this, PaymentSuccessActivity::class.java).apply {
                                    putExtra(
                                        PaymentSuccessActivity.EXTRA_ROLE_ID,
                                        currentRoleId
                                    )
                                    putExtra(
                                        PaymentSuccessActivity.EXTRA_TOTAL_PAID,
                                        confirmedTotals.totalFormatted
                                    )
                                    paymentMethodLabel?.let {
                                        putExtra(
                                            PaymentSuccessActivity.EXTRA_PAYMENT_LABEL,
                                            it
                                        )
                                    }
                                }
                            MockPaymentGateway.Result.FAILURE ->
                                Intent(this, PaymentFailedActivity::class.java).apply {
                                    putExtra(
                                        PaymentFailedActivity.EXTRA_ROLE_ID,
                                        currentRoleId
                                    )
                                }
                        }
                        startActivity(nextIntent)
                    }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG: String = "OrderReviewActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /** Clave del extra con el total a pagar ya formateado. */
        const val EXTRA_FORMATTED_TOTAL: String = "extra_formatted_total"

        /** Clave del extra con la línea principal de la dirección de envío. */
        const val EXTRA_SHIPPING_ADDRESS_LINE: String = "extra_shipping_address_line"

        /** Clave del extra con la línea secundaria (ciudad/departamento). */
        const val EXTRA_SHIPPING_ADDRESS_SECONDARY: String = "extra_shipping_address_secondary"

        /**
         * Clave del extra con la etiqueta corta del método de pago elegido
         * en el paso 2 ("Visa **** 4567"). Si llega vacío se usan los
         * datos sample.
         */
        const val EXTRA_PAYMENT_METHOD_LABEL: String = "extra_payment_method_label"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1
    }
}

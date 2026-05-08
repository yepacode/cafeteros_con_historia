package com.cafeteros.historia.ui.features.paymentsuccess

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.paymentsuccess.model.PaymentSuccessSampleData
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del paso 4 del checkout en la rama de éxito.
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * solo el comprador completa el flujo de compra. Se llega a ella tras un
 * pago exitoso simulado desde [com.cafeteros.historia.ui.features
 * .checkoutpayment.MockPaymentGateway].
 *
 * Recibe por extras del Intent (todos opcionales — si faltan se usan los
 * datos sample para que la pantalla sea visible aún en deep-links):
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat.
 *  - [EXTRA_ORDER_NUMBER]: número del pedido recién creado ("#OR-34521").
 *  - [EXTRA_TOTAL_PAID]: total cobrado ya formateado.
 *  - [EXTRA_ESTIMATED_DELIVERY]: fecha estimada de entrega ya formateada.
 *  - [EXTRA_PAYMENT_LABEL]: etiqueta corta del método con el que se pagó.
 *
 * Cuando exista backend, la fuente de los datos cambia a un repositorio que
 * carga el pedido recién creado por id; el resto del cableado de UI se
 * mantiene.
 */
class PaymentSuccessActivity : ComponentActivity() {

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
                "Rol $incomingRoleId no soportado en PaymentSuccessActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        val sampleOrder = PaymentSuccessSampleData.order
        val order = sampleOrder.copy(
            orderNumber = intent.getStringExtra(EXTRA_ORDER_NUMBER)
                ?.takeIf { it.isNotBlank() }
                ?: sampleOrder.orderNumber,
            totalPaidFormatted = intent.getStringExtra(EXTRA_TOTAL_PAID)
                ?.takeIf { it.isNotBlank() }
                ?: sampleOrder.totalPaidFormatted,
            estimatedDelivery = intent.getStringExtra(EXTRA_ESTIMATED_DELIVERY)
                ?.takeIf { it.isNotBlank() }
                ?: sampleOrder.estimatedDelivery,
            paymentLabel = intent.getStringExtra(EXTRA_PAYMENT_LABEL)
                ?.takeIf { it.isNotBlank() }
                ?: sampleOrder.paymentLabel
        )

        Log.d(
            TAG,
            "PaymentSuccessActivity iniciada con roleId=$currentRoleId, " +
                    "orderNumber=${order.orderNumber}, total=${order.totalPaidFormatted}"
        )

        setContent {
            CafeterosTheme {
                PaymentSuccessScreen(
                    order = order,
                    onClose = ::finish,
                    onTrackOrder = { toast("Mis pedidos próximamente") },
                    onKeepExploring = { toast("Volver a explorar próximamente") },
                    onRateExperience = { toast("Calificación próximamente") },
                    onViewCaficultorProfile = { caficultor ->
                        toast("Perfil de ${caficultor.fincaName} próximamente")
                    }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG: String = "PaymentSuccessActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /** Clave del extra con el número del pedido recién creado. */
        const val EXTRA_ORDER_NUMBER: String = "extra_order_number"

        /** Clave del extra con el total ya formateado. */
        const val EXTRA_TOTAL_PAID: String = "extra_total_paid"

        /** Clave del extra con la fecha de entrega ya formateada. */
        const val EXTRA_ESTIMATED_DELIVERY: String = "extra_estimated_delivery"

        /** Clave del extra con la etiqueta del método de pago usado. */
        const val EXTRA_PAYMENT_LABEL: String = "extra_payment_label"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1
    }
}

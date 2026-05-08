package com.cafeteros.historia.ui.features.paymentfailed

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.paymentfailed.model.PaymentFailedSampleData
import com.cafeteros.historia.ui.features.paymentfailed.model.PaymentFailureReason
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del paso 4 del checkout en la rama de fallo.
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * solo el comprador completa el flujo de compra. Se llega a ella tras un
 * pago fallido simulado desde [com.cafeteros.historia.ui.features
 * .checkoutpayment.MockPaymentGateway].
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat.
 *  - [EXTRA_REASON_LABEL]: motivo legible del fallo ("Fondos insuficientes").
 *  - [EXTRA_ERROR_CODE]: código de error del procesador de pagos ("51").
 *
 * Ambos extras son opcionales — si faltan se usa [PaymentFailedSampleData]
 * para que la pantalla siga siendo navegable desde deep-links de pruebas.
 *
 * Cuando exista backend, la fuente del motivo+código vendrá de la respuesta
 * del gateway real; el resto del cableado UI no cambia.
 */
class PaymentFailedActivity : ComponentActivity() {

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
                "Rol $incomingRoleId no soportado en PaymentFailedActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        val sampleReason = PaymentFailedSampleData.reason
        val reason = PaymentFailureReason(
            reasonLabel = intent.getStringExtra(EXTRA_REASON_LABEL)
                ?.takeIf { it.isNotBlank() }
                ?: sampleReason.reasonLabel,
            errorCode = intent.getStringExtra(EXTRA_ERROR_CODE)
                ?.takeIf { it.isNotBlank() }
                ?: sampleReason.errorCode
        )

        Log.d(
            TAG,
            "PaymentFailedActivity iniciada con roleId=$currentRoleId, " +
                    "reason=${reason.reasonLabel}, code=${reason.errorCode}"
        )

        setContent {
            CafeterosTheme {
                PaymentFailedScreen(
                    reason = reason,
                    onClose = ::finish,
                    onRetry = ::finish,
                    onChangeMethod = ::finish,
                    onContactSupport = { toast("Soporte próximamente") }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG: String = "PaymentFailedActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /** Clave del extra con el motivo legible del fallo. */
        const val EXTRA_REASON_LABEL: String = "extra_reason_label"

        /** Clave del extra con el código de error del procesador de pagos. */
        const val EXTRA_ERROR_CODE: String = "extra_error_code"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1
    }
}

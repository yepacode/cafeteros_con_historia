package com.cafeteros.historia.ui.features.checkoutshipping

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.addressbook.AddressBookActivity
import com.cafeteros.historia.ui.features.checkoutpayment.PaymentMethodActivity
import com.cafeteros.historia.ui.features.checkoutshipping.model.RecipientOption
import com.cafeteros.historia.ui.features.checkoutshipping.model.ShippingAddress
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del paso 1 del checkout: "Dirección de Envío".
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * solo el comprador completa el flujo de compra. Es el primer paso de un
 * stepper de 3 pasos (los siguientes 2 — método de pago y resumen — no
 * existen aún).
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat.
 *  - [EXTRA_FORMATTED_SUBTOTAL]: subtotal del carrito ya formateado para
 *    mostrarse en el bottom bar fijo. Cuando exista BD, este valor lo
 *    recalcula la pantalla a partir del cart real cargado por
 *    `getCart(roleId)`.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. `getAddresses(userId, roleId = currentRoleId)` carga las direcciones
 *     reales y `proceedToPayment(addressId, instructions, recipient,
 *     roleId = currentRoleId)` avanza al paso 2.
 *
 * Toda la UI vive en [ShippingAddressScreen]; aquí solo conectamos los
 * callbacks (back, gestionar, agregar, editar, continuar) con
 * Toasts/finish mientras los demás flujos no existen.
 */
class ShippingAddressActivity : ComponentActivity() {

    /**
     * Rol del usuario activo. Se inicializa desde el Intent extra y se
     * fuerza a [ROLE_ID_COMPRADOR] cuando llega un valor inesperado.
     */
    private var currentRoleId: Int = ROLE_ID_COMPRADOR
    private var formattedSubtotal: String = DEFAULT_SUBTOTAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val incomingRoleId = intent.getIntExtra(EXTRA_ROLE_ID, ROLE_ID_COMPRADOR)
        currentRoleId = if (incomingRoleId == ROLE_ID_COMPRADOR) {
            incomingRoleId
        } else {
            Log.w(
                TAG,
                "Rol $incomingRoleId no soportado en ShippingAddressActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        formattedSubtotal = intent.getStringExtra(EXTRA_FORMATTED_SUBTOTAL)
            ?.takeIf { it.isNotBlank() }
            ?: DEFAULT_SUBTOTAL

        Log.d(
            TAG,
            "ShippingAddressActivity iniciada con roleId=$currentRoleId, " +
                    "subtotal=$formattedSubtotal"
        )

        setContent {
            CafeterosTheme {
                ShippingAddressScreen(
                    formattedSubtotal = formattedSubtotal,
                    onBack = ::finish,
                    onManageAddresses = ::openAddressBook,
                    onAddAddress = { toast("Agregar dirección próximamente") },
                    onEditAddress = { address -> toast("Editar ${address.addressLine}") },
                    onContinue = { address, instructions, recipient ->
                        Log.d(
                            TAG,
                            "Continuar al paso 2 con address=${address.id}, " +
                                    "instructions='$instructions', " +
                                    "recipient=$recipient, roleId=$currentRoleId"
                        )
                        openPaymentMethod(address, instructions, recipient)
                    }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Lanza la pantalla "Mis Direcciones" propagando el rol activo. Usar
     * un Intent explícito mantiene la dependencia hacia la activity de
     * destino y permite a Studio detectar enlaces rotos al refactor.
     */
    private fun openAddressBook() {
        val intent = Intent(this, AddressBookActivity::class.java).apply {
            putExtra(AddressBookActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    /**
     * Lanza el paso 2 ("Método de pago") propagando rol, total y la
     * dirección elegida ya formateada para el resumen del paso 2.
     *
     * Las [instructions] y la opción de [recipient] aún no se transmiten
     * porque el paso 2 no las consume; se mantienen como parámetros para
     * reflejar la firma del callback `onContinue` y servir de referencia
     * cuando exista la BD.
     */
    private fun openPaymentMethod(
        address: ShippingAddress,
        @Suppress("UNUSED_PARAMETER") instructions: String,
        @Suppress("UNUSED_PARAMETER") recipient: RecipientOption
    ) {
        val shippingAddressLine = "${address.addressLine}, ${address.city.substringBefore(",")}"
        val intent = Intent(this, PaymentMethodActivity::class.java).apply {
            putExtra(PaymentMethodActivity.EXTRA_ROLE_ID, currentRoleId)
            putExtra(PaymentMethodActivity.EXTRA_FORMATTED_TOTAL, formattedSubtotal)
            putExtra(PaymentMethodActivity.EXTRA_SHIPPING_ADDRESS_LINE, shippingAddressLine)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG: String = "ShippingAddressActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /** Clave del extra con el subtotal del carrito ya formateado. */
        const val EXTRA_FORMATTED_SUBTOTAL: String = "extra_formatted_subtotal"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1

        private const val DEFAULT_SUBTOTAL: String = "$146.000"
    }
}

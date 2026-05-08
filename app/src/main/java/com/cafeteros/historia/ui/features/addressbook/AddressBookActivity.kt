package com.cafeteros.historia.ui.features.addressbook

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la pantalla "Mis Direcciones".
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * la libreta de direcciones forma parte del flujo de compra. Se abre
 * desde el enlace "Gestionar" del paso 1 del checkout.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat.
 *
 * Cuando se monten las llamadas a la BD/backend:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. `getAddresses(userId, roleId = currentRoleId)` carga las direcciones
 *     reales y los callbacks (predeterminada/editar/eliminar/agregar)
 *     llaman a las funciones equivalentes del backend.
 *
 * Toda la UI vive en [AddressBookScreen]; aquí solo conectamos los
 * callbacks (back, agregar, editar) con Toasts/finish mientras los
 * demás flujos no existen.
 */
class AddressBookActivity : ComponentActivity() {

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
                "Rol $incomingRoleId no soportado en AddressBookActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        Log.d(TAG, "AddressBookActivity iniciada con roleId=$currentRoleId")

        setContent {
            CafeterosTheme {
                AddressBookScreen(
                    onBack = ::finish,
                    onAddAddress = { toast("Agregar dirección próximamente") },
                    onEditAddress = { address -> toast("Editar ${address.addressLine}") }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG: String = "AddressBookActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1
    }
}

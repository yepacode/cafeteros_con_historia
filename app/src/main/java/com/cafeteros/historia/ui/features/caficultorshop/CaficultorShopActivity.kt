package com.cafeteros.historia.ui.features.caficultorshop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.cart.CartActivity
import com.cafeteros.historia.ui.features.filters.OriginFiltersActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del catálogo "el caficultor".
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * solo el comprador navega el catálogo y ve el empty state cuando sus
 * filtros no devuelven resultados.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat. El campo se guarda en
 *    [currentRoleId] para que, cuando la base de datos esté disponible,
 *    los casos de uso (`loadCatalog(filters, roleId = ...)`,
 *    `loadPopularSuggestions(roleId = ...)`, etc.) lo tomen de aquí.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. `loadCatalog(filters, roleId = currentRoleId)` ya está listo para
 *     parametrizarse con [currentRoleId]; cuando la lista regrese vacía,
 *     la pantalla muestra el empty state.
 *
 * Toda la UI vive en [CaficultorShopScreen]; aquí solo conectamos los
 * callbacks (`onFiltersClick`, `onMenuClick`, `onCartClick`, etc.) con
 * Toasts e Intents mientras los demás flujos no existen aún.
 */
class CaficultorShopActivity : ComponentActivity() {

    /**
     * Rol del usuario activo. Se inicializa desde el Intent extra y se
     * fuerza a [ROLE_ID_COMPRADOR] cuando llega un valor inesperado, porque
     * esta pantalla solo tiene sentido para compradores.
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
                "Rol $incomingRoleId no soportado en CaficultorShopActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        Log.d(TAG, "CaficultorShopActivity iniciada con roleId=$currentRoleId")

        setContent {
            CafeterosTheme {
                CaficultorShopScreen(
                    onMenuClick = ::finish,
                    onCartClick = ::openCart,
                    onFiltersClick = ::openFilters,
                    onClearFilters = { toast("Filtros limpiados") },
                    onSuggestionClick = { suggestion ->
                        toast("${suggestion.title} próximamente")
                    }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Abre la pantalla "Tu Selección" (carrito) al pulsar la bolsa del
     * top bar. Propaga el rol activo.
     */
    private fun openCart() {
        // CartActivity ya no usa extras (lee del CartStore singleton).
        CartActivity.start(this)
    }

    /**
     * Abre la pantalla "Filtros de Origen" para que el usuario ajuste sus
     * filtros y obtenga resultados. Propaga el rol activo.
     */
    private fun openFilters() {
        OriginFiltersActivity.start(this)
    }

    companion object {
        private const val TAG: String = "CaficultorShopActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1
    }
}

package com.cafeteros.historia.ui.features.searchresults

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.filters.OriginFiltersActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la pantalla "Resultados de búsqueda".
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * solo el comprador descubre café y aplica filtros sobre los resultados.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_QUERY]: query inicial con la que se abre la pantalla. Si no
 *    llega, se abre con texto vacío.
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat. El campo se guarda en
 *    [currentRoleId] para que, cuando la base de datos esté disponible,
 *    los casos de uso (`searchProducts(query, filters, roleId = ...)`,
 *    `paginate(roleId = ...)`, etc.) lo tomen de aquí sin tener que
 *    volver a inferirlo.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. `searchProducts(query, filters, roleId = currentRoleId)` ya está
 *     listo para parametrizarse con [currentRoleId].
 *
 * Toda la UI vive en [SearchResultsScreen]; aquí solo conectamos los
 * callbacks (back, abrir filtros, tap producto) con la navegación de
 * Android.
 */
class SearchResultsActivity : ComponentActivity() {

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
                "Rol $incomingRoleId no soportado en SearchResultsActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        val initialQuery = intent.getStringExtra(EXTRA_QUERY).orEmpty().ifBlank { "café huila" }

        Log.d(
            TAG,
            "SearchResultsActivity iniciada con roleId=$currentRoleId, query='$initialQuery'"
        )

        setContent {
            CafeterosTheme {
                SearchResultsScreen(
                    initialQuery = initialQuery,
                    onBack = ::finish,
                    onFiltersClick = ::openFilters,
                    onProductClick = { product ->
                        Toast.makeText(
                            this,
                            "Producto: ${product.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

    /**
     * Abre la pantalla "Filtros de Origen" propagando el rol activo. Reusa
     * la activity ya existente del feature `filters/`.
     */
    private fun openFilters() {
        val intent = Intent(this, OriginFiltersActivity::class.java).apply {
            putExtra(OriginFiltersActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG: String = "SearchResultsActivity"

        /** Clave del extra que transporta la query inicial. */
        const val EXTRA_QUERY: String = "extra_query"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1
    }
}

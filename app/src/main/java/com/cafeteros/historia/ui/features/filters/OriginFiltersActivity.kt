package com.cafeteros.historia.ui.features.filters

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la pantalla "Filtros de Origen".
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * solo el comprador filtra resultados al descubrir café. El caficultor no
 * tiene una experiencia equivalente.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat.
 *  - [EXTRA_RESULTS_COUNT]: número que se muestra dentro del CTA "VER X
 *    RESULTADOS". Cuando se conecte el backend, este valor lo recalcula
 *    el padre con cada cambio de filtros.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. `applyFilters(filters, roleId = currentRoleId)` ya está listo para
 *     parametrizarse con [currentRoleId].
 *
 * Toda la UI vive en [OriginFiltersScreen]; aquí solo conectamos los
 * callbacks (`onClose`, `onApply`) con la navegación de Android.
 */
class OriginFiltersActivity : ComponentActivity() {

    /**
     * Rol del usuario activo. Se inicializa desde el Intent extra y se
     * fuerza a [ROLE_ID_COMPRADOR] cuando llega un valor inesperado, porque
     * esta pantalla solo tiene sentido para compradores. Mientras no exista
     * BD, se conserva en memoria por el ciclo de vida de la Activity.
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
                "Rol $incomingRoleId no soportado en OriginFiltersActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        val resultsCount = intent.getIntExtra(EXTRA_RESULTS_COUNT, DEFAULT_RESULTS_COUNT)

        Log.d(
            TAG,
            "OriginFiltersActivity iniciada con roleId=$currentRoleId, " +
                    "resultsCount=$resultsCount"
        )

        setContent {
            CafeterosTheme {
                OriginFiltersScreen(
                    resultsCount = resultsCount,
                    onClose = ::finish,
                    onApply = { filters ->
                        Toast.makeText(
                            this,
                            "Aplicar filtros: ${filters.selectedZones.size} zonas, " +
                                    "${filters.selectedFlavors.size} sabores",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        private const val TAG: String = "OriginFiltersActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /** Clave del extra con el conteo de resultados que el padre ya conoce. */
        const val EXTRA_RESULTS_COUNT: String = "extra_results_count"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         * Se duplica como constante aquí para que esta pantalla pueda
         * defaultearlo sin importar el módulo de auth.
         */
        const val ROLE_ID_COMPRADOR: Int = 1

        private const val DEFAULT_RESULTS_COUNT: Int = 47
    }
}

package com.cafeteros.historia.ui.features.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.caficultordetail.CaficultorDetailActivity
import com.cafeteros.historia.ui.features.coffeemap.CoffeeMapActivity
import com.cafeteros.historia.ui.features.searchresults.SearchResultsActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la pantalla de búsqueda.
 *
 * Esta pantalla es **exclusiva del rol Comprador** ([ROLE_ID_COMPRADOR] = 1):
 * el caficultor no descubre café, así que no debería aterrizar aquí.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: rol del usuario activo. SIEMPRE debe ser
 *    [ROLE_ID_COMPRADOR] (1) — si llega otro valor, esta activity lo
 *    fuerza a comprador y lo registra en logcat. El campo se guarda en
 *    [currentRoleId] para que, cuando la base de datos esté disponible,
 *    los casos de uso (`searchCoffee(query, roleId = ...)`,
 *    `loadRecentSearches(userId, roleId = ...)`, etc.) lo tomen de aquí
 *    sin tener que volver a inferirlo.
 *
 * Toda la UI vive en [SearchScreen]; aquí solo conectamos los callbacks
 * vía Toasts e Intents (mapa cafetero / detalle de caficultor) mientras
 * los demás flujos (búsqueda real, filtros) son las únicas pantallas
 * pendientes.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. Las llamadas tipo `searchCoffee(query, roleId = roleId)` ya están
 *     listas para parametrizarse con [currentRoleId].
 */
class SearchActivity : ComponentActivity() {

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
                "Rol $incomingRoleId no soportado en SearchActivity; " +
                        "se fuerza a ROLE_ID_COMPRADOR=$ROLE_ID_COMPRADOR."
            )
            ROLE_ID_COMPRADOR
        }

        Log.d(TAG, "SearchActivity iniciada con roleId=$currentRoleId")

        setContent {
            CafeterosTheme {
                SearchScreen(
                    onBack = ::finish,
                    onMicClick = { toast("Búsqueda por voz próximamente") },
                    onSubmitQuery = ::openResults,
                    onCategoryClick = ::handleCategoryClick,
                    onCaficultorClick = { caficultor ->
                        openCaficultorDetail(caficultor.displayName)
                    }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Cada categoría del grid "Explora por" tiene un destino diferente; hoy
     * solo "ZONA" y "PERFIL_SABOR" tienen pantalla real (mapa cafetero), las
     * demás se muestran como Toast hasta que sus pantallas existan.
     */
    private fun handleCategoryClick(
        category: com.cafeteros.historia.ui.features.search.model.SearchCategory
    ) {
        when (category) {
            com.cafeteros.historia.ui.features.search.model.SearchCategory.ZONA -> {
                val intent = Intent(this, CoffeeMapActivity::class.java).apply {
                    putExtra(CoffeeMapActivity.EXTRA_ROLE_ID, currentRoleId)
                }
                startActivity(intent)
            }
            else -> toast("${category.title} próximamente")
        }
    }

    /**
     * Abre la pantalla de resultados de búsqueda con la query como extra.
     * Se invoca cuando el usuario toca un reciente, una tendencia o
     * confirma con Enter en el input.
     */
    private fun openResults(query: String) {
        val intent = Intent(this, SearchResultsActivity::class.java).apply {
            putExtra(SearchResultsActivity.EXTRA_QUERY, query)
            putExtra(SearchResultsActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    /**
     * Abre el detalle del caficultor cuando el usuario pulsa un avatar de
     * "Caficultores destacados". Reusa la activity ya existente.
     */
    private fun openCaficultorDetail(name: String) {
        val intent = Intent(this, CaficultorDetailActivity::class.java).apply {
            putExtra(CaficultorDetailActivity.EXTRA_CAFICULTOR_NAME, name)
            putExtra(CaficultorDetailActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG: String = "SearchActivity"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         * Se duplica como constante aquí para que esta pantalla pueda
         * defaultearlo sin importar el módulo de auth.
         */
        const val ROLE_ID_COMPRADOR: Int = 1
    }
}

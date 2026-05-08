package com.cafeteros.historia.ui.features.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.caficultordetail.CaficultorDetailActivity
import com.cafeteros.historia.ui.features.caficultorshop.CaficultorShopActivity
import com.cafeteros.historia.ui.features.cart.CartActivity
import com.cafeteros.historia.ui.features.coffeemap.CoffeeMapActivity
import com.cafeteros.historia.ui.features.filters.OriginFiltersActivity
import com.cafeteros.historia.ui.features.search.SearchActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de la home del comprador.
 *
 * Toda la UI vive en [ExploreScreen]; esta clase solo conecta los callbacks
 * de la pantalla a comportamientos de Android (Toast por ahora, navegación
 * real cuando existan los detalles de zona, caficultor, historia y producto).
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_USER_NAME]: primer nombre del comprador para personalizar el saludo.
 *  - [EXTRA_ROLE_ID]: identificador numérico del rol con el que se registró.
 *    Para esta pantalla SIEMPRE debe ser [ROLE_ID_COMPRADOR] (1). El campo
 *    queda guardado en [currentRoleId] para que, cuando la base de datos
 *    esté disponible, los casos de uso (cargar productos por rol, registrar
 *    pedidos, etc.) puedan tomarlo de aquí sin tener que volver a inferirlo.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. POST /auth/register devuelve `{ token, user_id, role_id }`.
 *  2. Esa response se persiste en una `UserSession` (DataStore / Room).
 *  3. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 */
class ExploreActivity : ComponentActivity() {

    /**
     * Rol del usuario activo. Se inicializa desde el Intent extra y, mientras
     * no exista BD, se conserva en memoria por el ciclo de vida de la
     * Activity. Pensado para ser leído por futuros casos de uso.
     */
    private var currentRoleId: Int = ROLE_ID_COMPRADOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userName = intent.getStringExtra(EXTRA_USER_NAME)?.takeIf { it.isNotBlank() }
            ?: DEFAULT_USER_NAME
        currentRoleId = intent.getIntExtra(EXTRA_ROLE_ID, ROLE_ID_COMPRADOR)

        Log.d(TAG, "ExploreActivity iniciada para usuario='$userName', roleId=$currentRoleId")

        setContent {
            CafeterosTheme {
                ExploreScreen(
                    userName = userName,
                    onMenuClick = ::openCaficultorShop,
                    onNotificationsClick = { toast("No tienes notificaciones nuevas") },
                    onFilterClick = ::openFilters,
                    onSearchClick = ::openSearch,
                    onCartTabClick = ::openCart,
                    onSeeAllRegions = { openCoffeeMap() },
                    onSeeAllCaficultores = { openCaficultorDetail("Don Alberto") },
                    onRegionClick = { toast("Zona ${it.name}") },
                    onCaficultorClick = { openCaficultorDetail(it.farmName) },
                    onStoryClick = { toast(it.title) },
                    onProductClick = { toast(it.name) }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Abre la pantalla dedicada de búsqueda al pulsar la barra de búsqueda
     * de la home. Propaga el [currentRoleId] para mantener la cadena del
     * rol activo.
     */
    private fun openSearch() {
        val intent = Intent(this, SearchActivity::class.java).apply {
            putExtra(SearchActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    /**
     * Abre el catálogo "el caficultor" desde el botón hamburguesa del top
     * bar. Mientras no exista un menú lateral real, ese botón es el atajo
     * más natural a la tienda. Propaga el [currentRoleId].
     */
    private fun openCaficultorShop() {
        val intent = Intent(this, CaficultorShopActivity::class.java).apply {
            putExtra(CaficultorShopActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    /**
     * Abre la pantalla "Tu Selección" (carrito) al pulsar el tab CARRITO
     * del bottom bar. Propaga el [currentRoleId].
     */
    private fun openCart() {
        val intent = Intent(this, CartActivity::class.java).apply {
            putExtra(CartActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    /**
     * Abre la pantalla "Filtros de Origen" al pulsar el botón verde de
     * filtros junto al buscador. Propaga el [currentRoleId] para mantener
     * la cadena del rol activo.
     */
    private fun openFilters() {
        val intent = Intent(this, OriginFiltersActivity::class.java).apply {
            putExtra(OriginFiltersActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    /**
     * Abre la pantalla del Mapa Cafetero al pulsar "Ver mapa" en la home.
     * Propaga el [currentRoleId] como extra para que la siguiente activity
     * conozca el rol del usuario activo sin tener que reconsultarlo.
     */
    private fun openCoffeeMap() {
        val intent = Intent(this, CoffeeMapActivity::class.java).apply {
            putExtra(CoffeeMapActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    /**
     * Abre el detalle del caficultor a partir de un identificador legible
     * (su nombre o el de su finca). Propaga el rol activo igual que el
     * resto de navegaciones.
     *
     * Hoy lo invocan tanto el callback "Ver todos" del header de
     * "Caficultores destacados" como el toque sobre cada card individual,
     * porque aún no existe una pantalla intermedia con la lista completa.
     * Cuando esa lista se construya, "Ver todos" se reenruta y "tap card"
     * sigue cayendo aquí.
     */
    private fun openCaficultorDetail(caficultorName: String) {
        val intent = Intent(this, CaficultorDetailActivity::class.java).apply {
            putExtra(CaficultorDetailActivity.EXTRA_CAFICULTOR_NAME, caficultorName)
            putExtra(CaficultorDetailActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG: String = "ExploreActivity"

        /** Clave del extra que transporta el primer nombre del comprador. */
        const val EXTRA_USER_NAME: String = "extra_user_name"

        /** Clave del extra que transporta el roleId del usuario que se acaba de registrar. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /**
         * Identificador del rol "Comprador" en la base de datos. Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         * Se duplica como constante aquí para que esta pantalla pueda
         * defaultearlo sin importar el módulo de auth.
         */
        const val ROLE_ID_COMPRADOR: Int = 1

        private const val DEFAULT_USER_NAME = "Mich"
    }
}

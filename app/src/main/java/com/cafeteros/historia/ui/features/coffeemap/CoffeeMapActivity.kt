package com.cafeteros.historia.ui.features.coffeemap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.coffeemap.model.CoffeeZone
import com.cafeteros.historia.ui.features.zonedetail.ZoneDetailActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del Mapa Cafetero.
 *
 * Toda la UI vive en [CoffeeMapScreen]; aquí solo se conecta la navegación.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ROLE_ID]: identificador numérico del rol del usuario que abrió
 *    el mapa. En esta app el mapa está asociado al rol Comprador
 *    ([ROLE_ID_COMPRADOR] = 1); el caficultor tiene su propia experiencia.
 *    El campo se guarda en [currentRoleId] para que, cuando la base de datos
 *    esté disponible, los casos de uso (filtrar zonas según rol, registrar
 *    interés en una zona, etc.) lo tomen de aquí sin tener que volver a
 *    inferirlo.
 *
 * Cuando se monten las llamadas a la BD/backend, el flujo esperado será:
 *  1. La sesión activa expone `roleId` desde una `UserSession` persistente.
 *  2. Esta activity deja de leer el rol del Intent y lo toma de la sesión.
 *  3. Las llamadas tipo `loadZones(roleId = roleId)` ya están listas para
 *     parametrizarse con [currentRoleId].
 */
class CoffeeMapActivity : ComponentActivity() {

    /**
     * Rol del usuario activo. Se inicializa desde el Intent extra y, mientras
     * no exista BD, se conserva en memoria por el ciclo de vida de la
     * Activity. Pensado para ser leído por futuros casos de uso.
     */
    private var currentRoleId: Int = ROLE_ID_COMPRADOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        currentRoleId = intent.getIntExtra(EXTRA_ROLE_ID, ROLE_ID_COMPRADOR)
        Log.d(TAG, "CoffeeMapActivity iniciada con roleId=$currentRoleId")

        setContent {
            CafeterosTheme {
                CoffeeMapScreen(
                    onBack = ::finish,
                    onFilter = { toast("Filtros del mapa próximamente") },
                    onZoneClick = ::openZoneDetail
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Abre el detalle de una zona al pulsar su marcador en el mapa.
     * Propaga el [currentRoleId] como extra para que la cadena del rol del
     * usuario activo se mantenga hasta la nueva activity.
     */
    private fun openZoneDetail(zone: CoffeeZone) {
        val intent = Intent(this, ZoneDetailActivity::class.java).apply {
            putExtra(ZoneDetailActivity.EXTRA_ZONE_NAME, zone.name)
            putExtra(ZoneDetailActivity.EXTRA_ROLE_ID, currentRoleId)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG: String = "CoffeeMapActivity"

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

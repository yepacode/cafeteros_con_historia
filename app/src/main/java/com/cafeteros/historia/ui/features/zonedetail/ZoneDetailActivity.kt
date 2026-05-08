package com.cafeteros.historia.ui.features.zonedetail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cafeteros.historia.ui.features.zonedetail.model.ZoneDetailSampleData
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora del detalle de una zona cafetera.
 *
 * Recibe por extras del Intent:
 *  - [EXTRA_ZONE_NAME]: nombre canónico de la zona ("Santander", "Huila"…).
 *    Se usa para resolver el [com.cafeteros.historia.ui.features.zonedetail.model.ZoneDetail]
 *    desde [ZoneDetailSampleData.forName]. Si no llega, se cae a "Santander".
 *  - [EXTRA_ROLE_ID]: identificador del rol activo (Comprador = 1). Se
 *    propaga desde [com.cafeteros.historia.ui.features.coffeemap.CoffeeMapActivity]
 *    para mantener la cadena del rol cuando se conecte la BD.
 *
 * Toda la UI vive en [ZoneDetailScreen]; aquí solo se conecta la navegación.
 * Los toques sobre caficultores y CTAs muestran un Toast por ahora hasta
 * que existan las pantallas correspondientes.
 */
class ZoneDetailActivity : ComponentActivity() {

    /** Rol del usuario activo, leído del Intent y reservado para casos de uso futuros. */
    private var currentRoleId: Int = ROLE_ID_COMPRADOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val zoneName = intent.getStringExtra(EXTRA_ZONE_NAME)?.takeIf { it.isNotBlank() }
            ?: DEFAULT_ZONE_NAME
        currentRoleId = intent.getIntExtra(EXTRA_ROLE_ID, ROLE_ID_COMPRADOR)

        Log.d(TAG, "ZoneDetailActivity iniciada para zone='$zoneName', roleId=$currentRoleId")

        val zoneDetail = ZoneDetailSampleData.forName(zoneName)

        setContent {
            CafeterosTheme {
                ZoneDetailScreen(
                    zoneDetail = zoneDetail,
                    onBack = ::finish,
                    onShare = { toast("Compartir ${zoneDetail.name}") },
                    onCaficultorClick = { toast(it.displayName) },
                    onDiscoverChronicles = { toast("Crónicas de ${zoneDetail.name}") }
                )
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG: String = "ZoneDetailActivity"

        /** Clave del extra que transporta el nombre de la zona a mostrar. */
        const val EXTRA_ZONE_NAME: String = "extra_zone_name"

        /** Clave del extra que transporta el roleId del usuario activo. */
        const val EXTRA_ROLE_ID: String = "extra_role_id"

        /**
         * Identificador del rol "Comprador". Espejo de
         * [com.cafeteros.historia.ui.features.auth.components.UserType.COMPRADOR.roleId].
         */
        const val ROLE_ID_COMPRADOR: Int = 1

        private const val DEFAULT_ZONE_NAME: String = "Santander"
    }
}

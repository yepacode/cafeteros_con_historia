package com.cafeteros.historia.data.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

/**
 * Estructura simple con los componentes de una dirección que sí usa la UI.
 * Evita exponer [Address] a las capas superiores y nos protege ante
 * variaciones por país (Geocoder devuelve campos `null` con frecuencia).
 */
data class ResolvedAddress(
    val fullLine: String,
    val locality: String?,
    val adminArea: String?,
    val countryName: String?
)

/**
 * Convierte coordenadas en una dirección legible usando el [Geocoder] del
 * sistema. NO requiere API key de Google — funciona con los datos del
 * dispositivo (la mayoría de Androids 7+ tienen el servicio).
 *
 * **Compatibilidad de API.** Antes de Android 13 (`TIRAMISU`) la API es
 * sincrónica y bloqueante. Desde 13, Google recomienda la versión con
 * callback para evitar ANRs. Esta clase elige la implementación correcta y
 * expone la misma función suspend en ambos casos.
 *
 * **Errores comunes.** En zonas rurales o con falta de internet, el
 * `Geocoder` puede devolver lista vacía. Por eso siempre devolvemos
 * `null` en lugar de lanzar — el llamador decide si pedir al usuario que
 * escriba la dirección manualmente.
 */
class GeocoderHelper(context: Context) {

    private val appContext = context.applicationContext
    private val geocoder: Geocoder = Geocoder(appContext, Locale("es", "CO"))

    /**
     * Resuelve [latitude]/[longitude] a una [ResolvedAddress] o `null` si
     * el sistema no pudo determinar la dirección.
     */
    suspend fun reverse(latitude: Double, longitude: Double): ResolvedAddress? {
        if (!Geocoder.isPresent()) return null
        val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            reverseAsync(latitude, longitude)
        } else {
            reverseLegacy(latitude, longitude)
        }
        val first = addresses.firstOrNull() ?: return null
        return first.toResolvedAddress()
    }

    /** API moderna (Android 13+) basada en callback. */
    private suspend fun reverseAsync(lat: Double, lng: Double): List<Address> =
        suspendCancellableCoroutine { cont ->
            try {
                geocoder.getFromLocation(lat, lng, MAX_RESULTS) { results ->
                    cont.resume(results)
                }
            } catch (error: Throwable) {
                // Cualquier fallo lo tratamos como "sin resultados".
                cont.resume(emptyList())
            }
        }

    /** API clásica (Android <= 12) bloqueante. */
    @Suppress("DEPRECATION")
    private fun reverseLegacy(lat: Double, lng: Double): List<Address> = try {
        geocoder.getFromLocation(lat, lng, MAX_RESULTS).orEmpty()
    } catch (error: Throwable) {
        emptyList()
    }

    /**
     * Construye una línea legible "Calle X #..., Pereira, Risaralda" a partir
     * del [Address] crudo. Filtra strings nulos para no terminar con comas
     * dobles ni espacios al inicio.
     */
    private fun Address.toResolvedAddress(): ResolvedAddress {
        val lineParts = (0..maxAddressLineIndex)
            .mapNotNull { getAddressLine(it) }
            .filter { it.isNotBlank() }
        val composedLine = if (lineParts.isNotEmpty()) {
            lineParts.joinToString(separator = ", ")
        } else {
            // Fallback: arma una línea con los componentes individuales.
            listOfNotNull(thoroughfare, subLocality, locality, adminArea, countryName)
                .joinToString(", ")
        }
        return ResolvedAddress(
            fullLine = composedLine,
            locality = locality,
            adminArea = adminArea,
            countryName = countryName
        )
    }

    private companion object {
        const val MAX_RESULTS = 1
    }
}

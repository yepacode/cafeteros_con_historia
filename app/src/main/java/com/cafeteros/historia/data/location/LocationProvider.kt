package com.cafeteros.historia.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

/**
 * Resultado de una solicitud de ubicación actual.
 *
 * Se modela como `sealed` para que la UI haga `when` exhaustivo y muestre
 * el mensaje adecuado en cada caso (permisos denegados, GPS apagado, etc.).
 */
sealed interface LocationResult {
    /** Coordenadas obtenidas con éxito. */
    data class Success(val latitude: Double, val longitude: Double) : LocationResult

    /** El usuario no concedió ni FINE ni COARSE_LOCATION. */
    data object PermissionDenied : LocationResult

    /** Permisos OK pero el sistema no devolvió ubicación (GPS apagado, sin señal…). */
    data object Unavailable : LocationResult

    /** Error inesperado del SDK; lleva el mensaje original para logs. */
    data class Error(val message: String) : LocationResult
}

/**
 * Wrapper de [FusedLocationProviderClient] que esconde:
 *  - La verificación de permisos antes de llamar al SDK (de lo contrario
 *    [SecurityException] sería el primer aviso, lo cual es feo).
 *  - El cambio de API entre versiones de Google Play Services.
 *
 * Uso típico:
 * ```
 * val provider = LocationProvider(context)
 * when (val result = provider.getCurrentLocation()) {
 *     is LocationResult.Success -> { /* usar lat/lng */ }
 *     LocationResult.PermissionDenied -> requestRuntimePermission()
 *     LocationResult.Unavailable -> showToast("Activa el GPS")
 *     is LocationResult.Error -> showToast(result.message)
 * }
 * ```
 *
 * **Nota sobre precisión.** Se solicita prioridad
 * [Priority.PRIORITY_BALANCED_POWER_ACCURACY] — suficiente para asociar una
 * dirección de entrega y más amable con la batería que GPS de alta precisión.
 */
class LocationProvider(context: Context) {

    private val appContext = context.applicationContext
    private val client: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    /**
     * Solicita la ubicación actual del dispositivo. Suspende hasta que el
     * sistema responde o falla.
     */
    @SuppressLint("MissingPermission") // chequeo manual abajo
    suspend fun getCurrentLocation(): LocationResult {
        if (!hasAnyLocationPermission()) {
            return LocationResult.PermissionDenied
        }
        return runCatching {
            val request = CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMaxUpdateAgeMillis(15_000L)
                .build()
            val location = client.getCurrentLocation(request, null).await()
            if (location == null) {
                LocationResult.Unavailable
            } else {
                LocationResult.Success(location.latitude, location.longitude)
            }
        }.getOrElse { error ->
            LocationResult.Error(error.message ?: "Error desconocido al leer la ubicación")
        }
    }

    /** True si el usuario concedió al menos uno de los dos permisos de ubicación. */
    private fun hasAnyLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            appContext, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            appContext, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }
}

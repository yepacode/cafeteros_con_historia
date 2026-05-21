package com.cafeteros.historia.ui.components

import android.graphics.drawable.ColorDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.cafeteros.historia.ui.theme.BrandColors
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

/**
 * Selector de ubicación basado en OpenStreetMap (osmdroid).
 *
 * El caficultor toca cualquier punto del mapa para marcar la ubicación de
 * su finca. El marcador se reubica al punto tocado y se notifica el
 * `GeoPoint` resultante via [onLocationPicked].
 *
 * **Por qué no usamos Google Maps:** evitar la dependencia de la API key
 * de Google y el costo de Maps Tile Service en producción. osmdroid tira
 * tiles directamente de OpenStreetMap, gratis para uso razonable.
 *
 * **Comportamiento:**
 *  - Centra el mapa en [initialLat]/[initialLng] al primer dibujo.
 *  - Si no hay coordenadas iniciales, centra en Colombia (Eje Cafetero).
 *  - Tap en cualquier punto del mapa → recoloca el marcador y dispara
 *    [onLocationPicked].
 *  - El zoom inicial es 12 (suficiente para ver el municipio entero).
 *
 * @param initialLat latitud del marcador inicial. Si es null, no se pinta
 *  marcador hasta que el usuario toque por primera vez.
 * @param initialLng longitud del marcador inicial. Idem.
 * @param onLocationPicked callback con las nuevas coordenadas al tap.
 * @param modifier modifier típico (controla tamaño/forma del contenedor).
 */
@Composable
fun MapLocationPicker(
    initialLat: Double?,
    initialLng: Double?,
    onLocationPicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // osmdroid necesita inicializar su config (user agent y carpeta de cache)
    // antes de inflar cualquier MapView. Lo hacemos perezosamente la primera
    // vez que este composable entra en composición.
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", android.content.Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = context.packageName
    }

    // Centro por defecto: Pereira (corazón del Eje Cafetero colombiano).
    val defaultCenter = remember { GeoPoint(4.8133, -75.6961) }
    val startPoint = remember(initialLat, initialLng) {
        if (initialLat != null && initialLng != null) {
            GeoPoint(initialLat, initialLng)
        } else {
            defaultCenter
        }
    }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            isHorizontalMapRepetitionEnabled = false
            isVerticalMapRepetitionEnabled = false
            // Fondo neutro mientras cargan los tiles.
            background = ColorDrawable(BrandColors.MapCanvasBackground.toArgb())
        }
    }

    // Adapta el ciclo de vida del MapView al de la pantalla (necesario para
    // que osmdroid libere el caché de tiles cuando la activity se va a fondo).
    DisposableLifecycleEffect(lifecycleOwner) { event ->
        when (event) {
            androidx.lifecycle.Lifecycle.Event.ON_RESUME -> mapView.onResume()
            androidx.lifecycle.Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            androidx.lifecycle.Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
            else -> Unit
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { view ->
            view.controller.setZoom(if (initialLat != null) 14.0 else 6.5)
            view.controller.setCenter(startPoint)

            // Borrar overlays previos para no acumular marcadores entre recomposiciones.
            view.overlays.clear()

            // Marcador inicial (si tenemos coordenadas guardadas).
            val marker = Marker(view).apply {
                position = startPoint
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = "Tu finca"
            }
            if (initialLat != null && initialLng != null) {
                view.overlays.add(marker)
            }

            // Receptor de taps: cada vez que se toca el mapa, mueve el
            // marcador a ese punto y notifica al composable padre.
            val events = object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                    marker.position = p
                    if (marker !in view.overlays) view.overlays.add(marker)
                    view.invalidate()
                    onLocationPicked(p.latitude, p.longitude)
                    return true
                }

                override fun longPressHelper(p: GeoPoint): Boolean = false
            }
            view.overlays.add(0, MapEventsOverlay(events))
            view.invalidate()
        }
    )

}

/**
 * Helper que enlaza un [LifecycleOwner] al composable y emite eventos. No
 * existe en Compose por defecto (los `DisposableEffect` con `LifecycleObserver`
 * se repiten en varias pantallas); centralizar reduce ruido.
 */
@Composable
private fun DisposableLifecycleEffect(
    lifecycleOwner: LifecycleOwner,
    onEvent: (androidx.lifecycle.Lifecycle.Event) -> Unit
) {
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                onEvent(androidx.lifecycle.Lifecycle.Event.ON_RESUME)
            }
            override fun onPause(owner: LifecycleOwner) {
                onEvent(androidx.lifecycle.Lifecycle.Event.ON_PAUSE)
            }
            override fun onDestroy(owner: LifecycleOwner) {
                onEvent(androidx.lifecycle.Lifecycle.Event.ON_DESTROY)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

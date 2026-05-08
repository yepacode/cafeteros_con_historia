package com.cafeteros.historia.ui.features.coffeemap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.coffeemap.model.CoffeeZone
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Lienzo ilustrado del mapa cafetero. Coloca cada [ZoneMarker] en su
 * `normalizedPosition (x, y)` dentro del área disponible.
 *
 * Como el diseño NO usa un mapa geográfico real (no hay calles, terreno ni
 * etiquetas), este contenedor es solo un [Box] con fondo beige y los
 * marcadores posicionados con `offset` proporcional al tamaño medido.
 *
 * Cuando se quiera saltar a un mapa real (Google Maps / MapBox), basta con
 * reemplazar este composable por el contenedor del SDK correspondiente y
 * pasar las latitudes/longitudes de [CoffeeZone] como `LatLng` — el resto
 * de la pantalla no cambia.
 *
 * @param modifier modifier opcional aplicado al contenedor raíz.
 * @param zones zonas a posicionar.
 * @param selectedZone zona actualmente seleccionada (afecta el feedback
 *  visual de su marcador). null = ninguna seleccionada.
 * @param onZoneClick callback al tocar un marcador específico.
 */
@Composable
fun IllustratedMapArea(
    modifier: Modifier = Modifier,
    zones: List<CoffeeZone>,
    selectedZone: CoffeeZone?,
    onZoneClick: (CoffeeZone) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.MapCanvasBackground)
    ) {
        val density = LocalDensity.current
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val maxHeightPx = with(density) { maxHeight.toPx() }

        zones.forEach { zone ->
            val (xRatio, yRatio) = zone.normalizedPosition
            val xDp = with(density) { (maxWidthPx * xRatio).toDp() }
            val yDp = with(density) { (maxHeightPx * yRatio).toDp() }

            // Centramos visualmente el marcador en (xDp, yDp): el círculo
            // mide 60dp y el badge sobresale ~8dp a la derecha, así que
            // restamos ~36dp en X y 30dp en Y para que el centro del
            // círculo coincida con la posición teórica de la zona.
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = xDp - 36.dp, y = yDp - 30.dp)
            ) {
                ZoneMarker(
                    zone = zone,
                    isSelected = zone == selectedZone,
                    onClick = { onZoneClick(zone) }
                )
            }
        }
    }
}

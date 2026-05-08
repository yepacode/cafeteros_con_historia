package com.cafeteros.historia.ui.features.coffeemap.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Zona cafetera del país representada como burbuja en el mapa.
 *
 * El campo [normalizedPosition] guarda la posición del marcador dentro del
 * lienzo del mapa como dos floats en `0.0..1.0` (x = horizontal, y = vertical).
 * Esto permite que las posiciones del diseño se mantengan correctas
 * independientemente del tamaño físico de la pantalla.
 *
 * Los campos [latitude] y [longitude] están listos para cuando se integre un
 * mapa real (Google Maps / MapBox): el contenedor visual cambiaría, pero el
 * resto del modelo (color, ícono, conteo, intensidad) se reutiliza tal cual.
 *
 * @property name nombre visible bajo el marcador ("SIERRA NEVADA").
 * @property caficultorCount conteo de caficultores en la zona, mostrado en
 *  el badge oscuro arriba-derecha del marcador.
 * @property color color identitario de la zona; el círculo del marcador y la
 *  pill resumen en el bottom sheet usan este mismo color.
 * @property icon ícono Material que se dibuja dentro del círculo del marcador.
 * @property normalizedPosition coordenadas (x, y) en `0.0..1.0` del lienzo.
 * @property intensity nivel de producción para el degradado de la leyenda.
 * @property latitude coordenada geográfica real (preparada para mapa real).
 * @property longitude coordenada geográfica real (preparada para mapa real).
 */
data class CoffeeZone(
    val name: String,
    val caficultorCount: Int,
    val color: Color,
    val icon: ImageVector,
    val normalizedPosition: Pair<Float, Float>,
    val intensity: ProductionIntensity,
    val latitude: Double,
    val longitude: Double
)

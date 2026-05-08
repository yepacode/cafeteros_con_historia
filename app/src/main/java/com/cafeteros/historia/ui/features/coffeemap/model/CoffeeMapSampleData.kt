package com.cafeteros.historia.ui.features.coffeemap.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Waves
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Data de muestra del mapa cafetero mientras no exista backend.
 *
 * Las posiciones [CoffeeZone.normalizedPosition] están escogidas para
 * reproducir el layout del diseño (Sierra Nevada arriba, Eje Cafetero al
 * centro-izquierda, Cundinamarca a la derecha del Eje, Santander arriba a
 * la derecha y Huila abajo).
 *
 * Las latitudes/longitudes son aproximaciones reales de cada zona en
 * Colombia para que el día que se conecte un mapa geográfico (Google Maps,
 * MapBox), los marcadores caigan donde corresponde sin tener que volver a
 * tocar el modelo.
 */
object CoffeeMapSampleData {

    val zones: List<CoffeeZone> = listOf(
        CoffeeZone(
            name = "SIERRA NEVADA",
            caficultorCount = 12,
            color = BrandColors.ZoneSierraNevada,
            icon = Icons.Filled.Waves,
            normalizedPosition = 0.50f to 0.10f,
            intensity = ProductionIntensity.BAJA,
            latitude = 10.83,
            longitude = -73.68
        ),
        CoffeeZone(
            name = "SANTANDER",
            caficultorCount = 38,
            color = BrandColors.ZoneSantander,
            icon = Icons.Filled.Landscape,
            normalizedPosition = 0.70f to 0.30f,
            intensity = ProductionIntensity.MEDIA,
            latitude = 6.65,
            longitude = -73.13
        ),
        CoffeeZone(
            name = "EJE CAFETERO",
            caficultorCount = 84,
            color = BrandColors.ZoneEjeCafetero,
            icon = Icons.Filled.Coffee,
            normalizedPosition = 0.32f to 0.50f,
            intensity = ProductionIntensity.ALTA,
            latitude = 5.07,
            longitude = -75.52
        ),
        CoffeeZone(
            name = "CUNDINAMARCA",
            caficultorCount = 29,
            color = BrandColors.ZoneCundinamarca,
            icon = Icons.Filled.Spa,
            normalizedPosition = 0.65f to 0.58f,
            intensity = ProductionIntensity.MEDIA,
            latitude = 4.71,
            longitude = -74.07
        ),
        CoffeeZone(
            name = "HUILA",
            caficultorCount = 56,
            color = BrandColors.ZoneHuila,
            icon = Icons.Filled.EmojiEvents,
            normalizedPosition = 0.40f to 0.70f,
            intensity = ProductionIntensity.ALTA,
            latitude = 2.93,
            longitude = -75.28
        )
    )

    /** Filtros expuestos al usuario en la barra horizontal del bottom sheet. */
    val filters: List<MapFilter> = MapFilter.entries
}

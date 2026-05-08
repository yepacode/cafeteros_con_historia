package com.cafeteros.historia.ui.features.zonedetail.model

import androidx.annotation.DrawableRes

/**
 * Modelo agregado con todo lo que necesita la pantalla [com.cafeteros.historia.ui.features.zonedetail.ZoneDetailScreen].
 *
 * Se construye a partir de los datos básicos de la zona (que ya viven en el
 * mapa) más los campos editoriales/cualitativos: subtítulo poético, altitud
 * promedio, calificación, perfil de sabor, párrafo descriptivo, listado de
 * municipios y caficultores destacados.
 *
 * Cuando exista BD, este modelo se va a poblar desde un caso de uso tipo
 * `GetZoneDetailUseCase(zoneName)`; los datos de muestra de
 * [ZoneDetailSampleData] son solo un placeholder para que el front se
 * pueda probar end-to-end sin backend.
 *
 * @property name nombre canónico ("Santander").
 * @property tagline subtítulo en itálica ("Café de altura, carácter fuerte").
 * @property heroImageRes foto de portada de la zona.
 * @property caficultorCount total para el primer stat.
 * @property averageAltitudeText altitud ya formateada ("1.400m").
 * @property averageRating valor numérico (0–5) usado en el tercer stat.
 * @property flavorTags pills de notas de sabor.
 * @property flavorTraits filas del slider de perfil de sabor.
 * @property description párrafo descriptivo.
 * @property municipalities lista para los chips horizontales.
 * @property caficultores grid 2-col en la parte baja.
 */
data class ZoneDetail(
    val name: String,
    val tagline: String,
    @param:DrawableRes val heroImageRes: Int,
    val caficultorCount: Int,
    val averageAltitudeText: String,
    val averageRating: Double,
    val flavorTags: List<String>,
    val flavorTraits: List<FlavorTrait>,
    val description: String,
    val municipalities: List<Municipality>,
    val caficultores: List<ZoneCaficultor>
)

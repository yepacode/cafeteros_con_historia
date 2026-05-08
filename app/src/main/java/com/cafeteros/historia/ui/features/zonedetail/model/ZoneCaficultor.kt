package com.cafeteros.historia.ui.features.zonedetail.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Caficultor individual mostrado en la grilla "Caficultores de {zona}".
 *
 * Si [portraitRes] es null se renderiza un avatar placeholder con las
 * iniciales sobre [placeholderColor]. Cuando se descarguen fotos reales,
 * basta con agregarlas al `drawable` y referenciarlas aquí.
 *
 * @property displayName nombre que aparece en grande en la card ("Don Ricardo").
 * @property farmAndLocation segunda línea con finca + municipio en mayúsculas.
 * @property formattedPriceFrom precio inicial ya formateado ("Desde $45.000").
 * @property rating calificación 0.0 – 5.0 mostrada en el badge sobre la foto.
 * @property portraitRes drawable opcional con la foto del caficultor.
 * @property placeholderColor color de respaldo cuando no hay foto.
 */
data class ZoneCaficultor(
    val displayName: String,
    val farmAndLocation: String,
    val formattedPriceFrom: String,
    val rating: Double,
    @param:DrawableRes val portraitRes: Int? = null,
    val placeholderColor: Color
)

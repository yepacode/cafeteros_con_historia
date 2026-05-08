package com.cafeteros.historia.ui.features.zonedetail.model

import androidx.compose.ui.graphics.Color
import com.cafeteros.historia.R

/**
 * Data de muestra del detalle de zona mientras no exista backend.
 *
 * Hoy solo "Santander" tiene contenido editorial completo (es la zona del
 * diseño que recibimos). Cuando se pulse cualquier otra zona del mapa, la
 * pantalla cae a [defaultFor] que reusa la copy y los caficultores de
 * Santander pero ajusta el nombre — útil para que la navegación nunca
 * "rompa" antes de que existan los textos de cada zona.
 */
object ZoneDetailSampleData {

    private val flavorTagsSantander = listOf(
        "Chocolate amargo",
        "Cuerpo fuerte",
        "Notas a nuez"
    )

    private val flavorTraitsSantander = listOf(
        FlavorTrait(name = "ACIDEZ", level = FlavorLevel.BAJA),
        FlavorTrait(name = "CUERPO", level = FlavorLevel.ALTO),
        FlavorTrait(name = "DULZURA", level = FlavorLevel.MEDIA),
        FlavorTrait(name = "AMARGOR", level = FlavorLevel.ALTO)
    )

    private val descriptionSantander = """
        En el corazón de la Cordillera Oriental, el departamento de Santander ha forjado un carácter único en sus granos. A diferencia de las suaves pendientes del Eje Cafetero, aquí el café crece bajo la sombra de imponentes árboles de guamo y cítricos, protegiéndose del sol inclemente del cañón del Chicamocha.

        Municipios como San Gil y El Socorro son cunas de una tradición que privilegia la intensidad. La geografía accidentada obliga a una recolección manual heroica, donde cada cereza es seleccionada con la paciencia que dicta la montaña.

        El resultado es una taza robusta, con notas profundas a chocolate oscuro y una fragancia que recuerda a la tierra húmeda de sus bosques nativos. Es un café para quienes buscan historias de resistencia y autenticidad en cada sorbo.
    """.trimIndent()

    private val municipalitiesSantander = listOf(
        Municipality(name = "San Gil", fincaCount = 12),
        Municipality(name = "Socorro", fincaCount = 8),
        Municipality(name = "Pinchote", fincaCount = 5),
        Municipality(name = "Curití", fincaCount = 7),
        Municipality(name = "Barichara", fincaCount = 6)
    )

    private val caficultoresSantander = listOf(
        ZoneCaficultor(
            displayName = "Don Ricardo",
            farmAndLocation = "FINCA LA ESPERANZA, SAN GIL",
            formattedPriceFrom = "Desde $45.000",
            rating = 4.9,
            portraitRes = R.drawable.ima_1,
            placeholderColor = Color(0xFF8B5A2B)
        ),
        ZoneCaficultor(
            displayName = "Doña Elena",
            farmAndLocation = "EL MIRADOR, SOCORRO",
            formattedPriceFrom = "Desde $42.000",
            rating = 4.7,
            placeholderColor = Color(0xFF7A4F2A)
        ),
        ZoneCaficultor(
            displayName = "Familia Ruiz",
            farmAndLocation = "LOS ARRAYANES, CURITÍ",
            formattedPriceFrom = "Desde $48.500",
            rating = 5.0,
            placeholderColor = Color(0xFF4F6E3F)
        ),
        ZoneCaficultor(
            displayName = "Gabriel Castro",
            farmAndLocation = "SAN ISIDRO, PINCHOTE",
            formattedPriceFrom = "Desde $44.000",
            rating = 4.8,
            placeholderColor = Color(0xFF6E4A36)
        )
    )

    private val santander: ZoneDetail = ZoneDetail(
        name = "Santander",
        tagline = "Café de altura, carácter fuerte",
        heroImageRes = R.drawable.imag_2,
        caficultorCount = 38,
        averageAltitudeText = "1.400m",
        averageRating = 4.8,
        flavorTags = flavorTagsSantander,
        flavorTraits = flavorTraitsSantander,
        description = descriptionSantander,
        municipalities = municipalitiesSantander,
        caficultores = caficultoresSantander
    )

    /**
     * Devuelve el detalle de la zona cuyo nombre coincide con [name]
     * (case-insensitive). Si no existe contenido específico, devuelve
     * Santander con el nombre intercambiado para que la pantalla siga
     * funcionando mientras se redactan las copys del resto de zonas.
     */
    fun forName(name: String): ZoneDetail {
        val normalized = name.trim().lowercase()
        return when (normalized) {
            "santander" -> santander
            else -> santander.copy(
                name = name.trim().lowercase().replaceFirstChar { it.uppercase() }
            )
        }
    }
}

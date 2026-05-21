package com.cafeteros.historia.data.model

/**
 * Perfil público de la finca del caficultor. Es lo que el comprador ve
 * cuando abre el detalle de un vendedor: historia, certificaciones,
 * fotos y datos técnicos del cultivo.
 *
 * Se persiste en `/farms/{caficultorUid}` (el id del documento coincide
 * con el uid del caficultor para facilitar el acceso `find by uid`).
 *
 * @property caficultorUid     uid del caficultor dueño de la finca (= id del doc).
 * @property name              nombre comercial de la finca (ej. "Finca La Esperanza").
 * @property title             titular corto que aparece en cards (ej. "Café de altura del Quindío").
 * @property highlight         frase destacada que resume la propuesta de valor.
 * @property story             historia narrativa larga, mostrada en el detalle.
 * @property region            departamento o eje cafetero (Risaralda, Quindío…).
 * @property altitudeMeters    altitud de la finca en m.s.n.m.
 * @property areaHectares      superficie cultivada en hectáreas.
 * @property varieties         variedades de café que cultiva (Caturra, Bourbon…).
 * @property shadeType         tipo de sombrío (sombrío parcial, libre exposición…).
 * @property processSteps      pasos del proceso post-cosecha (lavado, fermentación…).
 * @property certifications    certificaciones que ostenta (ver [SUGGESTED_CERTIFICATIONS]).
 * @property quote             frase personal del caficultor para humanizar el perfil.
 * @property videoUrl          URL opcional a un video corto presentando la finca.
 * @property principalPhotoBase64 foto principal de la finca, embebida como Base64.
 * @property farmerPhotoBase64 foto del propio caficultor (avatar más grande), Base64.
 * @property latitude          latitud para mostrar la finca en el mapa de origen.
 * @property longitude         longitud asociada.
 * @property updatedAtEpochMillis fecha de última edición, para mostrar
 *  "actualizado hace X" y para detectar perfiles desactualizados.
 */
data class FarmProfile(
    val caficultorUid: String = "",
    val name: String = "",
    val title: String = "",
    val highlight: String = "",
    val story: String = "",
    val region: String = "",
    val altitudeMeters: Int = 0,
    val areaHectares: Int = 0,
    val varieties: List<String> = emptyList(),
    val shadeType: String = "",
    val processSteps: List<String> = emptyList(),
    val certifications: List<String> = emptyList(),
    val quote: String = "",
    val videoUrl: String? = null,
    val principalPhotoBase64: String? = null,
    val farmerPhotoBase64: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val updatedAtEpochMillis: Long = System.currentTimeMillis()
) {
    /**
     * Porcentaje 0-100 indicando qué tan completo está el perfil.
     * Se usa en el panel del caficultor para incentivarlo a llenar los
     * campos faltantes (un perfil completo da más confianza al comprador).
     *
     * El cálculo es trivial: contar cuántos de 10 campos clave están
     * "no vacíos" y devolver el porcentaje. El criterio de "lleno" varía:
     * `> 0` para enteros, `isNotBlank` para texto, `isNotEmpty` para listas.
     */
    fun completionPercent(): Int {
        val checks = listOf(
            name.isNotBlank(),
            title.isNotBlank(),
            story.isNotBlank(),
            region.isNotBlank(),
            altitudeMeters > 0,
            areaHectares > 0,
            varieties.isNotEmpty(),
            processSteps.isNotEmpty(),
            certifications.isNotEmpty(),
            principalPhotoBase64 != null
        )
        return (checks.count { it } * 100) / checks.size
    }
}

/**
 * Lista predefinida de certificaciones reales del café colombiano que se
 * sugieren al caficultor en el editor de perfil para que las elija de un
 * dropdown en lugar de escribirlas a mano (evita typos y nombres distintos
 * para la misma certificación).
 */
val SUGGESTED_CERTIFICATIONS: List<String> = listOf(
    "Orgánico - ECOCERT",
    "Rainforest Alliance",
    "Denominación de Origen",
    "Fair Trade",
    "UTZ Certified",
    "Mujeres Cafeteras",
    "Bird Friendly",
    "4C Association"
)

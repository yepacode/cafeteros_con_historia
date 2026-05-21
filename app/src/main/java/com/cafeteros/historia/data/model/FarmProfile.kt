package com.cafeteros.historia.data.model

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

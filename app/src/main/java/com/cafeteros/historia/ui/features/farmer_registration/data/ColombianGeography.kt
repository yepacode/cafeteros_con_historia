package com.cafeteros.historia.ui.features.farmer_registration.data

/**
 * Catálogo simplificado de departamentos y municipios de Colombia para el
 * registro del caficultor.
 *
 * Es un *subset* representativo (10 departamentos cafeteros principales)
 * pensado como demo. Cuando se quiera el listado completo:
 *  - 32 departamentos + ~1.100 municipios oficiales del DANE.
 *  - Idealmente cargar desde un JSON en `assets/` o un endpoint remoto.
 *  - Mantener el mismo formato `código → nombre`.
 *
 * Compartido entre `PersonalDataStep` (donde se selecciona) y `FarmDetailsStep`
 * (donde se muestra la zona cafetera detectada en el banner informativo).
 */
object ColombianGeography {

    /** Pares (código interno, nombre humano) de departamentos. */
    val departments: List<Pair<String, String>> = listOf(
        "antioquia" to "Antioquia",
        "caldas" to "Caldas",
        "cundinamarca" to "Cundinamarca",
        "huila" to "Huila",
        "narino" to "Nariño",
        "quindio" to "Quindío",
        "risaralda" to "Risaralda",
        "santander" to "Santander",
        "tolima" to "Tolima",
        "valle" to "Valle del Cauca"
    )

    /** Mapa de municipios disponibles por código de departamento. */
    val citiesByDepartment: Map<String, List<Pair<String, String>>> = mapOf(
        "antioquia" to listOf(
            "medellin" to "Medellín",
            "envigado" to "Envigado",
            "rionegro" to "Rionegro"
        ),
        "caldas" to listOf("manizales" to "Manizales", "chinchina" to "Chinchiná"),
        "cundinamarca" to listOf(
            "bogota" to "Bogotá",
            "soacha" to "Soacha",
            "chia" to "Chía"
        ),
        "huila" to listOf(
            "neiva" to "Neiva",
            "pitalito" to "Pitalito",
            "garzon" to "Garzón"
        ),
        "narino" to listOf("pasto" to "Pasto", "ipiales" to "Ipiales"),
        "quindio" to listOf("armenia" to "Armenia", "salento" to "Salento"),
        "risaralda" to listOf(
            "pereira" to "Pereira",
            "dosquebradas" to "Dosquebradas"
        ),
        "santander" to listOf(
            "bucaramanga" to "Bucaramanga",
            "san_gil" to "San Gil"
        ),
        "tolima" to listOf("ibague" to "Ibagué", "honda" to "Honda"),
        "valle" to listOf(
            "cali" to "Cali",
            "buga" to "Buga",
            "palmira" to "Palmira"
        )
    )

    /**
     * Devuelve el nombre humano del departamento dado su código, o `null`
     * si el código no existe en el catálogo.
     */
    fun departmentLabel(code: String?): String? =
        code?.let { c -> departments.firstOrNull { it.first == c }?.second }

    /**
     * Devuelve el listado de municipios para el departamento dado, o lista
     * vacía si el código no aparece en [citiesByDepartment].
     */
    fun citiesFor(departmentCode: String?): List<Pair<String, String>> =
        departmentCode?.let { citiesByDepartment[it] }.orEmpty()

    /**
     * Devuelve el nombre humano del municipio dado su código y el código de
     * su departamento. Devuelve `null` si no se encuentra.
     */
    fun cityLabel(departmentCode: String?, cityCode: String?): String? {
        if (departmentCode == null || cityCode == null) return null
        return citiesFor(departmentCode).firstOrNull { it.first == cityCode }?.second
    }
}

package com.cafeteros.historia.ui.features.zonedetail.model

/**
 * Municipio dentro de una zona cafetera.
 *
 * Aparece como chip horizontal en la sección "Municipios" del detalle. El
 * primero de la lista suele venir activo por default; el padre maneja el
 * estado de selección.
 *
 * @property name nombre legible ("San Gil", "Socorro").
 * @property fincaCount número de fincas registradas en el municipio.
 */
data class Municipality(
    val name: String,
    val fincaCount: Int
)

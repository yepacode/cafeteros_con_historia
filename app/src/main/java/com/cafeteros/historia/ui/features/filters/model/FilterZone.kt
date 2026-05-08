package com.cafeteros.historia.ui.features.filters.model

import androidx.compose.ui.graphics.Color

/**
 * Una zona cafetera dentro de la sección "ZONA CAFETERA" del filtro.
 *
 * @property name nombre visible ("Santander").
 * @property count cantidad de caficultores en la zona, que se muestra
 *   como número gris a la derecha del nombre.
 * @property accentColor punto de color a la izquierda del chip — usa la
 *   misma paleta de zonas que tiene [com.cafeteros.historia.ui.theme.BrandColors].
 */
data class FilterZone(
    val name: String,
    val count: Int,
    val accentColor: Color
)

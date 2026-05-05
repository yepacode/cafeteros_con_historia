package com.cafeteros.historia.ui.features.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.Color
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.features.onboarding.model.OnboardingPage

/**
 * Lista estática de las páginas del onboarding de "Origen".
 *
 * Se mantiene como `val` en archivo dedicado (en vez de hardcoded dentro de
 * `OnboardingScreen`) para que sea trivial agregar, quitar o reordenar
 * páginas sin tocar la lógica de UI.
 *
 * Para reemplazar los placeholders por imágenes reales:
 *  1. Copia el archivo (PNG/JPG/WEBP recomendado WEBP por tamaño) en
 *     `app/src/main/res/drawable/`. El nombre debe ser en minúsculas y guion
 *     bajo, ej. `onboarding_regiones.webp`.
 *  2. Edita la página correspondiente abajo y agrega
 *     `imageRes = R.drawable.onboarding_regiones`.
 *  3. El placeholder se ignora automáticamente cuando hay `imageRes`.
 */
val OnboardingPages: List<OnboardingPage> = listOf(
    OnboardingPage(
        title = "Viaja por las zonas cafeteras de Colombia",
        description = "Descubre el café único de cada región: Santander, Huila, " +
                "Nariño, Cundinamarca, Eje Cafetero y Sierra Nevada.",
        imageRes = R.drawable.ima_1,
        placeholderIcon = Icons.Outlined.Landscape,
        placeholderColor = Color(0xFF2E7D32),
        contentDescription = "Paisaje de zona cafetera colombiana"
    ),
    OnboardingPage(
        title = "Conoce la historia detrás de cada grano",
        description = "Cada caficultor comparte su historia, su finca y su proceso. " +
                "No compras solo café, compras tradición.",
        imageRes = R.drawable.imag_2,
        placeholderIcon = Icons.Outlined.Person,
        placeholderColor = Color(0xFF4E342E),
        contentDescription = "Retrato simbólico de un caficultor"
    ),
    OnboardingPage(
        title = "Del árbol a tu taza",
        description = "Tu café llega directo del caficultor a tu casa en toda Colombia. " +
                "Apoyas al campo con cada compra.",
        imageRes = R.drawable.img_3,
        placeholderIcon = Icons.Outlined.Coffee,
        placeholderColor = Color(0xFF6D4C41),
        contentDescription = "Taza de café recién servida"
    )
)

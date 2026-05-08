package com.cafeteros.historia.ui.features.farmer_onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.ui.graphics.Color
import com.cafeteros.historia.ui.features.farmer_onboarding.model.FarmerOnboardingPage

/**
 * Lista estática de las 3 páginas del onboarding del caficultor.
 *
 * Para reemplazar los placeholders por imágenes reales:
 *  1. Copia los archivos en `app/src/main/res/drawable/` con nombres en
 *     minúsculas (ej. `farmer_onboarding_1.webp`).
 *  2. Edita la página correspondiente abajo y agrega
 *     `imageRes = R.drawable.farmer_onboarding_1`.
 *  3. El placeholder se ignora automáticamente cuando hay `imageRes`.
 */
val FarmerOnboardingPages: List<FarmerOnboardingPage> = listOf(
    FarmerOnboardingPage(
        title = "Vende tu café directo a Colombia entera",
        description = "Muestra tu finca, cuenta tu historia y llega a miles de " +
                "compradores que valoran tu trabajo.",
        placeholderIcon = Icons.Outlined.Eco,
        placeholderColor = Color(0xFF2E7D32),
        contentDescription = "Caficultor sosteniendo cerezas de café"
    ),
    FarmerOnboardingPage(
        title = "Tú controlas tu historia y tus precios",
        description = "Sube fotos, videos y la historia de tu finca. Define tus " +
                "precios y presentaciones. Origen es tu vitrina digital.",
        placeholderIcon = Icons.Outlined.PhoneIphone,
        placeholderColor = Color(0xFF5D4037),
        contentDescription = "Vitrina digital del caficultor sobre mesa de madera"
    ),
    FarmerOnboardingPage(
        title = "Recibe pagos seguros y a tiempo",
        description = "Tu dinero se consigna en tu cuenta bancaria. Sin " +
                "intermediarios. Sin sorpresas. Comisión justa del 12%.",
        placeholderIcon = Icons.Outlined.Savings,
        placeholderColor = Color(0xFF1B5E20),
        contentDescription = "Caficultor evaluando una taza de café",
        statsBadgeText = "☕ Ya somos +180 caficultores vendiendo en 6 zonas"
    )
)

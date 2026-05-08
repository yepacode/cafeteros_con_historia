package com.cafeteros.historia.ui.features.explore.model

import androidx.compose.ui.graphics.Color
import com.cafeteros.historia.R

/**
 * Datos de muestra para poblar la pantalla de exploración mientras no exista
 * un repositorio o backend conectado.
 *
 * Centralizar esta data aquí (en vez de dispersarla por los previews y la
 * pantalla) permite cambiar regiones, caficultores destacados, historias y
 * productos en un solo punto. Cuando llegue el backend real, este objeto se
 * borra y la pantalla recibe los listados desde un ViewModel.
 */
object ExploreSampleData {

    val regions: List<CoffeeRegion> = listOf(
        CoffeeRegion(
            name = "Huila",
            caficultorCount = 42,
            imageRes = R.drawable.ima_1,
            placeholderColor = Color(0xFF4F6E3F)
        ),
        CoffeeRegion(
            name = "Sierra Nevada",
            caficultorCount = 28,
            imageRes = R.drawable.imag_2,
            placeholderColor = Color(0xFF5C7B8A)
        ),
        CoffeeRegion(
            name = "Nariño",
            caficultorCount = 35,
            imageRes = R.drawable.img_3,
            placeholderColor = Color(0xFF7A4F2A)
        )
    )

    val featuredCaficultores: List<FeaturedCaficultor> = listOf(
        FeaturedCaficultor(
            farmName = "Finca La Esperanza",
            location = "Huila • Pitalito",
            rating = 4.9,
            reviewCount = 127,
            badge = CaficultorBadge.ORGANICO,
            avatarRes = R.drawable.finca_la_esperanza,
            avatarPlaceholderColor = Color(0xFF8B5A2B)
        ),
        FeaturedCaficultor(
            farmName = "El Mirador",
            location = "Cundinamarca",
            rating = 4.8,
            reviewCount = 94,
            badge = CaficultorBadge.SOSTENIBLE,
            avatarPlaceholderColor = Color(0xFF4F6E3F)
        ),
        FeaturedCaficultor(
            farmName = "Hacienda San José",
            location = "Antioquia • Jericó",
            rating = 4.7,
            reviewCount = 81,
            badge = CaficultorBadge.ORGANICO,
            avatarPlaceholderColor = Color(0xFF7A4F2A)
        )
    )

    val stories: List<CoffeeStory> = listOf(
        CoffeeStory(
            title = "Don Alberto y 40 años cultivando café",
            readTimeMinutes = 3,
            imageRes = R.drawable.ima_1,
            placeholderColor = Color(0xFF8B5A2B)
        ),
        CoffeeStory(
            title = "El renacer del café en la Sierra Nevada",
            readTimeMinutes = 5,
            imageRes = R.drawable.imag_2,
            placeholderColor = Color(0xFF4F6E3F)
        )
    )

    val popularProducts: List<CoffeeProduct> = listOf(
        CoffeeProduct(
            name = "Café Huila Pitalito",
            farmName = "Finca La Esperanza",
            formattedPrice = "$48.000",
            imageRes = R.drawable.cafe_huila_pitalito,
            placeholderColor = Color(0xFFE8DCC4)
        ),
        CoffeeProduct(
            name = "Origen Nariño",
            farmName = "Reserva del Sol",
            formattedPrice = "$52.000",
            imageRes = R.drawable.cafe_origen_narino,
            placeholderColor = Color(0xFF8B5A2B)
        ),
        CoffeeProduct(
            name = "Sierra Nevada Premium",
            farmName = "El Mirador",
            formattedPrice = "$55.000",
            imageRes = R.drawable.cafe_sierra_nevada_premium,
            placeholderColor = Color(0xFF4F6E3F)
        )
    )
}

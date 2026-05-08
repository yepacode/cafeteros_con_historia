package com.cafeteros.historia.ui.features.caficultordetail.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Color
import com.cafeteros.historia.R

/**
 * Data de muestra del detalle del caficultor mientras no exista backend.
 *
 * Hoy solo "Don Alberto" está poblado completamente (es el caficultor del
 * diseño que recibimos). [forName] devuelve esa data por default; cuando
 * lleguen otros caficultores, se agregan nuevos casos al `when`.
 */
object CaficultorDetailSampleData {

    private val donAlbertoCertifications = listOf(
        CertificationBadge.ORGANICO_CERTIFICADO,
        CertificationBadge.COMERCIO_JUSTO,
        CertificationBadge.DESDE_1982
    )

    private val donAlbertoProcessSteps = listOf(
        ProcessStep(label = "COSECHA MANUAL", icon = Icons.Filled.Park),
        ProcessStep(label = "DESPULPADO", icon = Icons.Filled.LocalFlorist),
        ProcessStep(label = "FERMENTACIÓN 24H", icon = Icons.Filled.HourglassBottom),
        ProcessStep(label = "SECADO AL SOL", icon = Icons.Filled.WbSunny)
    )

    private val donAlbertoProducts = listOf(
        CaficultorProduct(
            name = "Café Huila Pitalito",
            weight = "250g",
            formattedPrice = "$48.000",
            imageRes = R.drawable.cafe_huila_pitalito,
            placeholderColor = Color(0xFFE8DCC4)
        ),
        CaficultorProduct(
            name = "Reserva Especial",
            weight = "250g",
            formattedPrice = "$55.000",
            imageRes = R.drawable.cafe_origen_narino,
            placeholderColor = Color(0xFF8B5A2B)
        )
    )

    private val donAlbertoReviews = listOf(
        CaficultorReview(
            authorName = "Camila V.",
            rating = 5,
            body = "El mejor café que he probado este año. Se nota la frescura y el amor en cada grano.",
            avatarPlaceholderColor = Color(0xFFB16A4A)
        )
    )

    private val donAlberto = CaficultorProfile(
        farmName = "Finca La Esperanza",
        caficultorName = "Don Alberto Ramírez",
        displayShortName = "Don Alberto",
        location = "Pitalito, Huila",
        altitudeText = "1.650 msnm",
        rating = 4.9,
        reviewCount = 127,
        heroImageRes = R.drawable.finca_la_esperanza,
        avatarRes = R.drawable.ima_1,
        certifications = donAlbertoCertifications,
        historyQuote = "\"Cada grano que ves en este café lleva 40 años de tradición familiar\"",
        historyPart1 = "En el corazón de Pitalito, Huila, Don Alberto ha dedicado su vida a " +
                "perfeccionar el cultivo del café Caturra y Borbón. Su finca, La Esperanza, " +
                "no es solo un terreno, es un legado de paciencia y respeto por la tierra.",
        historyImageRes = R.drawable.ima_1,
        historyPart2 = "Desde la recolección manual hasta el secado en camas africanas, cada " +
                "proceso se realiza bajo la atenta mirada de Don Alberto y su familia. La " +
                "altitud de 1.650 metros proporciona ese perfil cítrico y dulzón tan " +
                "característico.",
        videoThumbnailRes = R.drawable.img_3,
        videoCaption = "Conoce la finca y el proceso de Don Alberto",
        processSteps = donAlbertoProcessSteps,
        productCount = 8,
        products = donAlbertoProducts,
        reviews = donAlbertoReviews
    )

    /**
     * Devuelve el perfil del caficultor cuyo nombre o finca coincide con
     * [name]. Si no existe, cae a Don Alberto cambiándole los identificadores
     * para que la pantalla nunca rompa mientras se crean los demás perfiles.
     */
    fun forName(name: String): CaficultorProfile {
        val normalized = name.trim().lowercase()
        return when {
            normalized.contains("alberto") || normalized.contains("esperanza") -> donAlberto
            else -> donAlberto.copy(
                farmName = name.trim(),
                caficultorName = name.trim(),
                displayShortName = name.trim().substringBefore(' ').ifBlank { name.trim() }
            )
        }
    }
}

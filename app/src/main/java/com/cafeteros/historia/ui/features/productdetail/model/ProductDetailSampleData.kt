package com.cafeteros.historia.ui.features.productdetail.model

import com.cafeteros.historia.R

/**
 * Data de muestra mientras no exista backend.
 *
 * Hoy solo "Café Huila Pitalito" está poblado completamente — es el producto
 * del diseño. [forName] devuelve esa data por default; cuando lleguen otros
 * productos, basta con agregar nuevos casos al `when`.
 */
object ProductDetailSampleData {

    private val cafeHuilaPitalito = ProductDetail(
        regionLabel = "HUILA",
        inStock = true,
        name = "Café Huila Pitalito 250g",
        tagline = "Tueste medio · Molido o en grano",
        rating = 4.9,
        reviewCount = 127,
        salesCount = 284,
        formattedPrice = "$48.000",
        formattedOriginalPrice = "$56.000",
        discountPercentLabel = "-15%",
        heroImageRes = R.drawable.cafe_huila_pitalito,
        heroGalleryImages = listOf(
            R.drawable.cafe_huila_pitalito,
            R.drawable.cafe_huila_pitalito,
            R.drawable.cafe_huila_pitalito
        ),
        producerName = "Don Alberto Ramírez",
        producerFarm = "Finca La Esperanza",
        producerAvatarRes = R.drawable.ima_1,
        descriptionQuote = "\"Ubicado en el corazón de Pitalito, este lote " +
                "proviene de la tradición cafetera de tres generaciones. " +
                "Las brisas de la montaña y el suelo volcánico otorgan una " +
                "dulzura excepcional...\"",
        specs = listOf(
            ProductSpec(label = "VARIEDAD", value = "Caturra & Castillo"),
            ProductSpec(label = "ALTITUD", value = "1,850 msnm"),
            ProductSpec(label = "PROCESO", value = "Lavado extendido"),
            ProductSpec(label = "CERTIFICACIÓN", value = "Rainforest Alliance")
        ),
        tastingNotes = listOf(
            "Cítricos brillantes",
            "Caramelo",
            "Chocolate con leche",
            "Final dulce y limpio"
        ),
        processSteps = listOf(
            "Cosecha manual selectiva al pico de maduración.",
            "Despulpado el mismo día de la recolección.",
            "Fermentación lavada de 24 horas en tanque.",
            "Secado al sol en camas africanas durante 12 días."
        ),
        shippingCity = "Bogotá",
        shippingEta = "Llega en 2-3 días hábiles"
    )

    /**
     * Devuelve el detalle del producto cuyo nombre coincide con [name].
     * Si no hay match, cae a "Café Huila Pitalito" para que la pantalla
     * nunca rompa mientras se modelan los demás productos.
     */
    fun forName(name: String): ProductDetail {
        val normalized = name.trim().lowercase()
        return when {
            normalized.contains("huila") || normalized.contains("pitalito") -> cafeHuilaPitalito
            else -> cafeHuilaPitalito.copy(name = name.trim().ifBlank { cafeHuilaPitalito.name })
        }
    }
}

package com.cafeteros.historia.ui.features.caficultordetail.model

import androidx.annotation.DrawableRes

/**
 * Modelo agregado para la pantalla de detalle del caficultor.
 *
 * Junta todo lo que necesita pintar [com.cafeteros.historia.ui.features.caficultordetail.CaficultorDetailScreen]:
 * datos identitarios (nombre, finca, ubicación, certificaciones), narrativa
 * editorial (historia con cita y párrafos), proceso, productos y reseñas.
 *
 * Cuando exista BD, este objeto vendrá de un caso de uso tipo
 * `GetCaficultorProfileUseCase(caficultorId)`. El sample data de hoy es
 * solo un placeholder.
 *
 * @property farmName nombre de la finca ("Finca La Esperanza").
 * @property caficultorName nombre del caficultor ("Don Alberto Ramírez").
 * @property displayShortName primer nombre usado en la UI ("Don Alberto"),
 *  sirve para títulos como "La historia de {displayShortName}".
 * @property location ubicación legible ("Pitalito, Huila").
 * @property altitudeText altitud ya formateada ("1.650 msnm").
 * @property rating valor del rating (0–5).
 * @property reviewCount total de reseñas.
 * @property heroImageRes foto de portada (paisaje del entorno de la finca).
 * @property avatarRes foto del caficultor en la profile card.
 * @property certifications listado de pills verdes.
 * @property historyQuote cita destacada en italics centrada.
 * @property historyPart1 primer párrafo del cuerpo editorial.
 * @property historyImageRes foto que va entre los párrafos.
 * @property historyPart2 segundo párrafo del cuerpo editorial.
 * @property videoThumbnailRes drawable usado como portada del video.
 * @property videoCaption caption italic bajo el video.
 * @property processSteps cuatro pasos del proceso de beneficio.
 * @property productCount total de productos (puede ser mayor que la lista
 *  cargada por paginación; se muestra en "Productos (n)").
 * @property products lista visible en la sección.
 * @property reviews reseñas a renderizar (top-N).
 */
data class CaficultorProfile(
    val farmName: String,
    val caficultorName: String,
    val displayShortName: String,
    val location: String,
    val altitudeText: String,
    val rating: Double,
    val reviewCount: Int,
    @param:DrawableRes val heroImageRes: Int,
    @param:DrawableRes val avatarRes: Int,
    val certifications: List<CertificationBadge>,
    val historyQuote: String,
    val historyPart1: String,
    @param:DrawableRes val historyImageRes: Int,
    val historyPart2: String,
    @param:DrawableRes val videoThumbnailRes: Int,
    val videoCaption: String,
    val processSteps: List<ProcessStep>,
    val productCount: Int,
    val products: List<CaficultorProduct>,
    val reviews: List<CaficultorReview>
)

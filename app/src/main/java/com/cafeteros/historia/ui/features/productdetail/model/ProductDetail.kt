package com.cafeteros.historia.ui.features.productdetail.model

import androidx.annotation.DrawableRes

/**
 * Dato completo que alimenta la pantalla de detalle de producto.
 *
 * Este modelo se centra solo en la UI: precios ya formateados, listas listas
 * para renderizar, etc. Cuando exista backend, se mapeará desde la response
 * a esta data class para mantener el contrato estable hacia la pantalla.
 *
 * @property regionLabel etiqueta corta de la zona ("HUILA") mostrada como pill.
 * @property inStock indica si el producto está disponible — controla la
 *   visibilidad del pill "En stock" y la habilitación del CTA.
 * @property name nombre comercial completo ("Café Huila Pitalito 250g").
 * @property tagline subtítulo bajo el título ("Tueste medio · Molido o en grano").
 * @property rating valor numérico del rating (4.9).
 * @property reviewCount total de reseñas.
 * @property salesCount total de unidades vendidas.
 * @property formattedPrice precio actual ya formateado ("$48.000").
 * @property formattedOriginalPrice precio anterior tachado, opcional.
 * @property discountPercentLabel etiqueta del badge "-15%", opcional.
 * @property heroImageRes drawable principal del hero (paquete frontal).
 * @property heroGalleryImages galería completa para el carrusel del hero.
 *   Si está vacía, se usa [heroImageRes] como única imagen.
 * @property producerName nombre del caficultor ("Don Alberto Ramírez").
 * @property producerFarm finca del caficultor ("Finca La Esperanza").
 * @property producerAvatarRes avatar circular del caficultor.
 * @property availablePresentations presentaciones disponibles. Por defecto
 *   las tres del catálogo.
 * @property availableRoasts niveles de tueste disponibles para este producto.
 * @property availableGrindTypes tipos de molienda disponibles.
 * @property defaultPresentation presentación pre-seleccionada al abrir.
 * @property defaultRoast tueste pre-seleccionado.
 * @property defaultGrindType molienda pre-seleccionada.
 * @property descriptionQuote cita italic mostrada en la tab "Descripción".
 * @property specs grilla de pares etiqueta/valor bajo la cita.
 * @property tastingNotes lista de notas de cata mostrada en la tab "Notas".
 * @property processSteps lista de pasos del proceso mostrada en la tab "Proceso".
 * @property shippingCity ciudad del usuario ("Bogotá").
 * @property shippingEta tiempo estimado ("Llega en 2-3 días hábiles").
 */
data class ProductDetail(
    val regionLabel: String,
    val inStock: Boolean,
    val name: String,
    val tagline: String,
    val rating: Double,
    val reviewCount: Int,
    val salesCount: Int,
    val formattedPrice: String,
    val formattedOriginalPrice: String? = null,
    val discountPercentLabel: String? = null,
    @param:DrawableRes val heroImageRes: Int,
    @param:DrawableRes val heroGalleryImages: List<Int> = emptyList(),
    val producerName: String,
    val producerFarm: String,
    @param:DrawableRes val producerAvatarRes: Int,
    val availablePresentations: List<ProductPresentation> = ProductPresentation.entries,
    val availableRoasts: List<ProductRoast> = ProductRoast.entries,
    val availableGrindTypes: List<ProductGrindType> = ProductGrindType.entries,
    val defaultPresentation: ProductPresentation = ProductPresentation.SIZE_250G,
    val defaultRoast: ProductRoast = ProductRoast.MEDIO,
    val defaultGrindType: ProductGrindType = ProductGrindType.EN_GRANO,
    val descriptionQuote: String,
    val specs: List<ProductSpec>,
    val tastingNotes: List<String>,
    val processSteps: List<String>,
    val shippingCity: String,
    val shippingEta: String
)

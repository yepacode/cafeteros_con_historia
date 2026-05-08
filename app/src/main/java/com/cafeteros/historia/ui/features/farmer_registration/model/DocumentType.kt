package com.cafeteros.historia.ui.features.farmer_registration.model

/**
 * Documentos obligatorios para validar la condición de caficultor.
 *
 * Cada valor lleva los textos de UI acoplados (título + subtítulo descriptivo)
 * para que el composable que los renderice no tenga que mantener un mapa
 * paralelo. Cuando se internacionalice la app, estos strings deben pasar a
 * `strings.xml` y leerse por `stringResource`.
 */
enum class MandatoryDocumentType(val title: String, val description: String) {
    RUT(
        title = "RUT actualizado (DIAN)",
        description = "No más de 30 días de expedición"
    ),
    CHAMBER_OF_COMMERCE(
        title = "Certificado Cámara de Comercio",
        description = "Si tu finca está registrada como empresa"
    ),
    PROPERTY_DEED(
        title = "Certificado de Tradición",
        description = "O declaración de tenencia familiar"
    ),
    FNC_CERTIFICATE(
        title = "Certificado FNC",
        description = "Cédula cafetera — recomendado"
    )
}

/**
 * Documentos opcionales que suman al perfil del caficultor.
 *
 * No son requeridos para avanzar pero mejoran la confianza con compradores.
 */
enum class OptionalDocumentType(val title: String) {
    QUALITY_CERTIFICATIONS(title = "Certificaciones de calidad"),
    QUALITY_ANALYSIS(title = "Análisis de calidad (perfil de taza)"),
    AWARDS_RECOGNITIONS(title = "Premios o reconocimientos")
}

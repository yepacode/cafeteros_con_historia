package com.cafeteros.historia.ui.features.farmer_registration.model

/**
 * Tipos de documento de identidad aceptados en el registro del caficultor.
 *
 * El [label] es el texto corto mostrado en el selector segmentado (CC/CE/PPT)
 * y se persiste en la DB cuando se guarde el formulario.
 */
enum class IdentificationType(val label: String) {
    CC(label = "C.C."),
    CE(label = "C.E."),
    PPT(label = "P.P.T.")
}

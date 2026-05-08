package com.cafeteros.historia.ui.features.farmer_registration.model

import android.net.Uri

/**
 * Estado completo del formulario de registro del caficultor (paso 1).
 *
 * Se mantiene como `data class` inmutable para que el ViewModel pueda
 * publicar nuevas versiones vía `copy()` sin riesgo de mutación accidental.
 *
 * Cuando se diseñen los pasos 2–5, esta clase se extenderá con los campos
 * adicionales correspondientes (información de finca, banco, catálogo, etc.).
 *
 * @property identificationType tipo de documento: C.C., C.E. o P.P.T.
 * @property documentNumber número del documento (string para no perder ceros).
 * @property documentIssuedAtEpochMillis fecha de expedición en epoch millis.
 * @property firstNames nombres.
 * @property lastNames apellidos.
 * @property birthDateEpochMillis fecha de nacimiento en epoch millis.
 * @property gender género seleccionado.
 * @property phoneNumber teléfono móvil sin prefijo (+57 se agrega aparte).
 * @property email correo electrónico.
 * @property emailVerified true si el OTP del correo fue verificado correctamente.
 * @property departmentCode código del departamento de Colombia.
 * @property cityCode código del municipio.
 * @property residenceAddress dirección literal escrita por el usuario.
 * @property documentFrontPhotoUri URI de la foto frontal de la cédula (null si no se tomó).
 * @property documentBackPhotoUri URI de la foto posterior de la cédula.
 * @property selfieWithDocumentUri URI de la selfie con cédula.
 * @property farmName nombre comercial de la finca (visible al comprador).
 * @property yearsOfCoffeeExperience años cultivando café.
 * @property altitudeMasl altitud de la finca en metros sobre el nivel del mar.
 * @property totalAreaHectares área total de la finca en hectáreas.
 * @property cultivatedAreaHectares área dedicada a café en hectáreas.
 * @property estimatedAnnualProductionKg producción anual estimada (café pergamino seco).
 * @property cultivatedVarieties variedades de café cultivadas (multi-select).
 * @property processes procesos de beneficio que maneja (multi-select).
 * @property harvestMethod método principal de cosecha (single-select).
 * @property hasAwards true si la finca ha recibido reconocimientos (Taza de la
 *  Excelencia u otros). Cuando es true, futuros pasos podrán capturar detalles.
 * @property mandatoryDocuments URIs de los documentos obligatorios del paso 4,
 *  indexados por su tipo. Una entrada `null` significa que aún no se ha
 *  subido el archivo correspondiente.
 * @property chamberOfCommerceNotApplicable true si el caficultor marca el
 *  certificado de Cámara de Comercio como "No aplica" (no tiene la finca
 *  registrada como empresa). Cuando es true, ese documento se considera
 *  satisfecho aunque su URI sea null.
 * @property optionalDocuments URIs de los documentos opcionales del paso 4.
 */
data class FarmerRegistrationFormState(
    // Paso 1 — datos personales
    val identificationType: IdentificationType = IdentificationType.CC,
    val documentNumber: String = "",
    val documentIssuedAtEpochMillis: Long? = null,
    val firstNames: String = "",
    val lastNames: String = "",
    val birthDateEpochMillis: Long? = null,
    val gender: Gender? = null,
    val phoneNumber: String = "",
    val email: String = "",
    val emailVerified: Boolean = false,
    val departmentCode: String? = null,
    val cityCode: String? = null,
    val residenceAddress: String = "",
    val documentFrontPhotoUri: Uri? = null,
    val documentBackPhotoUri: Uri? = null,
    val selfieWithDocumentUri: Uri? = null,
    // Paso 2 — finca
    val farmName: String = "",
    val yearsOfCoffeeExperience: String = "",
    val altitudeMasl: String = "",
    val totalAreaHectares: String = "",
    val cultivatedAreaHectares: String = "",
    val estimatedAnnualProductionKg: String = "",
    val cultivatedVarieties: Set<CoffeeVariety> = emptySet(),
    val processes: Set<CoffeeProcess> = emptySet(),
    val harvestMethod: HarvestMethod? = null,
    val hasAwards: Boolean = false,
    // Paso 4 — verificación documental
    val mandatoryDocuments: Map<MandatoryDocumentType, Uri> = emptyMap(),
    val chamberOfCommerceNotApplicable: Boolean = false,
    val optionalDocuments: Map<OptionalDocumentType, Uri> = emptyMap()
)

/** Identificadores de las 3 ranuras de foto del paso 1. */
enum class PhotoSlot {
    DOCUMENT_FRONT,
    DOCUMENT_BACK,
    SELFIE_WITH_DOCUMENT
}

/** Opciones de género del formulario. */
enum class Gender(val label: String) {
    FEMENINO("Femenino"),
    MASCULINO("Masculino"),
    NO_BINARIO("No binario"),
    PREFIERO_NO_DECIR("Prefiero no decir")
}

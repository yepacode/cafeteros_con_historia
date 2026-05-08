package com.cafeteros.historia.ui.features.farmer_registration.model

/**
 * Pasos del flujo de registro del caficultor.
 *
 * Cada paso conoce su [number] (base 1) y su [title]. La cuenta total de
 * pasos vive en [TOTAL] como única fuente de verdad para el contador
 * "X DE Y" del header.
 *
 * Por ahora solo [PersonalData] está implementado; los pasos 2–5 son
 * placeholders que se reemplazarán cuando se diseñen.
 */
enum class FarmerRegistrationStep(val number: Int, val title: String) {
    PersonalData(number = 1, title = "Tus datos personales"),
    FarmDetails(number = 2, title = "Tu finca"),
    BankInfo(number = 3, title = "Información bancaria"),
    Verification(number = 4, title = "Verifica tu condición de caficultor"),
    VerificationStatus(number = 5, title = "Estado de verificación"),

    /**
     * Pantalla post-aprobación. No es un paso del formulario (no aparece en
     * el contador "X de 5") — es la confirmación final que ve el caficultor
     * cuando su finca ya quedó verificada. En modo demo se llega vía botón
     * "Continuar" desde [VerificationStatus]; con backend real, llegará por
     * push o al refrescar el estado.
     */
    Approved(number = 6, title = "¡Bienvenido!");

    companion object {
        /** Total de pasos del formulario (1..4 + estado de verificación). */
        const val TOTAL: Int = 5
    }
}

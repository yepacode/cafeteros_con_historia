package com.cafeteros.historia.ui.features.farmer_registration.model

/**
 * Estado de un paso individual del timeline de verificación post-registro.
 *
 *  - [DONE]: el paso está completado (check verde + timestamp).
 *  - [IN_PROGRESS]: el equipo está trabajando en este paso ahora (badge "EN CURSO").
 *  - [PENDING]: el paso aún no ha empezado.
 */
enum class VerificationProgressStatus {
    DONE,
    IN_PROGRESS,
    PENDING
}

/**
 * Una entrada del timeline "Progreso de tu verificación".
 *
 * @property title título del paso (ej. "Registro recibido").
 * @property subtitle texto secundario: timestamp si está [VerificationProgressStatus.DONE],
 *  "EN CURSO" si está [VerificationProgressStatus.IN_PROGRESS], "Pendiente" si está
 *  [VerificationProgressStatus.PENDING]. La capa de UI puede formatear según convenga.
 * @property status estado actual del paso.
 */
data class VerificationProgressItem(
    val title: String,
    val subtitle: String,
    val status: VerificationProgressStatus
)

/**
 * Tarjeta de contenido informativo / CTA mostrada en la sección
 * "Mientras tanto..." durante la espera de la verificación.
 *
 * @property title título de la tarjeta (ej. "Guía del caficultor exitoso").
 * @property description copy corto que explica el contenido.
 * @property actionLabel etiqueta del enlace de acción (ej. "Leer guía →").
 */
data class MeantimeContentTip(
    val title: String,
    val description: String,
    val actionLabel: String
)

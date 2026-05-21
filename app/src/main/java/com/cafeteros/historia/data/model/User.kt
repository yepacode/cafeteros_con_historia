package com.cafeteros.historia.data.model

import com.cafeteros.historia.ui.features.auth.components.UserType

/**
 * Estado de aprobación de la cuenta en la plataforma.
 *
 * **Compradores y administradores** se crean directamente como
 * [APPROVED] — no requieren revisión. Solo los **caficultores** quedan
 * en [PENDING_APPROVAL] cuando se registran, esperando que un admin
 * revise sus documentos (RUT, Cámara de Comercio, etc.) y los
 * apruebe o rechace.
 */
enum class ApprovalStatus {
    /** Caficultor registrado, esperando revisión del admin. */
    PENDING_APPROVAL,

    /** Cuenta lista para operar normalmente. */
    APPROVED,

    /** Solicitud rechazada por el admin. */
    REJECTED;

    companion object {
        fun fromNameOrDefault(name: String?): ApprovalStatus =
            entries.firstOrNull { it.name == name } ?: APPROVED
    }
}

/**
 * Modelo de dominio del usuario de la app.
 *
 * @property id    UID asignado por Firebase Auth al crear la cuenta. Es
 *                 el mismo identificador que se usa como id del documento
 *                 en `/users/{uid}` en Firestore.
 * @property email Correo único, en minúsculas.
 * @property name  Nombre completo tal como lo escribió el usuario.
 * @property phone Teléfono (formato libre; la app no impone validación
 *                 estricta para no excluir números internacionales).
 * @property userType Rol del usuario en la plataforma.
 * @property approvalStatus estado de revisión de la cuenta.
 *  Compradores y admins arrancan en [ApprovalStatus.APPROVED]; el
 *  caficultor en [ApprovalStatus.PENDING_APPROVAL] hasta que un
 *  administrador apruebe sus documentos.
 */
data class User(
    val id: String,
    val email: String,
    val name: String,
    val phone: String,
    val userType: UserType,
    val approvalStatus: ApprovalStatus = ApprovalStatus.APPROVED
)

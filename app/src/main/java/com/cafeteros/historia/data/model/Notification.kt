package com.cafeteros.historia.data.model

/**
 * Tipos de notificación que se generan automáticamente como efecto de
 * eventos del sistema. Cada tipo decide el ícono y color en la UI.
 */
enum class NotificationType {
    /** Nuevo pedido recibido (caficultor). */
    NEW_ORDER,

    /** Cambio de estado de un pedido (comprador). */
    ORDER_STATUS_CHANGED,

    /** Nueva reseña recibida (caficultor). */
    NEW_REVIEW,

    /** Stock bajo en alguno de tus productos (caficultor). */
    LOW_STOCK,

    /** Sistema genérico (avisos, recordatorios). */
    GENERIC;

    companion object {
        fun fromNameOrDefault(name: String?): NotificationType =
            entries.firstOrNull { it.name == name } ?: GENERIC
    }
}

/**
 * Notificación local registrada en Firestore que se muestra en
 * `NotificationsScreen` del caficultor o `MyPurchasesActivity` del
 * comprador (vía un futuro hook). Hoy solo el caficultor las consume.
 *
 * @property id id del documento Firestore.
 * @property targetUid uid del usuario que debe verla.
 * @property type [NotificationType] que define el ícono y la severidad.
 * @property title texto corto para mostrar destacado (ej. "Nuevo pedido").
 * @property body línea explicativa (ej. "María G. te compró 1 unidad").
 * @property relatedId id del objeto relacionado (orderId, reviewId, etc.)
 *  para que la UI pueda navegar al detalle al tocar la notificación.
 * @property createdAtEpochMillis fecha de creación.
 * @property read true cuando el usuario ya la abrió.
 */
data class Notification(
    val id: String = "",
    val targetUid: String = "",
    val type: NotificationType = NotificationType.GENERIC,
    val title: String = "",
    val body: String = "",
    val relatedId: String = "",
    val createdAtEpochMillis: Long = System.currentTimeMillis(),
    val read: Boolean = false
)

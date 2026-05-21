package com.cafeteros.historia.data.repository

import com.cafeteros.historia.data.model.Notification
import com.cafeteros.historia.data.model.NotificationType
import com.cafeteros.historia.data.remote.firestore.NotificationsRemoteDataSource
import kotlinx.coroutines.flow.Flow

/**
 * Punto único de acceso a la colección `/notifications`.
 *
 * Las notificaciones se generan como efectos secundarios de otros
 * repositorios (OrderRepository, ReviewRepository) — esta clase
 * concentra la lógica de "qué decir" en cada evento (`notifyNewOrder`,
 * `notifyStatusChanged`, etc.) para que los callers no repitan la
 * construcción del mensaje.
 */
class NotificationRepository(
    private val remote: NotificationsRemoteDataSource
) {

    /** Notificación al caficultor cuando recibe un nuevo pedido. */
    suspend fun notifyNewOrder(
        caficultorUid: String,
        buyerName: String,
        orderId: String,
        totalCop: Int
    ) {
        runCatching {
            remote.create(
                Notification(
                    targetUid = caficultorUid,
                    type = NotificationType.NEW_ORDER,
                    title = "Nuevo pedido",
                    body = "$buyerName te compró por $" +
                        "%,d".format(totalCop).replace(',', '.'),
                    relatedId = orderId
                )
            )
        }
    }

    /** Notificación al comprador cuando su pedido cambia de estado. */
    suspend fun notifyStatusChanged(
        buyerUid: String,
        orderId: String,
        newStatusLabel: String
    ) {
        runCatching {
            remote.create(
                Notification(
                    targetUid = buyerUid,
                    type = NotificationType.ORDER_STATUS_CHANGED,
                    title = "Tu pedido fue $newStatusLabel",
                    body = "Toca para ver el detalle.",
                    relatedId = orderId
                )
            )
        }
    }

    /** Notificación al caficultor cuando recibe una reseña nueva. */
    suspend fun notifyNewReview(
        caficultorUid: String,
        buyerName: String,
        rating: Int,
        reviewId: String
    ) {
        runCatching {
            val stars = "★".repeat(rating) + "☆".repeat(5 - rating)
            remote.create(
                Notification(
                    targetUid = caficultorUid,
                    type = NotificationType.NEW_REVIEW,
                    title = "Nueva reseña $stars",
                    body = "$buyerName dejó una reseña sobre uno de tus productos.",
                    relatedId = reviewId
                )
            )
        }
    }

    /**
     * Notificación genérica. La usa el admin cuando aprueba/rechaza una
     * cuenta de caficultor para avisarle del cambio.
     */
    suspend fun notifyGeneric(
        targetUid: String,
        title: String,
        body: String,
        relatedId: String = ""
    ) {
        runCatching {
            remote.create(
                Notification(
                    targetUid = targetUid,
                    type = NotificationType.GENERIC,
                    title = title,
                    body = body,
                    relatedId = relatedId
                )
            )
        }
    }

    /** [Flow] reactivo de las notificaciones del usuario [uid]. */
    fun observeForUser(uid: String): Flow<List<Notification>> = remote.observeForUser(uid)

    /** Marca una notificación como leída. */
    suspend fun markAsRead(id: String) {
        runCatching { remote.markAsRead(id) }
    }
}

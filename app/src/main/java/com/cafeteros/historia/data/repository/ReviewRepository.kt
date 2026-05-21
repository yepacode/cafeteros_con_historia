package com.cafeteros.historia.data.repository

import com.cafeteros.historia.data.model.Review
import com.cafeteros.historia.data.remote.firestore.ReviewsRemoteDataSource
import kotlinx.coroutines.flow.Flow

/** Resultado expuesto a la UI al crear una reseña. */
sealed class ReviewOperationResult {
    data class Success(val reviewId: String) : ReviewOperationResult()
    data class Error(val message: String) : ReviewOperationResult()
}

/**
 * Punto único de acceso a la colección `/reviews`. Mantiene la firma de
 * los demás repositorios para que los ViewModels que ya conocen el
 * patrón puedan integrarlo sin sorpresas.
 */
class ReviewRepository(
    private val reviewsRemote: ReviewsRemoteDataSource
) {

    suspend fun createReview(review: Review): ReviewOperationResult =
        runCatching {
            val id = reviewsRemote.create(review)
            ReviewOperationResult.Success(id)
        }.getOrElse { error ->
            ReviewOperationResult.Error(error.message ?: "No se pudo crear la reseña")
        }

    /** Todas las reseñas que ha recibido el caficultor [uid]. */
    fun observeReceivedReviews(uid: String): Flow<List<Review>> =
        reviewsRemote.observeByCaficultor(uid)

    /** Reseñas de un producto específico (usado por la vista pública). */
    fun observeReviewsForProduct(productId: String): Flow<List<Review>> =
        reviewsRemote.observeByProduct(productId)

    /**
     * Devuelve la reseña que el comprador [buyerUid] ya dejó sobre el
     * pedido [orderId], o null si todavía no lo reseñó. Lo usa la UI del
     * comprador para mostrar "Dejar reseña" vs "Ya reseñaste".
     */
    suspend fun findReviewForOrder(buyerUid: String, orderId: String): Review? =
        reviewsRemote.findByBuyerAndOrder(buyerUid, orderId)
}

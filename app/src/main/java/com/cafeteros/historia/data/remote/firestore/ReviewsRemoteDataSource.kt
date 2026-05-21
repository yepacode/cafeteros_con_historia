package com.cafeteros.historia.data.remote.firestore

import com.cafeteros.historia.data.model.Review
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Fuente de datos remota para la colección `/reviews` de Firestore.
 *
 * Documento:
 * ```
 * /reviews/{reviewId} {
 *   productId:     String
 *   orderId:       String
 *   caficultorUid: String
 *   buyerUid:      String
 *   buyerName:     String
 *   rating:        Long (1..5)
 *   comment:       String
 *   createdAt:     Timestamp (server)
 * }
 * ```
 *
 * Como con productos y pedidos, no usamos `orderBy(createdAt)` en server
 * cuando hay un `whereEqualTo` previo — evita índices compuestos. El
 * orden se hace en cliente.
 */
class ReviewsRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collection = firestore.collection(COLLECTION_REVIEWS)

    suspend fun create(review: Review): String {
        val docRef = collection.document()
        docRef.set(review.toFirestoreMap()).await()
        return docRef.id
    }

    /** Reviews que recibió un caficultor (todos sus productos). */
    fun observeByCaficultor(caficultorUid: String): Flow<List<Review>> = callbackFlow {
        val registration: ListenerRegistration = collection
            .whereEqualTo(FIELD_CAFICULTOR_UID, caficultorUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeByCaficultor failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val reviews = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toReviewOrNull() }
                    .sortedByDescending { it.createdAtEpochMillis }
                trySend(reviews)
            }
        awaitClose { registration.remove() }
    }

    /** Reviews que un comprador ha dejado — para saber si ya reseñó un pedido. */
    suspend fun findByBuyerAndOrder(buyerUid: String, orderId: String): Review? {
        val snapshot = collection
            .whereEqualTo(FIELD_BUYER_UID, buyerUid)
            .whereEqualTo(FIELD_ORDER_ID, orderId)
            .get().await()
        return snapshot.documents.firstNotNullOfOrNull { it.toReviewOrNull() }
    }

    /** Reviews de un producto específico (para ProductDetail). */
    fun observeByProduct(productId: String): Flow<List<Review>> = callbackFlow {
        val registration = collection
            .whereEqualTo(FIELD_PRODUCT_ID, productId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeByProduct failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val reviews = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toReviewOrNull() }
                    .sortedByDescending { it.createdAtEpochMillis }
                trySend(reviews)
            }
        awaitClose { registration.remove() }
    }

    private fun Review.toFirestoreMap(): Map<String, Any?> = mapOf(
        FIELD_PRODUCT_ID to productId,
        FIELD_ORDER_ID to orderId,
        FIELD_CAFICULTOR_UID to caficultorUid,
        FIELD_BUYER_UID to buyerUid,
        FIELD_BUYER_NAME to buyerName,
        FIELD_RATING to rating,
        FIELD_COMMENT to comment,
        FIELD_CREATED_AT to FieldValue.serverTimestamp()
    )

    private fun DocumentSnapshot.toReviewOrNull(): Review? {
        if (!exists()) return null
        return Review(
            id = id,
            productId = getString(FIELD_PRODUCT_ID).orEmpty(),
            orderId = getString(FIELD_ORDER_ID).orEmpty(),
            caficultorUid = getString(FIELD_CAFICULTOR_UID).orEmpty(),
            buyerUid = getString(FIELD_BUYER_UID).orEmpty(),
            buyerName = getString(FIELD_BUYER_NAME).orEmpty(),
            rating = getLong(FIELD_RATING)?.toInt() ?: 0,
            comment = getString(FIELD_COMMENT).orEmpty(),
            createdAtEpochMillis = getTimestamp(FIELD_CREATED_AT)?.toDate()?.time
                ?: System.currentTimeMillis()
        )
    }

    private companion object {
        const val TAG = "ReviewsRemoteDS"
        const val COLLECTION_REVIEWS = "reviews"
        const val FIELD_PRODUCT_ID = "productId"
        const val FIELD_ORDER_ID = "orderId"
        const val FIELD_CAFICULTOR_UID = "caficultorUid"
        const val FIELD_BUYER_UID = "buyerUid"
        const val FIELD_BUYER_NAME = "buyerName"
        const val FIELD_RATING = "rating"
        const val FIELD_COMMENT = "comment"
        const val FIELD_CREATED_AT = "createdAt"
    }
}

package com.cafeteros.historia.data.remote.firestore

import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderItem
import com.cafeteros.historia.data.model.OrderStatus
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Fuente de datos remota para la colección `/orders` de Firestore.
 *
 * Estructura del documento:
 * ```
 * /orders/{orderId} {
 *   compradorUid:     String   // FK a /users/{uid}
 *   caficultorUid:    String   // FK a /users/{uid}
 *   compradorName:    String   // snapshot al momento de la compra
 *   items:            List<Map> // cada item con productId, name, price, qty, caficultorUid
 *   totalCop:         Long
 *   shippingCop:      Long
 *   shippingAddress:  String
 *   status:           String   // name del enum OrderStatus
 *   createdAt:        Timestamp (server)
 * }
 * ```
 *
 * **Por qué no usar orderBy en server:** las queries por
 * `caficultorUid + orderBy(createdAt)` y `compradorUid + orderBy(createdAt)`
 * exigirían índices compuestos en Firebase Console. Para evitar fricción
 * en el setup, ordenamos en cliente — el volumen por usuario (decenas)
 * no justifica el costo del índice.
 */
class OrdersRemoteDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collection = firestore.collection(COLLECTION_ORDERS)

    /** Crea un nuevo documento de pedido. Firestore asigna el id. */
    suspend fun create(order: Order): String {
        val docRef = collection.document()
        docRef.set(order.toFirestoreMap()).await()
        return docRef.id
    }

    /** Actualiza el estado de un pedido (caso de uso más común). */
    suspend fun updateStatus(orderId: String, status: OrderStatus) {
        collection.document(orderId).update(FIELD_STATUS, status.name).await()
    }

    /** Lectura puntual del pedido [orderId]. */
    suspend fun findById(orderId: String): Order? {
        val snapshot = collection.document(orderId).get().await()
        return snapshot.toOrderOrNull()
    }

    /** Flow de los pedidos donde el usuario [uid] es el VENDEDOR (caficultor). */
    fun observeAsSeller(uid: String): Flow<List<Order>> = callbackFlow {
        val registration: ListenerRegistration = collection
            .whereEqualTo(FIELD_CAFICULTOR_UID, uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeAsSeller failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val orders = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toOrderOrNull() }
                    .sortedByDescending { it.createdAtEpochMillis }
                trySend(orders)
            }
        awaitClose { registration.remove() }
    }

    /** Flow de los pedidos donde el usuario [uid] es el COMPRADOR. */
    fun observeAsBuyer(uid: String): Flow<List<Order>> = callbackFlow {
        val registration = collection
            .whereEqualTo(FIELD_COMPRADOR_UID, uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e(TAG, "observeAsBuyer failed", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val orders = snapshot?.documents.orEmpty()
                    .mapNotNull { it.toOrderOrNull() }
                    .sortedByDescending { it.createdAtEpochMillis }
                trySend(orders)
            }
        awaitClose { registration.remove() }
    }

    private fun Order.toFirestoreMap(): Map<String, Any?> = mapOf(
        FIELD_COMPRADOR_UID to compradorUid,
        FIELD_CAFICULTOR_UID to caficultorUid,
        FIELD_COMPRADOR_NAME to compradorName,
        FIELD_ITEMS to items.map { it.toMap() },
        FIELD_TOTAL_COP to totalCop,
        FIELD_SHIPPING_COP to shippingCop,
        FIELD_SHIPPING_ADDRESS to shippingAddress,
        FIELD_STATUS to status.name,
        FIELD_CREATED_AT to FieldValue.serverTimestamp()
    )

    private fun OrderItem.toMap(): Map<String, Any?> = mapOf(
        "productId" to productId,
        "productName" to productName,
        "unitPriceCop" to unitPriceCop,
        "quantity" to quantity,
        "caficultorUid" to caficultorUid
    )

    private fun DocumentSnapshot.toOrderOrNull(): Order? {
        if (!exists()) return null
        @Suppress("UNCHECKED_CAST")
        val rawItems = (get(FIELD_ITEMS) as? List<Map<String, Any?>>).orEmpty()
        return Order(
            id = id,
            compradorUid = getString(FIELD_COMPRADOR_UID).orEmpty(),
            caficultorUid = getString(FIELD_CAFICULTOR_UID).orEmpty(),
            compradorName = getString(FIELD_COMPRADOR_NAME).orEmpty(),
            items = rawItems.map {
                OrderItem(
                    productId = (it["productId"] as? String).orEmpty(),
                    productName = (it["productName"] as? String).orEmpty(),
                    unitPriceCop = (it["unitPriceCop"] as? Long)?.toInt() ?: 0,
                    quantity = (it["quantity"] as? Long)?.toInt() ?: 0,
                    caficultorUid = (it["caficultorUid"] as? String).orEmpty()
                )
            },
            totalCop = getLong(FIELD_TOTAL_COP)?.toInt() ?: 0,
            shippingCop = getLong(FIELD_SHIPPING_COP)?.toInt() ?: 0,
            shippingAddress = getString(FIELD_SHIPPING_ADDRESS).orEmpty(),
            status = OrderStatus.fromNameOrDefault(getString(FIELD_STATUS)),
            createdAtEpochMillis = getTimestamp(FIELD_CREATED_AT)?.toDate()?.time
                ?: System.currentTimeMillis()
        )
    }

    private companion object {
        const val TAG = "OrdersRemoteDS"
        const val COLLECTION_ORDERS = "orders"
        const val FIELD_COMPRADOR_UID = "compradorUid"
        const val FIELD_CAFICULTOR_UID = "caficultorUid"
        const val FIELD_COMPRADOR_NAME = "compradorName"
        const val FIELD_ITEMS = "items"
        const val FIELD_TOTAL_COP = "totalCop"
        const val FIELD_SHIPPING_COP = "shippingCop"
        const val FIELD_SHIPPING_ADDRESS = "shippingAddress"
        const val FIELD_STATUS = "status"
        const val FIELD_CREATED_AT = "createdAt"
    }
}

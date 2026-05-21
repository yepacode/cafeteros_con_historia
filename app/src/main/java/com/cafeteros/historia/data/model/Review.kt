package com.cafeteros.historia.data.model

/**
 * Reseña que un comprador deja sobre un producto después de recibirlo.
 *
 * Cada reseña referencia tanto el producto reseñado como el caficultor
 * dueño (duplicado para que las queries del caficultor no requieran join).
 *
 * Solo se pueden crear reseñas para pedidos en estado DELIVERED — la UI
 * del comprador filtra eso y la regla podría reforzarse en
 * `firestore.rules` cuando se implementen.
 *
 * @property id id del documento en Firestore (vacío al construir).
 * @property productId id del producto reseñado.
 * @property orderId id del pedido del que viene la reseña; sirve para
 *  evitar reseñas duplicadas del mismo pedido.
 * @property caficultorUid uid del dueño del producto; se duplica para que
 *  "todas las reseñas del caficultor" se pueda consultar sin join.
 * @property buyerUid uid del comprador que deja la reseña.
 * @property buyerName nombre del comprador al momento (snapshot).
 * @property rating estrellas de 1 a 5.
 * @property comment texto libre opcional.
 * @property createdAtEpochMillis fecha de creación (Firestore la
 *  sobreescribe con `serverTimestamp` al guardar).
 */
data class Review(
    val id: String = "",
    val productId: String = "",
    val orderId: String = "",
    val caficultorUid: String = "",
    val buyerUid: String = "",
    val buyerName: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)

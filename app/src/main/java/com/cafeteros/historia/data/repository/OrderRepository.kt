package com.cafeteros.historia.data.repository

import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.data.remote.firestore.OrdersRemoteDataSource
import kotlinx.coroutines.flow.Flow

/** Resultado expuesto a la UI tras crear o actualizar un pedido. */
sealed class OrderOperationResult {
    data class Success(val orderId: String) : OrderOperationResult()
    data class Error(val message: String) : OrderOperationResult()
}

/**
 * Punto único de acceso a la colección `/orders` para la capa de UI.
 *
 * Hoy es un wrapper delgado sobre [OrdersRemoteDataSource]; cuando se
 * integre la pasarela de pagos (ePayco) o el cálculo de envíos, esta
 * clase concentrará esa lógica para que los ViewModels no necesiten
 * preocuparse de la composición.
 */
class OrderRepository(
    private val ordersRemote: OrdersRemoteDataSource
) {

    /**
     * Persiste un nuevo pedido en Firestore. La UI ya debe haber
     * calculado `totalCop` y completado todos los campos del [order]
     * antes de llamar aquí.
     */
    suspend fun createOrder(order: Order): OrderOperationResult =
        runCatching {
            val id = ordersRemote.create(order)
            OrderOperationResult.Success(id)
        }.getOrElse { error ->
            OrderOperationResult.Error(error.message ?: "No se pudo crear el pedido")
        }

    /** Cambia el estado del pedido [orderId]. */
    suspend fun updateStatus(orderId: String, status: OrderStatus): OrderOperationResult =
        runCatching {
            ordersRemote.updateStatus(orderId, status)
            OrderOperationResult.Success(orderId)
        }.getOrElse { error ->
            OrderOperationResult.Error(error.message ?: "No se pudo actualizar el pedido")
        }

    /** Lectura puntual del pedido [orderId]. */
    suspend fun findById(orderId: String): Order? = ordersRemote.findById(orderId)

    /** Pedidos donde [uid] es el caficultor (vista del vendedor). */
    fun observeMySales(uid: String): Flow<List<Order>> = ordersRemote.observeAsSeller(uid)

    /** Pedidos donde [uid] es el comprador (historial del comprador). */
    fun observeMyPurchases(uid: String): Flow<List<Order>> = ordersRemote.observeAsBuyer(uid)
}

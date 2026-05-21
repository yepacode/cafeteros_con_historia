package com.cafeteros.historia.data.model

enum class OrderStatus(val label: String) {
    PENDING(label = "Pendiente"),
    ACCEPTED(label = "Aceptado"),
    PACKED(label = "Empacado"),
    SHIPPED(label = "Enviado"),
    DELIVERED(label = "Entregado"),
    CANCELLED(label = "Cancelado");

    companion object {
        fun fromNameOrDefault(name: String?): OrderStatus =
            entries.firstOrNull { it.name == name } ?: PENDING
    }
}
data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val unitPriceCop: Int = 0,
    val quantity: Int = 0,
    val caficultorUid: String = ""
) {
    val subtotalCop: Int get() = unitPriceCop * quantity
}
data class Order(
    val id: String = "",
    val compradorUid: String = "",
    val caficultorUid: String = "",
    val compradorName: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalCop: Int = 0,
    val shippingCop: Int = 0,
    val shippingAddress: String = "",
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAtEpochMillis: Long = System.currentTimeMillis()
) {
    val totalUnits: Int get() = items.sumOf { it.quantity }
}
data class CartItem(
    val productId: String,
    val quantity: Int
)

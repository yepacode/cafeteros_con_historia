package com.cafeteros.historia.data.model

/**
 * Estados por los que pasa un pedido desde que el comprador confirma el
 * pago hasta que recibe el café. El flujo lineal típico es:
 *
 * ```
 * PENDING → ACCEPTED → PACKED → SHIPPED → DELIVERED
 *                                       ↘ CANCELLED (cualquier punto)
 * ```
 *
 * @property label texto en español listo para mostrar en chips/badges.
 */
enum class OrderStatus(val label: String) {
    /** Pago confirmado pero el caficultor aún no acepta la orden. */
    PENDING(label = "Pendiente"),

    /** Caficultor confirmó disponibilidad y reservó stock. */
    ACCEPTED(label = "Aceptado"),

    /** El pedido ya está empacado y listo para entregar al courier. */
    PACKED(label = "Empacado"),

    /** En tránsito hacia el comprador. */
    SHIPPED(label = "Enviado"),

    /** El comprador recibió el pedido; habilita dejar reseña. */
    DELIVERED(label = "Entregado"),

    /** Cancelado por cualquiera de las partes (o el sistema). */
    CANCELLED(label = "Cancelado");

    companion object {
        /**
         * Convierte el `name` guardado en Firestore al enum.
         * Si llega un valor desconocido (por compatibilidad hacia atrás
         * con docs antiguos), cae a [PENDING] como valor seguro.
         */
        fun fromNameOrDefault(name: String?): OrderStatus =
            entries.firstOrNull { it.name == name } ?: PENDING
    }
}

/**
 * Línea de un pedido — una entrada por cada producto distinto que el
 * comprador agregó al carrito.
 *
 * @property productId       id del documento en `/products`.
 * @property productName     snapshot del nombre al momento de la compra
 *  (se conserva en el pedido aunque el producto luego cambie de nombre).
 * @property unitPriceCop    snapshot del precio unitario en pesos.
 * @property quantity        unidades compradas.
 * @property caficultorUid   uid del caficultor dueño del producto (denormalizado
 *  para no tener que hacer joins al listar pedidos).
 */
data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val unitPriceCop: Int = 0,
    val quantity: Int = 0,
    val caficultorUid: String = ""
) {
    /** Total de la línea: precio unitario × cantidad. */
    val subtotalCop: Int get() = unitPriceCop * quantity
}

/**
 * Pedido completo. Un comprador puede generar varias órdenes en un mismo
 * checkout (una por cada caficultor del carrito), pero cada [Order]
 * contiene productos de un único caficultor.
 *
 * @property id                   id de Firestore (asignado al crear).
 * @property compradorUid         uid del usuario que compró.
 * @property caficultorUid        uid del vendedor.
 * @property compradorName        snapshot del nombre del comprador (para
 *  mostrar al caficultor sin hacer otra query a `/users`).
 * @property items                líneas con productos comprados.
 * @property totalCop             total a pagar (suma de subtotales).
 * @property shippingCop          costo de envío (hoy `0`; se calculará
 *  según peso/distancia cuando se integre logística real).
 * @property shippingAddress      dirección de envío en texto plano.
 * @property status               estado actual del pedido (ver [OrderStatus]).
 * @property createdAtEpochMillis fecha de creación; se usa para ordenar el
 *  historial de manera descendente.
 */
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
    /** Cantidad total de unidades del pedido (suma de quantities). */
    val totalUnits: Int get() = items.sumOf { it.quantity }
}

/**
 * Ítem del carrito: solo guarda el id del producto y la cantidad. El
 * resto de la info (nombre, precio, imagen) se resuelve dinámicamente
 * desde `/products` cada vez que se renderiza, así si el caficultor
 * baja un precio antes del checkout, el comprador lo nota.
 */
data class CartItem(
    val productId: String,
    val quantity: Int
)

package com.cafeteros.historia.data.model

/**
 * Conversación 1-a-1 entre dos usuarios (típicamente comprador y caficultor).
 *
 * **ID determinístico:** el id del documento se calcula a partir de los
 * dos uids ordenados alfabéticamente unidos por "_". Esto permite a
 * cualquier parte resolver "¿existe ya una conversación entre A y B?"
 * sin queries — basta con concatenar los uids y consultar el doc.
 *
 * Estructura en Firestore:
 * ```
 * /conversations/{id} {
 *   id:               String
 *   participants:     [uidA, uidB]
 *   participantNames: { uidA: "Nombre A", uidB: "Nombre B" }
 *   lastMessage:      String         // preview en Inbox
 *   lastMessageAt:    Timestamp
 * }
 * /conversations/{id}/messages/{messageId} { ... }
 * ```
 *
 * @property participants los dos uids (siempre exactamente 2 en chat 1-1).
 * @property participantNames mapa uid → nombre para mostrar sin tener que
 *  hacer joins contra /users.
 * @property lastMessage preview del último mensaje (para la lista Inbox).
 * @property lastMessageAtEpochMillis timestamp del último mensaje. Sirve
 *  para ordenar las conversaciones en el Inbox.
 */
data class Conversation(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val participantNames: Map<String, String> = emptyMap(),
    val lastMessage: String = "",
    val lastMessageAtEpochMillis: Long = System.currentTimeMillis()
) {
    /** Nombre del otro participante visto desde [me]. */
    fun partnerName(me: String): String =
        participants.firstOrNull { it != me }?.let { participantNames[it] }.orEmpty()

    /** UID del otro participante visto desde [me]. */
    fun partnerUid(me: String): String =
        participants.firstOrNull { it != me }.orEmpty()

    companion object {
        /**
         * Genera el id determinístico para una conversación entre [uidA]
         * y [uidB]. El orden alfabético garantiza que ambas partes lleguen
         * al mismo id independientemente de quién la inicie.
         */
        fun conversationIdFor(uidA: String, uidB: String): String =
            listOf(uidA, uidB).sorted().joinToString(separator = "_")
    }
}

/**
 * Mensaje individual dentro de una [Conversation].
 *
 * @property id id del documento en `/conversations/{convId}/messages/{id}`.
 * @property senderUid uid del remitente; el receptor se infiere por
 *  exclusión del array `participants` de la conversación.
 * @property text contenido del mensaje (max 500 chars en UI por sanidad).
 * @property createdAtEpochMillis fecha de envío.
 */
data class ChatMessage(
    val id: String = "",
    val senderUid: String = "",
    val text: String = "",
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)

/**
 * Plantillas de respuesta rápida que el caficultor puede usar para
 * contestar mensajes comunes. Hardcoded para MVP — no se almacenan en
 * Firestore porque son del catálogo de la app, no del usuario.
 */
val QUICK_REPLIES: List<String> = listOf(
    "¡Hola! Gracias por contactarme. ¿En qué te puedo ayudar?",
    "Tu pedido será despachado mañana, te paso la guía cuando salga.",
    "Sí, ese café está disponible. Cuántas unidades quieres?",
    "El café de esta cosecha tiene notas a chocolate y miel.",
    "Gracias por tu compra, espero que disfrutes el café 🌱"
)

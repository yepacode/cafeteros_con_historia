package com.cafeteros.historia.ui.features.checkoutpayment.model

/**
 * Métodos de pago disponibles en el paso 2 del checkout.
 *
 * El [emoji] se renderiza como prefijo del título dentro de la card —
 * mantiene la fidelidad con el diseño sin cargar muchos íconos vectoriales.
 *
 * El [subtitle] es el texto auxiliar pequeño debajo del título; algunas
 * opciones (Daviplata) no lo muestran en el diseño.
 *
 * Cuando exista BD, este enum se mapea desde la columna `payment_methods.code`
 * o equivalente y la pantalla recibirá la lista por parámetro.
 *
 * @property code identificador estable enviado al backend al confirmar.
 * @property emoji ícono delante del título.
 * @property title etiqueta principal de la card.
 * @property subtitle texto auxiliar opcional.
 */
enum class PaymentMethod(
    val code: String,
    val emoji: String,
    val title: String,
    val subtitle: String?
) {
    TARJETA(
        code = "TARJETA",
        emoji = "💳", // 💳
        title = "Tarjeta de crédito/débito",
        subtitle = null
    ),
    PSE(
        code = "PSE",
        emoji = "🏦", // 🏦
        title = "PSE - Pagos Seguros en Línea",
        subtitle = "Paga con tu banco"
    ),
    NEQUI(
        code = "NEQUI",
        emoji = "📱", // 📱
        title = "Nequi",
        subtitle = "Paga con tu app Nequi"
    ),
    DAVIPLATA(
        code = "DAVIPLATA",
        emoji = "💜", // 💜
        title = "Daviplata",
        subtitle = null
    ),
    WOMPI(
        code = "WOMPI",
        emoji = "🏢", // 🏢
        title = "Wompi",
        subtitle = "Múltiples métodos"
    ),
    CONTRA_ENTREGA(
        code = "CONTRA_ENTREGA",
        emoji = "💵", // 💵
        title = "Contra entrega",
        subtitle = "Paga en efectivo al recibir (solo Bogotá)"
    )
}

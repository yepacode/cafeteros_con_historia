package com.cafeteros.historia.ui.features.checkoutpayment

/**
 * Pasarela de pago **simulada** usada por el front mientras no exista backend.
 *
 * Modela el resultado de presionar "Confirmar y pagar" con un sencillo
 * alternador determinístico: la primera invocación retorna [Result.SUCCESS],
 * la siguiente [Result.FAILURE], y así sucesivamente. Esto permite probar
 * de forma fiable los dos flujos (éxito y fallo) sin depender del azar — el
 * usuario sabe que un segundo tap reproduce el caso contrario.
 *
 * Cuando exista backend real, esta clase se reemplaza por una llamada a
 * `paymentService.charge(orderId, paymentChoice, roleId)` que devuelve un
 * resultado equivalente. La pantalla de revisión de pedido seguirá ramificando
 * a [com.cafeteros.historia.ui.features.paymentsuccess.PaymentSuccessActivity]
 * o [com.cafeteros.historia.ui.features.paymentfailed.PaymentFailedActivity]
 * según el resultado, así que solo cambia la fuente, no la rama.
 */
object MockPaymentGateway {

    /** Posibles resultados de un intento de pago. */
    enum class Result { SUCCESS, FAILURE }

    private var nextResult: Result = Result.SUCCESS

    /**
     * Devuelve el resultado del próximo "intento de pago" simulado y
     * automáticamente alterna el estado interno para el siguiente.
     */
    fun simulatePayment(): Result {
        val current = nextResult
        nextResult = if (current == Result.SUCCESS) Result.FAILURE else Result.SUCCESS
        return current
    }
}

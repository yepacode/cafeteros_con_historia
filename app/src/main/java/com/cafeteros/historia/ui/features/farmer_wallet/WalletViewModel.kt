package com.cafeteros.historia.ui.features.farmer_wallet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.data.repository.OrderRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Estado UI de la billetera del caficultor.
 *
 * **Nota MVP:** no tenemos integración real con pasarela de pagos
 * (ePayco/Mercado Pago/etc.). Por eso "saldo disponible" se calcula como
 * la suma de pedidos ya entregados y "pendiente" como los que aún están
 * procesándose. Cuando se conecte la pasarela, el saldo real saldrá del
 * estado contable de la cuenta del caficultor y los pedidos solo
 * informarán "venta exitosa".
 *
 * @param availableCop suma de `totalCop` de orders en estado DELIVERED.
 * @param pendingCop suma de `totalCop` de orders en proceso (ACCEPTED,
 *  PACKED, SHIPPED) — dinero que ya entró pero aún no se libera.
 * @param monthRevenueCop total ganado en los últimos 30 días (DELIVERED).
 * @param deliveredCount cantidad de pedidos entregados (histórico).
 * @param movements lista de pedidos como movimientos contables, ordenados
 *  por fecha descendente. Los DELIVERED son ingresos confirmados; el
 *  resto son ingresos en proceso.
 */
data class WalletUiState(
    val availableCop: Long = 0L,
    val pendingCop: Long = 0L,
    val monthRevenueCop: Long = 0L,
    val deliveredCount: Int = 0,
    val movements: List<Order> = emptyList()
)

/**
 * ViewModel de la billetera del caficultor. Deriva todas las métricas de
 * `/orders` — no hay una colección `/wallets` separada porque sería un
 * cache redundante.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WalletViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val orderRepository: OrderRepository = app.orderRepository

    val uiState: StateFlow<WalletUiState> = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else orderRepository.observeMySales(uid)
        }
        .map { orders -> orders.toWalletState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = WalletUiState()
        )

    private fun List<Order>.toWalletState(): WalletUiState {
        val delivered = filter { it.status == OrderStatus.DELIVERED }
        val pending = filter {
            it.status in setOf(OrderStatus.ACCEPTED, OrderStatus.PACKED, OrderStatus.SHIPPED)
        }
        val available = delivered.sumOf { it.totalCop.toLong() }
        val pendingValue = pending.sumOf { it.totalCop.toLong() }
        val thirtyDaysAgo = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000
        val monthRev = delivered
            .filter { it.createdAtEpochMillis >= thirtyDaysAgo }
            .sumOf { it.totalCop.toLong() }
        return WalletUiState(
            availableCop = available,
            pendingCop = pendingValue,
            monthRevenueCop = monthRev,
            deliveredCount = delivered.size,
            movements = (delivered + pending).sortedByDescending { it.createdAtEpochMillis }
        )
    }
}

package com.cafeteros.historia.ui.features.buyer_catalog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.repository.OrderRepository
import com.cafeteros.historia.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel de [MyPurchasesActivity]: observa los pedidos donde el usuario
 * logueado es el COMPRADOR. Es el espejo de
 * [com.cafeteros.historia.ui.features.farmer_sales.SalesViewModel] pero
 * desde el otro lado de la transacción.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MyPurchasesViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val orderRepository: OrderRepository = app.orderRepository

    val orders: StateFlow<List<Order>> = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else orderRepository.observeMyPurchases(uid)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}

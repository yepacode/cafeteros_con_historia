package com.cafeteros.historia.ui.features.checkoutshipping

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.local.cart.CartStore
import com.cafeteros.historia.data.model.Order
import com.cafeteros.historia.data.model.OrderItem
import com.cafeteros.historia.data.model.OrderStatus
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.model.SavedAddress
import com.cafeteros.historia.data.repository.AddressRepository
import com.cafeteros.historia.data.repository.NotificationRepository
import com.cafeteros.historia.data.repository.OrderOperationResult
import com.cafeteros.historia.data.repository.OrderRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.features.paymentsuccess.PaymentSuccessActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Estado UI del checkout. */
data class CheckoutUiState(
    val lines: List<CheckoutLine> = emptyList(),
    val totalCop: Long = 0L,
    val savedAddresses: List<SavedAddress> = emptyList(),
    val isProcessing: Boolean = false
)

data class CheckoutLine(
    val product: Product,
    val quantity: Int
) {
    val subtotalCop: Int get() = product.priceCop * quantity
}

sealed class CheckoutOutcome {
    data class Success(
        val orderId: String,
        val totalCop: Long,
        val address: String
    ) : CheckoutOutcome()
    data class Error(val message: String) : CheckoutOutcome()
}

/**
 * ViewModel del checkout. Carga los productos del carrito (resuelve los
 * `productId` con [ProductRepository.findById]), expone direcciones
 * guardadas para preseleccionar y arma la(s) `Order` al confirmar.
 *
 * **Multi-caficultor:** si el carrito tiene productos de distintos
 * caficultores, se crea UNA `Order` por cada uno (todos en la misma
 * transacción lógica de checkout). Cada caficultor recibirá su
 * notificación independiente.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val productRepository: ProductRepository = app.productRepository
    private val orderRepository: OrderRepository = app.orderRepository
    private val userRepository: UserRepository = app.userRepository
    private val addressRepository: AddressRepository = app.addressRepository
    private val notificationRepository: NotificationRepository = app.notificationRepository

    private val productCache = mutableMapOf<String, Product?>()

    private val _isProcessing = MutableStateFlow(false)
    private val _outcome = MutableStateFlow<CheckoutOutcome?>(null)
    val outcome: StateFlow<CheckoutOutcome?> = _outcome.asStateFlow()

    /** Direcciones del comprador para preseleccionar la default. */
    private val addressesFlow = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else addressRepository.observeMyAddresses(uid)
        }

    val uiState: StateFlow<CheckoutUiState> = kotlinx.coroutines.flow.combine(
        CartStore.items,
        addressesFlow,
        _isProcessing
    ) { items, addresses, processing ->
        // Resuelve productos faltantes en cache.
        items.forEach { item ->
            if (item.productId !in productCache) {
                productCache[item.productId] = productRepository.findById(item.productId)
            }
        }
        val lines = items.mapNotNull { item ->
            val product = productCache[item.productId] ?: return@mapNotNull null
            CheckoutLine(product = product, quantity = item.quantity)
        }
        CheckoutUiState(
            lines = lines,
            totalCop = lines.sumOf { it.subtotalCop.toLong() },
            savedAddresses = addresses,
            isProcessing = processing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CheckoutUiState()
    )

    /**
     * Crea la(s) Order(s) en Firestore (una por cada caficultor del
     * carrito), notifica a cada caficultor y limpia el [CartStore].
     */
    fun confirmCheckout(addressText: String) {
        if (_isProcessing.value) return
        val state = uiState.value
        if (state.lines.isEmpty()) return
        if (addressText.isBlank()) {
            _outcome.value = CheckoutOutcome.Error("Escribe una dirección de envío.")
            return
        }
        _isProcessing.value = true

        viewModelScope.launch {
            val buyer = userRepository.getCurrentUser()
            if (buyer == null) {
                _isProcessing.value = false
                _outcome.value = CheckoutOutcome.Error("Tu sesión expiró.")
                return@launch
            }

            // Agrupar líneas por caficultorUid para crear una Order por cada.
            val byFarmer = state.lines.groupBy { it.product.caficultorUid }
            var firstOrderId = ""
            var anyError: String? = null
            var grandTotal = 0L

            for ((farmerUid, lines) in byFarmer) {
                val items = lines.map { line ->
                    OrderItem(
                        productId = line.product.id,
                        productName = line.product.name,
                        unitPriceCop = line.product.priceCop,
                        quantity = line.quantity,
                        caficultorUid = farmerUid
                    )
                }
                val orderTotal = items.sumOf { it.subtotalCop }
                grandTotal += orderTotal
                val order = Order(
                    compradorUid = buyer.id,
                    caficultorUid = farmerUid,
                    compradorName = buyer.name,
                    items = items,
                    totalCop = orderTotal,
                    shippingCop = 0,
                    shippingAddress = addressText.trim(),
                    status = OrderStatus.PENDING
                )
                when (val result = orderRepository.createOrder(order)) {
                    is OrderOperationResult.Success -> {
                        if (firstOrderId.isBlank()) firstOrderId = result.orderId
                        notificationRepository.notifyNewOrder(
                            caficultorUid = farmerUid,
                            buyerName = buyer.name,
                            orderId = result.orderId,
                            totalCop = orderTotal
                        )
                    }
                    is OrderOperationResult.Error -> {
                        anyError = result.message
                    }
                }
            }

            _isProcessing.value = false
            if (anyError != null && firstOrderId.isBlank()) {
                _outcome.value = CheckoutOutcome.Error(anyError ?: "Error al crear pedido")
            } else {
                CartStore.clear()
                _outcome.value = CheckoutOutcome.Success(
                    orderId = firstOrderId,
                    totalCop = grandTotal,
                    address = addressText.trim()
                )
            }
        }
    }

    fun consumeOutcome() {
        _outcome.value = null
    }
}

/**
 * Activity de checkout en UNA pantalla: dirección + items + total +
 * botón "Confirmar pago". Reemplaza la antigua secuencia de 3 pantallas
 * (shipping → payment → review) por algo simple y funcional.
 */
class ShippingAddressActivity : ComponentActivity() {

    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val outcome by viewModel.outcome.collectAsStateWithLifecycle()

                LaunchedEffect(outcome) {
                    when (val o = outcome) {
                        is CheckoutOutcome.Success -> {
                            PaymentSuccessActivity.start(
                                context = this@ShippingAddressActivity,
                                orderId = o.orderId,
                                totalCop = o.totalCop,
                                address = o.address
                            )
                            viewModel.consumeOutcome()
                            finish()
                        }
                        is CheckoutOutcome.Error -> {
                            Toast.makeText(this@ShippingAddressActivity, o.message, Toast.LENGTH_LONG).show()
                            viewModel.consumeOutcome()
                        }
                        null -> Unit
                    }
                }

                CheckoutScreen(
                    state = state,
                    onBack = ::finish,
                    onConfirm = viewModel::confirmCheckout
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ShippingAddressActivity::class.java))
        }
    }
}

@Composable
private fun CheckoutScreen(
    state: CheckoutUiState,
    onBack: () -> Unit,
    onConfirm: (addressText: String) -> Unit
) {
    // Si hay direcciones guardadas, prellena con la default.
    val initialAddress = state.savedAddresses.firstOrNull { it.isDefault }
        ?: state.savedAddresses.firstOrNull()
    var addressText by remember(initialAddress) {
        mutableStateOf(initialAddress?.line.orEmpty())
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)
        .systemBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandColors.TextPrimary
                )
            }
            Text(
                text = "Confirmar pedido",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            // Dirección
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "DIRECCIÓN DE ENVÍO",
                    color = BrandColors.TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
                        .padding(BrandSpacing.md)
                ) {
                    if (addressText.isBlank()) {
                        Text(
                            text = "Calle 5 # 12-34, Bogotá",
                            color = BrandColors.TextSecondary,
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                    BasicTextField(
                        value = addressText,
                        onValueChange = { addressText = it },
                        textStyle = TextStyle(
                            color = BrandColors.TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        ),
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                    )
                }
                if (state.savedAddresses.isNotEmpty()) {
                    SavedAddressesRow(
                        addresses = state.savedAddresses,
                        onPick = { addressText = it.line }
                    )
                }
            }

            // Items
            Text(
                text = "PRODUCTOS (${state.lines.size})",
                color = BrandColors.TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.lines.isEmpty()) {
                    Text(
                        text = "Tu carrito está vacío.",
                        color = BrandColors.TextSecondary,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic
                    )
                } else {
                    state.lines.forEachIndexed { index, line ->
                        if (index > 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(BrandColors.DividerLine)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "${line.quantity}× ${line.product.name}",
                                modifier = Modifier.weight(1f),
                                color = BrandColors.TextPrimary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "$" + "%,d".format(line.subtotalCop).replace(',', '.'),
                                color = BrandColors.TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CoffeeBrown, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TOTAL A PAGAR",
                    color = BrandColors.CreamWhiteMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "$" + "%,d".format(state.totalCop).replace(',', '.'),
                    color = BrandColors.CreamWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }

            Text(
                text = "💳 Por ahora el pago es simulado; cuando se conecte la pasarela de pagos (ePayco) tu cobro será real.",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }

        Button(
            onClick = { onConfirm(addressText) },
            enabled = !state.isProcessing && state.lines.isNotEmpty() && addressText.isNotBlank(),
            modifier = Modifier
                .padding(BrandSpacing.md)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.CoffeeBrown,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (state.isProcessing) "Procesando…" else "Confirmar y pagar",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SavedAddressesRow(
    addresses: List<SavedAddress>,
    onPick: (SavedAddress) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Direcciones guardadas",
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        addresses.forEach { addr ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(8.dp))
                    .clickable { onPick(addr) }
                    .padding(BrandSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = BrandColors.FarmerPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = addr.label,
                        color = BrandColors.TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = addr.line,
                        color = BrandColors.TextSecondary,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
                if (addr.isDefault) {
                    Text(
                        text = "DEFAULT",
                        color = BrandColors.FarmerPrimary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

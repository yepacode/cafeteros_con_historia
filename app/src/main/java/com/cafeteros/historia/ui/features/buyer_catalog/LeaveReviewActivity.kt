package com.cafeteros.historia.ui.features.buyer_catalog

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.cafeteros.historia.data.model.Review
import com.cafeteros.historia.data.repository.NotificationRepository
import com.cafeteros.historia.data.repository.OrderRepository
import com.cafeteros.historia.data.repository.ReviewOperationResult
import com.cafeteros.historia.data.repository.ReviewRepository
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel del formulario "Dejar reseña".
 *
 * Carga el pedido por id para conocer al caficultor y el producto, y al
 * confirmar crea el documento de reseña en Firestore. Toma snapshots del
 * caficultorUid e item para no necesitar joins luego.
 */
class LeaveReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val orderRepository: OrderRepository = app.orderRepository
    private val reviewRepository: ReviewRepository = app.reviewRepository
    private val userRepository: UserRepository = app.userRepository
    private val notificationRepository: NotificationRepository = app.notificationRepository

    /** Para validar que NO se haya reseñado ya este pedido (idempotente). */
    private val _alreadyReviewed = MutableStateFlow(false)
    val alreadyReviewed: StateFlow<Boolean> = _alreadyReviewed.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _outcome = MutableStateFlow<ReviewOutcome?>(null)
    val outcome: StateFlow<ReviewOutcome?> = _outcome.asStateFlow()

    sealed class ReviewOutcome {
        data object Success : ReviewOutcome()
        data class Error(val message: String) : ReviewOutcome()
    }

    /**
     * Llamada al entrar a la pantalla para chequear si el comprador ya
     * dejó reseña sobre [orderId]. La UI bloquea el formulario si sí.
     */
    fun checkAlreadyReviewed(orderId: String) {
        viewModelScope.launch {
            val uid = userRepository.currentUid() ?: return@launch
            val existing = reviewRepository.findReviewForOrder(uid, orderId)
            _alreadyReviewed.value = existing != null
        }
    }

    /** Guarda la reseña en Firestore. */
    fun submit(orderId: String, rating: Int, comment: String) {
        if (_isSaving.value) return
        if (rating !in 1..5) {
            _outcome.value = ReviewOutcome.Error("Elige una calificación de 1 a 5 estrellas.")
            return
        }
        _isSaving.value = true
        viewModelScope.launch {
            val buyer = userRepository.getCurrentUser()
            if (buyer == null) {
                _isSaving.value = false
                _outcome.value = ReviewOutcome.Error("Tu sesión expiró.")
                return@launch
            }
            val order = orderRepository.findById(orderId)
            if (order == null) {
                _isSaving.value = false
                _outcome.value = ReviewOutcome.Error("No encontramos el pedido.")
                return@launch
            }
            // El pedido puede tener varios items; en MVP reseñamos el
            // primero (los pedidos del catálogo simple tienen 1 solo item).
            val firstItem = order.items.firstOrNull()
            val review = Review(
                productId = firstItem?.productId.orEmpty(),
                orderId = orderId,
                caficultorUid = order.caficultorUid,
                buyerUid = buyer.id,
                buyerName = buyer.name,
                rating = rating,
                comment = comment.trim()
            )
            val result = reviewRepository.createReview(review)
            _isSaving.value = false
            _outcome.value = when (result) {
                is ReviewOperationResult.Success -> {
                    // Notificar al caficultor de la nueva reseña recibida.
                    notificationRepository.notifyNewReview(
                        caficultorUid = order.caficultorUid,
                        buyerName = buyer.name,
                        rating = rating,
                        reviewId = result.reviewId
                    )
                    ReviewOutcome.Success
                }
                is ReviewOperationResult.Error -> ReviewOutcome.Error(result.message)
            }
        }
    }

    fun consumeOutcome() {
        _outcome.value = null
    }
}

/**
 * Activity simple para dejar una reseña sobre un pedido entregado. El
 * comprador llega aquí desde [MyPurchasesActivity] cuando toca "Dejar
 * reseña" en un pedido en estado DELIVERED.
 */
class LeaveReviewActivity : ComponentActivity() {

    private val viewModel: LeaveReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val orderId = intent.getStringExtra(EXTRA_ORDER_ID).orEmpty()
        setContent {
            CafeterosTheme {
                val alreadyReviewed by viewModel.alreadyReviewed.collectAsStateWithLifecycle()
                val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
                val outcome by viewModel.outcome.collectAsStateWithLifecycle()

                LaunchedEffect(orderId) {
                    if (orderId.isNotBlank()) viewModel.checkAlreadyReviewed(orderId)
                }

                LaunchedEffect(outcome) {
                    when (val o = outcome) {
                        is LeaveReviewViewModel.ReviewOutcome.Success -> {
                            Toast.makeText(this@LeaveReviewActivity, "¡Reseña enviada!", Toast.LENGTH_SHORT).show()
                            viewModel.consumeOutcome()
                            finish()
                        }
                        is LeaveReviewViewModel.ReviewOutcome.Error -> {
                            Toast.makeText(this@LeaveReviewActivity, o.message, Toast.LENGTH_LONG).show()
                            viewModel.consumeOutcome()
                        }
                        null -> Unit
                    }
                }

                LeaveReviewScreen(
                    alreadyReviewed = alreadyReviewed,
                    isSaving = isSaving,
                    onBack = ::finish,
                    onSubmit = { rating, comment -> viewModel.submit(orderId, rating, comment) }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_ORDER_ID = "extra_order_id"

        fun start(context: Context, orderId: String) {
            val intent = Intent(context, LeaveReviewActivity::class.java)
                .putExtra(EXTRA_ORDER_ID, orderId)
            context.startActivity(intent)
        }
    }
}

@Composable
private fun LeaveReviewScreen(
    alreadyReviewed: Boolean,
    isSaving: Boolean,
    onBack: () -> Unit,
    onSubmit: (rating: Int, comment: String) -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
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
                text = "Dejar reseña",
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
                .padding(BrandSpacing.lg)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            if (alreadyReviewed) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandColors.InfoBannerBackground, RoundedCornerShape(12.dp))
                        .padding(BrandSpacing.md)
                ) {
                    Text(
                        text = "✓ Ya dejaste reseña para este pedido. ¡Gracias!",
                        color = BrandColors.InfoBannerAction,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                return@Column
            }

            Text(
                text = "¿Cómo calificarías este pedido?",
                color = BrandColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            )

            // 5 estrellas tappables
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                repeat(5) { i ->
                    val starIndex = i + 1
                    val filled = starIndex <= rating
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "$starIndex estrellas",
                        tint = if (filled) Color(0xFFC9A24A) else BrandColors.IndicatorInactive,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(enabled = !isSaving) { rating = starIndex }
                    )
                }
            }

            Text(
                text = "Comentario (opcional)",
                color = BrandColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md)
            ) {
                BasicTextField(
                    value = comment,
                    onValueChange = { if (it.length <= 500) comment = it },
                    textStyle = TextStyle(
                        color = BrandColors.TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                if (comment.isBlank()) {
                    Text(
                        text = "Cuéntale al caficultor qué tal estuvo el café...",
                        color = BrandColors.TextSecondary,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(BrandSpacing.md))

            Button(
                onClick = { onSubmit(rating, comment) },
                enabled = !isSaving && rating > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.CoffeeBrown,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (isSaving) "Enviando…" else "Enviar reseña",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

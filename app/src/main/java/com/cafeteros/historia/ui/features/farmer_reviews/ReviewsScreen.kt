package com.cafeteros.historia.ui.features.farmer_reviews

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Review
import com.cafeteros.historia.data.repository.ReviewRepository
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** ViewModel: observa las reseñas que el caficultor logueado ha recibido. */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ReviewsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val reviewRepository: ReviewRepository = app.reviewRepository

    val reviews: StateFlow<List<Review>> = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else reviewRepository.observeReceivedReviews(uid)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}

/**
 * Activity contenedora de la pantalla "Mis Reseñas" del caficultor. Las
 * acciones (responder reseña, marcar útil) quedan fuera del MVP — la
 * tarjeta solo muestra info, no permite responder por ahora.
 */
class ReviewsActivity : ComponentActivity() {

    private val viewModel: ReviewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val reviews by viewModel.reviews.collectAsStateWithLifecycle()
                ReviewsScreen(reviews = reviews, onBack = ::finish)
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ReviewsActivity::class.java))
        }
    }
}

@Composable
private fun ReviewsScreen(
    reviews: List<Review>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        TopBar(count = reviews.size, average = averageRating(reviews), onBack = onBack)

        if (reviews.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = BrandSpacing.lg,
                    vertical = BrandSpacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                items(reviews) { review -> ReviewCard(review = review) }
            }
        }
    }
}

@Composable
private fun TopBar(count: Int, average: Double, onBack: () -> Unit) {
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Mis Reseñas",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            if (count > 0) {
                Text(
                    text = "★ ${"%.1f".format(average)} · $count reseña${if (count == 1) "" else "s"}",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(BrandColors.InputBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = Color(0xFFC9A24A),
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Aún sin reseñas",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cuando un comprador reciba un pedido y lo califique, verás su reseña aquí.",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = BrandSpacing.sm)) {
                Text(
                    text = review.buyerName.ifBlank { "Comprador" },
                    color = BrandColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = formatDate(review.createdAtEpochMillis),
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
            StarsRow(rating = review.rating)
        }
        if (review.comment.isNotBlank()) {
            Text(
                text = "\"${review.comment}\"",
                color = BrandColors.TextPrimary,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun StarsRow(rating: Int) {
    Row {
        repeat(5) { i ->
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = if (i < rating) Color(0xFFC9A24A) else BrandColors.IndicatorInactive,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

private fun averageRating(reviews: List<Review>): Double {
    if (reviews.isEmpty()) return 0.0
    return reviews.sumOf { it.rating.toDouble() } / reviews.size
}

private fun formatDate(epochMillis: Long): String {
    val fmt = SimpleDateFormat("dd MMM yyyy", Locale("es", "CO"))
    return fmt.format(Date(epochMillis))
}

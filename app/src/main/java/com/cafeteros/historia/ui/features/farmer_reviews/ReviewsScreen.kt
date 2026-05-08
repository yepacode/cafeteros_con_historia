package com.cafeteros.historia.ui.features.farmer_reviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

data class MockReview(
    val id: String,
    val author: String,
    val stars: Int,
    val date: String,
    val productTag: String,
    val body: String,
    val photosCount: Int = 0,
    val helpfulCount: Int = 0,
    val authorReply: String? = null
)

val MOCK_REVIEWS: List<MockReview> = listOf(
    MockReview(
        id = "r1",
        author = "María G.",
        stars = 5,
        date = "12 OCT 2025",
        productTag = "CAFÉ HUILA PITALITO",
        body = "Excelente aroma y tueste, se nota el cuidado en el proceso. Llegó muy rápido a Bogotá.",
        photosCount = 2,
        helpfulCount = 5
    ),
    MockReview(
        id = "r2",
        author = "Carlos R.",
        stars = 4,
        date = "08 OCT 2025",
        productTag = "CAFÉ NARIÑO",
        body = "Muy buen sabor, aunque el empaque llegó un poco arrugado. El café delicioso.",
        helpfulCount = 0,
        authorReply = "Muchas gracias Carlos, tendremos más cuidado con el transporte."
    )
)

private val FREQUENT_THEMES = listOf(
    "Aroma increíble" to 47,
    "Empaque impecable" to 38,
    "Tueste medio" to 22,
    "Envío rápido" to 19
)

private enum class ReviewsTab(val label: String) {
    ALL("Todas"), WITH_PHOTOS("Con foto"), FIVE_STARS("5 estrellas"), UNANSWERED("Sin responder")
}

class ReviewsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                ReviewsScreen(
                    onBack = ::finish,
                    onReply = { id -> ReplyReviewActivity.start(this, id) }
                )
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
fun ReviewsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onReply: (String) -> Unit
) {
    var activeTab by remember { mutableStateOf(ReviewsTab.ALL) }
    val filtered = when (activeTab) {
        ReviewsTab.ALL -> MOCK_REVIEWS
        ReviewsTab.WITH_PHOTOS -> MOCK_REVIEWS.filter { it.photosCount > 0 }
        ReviewsTab.FIVE_STARS -> MOCK_REVIEWS.filter { it.stars == 5 }
        ReviewsTab.UNANSWERED -> MOCK_REVIEWS.filter { it.authorReply == null }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.Menu, contentDescription = "Volver", tint = BrandColors.TextPrimary)
            }
            Text(
                text = "Reseñas",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(onClick = { /* TODO filtro */ }) {
                Icon(Icons.Outlined.FilterList, contentDescription = "Filtros", tint = BrandColors.TextPrimary)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = BrandSpacing.lg, vertical = BrandSpacing.sm),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            item { OverallRatingCard(score = 4.9f, totalReviews = 127) }
            item {
                Text(
                    text = "Temas frecuentes",
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
                )
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                    FREQUENT_THEMES.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                            row.forEach { (theme, count) ->
                                Box(
                                    modifier = Modifier
                                        .background(BrandColors.CardBackground, RoundedCornerShape(50))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(text = "$theme ($count)", color = BrandColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                    ReviewsTab.entries.forEach { t ->
                        val active = t == activeTab
                        Box(
                            modifier = Modifier
                                .background(if (active) BrandColors.CoffeeBrown else BrandColors.CardBackground, RoundedCornerShape(50))
                                .clickable { activeTab = t }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = t.label,
                                color = if (active) BrandColors.PrimaryButtonText else BrandColors.TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
            items(filtered) { r ->
                ReviewCard(review = r, onReply = { onReply(r.id) })
            }
            item { Spacer(modifier = Modifier.height(BrandSpacing.lg)) }
        }
    }
}

@Composable
private fun OverallRatingCard(score: Float, totalReviews: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(16.dp))
            .padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "%.1f".format(score),
            style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC9A24A))
        )
        Row {
            repeat(5) {
                Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFC9A24A), modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "$totalReviews reseñas", color = BrandColors.TextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        listOf(5 to 0.85f, 4 to 0.12f, 3 to 0.02f, 2 to 0.0f, 1 to 0.0f).forEach { (stars, pct) ->
            StarBar(stars = stars, percent = pct)
        }
    }
}

@Composable
private fun StarBar(stars: Int, percent: Float) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stars.toString(), color = BrandColors.TextSecondary, fontSize = 11.sp, modifier = Modifier.width(14.dp))
        Spacer(modifier = Modifier.size(4.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .background(BrandColors.IndicatorInactive, RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent)
                    .height(6.dp)
                    .background(Color(0xFFC9A24A), RoundedCornerShape(3.dp))
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "${(percent * 100).toInt()}%", color = BrandColors.TextSecondary, fontSize = 11.sp, modifier = Modifier.width(32.dp))
    }
}

@Composable
private fun ReviewCard(review: MockReview, onReply: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Cabecera
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).background(BrandColors.FarmerPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
                Text(text = review.author, color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(text = review.date, color = BrandColors.TextSecondary, fontSize = 10.sp)
            }
            Row { repeat(review.stars) { Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFC9A24A), modifier = Modifier.size(14.dp)) } }
        }

        // Producto tag
        Box(
            modifier = Modifier
                .background(Color(0xFFE8F4EC), RoundedCornerShape(50))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(text = review.productTag, color = BrandColors.FarmerPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        }

        Text(text = review.body, color = BrandColors.TextPrimary, fontSize = 13.sp, lineHeight = 17.sp)

        if (review.photosCount > 0) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(review.photosCount) {
                    Box(
                        modifier = Modifier.size(56.dp).background(BrandColors.InputBackground, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text(text = "📷", fontSize = 18.sp) }
                }
            }
        }

        if (review.authorReply != null) {
            // Respuesta del autor (caficultor)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.AuthBackground, RoundedCornerShape(8.dp))
                    .padding(BrandSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Don Alberto", color = BrandColors.TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.size(4.dp))
                    Box(
                        modifier = Modifier
                            .background(BrandColors.FarmerPrimary, RoundedCornerShape(50))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(text = "AUTOR", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text(text = review.authorReply, color = BrandColors.TextSecondary, fontSize = 12.sp, lineHeight = 15.sp)
                Text(
                    text = "Editar",
                    color = BrandColors.FarmerPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = onReply)
                )
            }
        }

        // Footer: helpful + responder
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, tint = BrandColors.TextSecondary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = if (review.helpfulCount > 0) "${review.helpfulCount} personas útil"
                        else "Marcar útil",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
            if (review.authorReply == null) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFC9A24A), RoundedCornerShape(50))
                        .clickable(onClick = onReply)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(text = "RESPONDER", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                }
            } else {
                Text(text = "Respondida", color = BrandColors.FarmerPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

package com.cafeteros.historia.ui.features.farmer_reviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

private const val MAX_REPLY_CHARS = 500

private val REPLY_TEMPLATES: List<Pair<String, String>> = listOf(
    "Agradecimiento corto" to "¡Muchas gracias por tu reseña! Nos alegra saber que disfrutaste el café.",
    "Respuesta cálida" to "¡Gracias, $0! Tu apoyo significa mucho para mi finca. Esperamos verte pronto."
)

class ReplyReviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val reviewId = intent.getStringExtra(EXTRA_REVIEW_ID) ?: ""
        val review = MOCK_REVIEWS.firstOrNull { it.id == reviewId }
        setContent {
            CafeterosTheme {
                ReplyReviewScreen(
                    review = review,
                    onDismiss = ::finish,
                    onPublish = {
                        Toast.makeText(this, "Respuesta publicada (próximamente con backend)", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onSaveDraft = {
                        Toast.makeText(this, "Borrador guardado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_REVIEW_ID = "extra_review_id"
        fun start(context: Context, reviewId: String) {
            context.startActivity(Intent(context, ReplyReviewActivity::class.java).putExtra(EXTRA_REVIEW_ID, reviewId))
        }
    }
}

@Composable
fun ReplyReviewScreen(
    modifier: Modifier = Modifier,
    review: MockReview?,
    onDismiss: () -> Unit,
    onPublish: () -> Unit,
    onSaveDraft: () -> Unit
) {
    var draft by remember { mutableStateOf(review?.authorReply.orEmpty()) }
    val canPublish = draft.isNotBlank() && draft.length <= MAX_REPLY_CHARS

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
            IconButton(onClick = onDismiss) {
                Icon(Icons.Outlined.Close, contentDescription = "Cerrar", tint = BrandColors.TextPrimary)
            }
            Text(
                text = "Responder reseña",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
            TextButton(onClick = onPublish, enabled = canPublish) {
                Text(text = "Publicar", color = if (canPublish) BrandColors.FarmerPrimary else BrandColors.TextSecondary, fontWeight = FontWeight.Bold)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            // Card de la reseña original
            if (review != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                        .padding(BrandSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = review.author, color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.size(8.dp))
                        Row { repeat(review.stars) { Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFC9A24A), modifier = Modifier.size(14.dp)) } }
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE8F4EC), RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = review.productTag, color = BrandColors.FarmerPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    }
                    Text(text = "\"${review.body}\"", color = BrandColors.TextPrimary, fontSize = 13.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, lineHeight = 17.sp)
                }
            }

            // Tu respuesta
            Text(
                text = "Tu respuesta pública",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md)
            ) {
                if (draft.isEmpty()) {
                    Text(text = "Agradece y genera conexión", color = BrandColors.InputHint, fontSize = 13.sp)
                }
                BasicTextField(
                    value = draft,
                    onValueChange = { if (it.length <= MAX_REPLY_CHARS) draft = it },
                    textStyle = TextStyle(color = BrandColors.TextPrimary, fontSize = 13.sp, lineHeight = 18.sp),
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )
            }
            Text(
                text = "${draft.length}/$MAX_REPLY_CHARS",
                color = BrandColors.TextSecondary,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )

            // Buenas prácticas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "💡 Buenas prácticas al responder", color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                listOf(
                    "Agradece siempre su tiempo y preferencia.",
                    "Sé personal, menciona detalles de su comentario.",
                    "Si hubo problema, reconócelo humildemente.",
                    "Ofrece solución clara y directa.",
                    "Invítalo a volver a vivir la experiencia."
                ).forEach { tip ->
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = BrandColors.FarmerPrimary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(text = tip, color = BrandColors.TextPrimary, fontSize = 11.sp, lineHeight = 14.sp)
                    }
                }
            }

            // Plantillas
            Text(
                text = "Plantillas",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                REPLY_TEMPLATES.forEach { (label, template) ->
                    Box(
                        modifier = Modifier
                            .background(BrandColors.CardBackground, RoundedCornerShape(50))
                            .clickable {
                                draft = template.replace("\$0", review?.author?.substringBefore('.').orEmpty())
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = label, color = BrandColors.TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Preview
            Text(
                text = "Así se verá tu respuesta:",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md)
            ) {
                Text(
                    text = if (draft.isBlank()) "Empieza a escribir para previsualizar tu respuesta aquí." else draft,
                    color = if (draft.isBlank()) BrandColors.TextSecondary else BrandColors.TextPrimary,
                    fontSize = 12.sp,
                    fontStyle = if (draft.isBlank()) androidx.compose.ui.text.font.FontStyle.Italic else androidx.compose.ui.text.font.FontStyle.Normal,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            Button(
                onClick = onPublish,
                enabled = canPublish,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.FarmerPrimary,
                    contentColor = Color.White,
                    disabledContainerColor = BrandColors.IndicatorInactive
                )
            ) {
                Text(text = "Publicar respuesta", fontWeight = FontWeight.SemiBold)
            }
            TextButton(onClick = onSaveDraft, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Guardar como borrador", color = BrandColors.TextSecondary, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

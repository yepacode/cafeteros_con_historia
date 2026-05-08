package com.cafeteros.historia.ui.features.farmer_help

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

private data class FaqGroup(val emoji: String, val title: String, val questions: List<String>)

private val MOCK_FAQS = listOf(
    FaqGroup("🚀", "Empezando", listOf(
        "¿Cómo verifico mi cuenta?",
        "¿Cuánto tarda la verificación?",
        "¿Qué documentos necesito?"
    )),
    FaqGroup("📦", "Mis productos", listOf(
        "¿Cómo publico un producto?",
        "¿Cómo tomar buenas fotos?",
        "¿Cómo escribir descripciones que vendan?"
    )),
    FaqGroup("🛒", "Ventas", listOf(
        "¿Cómo se ve un nuevo pedido?",
        "¿Cómo marco un pedido como despachado?"
    )),
    FaqGroup("💰", "Dinero", listOf(
        "¿Cómo retiro mi dinero?",
        "¿Cuál es la comisión de Origen?"
    ))
)

class HelpAndSupportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                HelpAndSupportScreen(
                    onBack = ::finish,
                    onAction = { Toast.makeText(this, "Próximamente: $it", Toast.LENGTH_SHORT).show() }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, HelpAndSupportActivity::class.java))
        }
    }
}

@Composable
fun HelpAndSupportScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onAction: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver", tint = BrandColors.TextPrimary)
            }
            Text(
                text = "Ayuda y Soporte",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(onClick = { onAction("Más opciones") }) {
                Icon(Icons.Outlined.MoreVert, contentDescription = "Más", tint = BrandColors.TextPrimary)
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
            // Buscador
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(50))
                    .clickable { onAction("Buscar ayuda") }
                    .padding(horizontal = BrandSpacing.md, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = BrandColors.TextSecondary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "¿Cómo te ayudamos?", color = BrandColors.InputHint, fontSize = 13.sp)
            }

            // Grid 2x2 de canales
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                ChannelCard(
                    modifier = Modifier.weight(1f),
                    title = "Chat con\nsoporte",
                    icon = Icons.Outlined.ChatBubbleOutline,
                    bg = BrandColors.FarmerPrimary,
                    fg = Color.White,
                    onClick = { onAction("Chat con soporte") }
                )
                ChannelCard(
                    modifier = Modifier.weight(1f),
                    title = "Llamar:\n018000 123 456",
                    icon = Icons.Outlined.Call,
                    bg = Color(0xFFC9A24A),
                    fg = Color.White,
                    onClick = { onAction("Llamar soporte") }
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                ChannelCard(
                    modifier = Modifier.weight(1f),
                    title = "Academia\nOrigen",
                    icon = Icons.Outlined.School,
                    bg = BrandColors.CoffeeBrown,
                    fg = Color.White,
                    onClick = { onAction("Academia Origen") }
                )
                ChannelCard(
                    modifier = Modifier.weight(1f),
                    title = "Comunidad\ncaficultores",
                    icon = Icons.Outlined.Group,
                    bg = Color(0xFFD9E8F0),
                    fg = BrandColors.TextPrimary,
                    onClick = { onAction("Comunidad") }
                )
            }

            // FAQs
            MOCK_FAQS.forEach { group ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${group.emoji} ${group.title}",
                        style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    ) {
                        group.questions.forEachIndexed { i, q ->
                            if (i > 0) {
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BrandColors.DividerLine))
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAction("FAQ: $q") }
                                    .padding(horizontal = BrandSpacing.md, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = q, color = BrandColors.TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                Text(text = "›", color = BrandColors.TextSecondary, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }

            // Banner "Mejora tu negocio"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(BrandColors.CoffeeBrown, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) { Text(text = "📷", fontSize = 48.sp) }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "📷 Mejora tu negocio",
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                )
                ImproveLink(emoji = "💡", text = "10 consejos para vender más", onClick = { onAction("10 consejos") })
                ImproveLink(emoji = "📚", text = "Cómo contar tu historia", onClick = { onAction("Contar historia") })
                ImproveLink(emoji = "⭐", text = "Cómo responder reseñas", onClick = { onAction("Responder reseñas") })
            }

            // Banner final
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "¿No encuentras tu respuesta?",
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                )
                Text(
                    text = "Estamos aquí para acompañar tu cosecha.",
                    color = BrandColors.TextSecondary,
                    fontSize = 12.sp
                )
                Button(
                    onClick = { onAction("Chat con soporte") },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandColors.FarmerPrimary, contentColor = Color.White)
                ) { Text(text = "Chat con soporte", fontWeight = FontWeight.SemiBold, fontSize = 13.sp) }
                OutlinedButton(
                    onClick = { onAction("Crear PQR") },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(8.dp)
                ) { Text(text = "Crear PQR", color = BrandColors.TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp) }
            }

            // Datos de contacto
            ContactRow(icon = Icons.Outlined.ChatBubbleOutline, label = "WHATSAPP CAFICULTORES", value = "300 xxx xxxx")
            ContactRow(icon = Icons.Outlined.Email, label = "CORREO ELECTRÓNICO", value = "caficultores@origen.co")
            ContactRow(icon = Icons.Outlined.Schedule, label = "HORARIO DE ATENCIÓN", value = "Lunes a sábado 7 AM - 7 PM")

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun ChannelCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    bg: Color,
    fg: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(bg, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(22.dp))
        Text(text = title, color = fg, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, lineHeight = 16.sp)
    }
}

@Composable
private fun ImproveLink(emoji: String, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, fontSize = 14.sp)
        Spacer(modifier = Modifier.size(6.dp))
        Text(text = text, color = BrandColors.TextPrimary, fontSize = 13.sp)
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(32.dp).background(BrandColors.IndicatorInactive, CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(icon, contentDescription = null, tint = BrandColors.TextPrimary, modifier = Modifier.size(16.dp)) }
        Column(modifier = Modifier.padding(start = BrandSpacing.sm)) {
            Text(text = label, color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Text(text = value, color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

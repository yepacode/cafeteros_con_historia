package com.cafeteros.historia.ui.features.farmer_messages

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
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

private enum class InboxFilter(val label: String) {
    ALL("Todos"), UNREAD("No leídos"), ARCHIVED("Archivados")
}

internal data class MockConversation(
    val id: String,
    val sender: String,
    val preview: String,
    val time: String,
    val unreadCount: Int = 0,
    val isFrequentClient: Boolean = false,
    val orderTag: String? = null,
    val isSupport: Boolean = false
)

internal val MOCK_CONVERSATIONS = listOf(
    MockConversation("c1", "Origen Soporte", "Su liquidación de cosecha ya está…", "Ahora", isSupport = true),
    MockConversation("c2", "María González", "Don Alberto, ¿cuál me recomien…", "10:45 AM", unreadCount = 1, isFrequentClient = true, orderTag = "OR-34521"),
    MockConversation("c3", "Ricardo Silva", "Excelente tostión, recibí el paquete ay…", "Ayer"),
    MockConversation("c4", "Café de la Luna", "¿Tendrán disponibilidad de 50kg?", "Lun", unreadCount = 2)
)

private val QUICK_REPLIES = listOf(
    "Gracias por tu compra" to "Agradecemos tu interés en…",
    "Tu pedido va en camino" to "Hola, te informamos que…",
    "Método de cata" to "Este grano resalta notas de…",
    "Consulta técnica" to "Nuestra finca se encuentra a…"
)

class InboxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                InboxScreen(
                    onBack = ::finish,
                    onConversationTap = { id -> ChatActivity.start(this, id) },
                    onTemplateTap = { template ->
                        Toast.makeText(this, "Próximamente: editar plantilla \"$template\"", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, InboxActivity::class.java))
        }
    }
}

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onConversationTap: (String) -> Unit,
    onTemplateTap: (String) -> Unit
) {
    var activeFilter by remember { mutableStateOf(InboxFilter.ALL) }
    val totalCount = MOCK_CONVERSATIONS.size
    val unreadCount = MOCK_CONVERSATIONS.count { it.unreadCount > 0 }
    val filtered = when (activeFilter) {
        InboxFilter.ALL -> MOCK_CONVERSATIONS
        InboxFilter.UNREAD -> MOCK_CONVERSATIONS.filter { it.unreadCount > 0 }
        InboxFilter.ARCHIVED -> emptyList()
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
                text = "Buzón de Origen",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
            )
            IconButton(onClick = { /* TODO: buscar */ }) {
                Icon(Icons.Outlined.Search, contentDescription = "Buscar", tint = BrandColors.TextPrimary)
            }
            IconButton(onClick = { /* TODO: filtro */ }) {
                Icon(Icons.Outlined.FilterList, contentDescription = "Filtros", tint = BrandColors.TextPrimary)
            }
        }

        // Banner amarillo
        if (unreadCount > 0) {
            Row(
                modifier = Modifier
                    .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.xs)
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                    .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.MailOutline, contentDescription = null, tint = Color(0xFF8C6E1F), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "$unreadCount mensajes sin leer",
                    color = BrandColors.TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Marcar todos como leídos",
                    color = Color(0xFF8C6E1F),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { /* TODO */ }
                )
            }
        }

        // Tabs
        Row(
            modifier = Modifier.padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.xs),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            InboxFilter.entries.forEach { f ->
                val active = f == activeFilter
                val label = when (f) {
                    InboxFilter.ALL -> "${f.label} ($totalCount)"
                    InboxFilter.UNREAD -> "${f.label} ($unreadCount)"
                    InboxFilter.ARCHIVED -> f.label
                }
                Box(
                    modifier = Modifier
                        .background(if (active) BrandColors.CoffeeBrown else BrandColors.CardBackground, RoundedCornerShape(50))
                        .clickable { activeFilter = f }
                        .padding(horizontal = BrandSpacing.md, vertical = 8.dp)
                ) {
                    Text(
                        text = label,
                        color = if (active) BrandColors.PrimaryButtonText else BrandColors.TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = BrandSpacing.lg, vertical = BrandSpacing.sm),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            items(filtered) { c ->
                ConversationRow(conversation = c, onClick = { onConversationTap(c.id) })
            }
            item { Spacer(modifier = Modifier.height(BrandSpacing.md)) }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Respuestas rápidas guardadas",
                        style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
                    )
                    Row(
                        modifier = Modifier.clickable { onTemplateTap("Nueva plantilla") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Add, contentDescription = null, tint = BrandColors.FarmerPrimary, modifier = Modifier.size(14.dp))
                        Text(text = "Nueva plantilla", color = BrandColors.FarmerPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                    QUICK_REPLIES.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                            row.forEach { (title, preview) ->
                                TemplateCard(
                                    modifier = Modifier.weight(1f),
                                    title = title,
                                    preview = preview,
                                    onClick = { onTemplateTap(title) }
                                )
                            }
                            if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(BrandSpacing.lg)) }
        }
    }
}

@Composable
private fun ConversationRow(conversation: MockConversation, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(if (conversation.isSupport) BrandColors.CoffeeBrown else BrandColors.FarmerPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
        }
        Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = conversation.sender,
                    color = BrandColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif
                )
                if (conversation.isFrequentClient) {
                    Spacer(modifier = Modifier.size(6.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF1B8), RoundedCornerShape(50))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "Cliente frecuente", color = Color(0xFF8C6E1F), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Text(text = conversation.preview, color = BrandColors.TextSecondary, fontSize = 12.sp, maxLines = 1)
            if (conversation.orderTag != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE8F4EC), RoundedCornerShape(50))
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(text = "Pedido #${conversation.orderTag}", color = BrandColors.FarmerPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = conversation.time, color = BrandColors.TextSecondary, fontSize = 10.sp)
            if (conversation.unreadCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier.size(18.dp).background(Color(0xFFC9A24A), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = conversation.unreadCount.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TemplateCard(
    modifier: Modifier = Modifier,
    title: String,
    preview: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(BrandSpacing.sm),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = title, color = BrandColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Text(text = preview, color = BrandColors.TextSecondary, fontSize = 10.sp, maxLines = 2)
    }
}

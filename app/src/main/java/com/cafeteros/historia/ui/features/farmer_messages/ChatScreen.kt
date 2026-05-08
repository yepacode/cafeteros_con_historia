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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.WarningAmber
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

private enum class MessageKind { TEXT, AUDIO }
private enum class MessageSide { THEM, ME }

private data class MockMessage(
    val text: String,
    val time: String,
    val side: MessageSide,
    val kind: MessageKind = MessageKind.TEXT,
    val durationLabel: String? = null
)

private val MOCK_THREAD: List<MockMessage> = listOf(
    MockMessage("Hola Don Alberto, acabo de comprar su café 😊", "09:41 AM", MessageSide.THEM),
    MockMessage("¡Hola María! Muchas gracias por tu compra ☕", "09:43 AM", MessageSide.ME),
    MockMessage("¿Me recomienda algún método para prepararlo?", "09:44 AM", MessageSide.THEM),
    MockMessage("", "09:46 AM", MessageSide.ME, kind = MessageKind.AUDIO, durationLabel = "02:24"),
    MockMessage("Te recomiendo V60 con agua a 93°C, 15g por cada 250ml. Floración de 30 segundos.", "09:47 AM", MessageSide.ME)
)

private val QUICK_REPLY_CHIPS = listOf("Gracias por tu compra", "Ya despachamos tu pedido")

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val conversationId = intent.getStringExtra(EXTRA_CONV_ID) ?: "—"
        val partner = MOCK_CONVERSATIONS.firstOrNull { it.id == conversationId }
        setContent {
            CafeterosTheme {
                ChatScreen(
                    partnerName = partner?.sender ?: "Conversación",
                    orderTag = partner?.orderTag,
                    onBack = ::finish,
                    onCall = { Toast.makeText(this, "Próximamente: llamada", Toast.LENGTH_SHORT).show() },
                    onMore = { Toast.makeText(this, "Próximamente: más opciones", Toast.LENGTH_SHORT).show() }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_CONV_ID = "extra_conv_id"
        fun start(context: Context, conversationId: String) {
            context.startActivity(Intent(context, ChatActivity::class.java).putExtra(EXTRA_CONV_ID, conversationId))
        }
    }
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    partnerName: String,
    orderTag: String?,
    onBack: () -> Unit,
    onCall: () -> Unit,
    onMore: () -> Unit
) {
    var draft by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground)
                .padding(horizontal = BrandSpacing.sm, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver", tint = BrandColors.TextPrimary)
            }
            Box(
                modifier = Modifier.size(36.dp).background(BrandColors.FarmerPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
                Text(text = partnerName, color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(BrandColors.FarmerPrimary, CircleShape))
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "En línea", color = BrandColors.TextSecondary, fontSize = 10.sp)
                }
            }
            IconButton(onClick = onCall) {
                Icon(Icons.Outlined.Call, contentDescription = "Llamar", tint = BrandColors.TextPrimary)
            }
            IconButton(onClick = onMore) {
                Icon(Icons.Outlined.MoreVert, contentDescription = "Más", tint = BrandColors.TextPrimary)
            }
        }

        // Banner amarillo de seguridad
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF1B8))
                .padding(horizontal = BrandSpacing.md, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.WarningAmber, contentDescription = null, tint = Color(0xFF8C6E1F), modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = "No compartas tu número personal o datos bancarios. Toda transacción va por Origen.",
                color = BrandColors.TextPrimary,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }

        // Card del pedido
        if (orderTag != null) {
            Row(
                modifier = Modifier
                    .padding(BrandSpacing.md)
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .clickable { /* TODO: ver pedido */ }
                    .padding(BrandSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).background(BrandColors.InputBackground, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) { Text(text = "📷", fontSize = 18.sp) }
                Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
                    Text(text = "#$orderTag · 3 productos", color = BrandColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "\$146.000", color = BrandColors.TextSecondary, fontSize = 11.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE8F4EC), RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = "EN CAMINO", color = BrandColors.FarmerPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Ver pedido →", color = BrandColors.FarmerPrimary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = BrandSpacing.md, vertical = BrandSpacing.xs),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
        ) {
            items(MOCK_THREAD) { msg -> MessageBubble(msg = msg) }
        }

        // Quick replies
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.md, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            QUICK_REPLY_CHIPS.forEach { chip ->
                Box(
                    modifier = Modifier
                        .background(BrandColors.CardBackground, RoundedCornerShape(50))
                        .clickable { draft = chip }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = chip, color = BrandColors.TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground)
                .padding(horizontal = BrandSpacing.sm, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: adjuntar */ }) {
                Icon(Icons.Outlined.Add, contentDescription = "Adjuntar", tint = BrandColors.TextSecondary)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(BrandColors.AuthBackground, RoundedCornerShape(50))
                    .padding(horizontal = BrandSpacing.md, vertical = 10.dp)
            ) {
                if (draft.isEmpty()) {
                    Text(text = "Escribe un mensaje…", color = BrandColors.InputHint, fontSize = 13.sp)
                }
                BasicTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    singleLine = true,
                    textStyle = TextStyle(color = BrandColors.TextPrimary, fontSize = 13.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(40.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape)
                    .clickable { /* TODO: grabar / enviar */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Mic, contentDescription = "Audio", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun MessageBubble(msg: MockMessage) {
    val isMe = msg.side == MessageSide.ME
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = if (isMe) BrandColors.CoffeeBrown else BrandColors.IndicatorInactive,
                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isMe) 14.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 14.dp
                    )
                )
                .padding(horizontal = BrandSpacing.sm, vertical = 8.dp)
        ) {
            when (msg.kind) {
                MessageKind.TEXT -> Text(
                    text = msg.text,
                    color = if (isMe) Color.White else BrandColors.TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 16.sp
                )
                MessageKind.AUDIO -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(28.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.size(6.dp))
                    // Pseudo-waveform (barras decorativas)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        listOf(6, 12, 8, 14, 10, 16, 8, 12, 6, 14).forEach { h ->
                            Box(
                                modifier = Modifier
                                    .size(width = 2.dp, height = h.dp)
                                    .background(Color.White.copy(alpha = 0.7f))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(text = msg.durationLabel ?: "", color = Color.White, fontSize = 11.sp)
                }
            }
            Text(
                text = msg.time,
                color = if (isMe) Color.White.copy(alpha = 0.6f) else BrandColors.TextSecondary,
                fontSize = 9.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

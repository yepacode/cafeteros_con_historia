package com.cafeteros.historia.ui.features.farmer_messages

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.Conversation
import com.cafeteros.historia.data.repository.ConversationRepository
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class InboxUiState(
    val conversations: List<Conversation> = emptyList(),
    val myUid: String = ""
)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class InboxViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val conversationRepository: ConversationRepository = app.conversationRepository

    val uiState: StateFlow<InboxUiState> = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(InboxUiState())
            else conversationRepository.observeMyConversations(uid)
                .map { InboxUiState(conversations = it, myUid = uid) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InboxUiState()
        )
}

/**
 * Activity Inbox: lista de conversaciones del usuario logueado. Sirve
 * tanto para el caficultor (que recibe mensajes de compradores) como
 * para el comprador (si en el futuro se agrega un acceso desde su lado).
 */
class InboxActivity : ComponentActivity() {

    private val viewModel: InboxViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                InboxScreen(
                    state = state,
                    onBack = ::finish,
                    onConversationTap = { conv ->
                        ChatActivity.start(
                            this,
                            conversationId = conv.id,
                            partnerName = conv.partnerName(state.myUid),
                            partnerUid = conv.partnerUid(state.myUid)
                        )
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
private fun InboxScreen(
    state: InboxUiState,
    onBack: () -> Unit,
    onConversationTap: (Conversation) -> Unit
) {
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
                text = "Mensajes",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
        }

        if (state.conversations.isEmpty()) {
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
                items(state.conversations) { conv ->
                    ConversationRow(
                        conversation = conv,
                        myUid = state.myUid,
                        onTap = { onConversationTap(conv) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(BrandSpacing.lg),
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
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Sin conversaciones",
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
            text = "Cuando un comprador te escriba, su conversación aparecerá aquí.",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ConversationRow(
    conversation: Conversation,
    myUid: String,
    onTap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(BrandColors.FarmerPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = BrandSpacing.sm)) {
            Text(
                text = conversation.partnerName(myUid).ifBlank { "Conversación" },
                color = BrandColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = conversation.lastMessage.ifBlank { "(sin mensajes)" },
                color = BrandColors.TextSecondary,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
        Text(
            text = formatTime(conversation.lastMessageAtEpochMillis),
            color = BrandColors.TextSecondary,
            fontSize = 11.sp
        )
    }
}

private fun formatTime(epochMillis: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - epochMillis
    val oneDay = 24L * 60 * 60 * 1000
    val timeFmt = SimpleDateFormat("HH:mm", Locale("es", "CO"))
    val dateFmt = SimpleDateFormat("dd MMM", Locale("es", "CO"))
    return when {
        diff < oneDay -> timeFmt.format(Date(epochMillis))
        diff < 2 * oneDay -> "Ayer"
        else -> dateFmt.format(Date(epochMillis))
    }
}

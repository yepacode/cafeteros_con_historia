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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.AutoAwesome
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
import com.cafeteros.historia.data.model.ChatMessage
import com.cafeteros.historia.data.model.Conversation
import com.cafeteros.historia.data.model.QUICK_REPLIES
import com.cafeteros.historia.data.repository.ConversationRepository
import com.cafeteros.historia.data.repository.UserRepository
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel del chat. Soporta dos modos de inicio:
 *  - **Conversación existente** (id no vacío): observa los mensajes
 *    directamente.
 *  - **Nueva conversación** (id vacío + partnerUid + partnerName): crea
 *    la conversación al enviar el primer mensaje, usando
 *    [ConversationRepository.startOrSend] que es idempotente.
 *
 * Esto permite que el comprador haga "Contactar caficultor" desde el
 * catálogo sin tener que crear la conversación con un fetch previo.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val conversationRepository: ConversationRepository = app.conversationRepository

    private val _conversationId = MutableStateFlow("")
    val conversationId: StateFlow<String> = _conversationId.asStateFlow()

    private val _myUid = MutableStateFlow("")
    val myUid: StateFlow<String> = _myUid.asStateFlow()

    /**
     * Datos del otro participante. Se guardan al iniciar la pantalla
     * para que podamos crear la conversación al primer mensaje sin
     * tener que volver a pedirlos.
     */
    private var partnerUid: String = ""
    private var partnerName: String = ""

    val messages: StateFlow<List<ChatMessage>> = _conversationId
        .flatMapLatest { id ->
            if (id.isBlank()) flowOf(emptyList())
            else conversationRepository.observeMessages(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /**
     * Inicializa la pantalla. Si [existingConvId] viene null/blank, se
     * usa el id determinístico generado por [Conversation.conversationIdFor]
     * — así si la conversación ya existe, los mensajes salen al toque.
     */
    fun init(existingConvId: String?, partnerUid: String, partnerName: String) {
        viewModelScope.launch {
            val me = userRepository.currentUid() ?: return@launch
            _myUid.value = me
            this@ChatViewModel.partnerUid = partnerUid
            this@ChatViewModel.partnerName = partnerName
            _conversationId.value = existingConvId?.takeIf { it.isNotBlank() }
                ?: Conversation.conversationIdFor(me, partnerUid)
        }
    }

    /** Envía [text]; crea la conversación si es el primer mensaje. */
    fun send(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            val me = userRepository.getCurrentUser() ?: return@launch
            val id = conversationRepository.startOrSend(
                senderUid = me.id,
                senderName = me.name,
                receiverUid = partnerUid,
                receiverName = partnerName,
                text = trimmed
            )
            _conversationId.value = id
        }
    }
}

/**
 * Activity del chat 1-a-1. Se abre con un partnerUid + partnerName
 * obligatorios y opcionalmente un conversationId existente.
 */
class ChatActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val conversationId = intent.getStringExtra(EXTRA_CONVERSATION_ID)
        val partnerUid = intent.getStringExtra(EXTRA_PARTNER_UID).orEmpty()
        val partnerName = intent.getStringExtra(EXTRA_PARTNER_NAME).orEmpty()

        setContent {
            CafeterosTheme {
                val messages by viewModel.messages.collectAsStateWithLifecycle()
                val myUid by viewModel.myUid.collectAsStateWithLifecycle()

                LaunchedEffect(conversationId, partnerUid) {
                    viewModel.init(
                        existingConvId = conversationId,
                        partnerUid = partnerUid,
                        partnerName = partnerName
                    )
                }

                ChatScreen(
                    partnerName = partnerName.ifBlank { "Conversación" },
                    myUid = myUid,
                    messages = messages,
                    onBack = ::finish,
                    onSend = viewModel::send
                )
            }
        }
    }

    companion object {
        private const val EXTRA_CONVERSATION_ID = "extra_conversation_id"
        private const val EXTRA_PARTNER_UID = "extra_partner_uid"
        private const val EXTRA_PARTNER_NAME = "extra_partner_name"

        fun start(
            context: Context,
            conversationId: String? = null,
            partnerUid: String,
            partnerName: String
        ) {
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_CONVERSATION_ID, conversationId)
                putExtra(EXTRA_PARTNER_UID, partnerUid)
                putExtra(EXTRA_PARTNER_NAME, partnerName)
            }
            context.startActivity(intent)
        }
    }
}

@Composable
private fun ChatScreen(
    partnerName: String,
    myUid: String,
    messages: List<ChatMessage>,
    onBack: () -> Unit,
    onSend: (String) -> Unit
) {
    var draft by remember { mutableStateOf("") }
    var showTemplates by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Auto-scroll al último mensaje cada vez que llega uno nuevo.
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        TopBar(partnerName = partnerName, onBack = onBack)

        if (messages.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún sin mensajes. Sé el primero en escribir.",
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(BrandSpacing.lg)
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = BrandSpacing.md,
                    vertical = BrandSpacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(messages) { msg ->
                    MessageBubble(message = msg, isMine = msg.senderUid == myUid)
                }
            }
        }

        if (showTemplates) {
            QuickRepliesRow(
                onPick = { template ->
                    draft = template
                    showTemplates = false
                }
            )
        }

        // Composer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground)
                .padding(BrandSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showTemplates = !showTemplates }) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = "Plantillas",
                    tint = if (showTemplates) BrandColors.FarmerPrimary else BrandColors.TextSecondary
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(BrandColors.InputBackground, RoundedCornerShape(20.dp))
                    .padding(horizontal = BrandSpacing.md, vertical = 10.dp)
            ) {
                if (draft.isBlank()) {
                    Text(
                        text = "Escribe un mensaje…",
                        color = BrandColors.TextSecondary,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
                BasicTextField(
                    value = draft,
                    onValueChange = { if (it.length <= 500) draft = it },
                    textStyle = TextStyle(
                        color = BrandColors.TextPrimary,
                        fontSize = 13.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.size(BrandSpacing.sm))
            IconButton(
                onClick = {
                    onSend(draft)
                    draft = ""
                },
                enabled = draft.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Enviar",
                    tint = if (draft.isNotBlank()) BrandColors.FarmerPrimary else BrandColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun TopBar(partnerName: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground)
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
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(BrandColors.FarmerPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = partnerName.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = partnerName,
            modifier = Modifier
                .weight(1f)
                .padding(start = BrandSpacing.sm),
            color = BrandColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Serif
        )
    }
}

@Composable
private fun MessageBubble(message: ChatMessage, isMine: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isMine) BrandColors.CoffeeBrown else BrandColors.CardBackground,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMine) 16.dp else 4.dp,
                        bottomEnd = if (isMine) 4.dp else 16.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.text,
                color = if (isMine) BrandColors.CreamWhite else BrandColors.TextPrimary,
                fontSize = 13.sp
            )
            Text(
                text = formatTime(message.createdAtEpochMillis),
                color = if (isMine) BrandColors.CreamWhiteMuted else BrandColors.TextSecondary,
                fontSize = 9.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun QuickRepliesRow(onPick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.InfoBannerBackground)
            .padding(BrandSpacing.sm),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "RESPUESTAS RÁPIDAS",
            color = BrandColors.InfoBannerAction,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(horizontal = BrandSpacing.sm, vertical = 4.dp)
        )
        QUICK_REPLIES.forEach { reply ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(8.dp))
                    .clickable { onPick(reply) }
                    .padding(horizontal = BrandSpacing.sm, vertical = 6.dp)
            ) {
                Text(
                    text = reply,
                    color = BrandColors.TextPrimary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

private fun formatTime(epochMillis: Long): String {
    val fmt = SimpleDateFormat("HH:mm", Locale("es", "CO"))
    return fmt.format(Date(epochMillis))
}

package com.cafeteros.historia.ui.features.farmer_notifications

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
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WarningAmber
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
import com.cafeteros.historia.data.model.Notification
import com.cafeteros.historia.data.model.NotificationType
import com.cafeteros.historia.data.repository.NotificationRepository
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel: observa las notificaciones del usuario logueado (cualquier
 * rol — la misma pantalla sirve para comprador y caficultor).
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val notificationRepository: NotificationRepository = app.notificationRepository

    val notifications: StateFlow<List<Notification>> = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else notificationRepository.observeForUser(uid)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun markAsRead(id: String) {
        viewModelScope.launch { notificationRepository.markAsRead(id) }
    }
}

class NotificationsActivity : ComponentActivity() {

    private val viewModel: NotificationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val notifs by viewModel.notifications.collectAsStateWithLifecycle()
                NotificationsScreen(
                    notifications = notifs,
                    onBack = ::finish,
                    onNotificationTap = { id -> viewModel.markAsRead(id) }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NotificationsActivity::class.java))
        }
    }
}

@Composable
private fun NotificationsScreen(
    notifications: List<Notification>,
    onBack: () -> Unit,
    onNotificationTap: (id: String) -> Unit
) {
    val unread = notifications.count { !it.read }
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Notificaciones",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandColors.TextPrimary
                    )
                )
                if (unread > 0) {
                    Text(
                        text = "$unread sin leer",
                        color = BrandColors.FarmerPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        if (notifications.isEmpty()) {
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
                items(notifications) { notif ->
                    NotificationRow(notif = notif, onTap = { onNotificationTap(notif.id) })
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
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Sin notificaciones",
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
            text = "Cuando recibas pedidos, reseñas o avisos importantes, aparecerán aquí.",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NotificationRow(notif: Notification, onTap: () -> Unit) {
    val (icon, tint) = when (notif.type) {
        NotificationType.NEW_ORDER -> Icons.Outlined.ShoppingBag to BrandColors.FarmerPrimary
        NotificationType.ORDER_STATUS_CHANGED -> Icons.Outlined.ShoppingBag to BrandColors.CoffeeBrown
        NotificationType.NEW_REVIEW -> Icons.Outlined.Star to Color(0xFFC9A24A)
        NotificationType.LOW_STOCK -> Icons.Outlined.WarningAmber to Color(0xFFB23A3A)
        NotificationType.GENERIC -> Icons.Outlined.Notifications to BrandColors.TextSecondary
    }
    val bgColor =
        if (notif.read) BrandColors.CardBackground
        else BrandColors.InfoBannerBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(tint.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = BrandSpacing.sm)) {
            Text(
                text = notif.title,
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = if (notif.read) FontWeight.Medium else FontWeight.Bold
            )
            Text(
                text = notif.body,
                color = BrandColors.TextSecondary,
                fontSize = 12.sp
            )
            Text(
                text = formatRelativeDate(notif.createdAtEpochMillis),
                color = BrandColors.TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
        if (!notif.read) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape)
            )
        }
    }
}

private fun formatRelativeDate(epochMillis: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - epochMillis
    val oneDay = 24L * 60 * 60 * 1000
    val timeFmt = SimpleDateFormat("HH:mm", Locale("es", "CO"))
    val dateFmt = SimpleDateFormat("dd MMM", Locale("es", "CO"))
    return when {
        diff < oneDay -> "Hoy ${timeFmt.format(Date(epochMillis))}"
        diff < 2 * oneDay -> "Ayer"
        else -> dateFmt.format(Date(epochMillis))
    }
}

package com.cafeteros.historia.ui.features.farmer_notifications

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WarningAmber
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_messages.InboxActivity
import com.cafeteros.historia.ui.features.farmer_sales.SalesActivity
import com.cafeteros.historia.ui.features.farmer_wallet.WalletActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

private enum class NotifTab(val label: String) {
    ALL("Todas"), SALES("Ventas"), MESSAGES("Mensajes"), SYSTEM("Sistema")
}

enum class NotifKind { SALE, MESSAGE, REVIEW, MONEY, STOCK, TIP, MILESTONE, DOCUMENT }

private data class MockNotification(
    val id: String,
    val title: String,
    val body: String,
    val timeAgo: String,
    val kind: NotifKind,
    val unread: Boolean,
    val highlight: Boolean = false
)

private val MOCK_NOTIFICATIONS: List<MockNotification> = listOf(
    MockNotification("n1", "Nueva venta", "María G. compró Café Huila 250g ×2", "5 MIN AGO", NotifKind.SALE, true, highlight = true),
    MockNotification("n2", "Nuevo mensaje", "María G. te escribió", "AHORA", NotifKind.MESSAGE, true, highlight = true),
    MockNotification("n3", "Nueva reseña", "★★★★★ por Café Nariño", "2H", NotifKind.REVIEW, true),
    MockNotification("n4", "Retiro confirmado", "\$500.000 en tu cuenta Bancolombia", "1D", NotifKind.MONEY, false),
    MockNotification("n5", "Stock bajo", "Café Huila 250g: solo 3 unidades", "2D", NotifKind.STOCK, false),
    MockNotification("n6", "Nuevo consejo", "Cómo tomar mejores fotos de tu café", "3D", NotifKind.TIP, false),
    MockNotification("n7", "¡Felicidades!", "Superaste tus ventas del mes pasado", "5D", NotifKind.MILESTONE, false),
    MockNotification("n8", "RUT por vencer", "Actualiza tu RUT en los próximos 5 días", "1W", NotifKind.DOCUMENT, false)
)

class NotificationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                NotificationsScreen(
                    onBack = ::finish,
                    onMarkAllRead = ::finish,
                    onTap = { kind ->
                        when (kind) {
                            NotifKind.MESSAGE -> InboxActivity.start(this)
                            NotifKind.SALE -> SalesActivity.start(this)
                            NotifKind.MONEY -> WalletActivity.start(this)
                            else -> { /* no-op para los demás */ }
                        }
                    }
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
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onMarkAllRead: () -> Unit,
    onTap: (kind: NotifKind) -> Unit
) {
    var activeTab by remember { mutableStateOf(NotifTab.ALL) }
    val filtered = MOCK_NOTIFICATIONS.filter { matchesTab(it.kind, activeTab) }

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
                text = "Notificaciones",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
            )
            TextButton(onClick = onMarkAllRead) {
                Text(text = "Marcar como leído", color = BrandColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Tabs
        Row(
            modifier = Modifier.padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.xs),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            NotifTab.entries.forEach { t ->
                val active = t == activeTab
                Box(
                    modifier = Modifier
                        .background(if (active) BrandColors.CoffeeBrown else BrandColors.CardBackground, RoundedCornerShape(50))
                        .clickable { activeTab = t }
                        .padding(horizontal = BrandSpacing.md, vertical = 8.dp)
                ) {
                    Text(
                        text = t.label,
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
            items(filtered) { n ->
                NotificationRow(notif = n, onClick = { onTap(n.kind) })
            }
            item { Spacer(modifier = Modifier.height(BrandSpacing.lg)) }
        }
    }
}

private fun matchesTab(kind: NotifKind, tab: NotifTab): Boolean = when (tab) {
    NotifTab.ALL -> true
    NotifTab.SALES -> kind == NotifKind.SALE || kind == NotifKind.MONEY
    NotifTab.MESSAGES -> kind == NotifKind.MESSAGE || kind == NotifKind.REVIEW
    NotifTab.SYSTEM -> kind == NotifKind.STOCK || kind == NotifKind.TIP || kind == NotifKind.MILESTONE || kind == NotifKind.DOCUMENT
}

@Composable
private fun NotificationRow(notif: MockNotification, onClick: () -> Unit) {
    val (iconBg, iconTint, icon) = iconForKind(notif.kind)
    val rowBg = if (notif.highlight) Color(0xFFFFF8DC) else BrandColors.CardBackground

    // Box exterior simula el "borde izquierdo dorado" cuando highlight=true.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (notif.highlight) Color(0xFFC9A24A) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = if (notif.highlight) 4.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(rowBg, RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
                .padding(BrandSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
        Box(
            modifier = Modifier.size(44.dp).background(iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
        }
        Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
            Text(
                text = notif.title,
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
            )
            Text(text = notif.body, color = BrandColors.TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = notif.timeAgo,
                color = BrandColors.TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp
            )
            if (notif.unread) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.size(8.dp).background(Color(0xFFC9A24A), CircleShape))
            }
        }
        } // cierre Row
    } // cierre Box
}

private fun iconForKind(kind: NotifKind): Triple<Color, Color, ImageVector> = when (kind) {
    NotifKind.SALE -> Triple(Color(0xFFC9A24A), Color.White, Icons.Outlined.Inventory2)
    NotifKind.MESSAGE -> Triple(Color(0xFFC2EBD3), BrandColors.FarmerPrimary, Icons.Outlined.ChatBubbleOutline)
    NotifKind.REVIEW -> Triple(Color(0xFFFFE9A8), Color(0xFF8C6E1F), Icons.Outlined.Star)
    NotifKind.MONEY -> Triple(Color(0xFFC2EBD3), BrandColors.FarmerPrimary, Icons.Outlined.AttachMoney)
    NotifKind.STOCK -> Triple(Color(0xFFF6D9D2), Color(0xFFB23A3A), Icons.Outlined.WarningAmber)
    NotifKind.TIP -> Triple(Color(0xFFF6D9D2), Color(0xFF8C6E1F), Icons.Outlined.MenuBook)
    NotifKind.MILESTONE -> Triple(Color(0xFFC2EBD3), BrandColors.FarmerPrimary, Icons.Outlined.Celebration)
    NotifKind.DOCUMENT -> Triple(BrandColors.IndicatorInactive, BrandColors.TextPrimary, Icons.Outlined.Description)
}

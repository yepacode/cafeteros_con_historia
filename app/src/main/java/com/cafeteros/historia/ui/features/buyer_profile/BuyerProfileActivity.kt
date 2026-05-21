package com.cafeteros.historia.ui.features.buyer_profile

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.features.addressbook.AddressBookActivity
import com.cafeteros.historia.ui.features.buyer_catalog.MyPurchasesActivity
import com.cafeteros.historia.ui.features.farmer_messages.InboxActivity
import com.cafeteros.historia.ui.features.farmer_notifications.NotificationsActivity
import com.cafeteros.historia.ui.features.settings.SettingsActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope

/**
 * ViewModel del perfil del comprador. Solo observa el usuario logueado.
 */
class BuyerProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as CafeterosApplication).userRepository

    val user: StateFlow<User?> = userRepository.currentUserFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
}

/**
 * Activity "Mi Cuenta" del comprador. Centraliza accesos a sus datos:
 * pedidos, direcciones, notificaciones, mensajes, settings y logout.
 * Reemplaza el botón "hamburguesa" del top bar de Explore — desde ahí se
 * llega aquí.
 */
class BuyerProfileActivity : ComponentActivity() {

    private val viewModel: BuyerProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val user by viewModel.user.collectAsStateWithLifecycle()
                BuyerProfileScreen(
                    user = user,
                    onBack = ::finish,
                    onOpenOrders = { MyPurchasesActivity.start(this) },
                    onOpenAddresses = { AddressBookActivity.start(this) },
                    onOpenMessages = { InboxActivity.start(this) },
                    onOpenNotifications = { NotificationsActivity.start(this) },
                    onOpenSettings = { SettingsActivity.start(this) }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, BuyerProfileActivity::class.java))
        }
    }
}

@Composable
private fun BuyerProfileScreen(
    user: User?,
    onBack: () -> Unit,
    onOpenOrders: () -> Unit,
    onOpenAddresses: () -> Unit,
    onOpenMessages: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)
        .systemBarsPadding()
        .verticalScroll(rememberScrollState())) {

        // Top bar
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
                text = "Mi Cuenta",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 48.dp),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                ),
                textAlign = TextAlign.Center
            )
        }

        // Hero con avatar
        Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
            Box(modifier = Modifier.fillMaxSize().background(BrandColors.CoffeeBrown))
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 32.dp)
                    .size(80.dp)
                    .background(Color.White, CircleShape)
                    .padding(3.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        // Nombre + email
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = user?.name?.takeIf { it.isNotBlank() } ?: "Comprador",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            if (!user?.email.isNullOrBlank()) {
                Text(
                    text = user?.email.orEmpty(),
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            if (!user?.phone.isNullOrBlank()) {
                Text(
                    text = "📞 ${user?.phone}",
                    color = BrandColors.TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        // Secciones
        ProfileSection(title = "MI ACTIVIDAD") {
            ProfileLink(icon = Icons.Outlined.Receipt, title = "Mis pedidos", onClick = onOpenOrders)
            ProfileLink(icon = Icons.Outlined.ChatBubbleOutline, title = "Mis mensajes", onClick = onOpenMessages)
            ProfileLink(icon = Icons.Outlined.Notifications, title = "Notificaciones", onClick = onOpenNotifications)
        }
        ProfileSection(title = "MIS DATOS") {
            ProfileLink(icon = Icons.Outlined.LocationOn, title = "Mis direcciones", onClick = onOpenAddresses)
            ProfileLink(icon = Icons.Outlined.AccountCircle, title = "Datos personales", onClick = onOpenSettings)
        }
        ProfileSection(title = "CUENTA") {
            ProfileLink(icon = Icons.Outlined.Settings, title = "Configuración y cerrar sesión", onClick = onOpenSettings)
        }

        Spacer(modifier = Modifier.height(BrandSpacing.lg))
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier
        .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.sm)
        .fillMaxWidth()) {
        Text(
            text = title,
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(vertical = BrandSpacing.xs)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
        ) {
            content()
        }
    }
}

@Composable
private fun ProfileLink(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(BrandColors.InputBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandColors.CoffeeBrown,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = BrandSpacing.sm),
            color = BrandColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(text = "›", color = BrandColors.TextSecondary, fontSize = 18.sp)
    }
}

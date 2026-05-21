package com.cafeteros.historia.ui.features.farmer_profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
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
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.Workspaces
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_help.HelpAndSupportActivity
import com.cafeteros.historia.ui.features.farmer_help.SellerAgreementActivity
import com.cafeteros.historia.ui.features.farmer_messages.InboxActivity
import com.cafeteros.historia.ui.features.farmer_notifications.NotificationsActivity
import com.cafeteros.historia.ui.features.farmer_reviews.ReviewsActivity
import com.cafeteros.historia.ui.features.farmer_stats.StatsActivity
import com.cafeteros.historia.ui.features.farmer_wallet.WalletActivity
import com.cafeteros.historia.ui.features.settings.SettingsActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

class MyProfileActivity : ComponentActivity() {

    private val viewModel: MyProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repo = (application as com.cafeteros.historia.CafeterosApplication).userRepository
        setContent {
            CafeterosTheme {
                val state by viewModel.uiState
                    .collectAsStateWithLifecycle()
                MyProfileScreen(
                    state = state,
                    // El icono de engranaje del top bar abre la pantalla de
                    // Configuración con el toggle real de huella, en lugar
                    // de la pantalla mock de FarmerSettingsActivity.
                    onSettings = { SettingsActivity.start(this) },
                    onPreviewPublic = { PublicProfilePreviewActivity.start(this) },
                    onOpenEditFarm = { EditFarmActivity.start(this) },
                    onOpenCertifications = { CertificationsActivity.start(this) },
                    onOpenWallet = { WalletActivity.start(this) },
                    onOpenStats = { StatsActivity.start(this) },
                    onOpenInbox = { InboxActivity.start(this) },
                    onOpenReviews = { ReviewsActivity.start(this) },
                    onOpenNotifications = { NotificationsActivity.start(this) },
                    onOpenHelp = { HelpAndSupportActivity.start(this) },
                    onOpenTerms = { SellerAgreementActivity.start(this) },
                    onOpenProductList = {
                        com.cafeteros.historia.ui.features.farmer_products
                            .ProductListActivity.start(this)
                    },
                    onLogout = {
                        lifecycleScope.launch {
                            repo.logout()
                            goToLogin()
                        }
                    },
                    onDeleteAccount = {
                        lifecycleScope.launch {
                            repo.deleteCurrentAccount()
                            android.widget.Toast.makeText(
                                this@MyProfileActivity,
                                "Cuenta eliminada",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                            goToLogin()
                        }
                    }
                )
            }
        }
    }

    private fun goToLogin() {
        val intent = Intent(
            this,
            com.cafeteros.historia.ui.features.auth.LoginActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MyProfileActivity::class.java))
        }
    }
}

@Composable
fun MyProfileScreen(
    state: MyProfileUiState,
    onSettings: () -> Unit,
    onPreviewPublic: () -> Unit,
    onOpenEditFarm: () -> Unit,
    onOpenCertifications: () -> Unit,
    onOpenWallet: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenInbox: () -> Unit,
    onOpenReviews: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenHelp: () -> Unit,
    onOpenTerms: () -> Unit,
    onLogout: () -> Unit,
    onOpenProductList: () -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showDeleteConfirm = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = BrandSpacing.lg)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(48.dp))
            Text(
                text = "Mi perfil",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(onClick = onSettings) {
                Icon(Icons.Outlined.Settings, contentDescription = "Configuración", tint = BrandColors.TextPrimary)
            }
        }

        // Hero: foto real de la finca como banner + avatar circular
        // sobrepuesto al fondo. Si no hay foto, usa el color de marca como
        // fallback para no dejar la zona en blanco.
        Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
            if (state.farmPhotoBase64 != null) {
                com.cafeteros.historia.ui.components.Base64Image(
                    base64 = state.farmPhotoBase64,
                    contentDescription = "Foto de mi finca",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(BrandColors.FarmerPrimary)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 32.dp)
                    .size(80.dp)
                    .background(Color.White, CircleShape)
                    .padding(3.dp)
                    .background(BrandColors.CoffeeBrown, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (state.farmerPhotoBase64 != null) {
                    com.cafeteros.historia.ui.components.Base64Image(
                        base64 = state.farmerPhotoBase64,
                        contentDescription = "Tu foto de perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                } else {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = state.fullName.ifBlank { "—" },
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = state.farmName,
                color = BrandColors.TextSecondary,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif
            )

            // Badges
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Badge(
                    label = "CAFICULTOR VERIFICADO",
                    icon = Icons.Outlined.VerifiedUser,
                    bg = Color(0xFFE8F4EC),
                    fg = BrandColors.FarmerPrimary
                )
                Badge(
                    label = state.region.uppercase(),
                    icon = Icons.Outlined.LocationOn,
                    bg = BrandColors.CardBackground,
                    fg = BrandColors.TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))

        // Métricas
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.lg),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileMetric(value = "47", label = "VENTAS")
            ProfileMetric(value = "126", label = "PROD. VENDIDOS")
            ProfileMetric(value = "★4.9", label = "127 RESEÑAS", highlight = true)
            ProfileMetric(value = "1.2K", label = "SEGUIDORES")
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))

        // Banner perfil completo
        Column(
            modifier = Modifier
                .padding(horizontal = BrandSpacing.lg)
                .fillMaxWidth()
                .background(Color(0xFFFFF1B8), RoundedCornerShape(12.dp))
                .padding(BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Tu perfil está ${state.completionPercent}% completo",
                    color = BrandColors.TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "EDITAR",
                    color = Color(0xFF8C6E1F),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.clickable(onClick = onOpenEditFarm)
                )
            }
            Text(
                text = "Aumenta tu visibilidad completando tu historia.",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Color(0xFFEEDFA8), RoundedCornerShape(3.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(state.completionPercent / 100f)
                        .height(6.dp)
                        .background(Color(0xFFC9A24A), RoundedCornerShape(3.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.sm))

        // CTA "Ver perfil público"
        Row(
            modifier = Modifier
                .padding(horizontal = BrandSpacing.lg)
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .clickable(onClick = onPreviewPublic)
                .padding(BrandSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Visibility, contentDescription = null, tint = BrandColors.TextPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Ver mi perfil como lo ven los compradores", color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text(text = "›", color = BrandColors.TextSecondary, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))

        // Secciones
        ProfileSection("MI FINCA") {
            ProfileLink(icon = Icons.Outlined.Description, title = "Mi historia y fotos", onClick = onOpenEditFarm)
            ProfileLink(icon = Icons.Outlined.LocationOn, title = "Ubicación de la finca", onClick = onOpenEditFarm)
            ProfileLink(icon = Icons.Outlined.Shield, title = "Certificaciones y documentos", onClick = onOpenCertifications)
            ProfileLink(icon = Icons.Outlined.EmojiEvents, title = "Premios y reconocimientos", onClick = onOpenEditFarm)
        }
        ProfileSection("MI NEGOCIO") {
            ProfileLink(
                icon = Icons.Outlined.Inventory2,
                title = "Mis productos",
                badge = if (state.productCount > 0) state.productCount.toString() else null,
                onClick = onOpenProductList
            )
            ProfileLink(icon = Icons.Outlined.AccountBalanceWallet, title = "Billetera", onClick = onOpenWallet)
            ProfileLink(icon = Icons.Outlined.BarChart, title = "Estadísticas", onClick = onOpenStats)
            ProfileLink(icon = Icons.Outlined.Receipt, title = "Facturación electrónica", onClick = onSettings)
        }
        ProfileSection("COMUNICACIÓN") {
            ProfileLink(icon = Icons.Outlined.ChatBubbleOutline, title = "Mensajes", onClick = onOpenInbox)
            ProfileLink(icon = Icons.Outlined.Star, title = "Reseñas", onClick = onOpenReviews)
            ProfileLink(icon = Icons.Outlined.Notifications, title = "Notificaciones", onClick = onOpenNotifications)
        }
        ProfileSection("APRENDIZAJE") {
            ProfileLink(icon = Icons.Outlined.School, title = "Academia Origen", onClick = onSettings)
            ProfileLink(icon = Icons.AutoMirrored.Outlined.MenuBook, title = "Guía del caficultor", onClick = onSettings)
            ProfileLink(icon = Icons.Outlined.PlayCircleOutline, title = "Videos tutoriales", onClick = onSettings)
            ProfileLink(icon = Icons.Outlined.Group, title = "Comunidad caficultores", onClick = onSettings)
        }
        ProfileSection("CUENTA") {
            ProfileLink(icon = Icons.Outlined.AccountCircle, title = "Datos personales", onClick = onSettings)
            ProfileLink(icon = Icons.Outlined.AccountBalance, title = "Cuenta bancaria", onClick = onSettings)
            ProfileLink(icon = Icons.Outlined.Lock, title = "Contraseña y seguridad", onClick = onSettings)
            ProfileLink(icon = Icons.Outlined.Tune, title = "Preferencias", onClick = onSettings)
            ProfileLink(icon = Icons.Outlined.Language, title = "Idioma y región", onClick = onSettings)
        }
        ProfileSection("AYUDA") {
            ProfileLink(icon = Icons.Outlined.HelpOutline, title = "Centro de ayuda", onClick = onOpenHelp)
            ProfileLink(icon = Icons.Outlined.SupportAgent, title = "Contactar soporte", onClick = onOpenHelp)
            ProfileLink(icon = Icons.Outlined.Description, title = "Términos", onClick = onOpenTerms)
            ProfileLink(icon = Icons.Outlined.Workspaces, title = "Privacidad", onClick = onOpenTerms)
        }
        ProfileSection("OTROS") {
            ProfileLink(icon = Icons.Outlined.Info, title = "Acerca de v1.0.0", onClick = onSettings)
            ProfileLink(icon = Icons.Outlined.ThumbUp, title = "Califica la app", onClick = onSettings)
        }

        Spacer(modifier = Modifier.height(BrandSpacing.md))
        Row(
            modifier = Modifier
                .padding(horizontal = BrandSpacing.lg)
                .fillMaxWidth()
                .clickable(onClick = onLogout)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Logout, contentDescription = null, tint = Color(0xFFB23A3A), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Cerrar sesión", color = Color(0xFFB23A3A), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }

        // Eliminar cuenta — acción destructiva, requiere confirmación
        Row(
            modifier = Modifier
                .padding(horizontal = BrandSpacing.lg)
                .fillMaxWidth()
                .clickable { showDeleteConfirm.value = true }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                androidx.compose.material.icons.Icons.Outlined.Delete,
                contentDescription = null,
                tint = Color(0xFFB23A3A),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Eliminar cuenta",
                color = Color(0xFFB23A3A),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    if (showDeleteConfirm.value) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteConfirm.value = false },
            title = {
                Text(
                    text = "¿Eliminar tu cuenta?",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandColors.TextPrimary
                    )
                )
            },
            text = {
                Text(
                    text = "Esta acción es permanente. Se borrarán tus datos de la cuenta y tendrás que volver a registrarte si quieres usar Origen.",
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        showDeleteConfirm.value = false
                        onDeleteAccount()
                    }
                ) {
                    Text(text = "Eliminar", color = Color(0xFFB23A3A), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDeleteConfirm.value = false }) {
                    Text(text = "Cancelar", color = BrandColors.TextPrimary)
                }
            },
            containerColor = BrandColors.CardBackground
        )
    }
}

@Composable
private fun Badge(label: String, icon: ImageVector, bg: Color, fg: Color) {
    Row(
        modifier = Modifier
            .background(bg, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = label, color = fg, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun ProfileMetric(value: String, label: String, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = if (highlight) Color(0xFFC9A24A) else BrandColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Text(text = label, color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(top = BrandSpacing.sm)) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = BrandSpacing.lg, vertical = 8.dp),
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Column(
            modifier = Modifier
                .padding(horizontal = BrandSpacing.lg)
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
        ) {
            content()
        }
    }
}

@Composable
private fun ProfileLink(
    icon: ImageVector,
    title: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = BrandColors.TextPrimary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.size(BrandSpacing.sm))
        Text(text = title, color = BrandColors.TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
        if (badge != null) {
            Box(
                modifier = Modifier.background(BrandColors.FarmerPrimary, CircleShape).padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = badge, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.size(8.dp))
        }
        Text(text = "›", color = BrandColors.TextSecondary, fontSize = 18.sp)
    }
}

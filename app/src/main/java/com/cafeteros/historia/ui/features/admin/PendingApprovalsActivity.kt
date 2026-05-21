package com.cafeteros.historia.ui.features.admin

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.NotificationRepository
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel del panel de "Caficultores pendientes" del admin.
 *
 * Observa el flow de pendientes y, por cada uno, lazy-resuelve su finca
 * para mostrar más contexto en la card (nombre de finca, región,
 * documentos si los hay). Al pulsar Aprobar/Rechazar persiste el cambio
 * en Firestore y notifica al caficultor afectado.
 */
class PendingApprovalsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val farmRepository: FarmRepository = app.farmRepository
    private val notificationRepository: NotificationRepository = app.notificationRepository

    val pending: StateFlow<List<User>> = userRepository.observePendingApprovals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _farmCache = MutableStateFlow<Map<String, FarmProfile?>>(emptyMap())
    val farmCache: StateFlow<Map<String, FarmProfile?>> = _farmCache.asStateFlow()

    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast.asStateFlow()

    private val _processing = MutableStateFlow(false)
    val processing: StateFlow<Boolean> = _processing.asStateFlow()

    /** Resuelve la finca de [uid] si todavía no está en cache. */
    fun ensureFarmLoaded(uid: String) {
        if (_farmCache.value.containsKey(uid)) return
        viewModelScope.launch {
            val farm = farmRepository.findByCaficultor(uid)
            _farmCache.value = _farmCache.value + (uid to farm)
        }
    }

    fun approve(user: User) {
        if (_processing.value) return
        _processing.value = true
        viewModelScope.launch {
            runCatching { userRepository.approveAccount(user.id) }
                .onSuccess {
                    notificationRepository.notifyGeneric(
                        targetUid = user.id,
                        title = "¡Bienvenido a Origen!",
                        body = "Tu cuenta fue aprobada. Ya puedes publicar tus productos."
                    )
                    _toast.value = "Cuenta aprobada"
                }
                .onFailure { _toast.value = "Error al aprobar: ${it.message}" }
            _processing.value = false
        }
    }

    fun reject(user: User) {
        if (_processing.value) return
        _processing.value = true
        viewModelScope.launch {
            runCatching { userRepository.rejectAccount(user.id) }
                .onSuccess {
                    notificationRepository.notifyGeneric(
                        targetUid = user.id,
                        title = "Solicitud rechazada",
                        body = "Tu solicitud para vender en Origen no fue aprobada."
                    )
                    _toast.value = "Cuenta rechazada"
                }
                .onFailure { _toast.value = "Error al rechazar: ${it.message}" }
            _processing.value = false
        }
    }

    fun consumeToast() {
        _toast.value = null
    }
}

class PendingApprovalsActivity : ComponentActivity() {

    private val viewModel: PendingApprovalsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val pending by viewModel.pending.collectAsStateWithLifecycle()
                val farmCache by viewModel.farmCache.collectAsStateWithLifecycle()
                val processing by viewModel.processing.collectAsStateWithLifecycle()
                val toast by viewModel.toast.collectAsStateWithLifecycle()

                // Lazy load de fincas para los pendientes
                LaunchedEffect(pending) {
                    pending.forEach { viewModel.ensureFarmLoaded(it.id) }
                }

                LaunchedEffect(toast) {
                    toast?.let {
                        Toast.makeText(this@PendingApprovalsActivity, it, Toast.LENGTH_SHORT).show()
                        viewModel.consumeToast()
                    }
                }

                PendingApprovalsScreen(
                    pending = pending,
                    farmsByUid = farmCache,
                    isProcessing = processing,
                    onBack = ::finish,
                    onApprove = viewModel::approve,
                    onReject = viewModel::reject
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PendingApprovalsActivity::class.java))
        }
    }
}

@Composable
private fun PendingApprovalsScreen(
    pending: List<User>,
    farmsByUid: Map<String, FarmProfile?>,
    isProcessing: Boolean,
    onBack: () -> Unit,
    onApprove: (User) -> Unit,
    onReject: (User) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)
        .systemBarsPadding()) {
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
                    text = "Caficultores pendientes",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandColors.TextPrimary
                    )
                )
                Text(
                    text = "${pending.size} cuenta${if (pending.size == 1) "" else "s"} esperando revisión",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        if (pending.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = BrandSpacing.lg,
                    vertical = BrandSpacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                items(pending) { user ->
                    PendingCard(
                        user = user,
                        farm = farmsByUid[user.id],
                        isProcessing = isProcessing,
                        onApprove = { onApprove(user) },
                        onReject = { onReject(user) }
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
                .background(Color(0xFFE8F4EC), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.HourglassEmpty,
                contentDescription = null,
                tint = BrandColors.FarmerPrimary,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Sin solicitudes pendientes",
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
            text = "Cuando un caficultor se registre, aparecerá aquí para que revises sus datos y documentos.",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun PendingCard(
    user: User,
    farm: FarmProfile?,
    isProcessing: Boolean,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    var showRejectDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header con avatar + nombre + email
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(BrandColors.FarmerPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = BrandSpacing.sm)) {
                Text(
                    text = user.name.ifBlank { "(Sin nombre)" },
                    color = BrandColors.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = user.email,
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
                if (user.phone.isNotBlank()) {
                    Text(
                        text = "📞 ${user.phone}",
                        color = BrandColors.TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Detalles de la finca (si ya tiene)
        if (farm != null && (farm.name.isNotBlank() || farm.region.isNotBlank())) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.InfoBannerBackground, RoundedCornerShape(8.dp))
                    .padding(BrandSpacing.sm)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "FINCA",
                        color = BrandColors.InfoBannerAction,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    if (farm.name.isNotBlank()) {
                        Text(
                            text = farm.name,
                            color = BrandColors.TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (farm.region.isNotBlank()) {
                        Text(
                            text = "📍 ${farm.region}",
                            color = BrandColors.TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    if (farm.altitudeMeters > 0 || farm.areaHectares > 0) {
                        Text(
                            text = buildString {
                                if (farm.altitudeMeters > 0) append("${farm.altitudeMeters} msnm")
                                if (farm.altitudeMeters > 0 && farm.areaHectares > 0) append(" · ")
                                if (farm.areaHectares > 0) append("${farm.areaHectares} ha")
                            },
                            color = BrandColors.TextSecondary,
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Sin datos de finca aún — el caficultor no completó esa parte.",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic
            )
        }

        // Acciones
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            OutlinedButton(
                onClick = { showRejectDialog = true },
                enabled = !isProcessing,
                modifier = Modifier.weight(1f).height(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    tint = Color(0xFFB23A3A),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "Rechazar",
                    color = Color(0xFFB23A3A),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Button(
                onClick = onApprove,
                enabled = !isProcessing,
                modifier = Modifier.weight(1.4f).height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.FarmerPrimary,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "Aprobar", fontWeight = FontWeight.SemiBold)
            }
        }
    }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = {
                Text(
                    text = "¿Rechazar a ${user.name}?",
                    color = BrandColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "El caficultor no podrá vender en Origen. Verá un mensaje de solicitud rechazada y solo podrá cerrar sesión.",
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showRejectDialog = false
                    onReject()
                }) {
                    Text(
                        text = "Rechazar",
                        color = Color(0xFFB23A3A),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text(text = "Cancelar", color = BrandColors.TextPrimary)
                }
            },
            containerColor = BrandColors.CardBackground
        )
    }
}

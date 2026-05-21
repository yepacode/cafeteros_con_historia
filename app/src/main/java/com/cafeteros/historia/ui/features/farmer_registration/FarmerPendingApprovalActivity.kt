package com.cafeteros.historia.ui.features.farmer_registration

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.data.model.ApprovalStatus
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/**
 * Pantalla a la que cae el caficultor mientras su cuenta está pendiente
 * de aprobación por un administrador. Sustituye al antiguo "Continuar"
 * que auto-aprobaba el flujo desde el cliente.
 *
 * Si el admin marca la cuenta como REJECTED, también se muestra aquí pero
 * con copy distinto y la opción "Cerrar sesión" para que el caficultor
 * pueda intentar registrarse de nuevo con otros datos. La transición a
 * APPROVED la observa [com.cafeteros.historia.MainActivity] y redirige
 * automáticamente al panel del caficultor.
 */
class FarmerPendingApprovalActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val statusName = intent.getStringExtra(EXTRA_STATUS).orEmpty()
        val status = ApprovalStatus.fromNameOrDefault(statusName)

        setContent {
            CafeterosTheme {
                FarmerPendingApprovalScreen(
                    status = status,
                    onLogout = { logoutAndGoToLogin() }
                )
            }
        }
    }

    private fun logoutAndGoToLogin() {
        val app = application as com.cafeteros.historia.CafeterosApplication
        lifecycleScope.launch {
            app.userRepository.logout()
            startActivity(
                Intent(
                    this@FarmerPendingApprovalActivity,
                    com.cafeteros.historia.ui.features.auth.LoginActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            finish()
        }
    }

    companion object {
        private const val EXTRA_STATUS = "extra_status"

        fun start(context: Context, status: ApprovalStatus) {
            val intent = Intent(context, FarmerPendingApprovalActivity::class.java).apply {
                putExtra(EXTRA_STATUS, status.name)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }
}

@Composable
private fun FarmerPendingApprovalScreen(
    status: ApprovalStatus,
    onLogout: () -> Unit
) {
    val isRejected = status == ApprovalStatus.REJECTED

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono central
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(
                    color = if (isRejected) BrandColors.PaymentFailedReasonCardBackground
                    else BrandColors.InfoBannerBackground,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isRejected) "✕" else "⏳",
                fontSize = 56.sp
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        Text(
            text = if (isRejected) "Solicitud rechazada"
            else "Tu cuenta está en revisión",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Text(
            text = if (isRejected) {
                "Lamentamos que tu solicitud no haya sido aprobada esta vez. " +
                    "Puedes contactarnos para entender los motivos o cerrar " +
                    "sesión e intentar registrarte de nuevo con la información correcta."
            } else {
                "Recibimos tus datos y documentos. Un administrador los está " +
                    "revisando. Te avisaremos cuando tu cuenta sea aprobada para " +
                    "que puedas empezar a vender tu café."
            },
            color = BrandColors.TextSecondary,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(BrandSpacing.lg))

        if (!isRejected) {
            // Card informativa con timeline simple
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TimelineRow(
                    emoji = "✅",
                    title = "Datos recibidos",
                    subtitle = "Tu solicitud fue enviada correctamente.",
                    done = true
                )
                TimelineRow(
                    emoji = "🔍",
                    title = "Verificación de documentos",
                    subtitle = "Un administrador está revisando tu RUT y soportes.",
                    done = false,
                    current = true
                )
                TimelineRow(
                    emoji = "🌱",
                    title = "Bienvenido a Origen",
                    subtitle = "Te avisaremos cuando puedas empezar a vender.",
                    done = false
                )
            }
        }

        Spacer(modifier = Modifier.height(BrandSpacing.xl))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(8.dp))
                .clickable(onClick = onLogout)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Cerrar sesión",
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun TimelineRow(
    emoji: String,
    title: String,
    subtitle: String,
    done: Boolean,
    current: Boolean = false
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "$emoji  $title",
                color = if (done || current) BrandColors.TextPrimary
                else BrandColors.TextSecondary,
                fontSize = 13.sp,
                fontWeight = if (current) FontWeight.Bold else FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontStyle = if (current) FontStyle.Italic else FontStyle.Normal
            )
        }
    }
}

package com.cafeteros.historia.ui.features.farmer_profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

private data class CertItem(
    val name: String,
    val validUntil: String,
    val emoji: String,
    val hasDocument: Boolean = false
)

private val ACTIVE_CERTS = listOf(
    CertItem("Orgánico - ECOCERT", "Válido hasta: 15 Nov 2026", "🌱", hasDocument = true),
    CertItem("Rainforest Alliance", "Válido hasta: 20 Ene 2027", "🌲"),
    CertItem("Denominación de Origen Huila", "Válido hasta: 05 Mar 2028", "🏅")
)

private data class AvailableCert(val name: String, val description: String, val emoji: String)

private val AVAILABLE_CERTS = listOf(
    AvailableCert("UTZ Certified", "Prácticas agrícolas sostenibles y mejores.", "⚡"),
    AvailableCert("Mujeres Cafeteras", "Empoderamiento y equidad en el campo.", "👩"),
    AvailableCert("Bird Friendly", "Protección de biodiversidad y hábitats.", "🌿"),
    AvailableCert("4C Association", "Estándar básico para sostenibilidad.", "🌍")
)

class CertificationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                CertificationsScreen(
                    onBack = ::finish,
                    onAction = { action ->
                        Toast.makeText(this, "Próximamente: $action", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CertificationsActivity::class.java))
        }
    }
}

@Composable
fun CertificationsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onAction: (String) -> Unit
) {
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
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver", tint = BrandColors.TextPrimary)
            }
            Text(
                text = "Certificaciones",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(onClick = { onAction("Más opciones") }) {
                Icon(Icons.Outlined.MoreVert, contentDescription = "Más", tint = BrandColors.TextPrimary)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.xs))
            Text(
                text = "Mis certificaciones activas (${ACTIVE_CERTS.size})",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
            )
            ACTIVE_CERTS.forEach { cert -> ActiveCertCard(cert = cert, onView = { onAction("Ver ${cert.name}") }) }

            // Add certification (dashed)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = BrandColors.IndicatorInactive, shape = RoundedCornerShape(12.dp))
                    .clickable { onAction("Agregar certificación") }
                    .padding(vertical = BrandSpacing.md),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(24.dp).background(BrandColors.FarmerPrimary, androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Outlined.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp)) }
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Agregar certificación", color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Text(
                text = "En verificación (1)",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
            )
            InVerificationCard(name = "Fair Trade", subtitle = "Enviado hace 2 días")

            Text(
                text = "Certificaciones que puedes obtener",
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary, lineHeight = 22.sp)
            )
            AVAILABLE_CERTS.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                    row.forEach { cert ->
                        AvailableCertCard(
                            modifier = Modifier.weight(1f),
                            cert = cert,
                            onLearnMore = { onAction("Saber más sobre ${cert.name}") }
                        )
                    }
                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }

            // Banner academia
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F4EC), RoundedCornerShape(12.dp))
                    .clickable { onAction("Academia Origen") }
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).background(BrandColors.FarmerPrimary, androidx.compose.foundation.shape.CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Outlined.School, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp)) }
                Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
                    Text(text = "Aprende cómo certificarte.", color = BrandColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "Curso gratis de 20 min en la Academia Origen.", color = BrandColors.TextSecondary, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun ActiveCertCard(cert: CertItem, onView: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(BrandColors.InputBackground, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) { Text(text = cert.emoji, fontSize = 22.sp) }
            Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
                Text(
                    text = cert.name,
                    style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BrandColors.TextPrimary)
                )
                Text(text = cert.validUntil, color = BrandColors.TextSecondary, fontSize = 11.sp)
            }
            Icon(Icons.Outlined.MoreVert, contentDescription = null, tint = BrandColors.TextSecondary, modifier = Modifier.size(18.dp))
        }
        if (cert.hasDocument) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.AuthBackground, RoundedCornerShape(8.dp))
                    .clickable(onClick = onView)
                    .padding(horizontal = BrandSpacing.sm, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Description, contentDescription = null, tint = BrandColors.TextPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "VER DOCUMENTO.PDF", color = BrandColors.FarmerPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
        }
    }
}

@Composable
private fun InVerificationCard(name: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(Color(0xFFFFF1B8), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Outlined.HourglassEmpty, contentDescription = null, tint = Color(0xFF8C6E1F), modifier = Modifier.size(20.dp)) }
        Column(modifier = Modifier.weight(1f).padding(horizontal = BrandSpacing.sm)) {
            Text(text = name, color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif)
            Text(text = subtitle, color = BrandColors.TextSecondary, fontSize = 11.sp)
        }
        Box(
            modifier = Modifier
                .background(Color(0xFFC9A24A), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text = "EN REVISIÓN", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        }
    }
}

@Composable
private fun AvailableCertCard(
    modifier: Modifier = Modifier,
    cert: AvailableCert,
    onLearnMore: () -> Unit
) {
    Column(
        modifier = modifier
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = cert.emoji, fontSize = 22.sp)
        Text(
            text = cert.name,
            color = BrandColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Text(text = cert.description, color = BrandColors.TextSecondary, fontSize = 11.sp, lineHeight = 14.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "SABER MÁS ›",
            color = Color(0xFFC9A24A),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.clickable(onClick = onLearnMore)
        )
    }
}

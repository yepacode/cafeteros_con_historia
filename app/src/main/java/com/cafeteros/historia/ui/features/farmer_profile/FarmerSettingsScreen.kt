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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

class FarmerSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                FarmerSettingsScreen(
                    onBack = ::finish,
                    onAction = { Toast.makeText(this, "Próximamente: $it", Toast.LENGTH_SHORT).show() },
                    onLogout = ::finish
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, FarmerSettingsActivity::class.java))
        }
    }
}

@Composable
fun FarmerSettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onAction: (String) -> Unit,
    onLogout: () -> Unit
) {
    var pushNotif by remember { mutableStateOf(true) }
    var emailNotif by remember { mutableStateOf(true) }
    var smsNotif by remember { mutableStateOf(true) }
    var newSalesAlert by remember { mutableStateOf(true) }
    var messageAlert by remember { mutableStateOf(true) }
    var documentReminders by remember { mutableStateOf(true) }
    var autoReply by remember { mutableStateOf(true) }
    var vacationMode by remember { mutableStateOf(false) }
    var publicProfile by remember { mutableStateOf(true) }
    var sharePhone by remember { mutableStateOf(false) }
    var biometric by remember { mutableStateOf(true) }
    var twoFA by remember { mutableStateOf(true) }
    var shareData by remember { mutableStateOf(true) }
    var betaFeatures by remember { mutableStateOf(false) }

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
                text = "Configuración",
                modifier = Modifier.weight(1f),
                style = TextStyle(fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.TextPrimary)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            // Header del usuario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp).background(BrandColors.FarmerPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp)) }
                Column(modifier = Modifier.padding(horizontal = BrandSpacing.sm)) {
                    Text(text = "Don Alberto Ramírez", color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif)
                    Text(text = "Finca La Esperanza, Huila", color = BrandColors.TextSecondary, fontSize = 11.sp)
                }
            }

            // NOTIFICACIONES
            SettingSection("NOTIFICACIONES") {
                SwitchRow("Push en el celular", pushNotif) { pushNotif = it }
                SwitchRow("Correo electrónico", emailNotif) { emailNotif = it }
                SwitchRow("SMS para ventas importantes", smsNotif) { smsNotif = it }
                SwitchRow("Alertas de nuevas ventas", newSalesAlert) { newSalesAlert = it }
                SwitchRow("Alertas de mensajes", messageAlert) { messageAlert = it }
                SwitchRow("Recordatorios de documentos", documentReminders) { documentReminders = it }
            }

            // PREFERENCIAS DE VENTA
            SettingSection("PREFERENCIAS DE VENTA") {
                NavRow(title = "Horario de atención", subtitle = "Lun-Vie 8:00 - 18:00, Sáb 9:00 - 13:00", onClick = { onAction("Horario de atención") })
                SwitchRow("Respuesta automática fuera de horario", autoReply) { autoReply = it }
                if (autoReply) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(BrandColors.AuthBackground, RoundedCornerShape(8.dp))
                            .padding(BrandSpacing.sm)
                    ) {
                        Text(
                            text = "\"Hola, gracias por contactarnos. En este momento no estamos disponibles, pero te responderemos lo antes posible. ¡Gracias por preferir el café de nuestra finca!\"",
                            color = BrandColors.TextSecondary,
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 14.sp
                        )
                    }
                }
                SwitchRow(
                    label = "Modo vacaciones",
                    sublabel = "Pausa todos tus productos temporalmente.",
                    checked = vacationMode,
                    onCheckedChange = { vacationMode = it }
                )
            }

            // PAGOS Y FACTURACIÓN
            SettingSection("PAGOS Y FACTURACIÓN") {
                NavRow(title = "🏦 Bancolombia", subtitle = "Ahorros ****8754", onClick = { onAction("Cuenta bancaria") })
                NavRow(title = "Datos fiscales", subtitle = "RUT, Régimen Simplificado", onClick = { onAction("Datos fiscales") })
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Facturación electrónica", color = BrandColors.TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE8F4EC), RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = "ACTIVO", color = BrandColors.FarmerPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    }
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(text = "SIIGO", color = BrandColors.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Frecuencia de retiros", color = BrandColors.TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text(text = "Semanal ▾", color = BrandColors.FarmerPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // PRIVACIDAD
            SettingSection("PRIVACIDAD") {
                SwitchRow("Perfil público", publicProfile) { publicProfile = it }
                SwitchRow("Compartir mi teléfono con compradores", sharePhone) { sharePhone = it }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Ubicación del mapa", color = BrandColors.TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text(text = "Solo municipio", color = BrandColors.TextSecondary, fontSize = 12.sp)
                }
            }

            // SEGURIDAD
            SettingSection("SEGURIDAD") {
                NavRow(title = "Cambiar contraseña", subtitle = null, onClick = { onAction("Cambiar contraseña") })
                SwitchRow("Biometría", biometric) { biometric = it }
                SwitchRow("Verificación 2 pasos", twoFA) { twoFA = it }
            }

            // AVANZADO
            SettingSection("AVANZADO") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Caché", color = BrandColors.TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text(text = "87 MB", color = BrandColors.TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "LIBERAR", color = Color(0xFFC9A24A), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onAction("Liberar caché") })
                }
                SwitchRow("Compartir datos para mejorar", shareData) { shareData = it }
                SwitchRow("Funciones beta", betaFeatures) { betaFeatures = it }
            }

            // LEGAL
            SettingSection("LEGAL") {
                NavRow(title = "Términos del vendedor", subtitle = null, onClick = { onAction("Términos") })
                NavRow(title = "Política de privacidad", subtitle = null, onClick = { onAction("Privacidad") })
                NavRow(title = "Política de datos", subtitle = null, onClick = { onAction("Política de datos") })
                NavRow(title = "Resolución de disputas", subtitle = null, onClick = { onAction("Disputas") })
            }

            // SOBRE LA APP
            SettingSection("SOBRE LA APP") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Versión", color = BrandColors.TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text(text = "1.0.0", color = BrandColors.TextSecondary, fontSize = 12.sp)
                }
                NavRow(title = "Calificar en Play Store", subtitle = null, trailing = "★", onClick = { onAction("Play Store") })
                NavRow(title = "Compartir con otros caficultores", subtitle = null, trailing = "REFERIR", onClick = { onAction("Referir") })
            }

            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color(0xFFB23A3A), shape = RoundedCornerShape(50))
                    .clickable(onClick = onLogout)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Cerrar sesión", color = Color(0xFFB23A3A), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(
                text = "HECHO CON PASIÓN EN COLOMBIA",
                color = BrandColors.TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.fillMaxWidth().padding(top = BrandSpacing.sm),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun SettingSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(horizontal = BrandSpacing.md)
        ) {
            content()
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    sublabel: String? = null,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = BrandColors.TextPrimary, fontSize = 13.sp)
            if (sublabel != null) {
                Text(text = sublabel, color = BrandColors.TextSecondary, fontSize = 10.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BrandColors.FarmerPrimary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = BrandColors.IndicatorInactive
            )
        )
    }
}

@Composable
private fun NavRow(title: String, subtitle: String?, trailing: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = BrandColors.TextPrimary, fontSize = 13.sp)
            if (subtitle != null) {
                Text(text = subtitle, color = BrandColors.TextSecondary, fontSize = 11.sp)
            }
        }
        if (trailing != null) {
            Text(text = trailing, color = BrandColors.FarmerPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.size(4.dp))
        }
        Text(text = "›", color = BrandColors.TextSecondary, fontSize = 18.sp)
    }
}

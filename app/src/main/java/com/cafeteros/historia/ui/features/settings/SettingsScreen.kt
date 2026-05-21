package com.cafeteros.historia.ui.features.settings

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
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Pantalla de Configuración general — toggle de autenticación biométrica.
 *
 * Es agnóstica del rol (Comprador, Caficultor, Administrador). Los tres
 * roles llegan aquí desde sus respectivas pantallas principales para
 * habilitar/deshabilitar el inicio de sesión con huella.
 *
 * @param state estado actual de la configuración (proviene de [SettingsViewModel]).
 * @param onBack invocada al pulsar la flecha del encabezado para cerrar la
 *  pantalla. Típicamente conectada a `Activity::finish`.
 * @param onToggleBiometric notifica al contenedor que el usuario movió el
 *  Switch. **No** modifica el estado directamente: la `Activity` debe lanzar
 *  el `BiometricPrompt` cuando `newValue=true` y solo persistir el cambio si
 *  la verificación pasa. Cuando `newValue=false`, la `Activity` puede
 *  persistir el apagado de inmediato.
 */
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onBack: () -> Unit,
    onToggleBiometric: (newValue: Boolean) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        TopBar(onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            SecuritySection(
                state = state,
                onToggleBiometric = onToggleBiometric
            )

            Spacer(modifier = Modifier.height(BrandSpacing.lg))

            LogoutButton(onClick = { showLogoutDialog = true })

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "¿Cerrar sesión?",
                    color = BrandColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Tendrás que volver a iniciar sesión la próxima vez. " +
                        "Si tienes huella activada, podrás usarla como atajo.",
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text(
                        text = "Cerrar sesión",
                        color = androidx.compose.ui.graphics.Color(0xFFB23A3A),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(text = "Cancelar", color = BrandColors.TextPrimary)
                }
            },
            containerColor = BrandColors.CardBackground
        )
    }
}

/**
 * Botón de "Cerrar sesión" en rojo discreto. Tap dispara la confirmación
 * via [AlertDialog] (definida en el caller) — evita logouts accidentales.
 */
@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = androidx.compose.ui.graphics.Color(0xFFB23A3A),
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            contentDescription = null,
            tint = androidx.compose.ui.graphics.Color(0xFFB23A3A),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Cerrar sesión",
            color = androidx.compose.ui.graphics.Color(0xFFB23A3A),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Encabezado con flecha "atrás" y título "Configuración". Replica el estilo
 * usado en `FarmerSettingsScreen` para mantener coherencia entre pantallas.
 */
@Composable
private fun TopBar(onBack: () -> Unit) {
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
            text = "Configuración",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
    }
}

/**
 * Sección "SEGURIDAD" con el toggle de huella. Si la biometría no está
 * disponible (sin sensor o sin huellas enroladas) el switch se deshabilita
 * y se muestra la razón debajo en texto pequeño para que el usuario sepa
 * qué arreglar en los ajustes del sistema.
 */
@Composable
private fun SecuritySection(
    state: SettingsUiState,
    onToggleBiometric: (newValue: Boolean) -> Unit
) {
    Column {
        Text(
            text = "SEGURIDAD",
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(vertical = BrandSpacing.sm)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.xs)
        ) {
            BiometricRow(
                checked = state.biometricEnabled,
                enabled = state.biometricAvailable,
                onCheckedChange = onToggleBiometric
            )
            if (!state.biometricAvailable && state.biometricUnavailableReason != null) {
                Text(
                    text = state.biometricUnavailableReason,
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = BrandSpacing.sm)
                )
            }
        }
    }
}

/**
 * Fila individual del Switch de huella. El usuario activa o desactiva con
 * el `Switch`; los efectos secundarios (BiometricPrompt, persistencia) los
 * decide la Activity que recibe [onCheckedChange].
 */
@Composable
private fun BiometricRow(
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Usar huella para entrar",
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Inicia sesión rápidamente con tu huella registrada en el dispositivo.",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BrandColors.FarmerPrimary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = BrandColors.IndicatorInactive
            )
        )
    }
}

package com.cafeteros.historia.ui.features.password_recovery.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.delay

/** Duración por defecto del cooldown de reenvío en segundos (45s). */
const val DEFAULT_RESEND_COOLDOWN_SECONDS: Int = 45

/**
 * Botón/contador para reenviar el código OTP.
 *
 * Mientras [cooldownSeconds] no llegue a cero, muestra el texto
 * "¿No recibiste el código? Reenviar en M:SS" con el botón deshabilitado.
 * Al llegar a cero el botón se habilita y al pulsarlo:
 *  1. Invoca [onResendClick].
 *  2. Reinicia el contador.
 *
 * @param modifier modifier opcional.
 * @param cooldownSeconds duración del bloqueo entre reenvíos.
 * @param onResendClick callback al pulsar "Reenviar" cuando está disponible.
 */
@Composable
fun ResendCodeButton(
    modifier: Modifier = Modifier,
    cooldownSeconds: Int = DEFAULT_RESEND_COOLDOWN_SECONDS,
    onResendClick: () -> Unit
) {
    var remainingSeconds by remember { mutableIntStateOf(cooldownSeconds) }

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        }
    }

    val isEnabled = remainingSeconds == 0

    TextButton(
        modifier = modifier,
        onClick = {
            onResendClick()
            remainingSeconds = cooldownSeconds
        },
        enabled = isEnabled
    ) {
        Text(
            text = if (isEnabled) {
                "¿No recibiste el código? Reenviar"
            } else {
                "¿No recibiste el código? Reenviar en ${formatSeconds(remainingSeconds)}"
            },
            style = BrandTypography.BrandTagline.copy(
                color = if (isEnabled) BrandColors.LinkGreen else BrandColors.TextSecondary,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp,
                fontSize = 13.sp
            )
        )
    }
}

/** Convierte segundos a formato `M:SS` (ej. 65 → "1:05"). */
private fun formatSeconds(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

@Preview(name = "ResendCodeButton", showBackground = true, widthDp = 360)
@Composable
private fun ResendCodeButtonPreview() {
    CafeterosTheme { ResendCodeButton(cooldownSeconds = 45, onResendClick = {}) }
}

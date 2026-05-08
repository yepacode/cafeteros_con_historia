package com.cafeteros.historia.ui.features.password_recovery.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.auth.components.AuthPrimaryButton
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla final del flujo: confirmación de que la contraseña se actualizó.
 *
 * Muestra un círculo verde con un check, un mensaje de confirmación y dos
 * acciones:
 *  - "Iniciar sesión" → cierra el flujo y vuelve a Login.
 *  - "Contactar a soporte técnico" → callback opcional (mailto / chat).
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param onLogin callback al pulsar "Iniciar sesión".
 * @param onContactSupport callback al pulsar el enlace de soporte.
 */
@Composable
fun PasswordUpdatedScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit = {},
    onContactSupport: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .padding(horizontal = BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SuccessCheckIcon()
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "¡Contraseña actualizada!",
            style = BrandTypography.LoginTitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        Text(
            text = "Ya puedes iniciar sesión con tu nueva contraseña.",
            style = BrandTypography.LoginSubtitle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(BrandSpacing.xl))
        AuthPrimaryButton(
            label = "Iniciar sesión",
            onClick = onLogin
        )
        Spacer(modifier = Modifier.height(BrandSpacing.md))
        TextButton(onClick = onContactSupport) {
            Text(
                text = "Contactar a soporte técnico",
                style = BrandTypography.BrandTagline.copy(
                    color = BrandColors.LinkGreen,
                    letterSpacing = 0.sp,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun SuccessCheckIcon() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .background(color = BrandColors.LinkGreen, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Check,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(56.dp)
        )
    }
}

@Preview(name = "PasswordUpdatedScreen", widthDp = 360, heightDp = 800)
@Composable
private fun PasswordUpdatedScreenPreview() {
    CafeterosTheme {
        PasswordUpdatedScreen()
    }
}

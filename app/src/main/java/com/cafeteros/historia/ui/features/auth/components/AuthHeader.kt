package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Cabecera reutilizable para los formularios de autenticación.
 *
 * Compone: flecha de regreso + wordmark "Origen" + título grande + subtítulo.
 * El título y subtítulo son parámetros para que cada pantalla configure su
 * propio mensaje (ej. "Bienvenido de vuelta" en login, "Crear tu cuenta" en
 * registro).
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param title texto principal grande (Serif bold).
 * @param subtitle texto descriptivo bajo el título.
 * @param onBackClick callback de la flecha de regreso.
 */
@Composable
fun AuthHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = BrandColors.TextPrimary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            OrigenWordmark()
            Spacer(modifier = Modifier.height(BrandSpacing.md))
            Text(
                text = title,
                style = BrandTypography.LoginTitle
            )
            Text(
                text = subtitle,
                style = BrandTypography.LoginSubtitle
            )
        }
    }
}

@Preview(name = "AuthHeader – Login", showBackground = true, widthDp = 360)
@Composable
private fun AuthHeaderLoginPreview() {
    CafeterosTheme {
        AuthHeader(
            title = "Bienvenido de vuelta",
            subtitle = "Inicia sesión para seguir explorando",
            onBackClick = {}
        )
    }
}

@Preview(name = "AuthHeader – Register", showBackground = true, widthDp = 360)
@Composable
private fun AuthHeaderRegisterPreview() {
    CafeterosTheme {
        AuthHeader(
            title = "Crear tu cuenta",
            subtitle = "Únete a Origen y descubre cafés con historia",
            onBackClick = {}
        )
    }
}

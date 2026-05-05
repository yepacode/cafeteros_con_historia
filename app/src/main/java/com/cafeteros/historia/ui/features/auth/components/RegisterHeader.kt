package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Cabecera de la pantalla de Registro.
 *
 * A diferencia de [AuthHeader] (usado en Login), este header no muestra el
 * wordmark "Origen" y centra el título en una toolbar visual: flecha de
 * regreso a la izquierda y "Crear cuenta" centrado.
 *
 * El subtítulo va debajo, alineado a la izquierda, dentro del padding general
 * del formulario.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param title título centrado en la barra (ej. "Crear cuenta").
 * @param subtitle texto descriptivo que aparece debajo de la barra.
 * @param onBackClick callback de la flecha de regreso.
 */
@Composable
fun RegisterHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandColors.TextPrimary
                )
            }
            Text(
                text = title,
                style = BrandTypography.ScreenTitleCentered,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.md))
        Text(
            text = subtitle,
            style = BrandTypography.ScreenSubtitleCentered,
            modifier = Modifier.padding(horizontal = BrandSpacing.lg)
        )
    }
}

@Preview(name = "RegisterHeader", showBackground = true, widthDp = 360)
@Composable
private fun RegisterHeaderPreview() {
    CafeterosTheme {
        RegisterHeader(
            title = "Crear cuenta",
            subtitle = "Únete a la comunidad cafetera colombiana",
            onBackClick = {}
        )
    }
}

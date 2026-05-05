package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Enlace "¿Olvidaste tu contraseña?" alineado a la derecha bajo el campo de
 * contraseña.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param onClick callback al pulsar el enlace.
 */
@Composable
fun ForgotPasswordLink(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = onClick) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                style = BrandTypography.ForgotPasswordLink
            )
        }
    }
}

@Preview(name = "ForgotPasswordLink", showBackground = true, widthDp = 360)
@Composable
private fun ForgotPasswordLinkPreview() {
    CafeterosTheme {
        ForgotPasswordLink(onClick = {})
    }
}

package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Botón primario reutilizable para los formularios de autenticación.
 *
 * Lo usa [com.cafeteros.historia.ui.features.auth.LoginScreen] con label
 * "Iniciar sesión" y [com.cafeteros.historia.ui.features.auth.RegisterScreen]
 * con label "Crear cuenta". Mismo color, forma y altura para mantener una
 * identidad visual consistente entre pantallas.
 *
 * @param modifier modifier opcional.
 * @param onClick callback al pulsar.
 * @param label texto visible (ej. "Iniciar sesión", "Crear cuenta").
 * @param enabled cuando es false, el botón se atenúa y no responde al tap.
 */
@Composable
fun AuthPrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandColors.PrimaryButton,
            contentColor = BrandColors.PrimaryButtonText,
            disabledContainerColor = BrandColors.PrimaryButton.copy(alpha = 0.4f),
            disabledContentColor = BrandColors.PrimaryButtonText.copy(alpha = 0.6f)
        )
    ) {
        Text(
            text = label,
            style = BrandTypography.PrimaryButtonLabel
        )
    }
}

@Preview(name = "AuthPrimaryButton", showBackground = true, widthDp = 360)
@Composable
private fun AuthPrimaryButtonPreview() {
    CafeterosTheme {
        AuthPrimaryButton(onClick = {}, label = "Iniciar sesión")
    }
}

@Preview(name = "AuthPrimaryButton – disabled", showBackground = true, widthDp = 360)
@Composable
private fun AuthPrimaryButtonDisabledPreview() {
    CafeterosTheme {
        AuthPrimaryButton(onClick = {}, label = "Crear cuenta", enabled = false)
    }
}

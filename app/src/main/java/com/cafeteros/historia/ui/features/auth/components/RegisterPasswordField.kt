package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Campo de contraseña del Registro: input estilo [AuthTextField] (sin ícono
 * leading) con toggle de visibilidad y, opcionalmente, texto de ayuda debajo
 * con los requisitos.
 *
 * Para "Confirmar contraseña" basta con omitir [helperText] y [showVisibilityToggle].
 *
 * @param modifier modifier opcional.
 * @param label etiqueta superior (ej. "CONTRASEÑA" o "CONFIRMAR CONTRASEÑA").
 * @param value contraseña actual.
 * @param onValueChange callback al editar.
 * @param isVisible si la contraseña se muestra en claro.
 * @param onVisibilityToggle callback del icono del ojo.
 * @param helperText texto opcional debajo del input (requisitos).
 * @param showVisibilityToggle si false, no se muestra el icono del ojo.
 */
@Composable
fun RegisterPasswordField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    helperText: String? = null,
    showVisibilityToggle: Boolean = true
) {
    AuthTextField(
        modifier = modifier,
        label = label,
        placeholder = "••••••••",
        value = value,
        onValueChange = onValueChange,
        keyboardType = KeyboardType.Password,
        visualTransformation = if (isVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        helperText = helperText,
        trailing = if (showVisibilityToggle) {
            {
                IconButton(onClick = onVisibilityToggle) {
                    Icon(
                        imageVector = if (isVisible) {
                            Icons.Outlined.VisibilityOff
                        } else {
                            Icons.Outlined.Visibility
                        },
                        contentDescription = if (isVisible) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        },
                        tint = BrandColors.InputHint
                    )
                }
            }
        } else null
    )
}

@Preview(name = "RegisterPasswordField – con helper", showBackground = true, widthDp = 360)
@Composable
private fun RegisterPasswordFieldWithHelperPreview() {
    CafeterosTheme {
        RegisterPasswordField(
            label = "CONTRASEÑA",
            value = "secreto12",
            onValueChange = {},
            isVisible = false,
            onVisibilityToggle = {},
            helperText = "Mínimo 8 caracteres, 1 mayúscula y 1 número"
        )
    }
}

@Preview(name = "RegisterPasswordField – confirmación", showBackground = true, widthDp = 360)
@Composable
private fun RegisterPasswordFieldConfirmPreview() {
    CafeterosTheme {
        RegisterPasswordField(
            label = "CONFIRMAR CONTRASEÑA",
            value = "secreto12",
            onValueChange = {},
            isVisible = false,
            onVisibilityToggle = {},
            showVisibilityToggle = false
        )
    }
}

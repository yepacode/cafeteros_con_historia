package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Campo de contraseña del login con toggle de visibilidad.
 *
 * Stateless en cuanto al texto ([value]/[onValueChange]), pero el estado de
 * visibilidad ([isVisible]/[onVisibilityToggle]) también se hoistea al padre
 * para que LoginScreen mantenga el control de toda la UI.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param value contraseña actual.
 * @param onValueChange callback al editar.
 * @param isVisible si la contraseña se muestra en claro.
 * @param onVisibilityToggle callback al pulsar el icono del ojo.
 */
@Composable
fun LoginPasswordField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onVisibilityToggle: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "CONTRASEÑA",
            style = BrandTypography.FieldLabel
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            placeholder = {
                Text(
                    text = "••••••••",
                    style = BrandTypography.FieldText.copy(color = BrandColors.InputHint)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = BrandColors.InputHint
                )
            },
            trailingIcon = {
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
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            textStyle = BrandTypography.FieldText,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BrandColors.InputBackground,
                unfocusedContainerColor = BrandColors.InputBackground,
                disabledContainerColor = BrandColors.InputBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = BrandColors.TextPrimary,
                unfocusedTextColor = BrandColors.TextPrimary,
                cursorColor = BrandColors.CoffeeBrown
            )
        )
    }
}

@Preview(name = "PasswordField – oculta", showBackground = true, widthDp = 360)
@Composable
private fun LoginPasswordFieldHiddenPreview() {
    CafeterosTheme {
        LoginPasswordField(
            value = "supersecreto",
            onValueChange = {},
            isVisible = false,
            onVisibilityToggle = {}
        )
    }
}

@Preview(name = "PasswordField – visible", showBackground = true, widthDp = 360)
@Composable
private fun LoginPasswordFieldVisiblePreview() {
    CafeterosTheme {
        LoginPasswordField(
            value = "supersecreto",
            onValueChange = {},
            isVisible = true,
            onVisibilityToggle = {}
        )
    }
}

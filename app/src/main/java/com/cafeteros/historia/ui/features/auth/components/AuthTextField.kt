package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Campo de texto genérico para los formularios de autenticación.
 *
 * Reproduce el estilo del diseño: input redondeado con fondo gris-beige y sin
 * línea inferior. A diferencia de [LoginEmailField] y [LoginPasswordField]
 * (que son específicos del login con sus íconos de sobre y candado), este
 * componente es flexible: acepta opcionalmente prefix, trailing y helper text.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param label etiqueta superior en mayúsculas (ej. "NOMBRE COMPLETO").
 * @param placeholder texto guía dentro del input (ej. "Ej: María González").
 * @param value contenido actual.
 * @param onValueChange callback al editar.
 * @param prefix texto opcional dentro del input al inicio (ej. "+57").
 * @param trailing composable opcional al final (ej. ícono de ojo).
 * @param helperText opcional debajo del input (ej. requisitos de contraseña).
 * @param keyboardType tipo de teclado a mostrar.
 * @param visualTransformation transformación visual (ej. para contraseñas).
 * @param singleLine true para una sola línea (default).
 */
@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    prefix: String? = null,
    trailing: (@Composable () -> Unit)? = null,
    helperText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = BrandTypography.FieldLabel
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    style = BrandTypography.FieldText.copy(color = BrandColors.InputHint)
                )
            },
            prefix = if (prefix != null) {
                {
                    Text(
                        text = prefix,
                        style = BrandTypography.FieldPrefix
                    )
                }
            } else null,
            trailingIcon = trailing,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
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
        if (helperText != null) {
            Spacer(modifier = Modifier.height(BrandSpacing.xs))
            Text(
                text = helperText,
                style = BrandTypography.FieldHelper
            )
        }
    }
}

@Preview(name = "AuthTextField – simple", showBackground = true, widthDp = 360)
@Composable
private fun AuthTextFieldSimplePreview() {
    CafeterosTheme {
        AuthTextField(
            label = "NOMBRE COMPLETO",
            placeholder = "Ej: María González",
            value = "",
            onValueChange = {}
        )
    }
}

@Preview(name = "AuthTextField – con prefix +57", showBackground = true, widthDp = 360)
@Composable
private fun AuthTextFieldPhonePreview() {
    CafeterosTheme {
        AuthTextField(
            label = "TELÉFONO CELULAR",
            placeholder = "300 123 4567",
            value = "",
            onValueChange = {},
            prefix = "+57",
            keyboardType = KeyboardType.Phone
        )
    }
}

@Preview(name = "AuthTextField – con helper", showBackground = true, widthDp = 360)
@Composable
private fun AuthTextFieldWithHelperPreview() {
    CafeterosTheme {
        AuthTextField(
            label = "CONTRASEÑA",
            placeholder = "••••••••",
            value = "",
            onValueChange = {},
            helperText = "Mínimo 8 caracteres, 1 mayúscula y 1 número"
        )
    }
}

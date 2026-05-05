package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Campo de texto para el correo electrónico del login.
 *
 * Stateless: el padre mantiene [value] y reacciona con [onValueChange].
 * Visualmente reproduce el input redondeado del diseño: fondo gris-beige,
 * ícono de sobre a la izquierda, sin línea inferior, placeholder "tu@correo.com".
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param value texto actual del campo.
 * @param onValueChange callback al editar.
 */
@Composable
fun LoginEmailField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "CORREO ELECTRÓNICO",
            style = BrandTypography.FieldLabel
        )
        Spacer(modifier = Modifier.height(BrandSpacing.sm))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = "tu@correo.com",
                    style = BrandTypography.FieldText.copy(color = BrandColors.InputHint)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.MailOutline,
                    contentDescription = null,
                    tint = BrandColors.InputHint
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

@Preview(name = "LoginEmailField – vacío", showBackground = true, widthDp = 360)
@Composable
private fun LoginEmailFieldEmptyPreview() {
    CafeterosTheme {
        LoginEmailField(value = "", onValueChange = {})
    }
}

@Preview(name = "LoginEmailField – con valor", showBackground = true, widthDp = 360)
@Composable
private fun LoginEmailFieldFilledPreview() {
    CafeterosTheme {
        LoginEmailField(value = "ana@cafeteros.co", onValueChange = {})
    }
}

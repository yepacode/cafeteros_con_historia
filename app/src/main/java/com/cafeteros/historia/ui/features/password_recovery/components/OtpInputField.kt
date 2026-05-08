package com.cafeteros.historia.ui.features.password_recovery.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/** Cantidad de dígitos del código OTP que la app espera del usuario. */
const val OTP_LENGTH: Int = 4

/**
 * Campo de entrada del código OTP visualmente compuesto por [OTP_LENGTH]
 * cajas separadas, pero internamente respaldado por un único [BasicTextField]
 * invisible que captura toda la entrada del teclado.
 *
 * Esta estrategia (un solo TextField + N cajas decorativas) es más robusta
 * que tener N campos focusables separados: evita problemas con el
 * autocompletado, el copiar/pegar y el cambio de foco entre dígitos.
 *
 * Solo permite dígitos (`0-9`) y limita la longitud a [OTP_LENGTH].
 *
 * @param modifier modifier opcional aplicado al [Box] raíz.
 * @param value cadena actual con el código (0..[OTP_LENGTH] dígitos).
 * @param onValueChange callback con el nuevo valor cuando cambia.
 */
@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                val digitsOnly = newValue.filter { it.isDigit() }.take(OTP_LENGTH)
                onValueChange(digitsOnly)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            decorationBox = { _ ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        BrandSpacing.md,
                        Alignment.CenterHorizontally
                    )
                ) {
                    repeat(OTP_LENGTH) { index ->
                        OtpCharBox(char = value.getOrNull(index))
                    }
                }
            },
            textStyle = TextStyle(color = BrandColors.CoffeeBrown)
        )
    }
}

/** Caja individual que muestra un dígito del OTP (o el placeholder vacío). */
@Composable
private fun OtpCharBox(char: Char?) {
    val isFilled = char != null
    Box(
        modifier = Modifier
            .width(56.dp)
            .height(64.dp)
            .background(
                color = BrandColors.InputBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isFilled) 2.dp else 0.dp,
                color = if (isFilled) BrandColors.CoffeeBrown else BrandColors.InputBackground,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char?.toString() ?: "•",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (isFilled) BrandColors.CoffeeBrown else BrandColors.InputHint
            )
        )
    }
}

@Preview(name = "OtpInputField – vacío", showBackground = true, widthDp = 360)
@Composable
private fun OtpInputFieldEmptyPreview() {
    CafeterosTheme { OtpInputField(value = "", onValueChange = {}) }
}

@Preview(name = "OtpInputField – parcial", showBackground = true, widthDp = 360)
@Composable
private fun OtpInputFieldPartialPreview() {
    CafeterosTheme { OtpInputField(value = "12", onValueChange = {}) }
}

@Preview(name = "OtpInputField – completo", showBackground = true, widthDp = 360)
@Composable
private fun OtpInputFieldFullPreview() {
    CafeterosTheme { OtpInputField(value = "1234", onValueChange = {}) }
}

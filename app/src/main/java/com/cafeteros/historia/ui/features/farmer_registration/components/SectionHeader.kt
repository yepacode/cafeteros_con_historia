package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Encabezado de sección dentro de un paso del registro
 * (ej. "IDENTIFICACIÓN", "INFORMACIÓN CIVIL", "CONTACTO").
 *
 * Estilo: mayúsculas, peso medio, letter-spacing amplio. Sirve como divisor
 * visual entre grupos de campos.
 *
 * @param modifier modifier opcional.
 * @param text texto a renderizar (se muestra tal cual; pasa el texto en
 *  mayúsculas si quieres respetar el estilo de marca).
 */
@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            color = BrandColors.TextPrimary
        )
    )
}

@Preview(widthDp = 240)
@Composable
private fun SectionHeaderPreview() {
    SectionHeader(text = "IDENTIFICACIÓN")
}

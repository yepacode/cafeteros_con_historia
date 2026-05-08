package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Footer de ayuda al final del paso 4.
 *
 * Compuesto por dos filas con ícono + texto:
 *  - Tiempo estimado de verificación.
 *  - Línea de soporte para resolver dudas con los documentos.
 *
 * El número telefónico aparece en negrita para destacarlo. El componente es
 * solo informativo (no maneja clicks); si en el futuro se quiere abrir el
 * dialer al tocar el número, se puede envolver en un `Modifier.clickable`
 * con un `Intent.ACTION_DIAL`.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param verificationTimeText texto del tiempo de verificación.
 * @param supportPhone número de soporte a destacar en negrita.
 */
@Composable
fun HelpFooter(
    modifier: Modifier = Modifier,
    verificationTimeText: String = "La verificación toma entre 24 y 72 horas hábiles.",
    supportPhone: String = "018000-ORIGEN"
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        IconLine(
            icon = Icons.Outlined.AccessTime,
            text = verificationTimeText
        )
        IconLine(
            icon = Icons.Outlined.Phone,
            annotatedText = buildAnnotatedString {
                append("Si necesitas ayuda con algún documento, contáctanos al ")
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = BrandColors.FarmerPrimary
                    )
                ) {
                    append(supportPhone)
                }
            }
        )
    }
}

@Composable
private fun IconLine(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String? = null,
    annotatedText: androidx.compose.ui.text.AnnotatedString? = null
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandColors.TextSecondary,
            modifier = Modifier.size(16.dp)
        )
        val textStyle = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = BrandColors.TextSecondary
        )
        when {
            annotatedText != null -> Text(text = annotatedText, style = textStyle)
            text != null -> Text(text = text, style = textStyle)
        }
    }
}

@Preview(name = "HelpFooter", widthDp = 360)
@Composable
private fun HelpFooterPreview() {
    HelpFooter()
}

package com.cafeteros.historia.ui.features.orderreview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila de aceptación de términos previa al CTA de pago.
 *
 * Layout: checkbox cuadrado a la izquierda + párrafo con tres enlaces
 * subrayados (Términos de Compra, Política de Devoluciones, Derecho de
 * Retracto). El checkbox y el área completa del texto activan el toggle —
 * los enlaces individuales también lo hacen mientras no se conecte una vista
 * legal real para evitar accidentalmente "engañar" al usuario haciéndole
 * creer que el tap abre algo.
 *
 * @param modifier modifier opcional.
 * @param checked estado actual del checkbox.
 * @param onCheckedChange callback con el nuevo valor cuando el usuario lo
 *   alterna.
 */
@Composable
fun TermsAcceptanceRow(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = BrandSpacing.xs),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        TermsCheckbox(checked = checked)
        Text(
            text = termsParagraph(),
            style = BrandTypography.OrderReviewTermsBody,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun TermsCheckbox(modifier: Modifier = Modifier, checked: Boolean) {
    val shape = RoundedCornerShape(4.dp)
    if (checked) {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(shape)
                .background(BrandColors.OrderReviewCheckboxChecked),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = BrandColors.OrderReviewCheckboxCheckIcon,
                modifier = Modifier.size(14.dp)
            )
        }
    } else {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(shape)
                .border(
                    width = 1.5.dp,
                    color = BrandColors.OrderReviewCheckboxBorder,
                    shape = shape
                )
        )
    }
}

private fun termsParagraph() = buildAnnotatedString {
    append("He leído y acepto los ")
    withStyle(
        SpanStyle(
            color = BrandColors.OrderReviewTermsLink,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline
        )
    ) {
        append("Términos de Compra")
    }
    append(", ")
    withStyle(
        SpanStyle(
            color = BrandColors.OrderReviewTermsLink,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline
        )
    ) {
        append("Política de Devoluciones")
    }
    append(" y ")
    withStyle(
        SpanStyle(
            color = BrandColors.OrderReviewTermsLink,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline
        )
    ) {
        append("Derecho de Retracto")
    }
    append(".")
}

@Preview(name = "TermsAcceptanceRow — sin marcar", showBackground = true, widthDp = 360)
@Composable
private fun TermsAcceptanceRowUncheckedPreview() {
    CafeterosTheme {
        var checked by remember { mutableStateOf(false) }
        TermsAcceptanceRow(
            modifier = Modifier.padding(BrandSpacing.lg),
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}

@Preview(name = "TermsAcceptanceRow — marcado", showBackground = true, widthDp = 360)
@Composable
private fun TermsAcceptanceRowCheckedPreview() {
    CafeterosTheme {
        TermsAcceptanceRow(
            modifier = Modifier.padding(BrandSpacing.lg),
            checked = true,
            onCheckedChange = {}
        )
    }
}

package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila checkbox + texto para consentimientos del Registro.
 *
 * El texto se acepta como [AnnotatedString] para que el llamante pueda darle
 * estilo a porciones específicas (ej. subrayar "Términos y Condiciones") usando
 * [BrandTypography.ConsentLink]. Pulsar el texto también marca/desmarca el
 * checkbox, igual que en patrones nativos de Material.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param checked estado actual del checkbox.
 * @param onCheckedChange callback al cambiar el estado.
 * @param label texto a la derecha del checkbox.
 */
@Composable
fun ConsentCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: AnnotatedString
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = BrandColors.CheckboxChecked,
                uncheckedColor = BrandColors.CheckboxBorder,
                checkmarkColor = BrandColors.CardBackground
            )
        )
        Text(
            text = label,
            style = BrandTypography.ConsentText,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(name = "ConsentCheckbox – términos", showBackground = true, widthDp = 360)
@Composable
private fun ConsentCheckboxTermsPreview() {
    CafeterosTheme {
        var checked by remember { mutableStateOf(false) }
        ConsentCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            label = androidx.compose.ui.text.buildAnnotatedString {
                append("Acepto los ")
                pushStyle(BrandTypography.ConsentLink.toSpanStyle())
                append("Términos y Condiciones")
                pop()
                append(" y la ")
                pushStyle(BrandTypography.ConsentLink.toSpanStyle())
                append("Política de Tratamiento de Datos")
                pop()
            }
        )
    }
}

@Preview(name = "ConsentCheckbox – marketing", showBackground = true, widthDp = 360)
@Composable
private fun ConsentCheckboxMarketingPreview() {
    CafeterosTheme {
        var checked by remember { mutableStateOf(true) }
        ConsentCheckbox(
            checked = checked,
            onCheckedChange = { checked = it },
            label = AnnotatedString("Quiero recibir novedades y promociones de caficultores")
        )
    }
}

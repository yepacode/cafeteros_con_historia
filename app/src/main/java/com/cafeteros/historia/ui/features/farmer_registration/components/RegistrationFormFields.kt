package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Wrapper genérico que dibuja un label en mayúsculas pequeñas arriba y delega
 * el campo de entrada al [content] slot debajo.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param label texto del label (típicamente en mayúsculas).
 * @param content composable del campo (TextField, dropdown, etc.).
 */
@Composable
fun LabeledField(
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp,
                color = BrandColors.TextPrimary
            )
        )
        content()
    }
}

/**
 * Campo de texto simple con [LabeledField] y estilo de marca.
 *
 * @param label texto del label.
 * @param value valor actual.
 * @param onValueChange callback al escribir.
 * @param placeholder texto guía cuando está vacío.
 * @param keyboardType tipo de teclado (texto, número, email, teléfono).
 * @param prefix prefijo opcional (ej. "+57").
 * @param suffix sufijo opcional (ej. "años", "msnm", "hectáreas").
 * @param helperText pequeño texto de ayuda bajo el campo.
 * @param singleLine si el input es de una sola línea (default true).
 */
@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    prefix: String? = null,
    suffix: String? = null,
    helperText: String? = null,
    singleLine: Boolean = true
) {
    LabeledField(modifier = modifier, label = label) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(color = BrandColors.InputHint)
                )
            },
            prefix = prefix?.let {
                {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = BrandColors.TextPrimary
                        ),
                        modifier = Modifier.padding(end = BrandSpacing.sm)
                    )
                }
            },
            suffix = suffix?.let {
                {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = BrandColors.TextSecondary
                        )
                    )
                }
            },
            singleLine = singleLine,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BrandColors.InputBackground,
                unfocusedContainerColor = BrandColors.InputBackground,
                disabledContainerColor = BrandColors.InputBackground,
                focusedTextColor = BrandColors.TextPrimary,
                unfocusedTextColor = BrandColors.TextPrimary,
                disabledTextColor = BrandColors.TextPrimary,
                cursorColor = BrandColors.CoffeeBrown,
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
        if (helperText != null) {
            Text(
                text = helperText,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = BrandColors.TextSecondary
                )
            )
        }
    }
}

/**
 * Dropdown con label superior. Al tocar, despliega un menú con [options];
 * al elegir uno, llama [onSelect].
 *
 * @param label texto del label.
 * @param selectedLabel etiqueta visible del valor actual; null/empty muestra placeholder.
 * @param placeholder texto cuando no hay nada seleccionado.
 * @param options pares (clave-interna, etiqueta-visible) para el menú.
 * @param onSelect callback cuando el usuario elige una opción.
 */
@Composable
fun LabeledDropdown(
    modifier: Modifier = Modifier,
    label: String,
    selectedLabel: String?,
    placeholder: String,
    options: List<Pair<String, String>>,
    onSelect: (key: String, displayLabel: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    LabeledField(modifier = modifier, label = label) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = BrandColors.InputBackground,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = BrandSpacing.md, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedLabel ?: placeholder,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                        color = if (selectedLabel.isNullOrEmpty()) {
                            BrandColors.InputHint
                        } else {
                            BrandColors.TextPrimary
                        }
                    )
                )
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null,
                    tint = BrandColors.TextSecondary
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { (key, displayLabel) ->
                    DropdownMenuItem(
                        text = { Text(text = displayLabel) },
                        onClick = {
                            onSelect(key, displayLabel)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Campo de fecha con label superior. Al tocar abre un Material 3 [DatePicker];
 * al confirmar, llama [onDateSelected] con el epoch millis elegido.
 *
 * @param label texto del label.
 * @param epochMillis fecha actualmente seleccionada (epoch en UTC) o null.
 * @param onDateSelected callback con la fecha elegida en epoch millis.
 * @param placeholder texto guía (ej. "mm/dd/yyyy").
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledDateField(
    modifier: Modifier = Modifier,
    label: String,
    epochMillis: Long?,
    onDateSelected: (Long) -> Unit,
    placeholder: String = "mm/dd/yyyy"
) {
    var showPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = epochMillis)

    val displayText = epochMillis?.let { formatDate(it) }

    LabeledField(modifier = modifier, label = label) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = BrandColors.InputBackground,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { showPicker = true }
                .padding(horizontal = BrandSpacing.md, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = displayText ?: placeholder,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    color = if (displayText == null) BrandColors.InputHint else BrandColors.TextPrimary
                )
            )
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    showPicker = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/** Formatea epoch millis al estilo `MM/dd/yyyy` consistente con el placeholder. */
private fun formatDate(epochMillis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(epochMillis))
}

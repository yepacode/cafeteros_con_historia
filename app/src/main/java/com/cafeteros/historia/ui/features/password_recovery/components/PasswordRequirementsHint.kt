package com.cafeteros.historia.ui.features.password_recovery.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/** Longitud mínima exigida para una contraseña válida. */
const val MIN_PASSWORD_LENGTH: Int = 8

/** Resultado de evaluar un requisito sobre una contraseña. */
private data class EvaluatedRequirement(val label: String, val isMet: Boolean)

/** Evalúa la contraseña contra todas las reglas y devuelve la lista. */
private fun evaluateRequirements(password: String): List<EvaluatedRequirement> = listOf(
    EvaluatedRequirement(
        label = "Al menos $MIN_PASSWORD_LENGTH caracteres",
        isMet = password.length >= MIN_PASSWORD_LENGTH
    ),
    EvaluatedRequirement(
        label = "Al menos una mayúscula",
        isMet = password.any { it.isUpperCase() }
    ),
    EvaluatedRequirement(
        label = "Al menos un número",
        isMet = password.any { it.isDigit() }
    )
)

/**
 * Indica si una contraseña cumple todos los requisitos. Útil para habilitar
 * o deshabilitar el botón "Guardar contraseña" desde la pantalla.
 */
fun isPasswordValid(password: String): Boolean =
    evaluateRequirements(password).all { it.isMet }

/**
 * Lista visual de los requisitos que debe cumplir la nueva contraseña.
 *
 * Cada requisito se renderiza como una fila con un ícono de check (verde si
 * el requisito ya se cumple, gris si aún no) y su descripción. Esto le da
 * feedback inmediato a la usuaria a medida que escribe la nueva contraseña.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param password contraseña actual a evaluar contra cada requisito.
 */
@Composable
fun PasswordRequirementsHint(
    modifier: Modifier = Modifier,
    password: String
) {
    val requirements = remember(password) { evaluateRequirements(password) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)
    ) {
        requirements.forEach { requirement ->
            RequirementRow(label = requirement.label, isMet = requirement.isMet)
        }
    }
}

@Composable
private fun RequirementRow(label: String, isMet: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = if (isMet) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isMet) BrandColors.LinkGreen else BrandColors.InputHint,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            style = BrandTypography.BrandTagline.copy(
                color = if (isMet) BrandColors.LinkGreen else BrandColors.TextSecondary,
                letterSpacing = 0.sp,
                fontSize = 12.sp
            )
        )
    }
}

@Preview(name = "Requirements – ninguno cumplido", showBackground = true, widthDp = 360)
@Composable
private fun RequirementsEmptyPreview() {
    CafeterosTheme { PasswordRequirementsHint(password = "") }
}

@Preview(name = "Requirements – todos cumplidos", showBackground = true, widthDp = 360)
@Composable
private fun RequirementsFullPreview() {
    CafeterosTheme { PasswordRequirementsHint(password = "Cafeteros2025") }
}

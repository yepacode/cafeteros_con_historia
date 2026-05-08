package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography

/**
 * Barra inferior con dos acciones del paso del registro: "Atrás" (secundario,
 * blanco con borde) y "Continuar" (primario, verde de marca caficultor).
 *
 * Vive como una fila fija al fondo de cada paso. La separación entre
 * botones es 1:2 (Atrás más estrecho que Continuar) — coincide con el diseño.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param onBackClick callback de "Atrás".
 * @param onContinueClick callback de "Continuar".
 * @param continueEnabled true si el usuario completó lo mínimo para avanzar.
 * @param continueLabel texto del botón Continuar (default "Continuar").
 */
@Composable
fun RegistrationActionBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    continueEnabled: Boolean = true,
    continueLabel: String = "Continuar"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.AuthBackground)
            .padding(
                horizontal = BrandSpacing.lg,
                vertical = BrandSpacing.md
            ),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = BrandColors.CardBackground,
                contentColor = BrandColors.TextPrimary
            ),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                brush = SolidColor(BrandColors.IndicatorInactive)
            )
        ) {
            Text(
                text = "Atrás",
                style = BrandTypography.PrimaryButtonLabel.copy(
                    color = BrandColors.TextPrimary
                )
            )
        }

        Button(
            onClick = onContinueClick,
            enabled = continueEnabled,
            modifier = Modifier
                .weight(2f)
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.FarmerPrimary,
                contentColor = BrandColors.PrimaryButtonText,
                disabledContainerColor = BrandColors.IndicatorInactive,
                disabledContentColor = BrandColors.PrimaryButtonText
            )
        ) {
            Text(
                text = continueLabel,
                style = BrandTypography.PrimaryButtonLabel
            )
        }
    }
}

@Preview(name = "RegistrationActionBar", widthDp = 360)
@Composable
private fun RegistrationActionBarPreview() {
    RegistrationActionBar(
        onBackClick = {},
        onContinueClick = {}
    )
}

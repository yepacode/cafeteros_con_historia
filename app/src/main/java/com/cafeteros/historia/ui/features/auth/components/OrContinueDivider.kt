package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Divider tipo "línea — texto — línea" con la leyenda "O CONTINÚA CON".
 *
 * Separa visualmente el formulario tradicional (email + password) de los
 * métodos de autenticación alternativos (Google, huella, etc.).
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 */
@Composable
fun OrContinueDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = BrandColors.DividerLine
        )
        Text(
            text = "O CONTINÚA CON",
            style = BrandTypography.DividerLabel,
            modifier = Modifier.padding(horizontal = BrandSpacing.xs)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = BrandColors.DividerLine
        )
    }
}

@Preview(name = "OrContinueDivider", showBackground = true, widthDp = 360)
@Composable
private fun OrContinueDividerPreview() {
    CafeterosTheme {
        OrContinueDivider()
    }
}

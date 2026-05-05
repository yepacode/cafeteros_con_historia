package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pie de página del Registro: "¿Ya tienes cuenta? Inicia sesión".
 *
 * Inverso conceptual de [RegisterFooter] (que aparece en Login). Solo el
 * verbo "Inicia sesión" es interactivo.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param onLoginClick callback al pulsar "Inicia sesión".
 */
@Composable
fun AlreadyHaveAccountFooter(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¿Ya tienes cuenta? ",
            style = BrandTypography.RegisterFooterText
        )
        Text(
            text = "Inicia sesión",
            style = BrandTypography.RegisterFooterAction,
            modifier = Modifier
                .clickable(onClick = onLoginClick)
                .padding(horizontal = BrandSpacing.xs)
        )
    }
}

@Preview(name = "AlreadyHaveAccountFooter", showBackground = true, widthDp = 360)
@Composable
private fun AlreadyHaveAccountFooterPreview() {
    CafeterosTheme {
        AlreadyHaveAccountFooter(onLoginClick = {})
    }
}

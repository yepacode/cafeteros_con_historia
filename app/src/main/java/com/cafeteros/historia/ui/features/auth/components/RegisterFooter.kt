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
 * Pie de página del login: "¿No tienes cuenta? Regístrate".
 *
 * El texto fijo se muestra siempre y el verbo "Regístrate" es la única
 * porción interactiva. Cuando exista la pantalla de registro, [onRegisterClick]
 * deberá lanzarla; por ahora puede dejarse como stub.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param onRegisterClick callback al pulsar "Regístrate".
 */
@Composable
fun RegisterFooter(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¿No tienes cuenta? ",
            style = BrandTypography.RegisterFooterText
        )
        Text(
            text = "Regístrate",
            style = BrandTypography.RegisterFooterAction,
            modifier = Modifier
                .clickable(onClick = onRegisterClick)
                .padding(horizontal = BrandSpacing.xs)
        )
    }
}

@Preview(name = "RegisterFooter", showBackground = true, widthDp = 360)
@Composable
private fun RegisterFooterPreview() {
    CafeterosTheme {
        RegisterFooter(onRegisterClick = {})
    }
}

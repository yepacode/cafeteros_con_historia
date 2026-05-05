package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Wordmark "Origen" en serif itálico. Aparece en la cabecera del login y de
 * cualquier pantalla del flujo de autenticación.
 *
 * Es solo texto: no carga drawables ni recursos extra. Cuando exista una
 * fuente custom de marca, basta con cambiar [BrandTypography.Wordmark].
 */
@Composable
fun OrigenWordmark(modifier: Modifier = Modifier) {
    Text(
        text = "Origen",
        style = BrandTypography.Wordmark,
        modifier = modifier
    )
}

@Preview(name = "OrigenWordmark", showBackground = true)
@Composable
private fun OrigenWordmarkPreview() {
    CafeterosTheme {
        OrigenWordmark()
    }
}

package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Botón ancho café oscuro al final del scroll que abre el listado completo
 * de productos del caficultor.
 *
 * Visual idéntico al "Crear mi cuenta" del registro pero con su propia
 * etiqueta tipográfica (semibold sans-serif). Si en el futuro se decide
 * unificar todos los botones primarios oscuros, se reemplaza por
 * `AuthPrimaryButton` con el label apropiado.
 *
 * @param modifier modifier opcional.
 * @param onClick callback al pulsar.
 */
@Composable
fun ViewAllProductsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandColors.WideDarkButtonBackground,
            contentColor = BrandColors.WideDarkButtonText
        )
    ) {
        Text(text = "Ver todos los productos", style = BrandTypography.WideDarkButtonLabel)
    }
}

@Preview(name = "ViewAllProductsButton", showBackground = true, widthDp = 360)
@Composable
private fun ViewAllProductsButtonPreview() {
    CafeterosTheme {
        ViewAllProductsButton(onClick = {})
    }
}

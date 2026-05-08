package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bloque "Santander" + tagline italic justo debajo del hero.
 *
 * Es un componente trivial pero se mantiene en su propio archivo para
 * cumplir con la convención del proyecto (un componente, un archivo) y
 * permitir reutilizarlo en otras pantallas que necesiten el mismo patrón
 * de "título grande + subtítulo poético".
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param title nombre de la zona ("Santander").
 * @param tagline subtítulo italic ("Café de altura, carácter fuerte").
 */
@Composable
fun ZoneTitleBlock(
    modifier: Modifier = Modifier,
    title: String,
    tagline: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = title, style = BrandTypography.ZoneDetailTitle)
        Text(text = tagline, style = BrandTypography.ZoneDetailSubtitle)
    }
}

@Preview(name = "ZoneTitleBlock", showBackground = true, widthDp = 360)
@Composable
private fun ZoneTitleBlockPreview() {
    CafeterosTheme {
        ZoneTitleBlock(
            title = "Santander",
            tagline = "Café de altura, carácter fuerte"
        )
    }
}

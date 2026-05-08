package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "La historia de {caficultor}".
 *
 * Estructura:
 *  1. Título serif bold.
 *  2. Cita destacada en italic centrada.
 *  3. Primer párrafo en sans-serif con line-height generoso.
 *  4. Foto del caficultor recortada en una card con esquinas redondeadas.
 *  5. Segundo párrafo.
 *  6. Link "Leer toda la historia" en verde.
 *
 * @param modifier modifier opcional.
 * @param shortName nombre corto a usar en el título ("Don Alberto").
 * @param quote cita destacada que va dentro de comillas.
 * @param part1 primer párrafo del cuerpo.
 * @param imageRes foto entre los dos párrafos.
 * @param part2 segundo párrafo del cuerpo.
 * @param onReadFullStoryClick callback al pulsar el link.
 */
@Composable
fun CaficultorHistorySection(
    modifier: Modifier = Modifier,
    shortName: String,
    quote: String,
    part1: String,
    imageRes: Int,
    part2: String,
    onReadFullStoryClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(
            text = "La historia de $shortName",
            style = BrandTypography.ZoneSectionTitle
        )

        Text(
            text = quote,
            style = BrandTypography.HistoryQuote,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = part1, style = BrandTypography.ZoneDescriptionBody)

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(14.dp))
        )

        Text(text = part2, style = BrandTypography.ZoneDescriptionBody)

        Text(
            text = "Leer toda la historia",
            style = BrandTypography.ZoneDescriptionReadMore,
            modifier = Modifier.clickable(onClick = onReadFullStoryClick)
        )
    }
}

@Preview(name = "CaficultorHistorySection", showBackground = true, widthDp = 360)
@Composable
private fun CaficultorHistorySectionPreview() {
    CafeterosTheme {
        CaficultorHistorySection(
            shortName = "Don Alberto",
            quote = "\"Cada grano que ves en este café lleva 40 años de tradición familiar\"",
            part1 = "En el corazón de Pitalito, Huila, Don Alberto ha dedicado su vida a " +
                    "perfeccionar el cultivo del café Caturra y Borbón.",
            imageRes = R.drawable.ima_1,
            part2 = "Desde la recolección manual hasta el secado en camas africanas, cada " +
                    "proceso se realiza bajo la atenta mirada de Don Alberto y su familia."
        )
    }
}

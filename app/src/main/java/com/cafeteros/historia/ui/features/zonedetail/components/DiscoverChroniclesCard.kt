package com.cafeteros.historia.ui.features.zonedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card oscura de cierre con CTA "DESCUBRIR CRÓNICAS".
 *
 * Estructura: bloque café con título serif bold, párrafo en crema sutil, y
 * un botón pill dorado de ancho completo abajo. Es el call-to-action final
 * de la pantalla, invitando a profundizar en las historias editoriales de
 * la región.
 *
 * @param modifier modifier opcional.
 * @param zoneName nombre de la zona usado en la pregunta del título.
 * @param onDiscoverClick callback del botón "DESCUBRIR CRÓNICAS".
 */
@Composable
fun DiscoverChroniclesCard(
    modifier: Modifier = Modifier,
    zoneName: String,
    onDiscoverClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BrandColors.DiscoverChroniclesBackground)
            .padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(
            text = "¿Quieres saber más sobre $zoneName?",
            style = BrandTypography.DiscoverChroniclesTitle,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Explora las historias completas de nuestras fincas y el proceso " +
                    "de beneficio exclusivo de esta región.",
            style = BrandTypography.DiscoverChroniclesBody,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onDiscoverClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.DiscoverChroniclesButtonBackground,
                contentColor = BrandColors.DiscoverChroniclesButtonText
            )
        ) {
            Text(text = "DESCUBRIR CRÓNICAS", style = BrandTypography.DiscoverChroniclesButton)
        }
    }
}

@Preview(name = "DiscoverChroniclesCard", showBackground = true, widthDp = 360)
@Composable
private fun DiscoverChroniclesCardPreview() {
    CafeterosTheme {
        DiscoverChroniclesCard(zoneName = "Santander")
    }
}

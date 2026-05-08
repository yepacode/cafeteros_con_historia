package com.cafeteros.historia.ui.features.caficultordetail.components

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
 * Card cremita "¿Tienes preguntas para {caficultor}?" con un botón verde
 * suave de mensaje en su interior.
 *
 * Es el call-to-action que invita al comprador a abrir un chat directo
 * con el caficultor; por ahora el toque dispara [onSendMessage] que la
 * Activity puede mapear a un Toast hasta que exista la pantalla de chat.
 *
 * @param modifier modifier opcional.
 * @param caficultorShortName nombre corto usado en el título.
 * @param onSendMessage callback del botón "Enviar mensaje".
 */
@Composable
fun ContactCaficultorCard(
    modifier: Modifier = Modifier,
    caficultorShortName: String,
    onSendMessage: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BrandColors.ContactCardBackground)
            .padding(BrandSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(
            text = "¿Tienes preguntas para $caficultorShortName?",
            style = BrandTypography.ContactCardTitle,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onSendMessage,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.ContactButtonBackground,
                contentColor = BrandColors.ContactButtonText
            )
        ) {
            Text(text = "Enviar mensaje 💬", style = BrandTypography.ContactButtonLabel)
        }
    }
}

@Preview(name = "ContactCaficultorCard", showBackground = true, widthDp = 360)
@Composable
private fun ContactCaficultorCardPreview() {
    CafeterosTheme {
        ContactCaficultorCard(caficultorShortName = "Don Alberto")
    }
}

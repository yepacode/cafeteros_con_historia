package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pill horizontal con info del productor:
 *  - Avatar circular a la izquierda.
 *  - Bloque central: "PRODUCIDO POR" + nombre serif + finca italic.
 *  - Enlace amber "Ver perfil →" a la derecha que lleva al detalle del
 *    caficultor.
 *
 * @param modifier modifier opcional.
 * @param avatarRes drawable del avatar.
 * @param producerName nombre del caficultor.
 * @param producerFarm nombre de la finca.
 * @param onViewProfile callback del enlace "Ver perfil".
 */
@Composable
fun ProductProducerPill(
    modifier: Modifier = Modifier,
    avatarRes: Int,
    producerName: String,
    producerFarm: String,
    onViewProfile: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.ProducerPillBackground)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = "PRODUCIDO POR", style = BrandTypography.ProducerSectionLabel)
            Text(text = producerName, style = BrandTypography.ProducerName)
            Text(text = producerFarm, style = BrandTypography.ProducerFarm)
        }

        Text(
            text = "Ver perfil →",
            style = BrandTypography.ViewProfileLink,
            modifier = Modifier.clickable(onClick = onViewProfile)
        )
    }
}

@Preview(name = "ProductProducerPill", showBackground = true, widthDp = 360)
@Composable
private fun ProductProducerPillPreview() {
    CafeterosTheme {
        ProductProducerPill(
            modifier = Modifier.padding(BrandSpacing.lg),
            avatarRes = R.drawable.ima_1,
            producerName = "Don Alberto Ramírez",
            producerFarm = "Finca La Esperanza"
        )
    }
}

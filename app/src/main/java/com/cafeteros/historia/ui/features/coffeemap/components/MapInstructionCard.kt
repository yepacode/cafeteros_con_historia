package com.cafeteros.historia.ui.features.coffeemap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card flotante de instrucción "Toca una zona para explorar" que aparece
 * sobre el lienzo del mapa.
 *
 * Visual: pill blanca redondeada con sombra suave, ícono de "toque" en
 * dorado/café a la izquierda y el texto a la derecha en dos líneas.
 *
 * Es puramente decorativa/informativa — no maneja estado propio.
 *
 * @param modifier modifier opcional aplicado al contenedor.
 * @param text mensaje a mostrar (default = "Toca una zona para explorar").
 */
@Composable
fun MapInstructionCard(
    modifier: Modifier = Modifier,
    text: String = "Toca una zona para explorar"
) {
    Row(
        modifier = modifier
            .widthIn(max = 190.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
            .background(BrandColors.MapInstructionCardBackground)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = Icons.Outlined.TouchApp,
            contentDescription = null,
            tint = BrandColors.RatingStar,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = text,
            style = BrandTypography.MapInstructionText
        )
    }
}

@Preview(name = "MapInstructionCard", showBackground = true, widthDp = 280)
@Composable
private fun MapInstructionCardPreview() {
    CafeterosTheme {
        MapInstructionCard()
    }
}

package com.cafeteros.historia.ui.features.coffeemap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Barra superior de la pantalla de Mapa Cafetero.
 *
 * Renderiza tres elementos en una sola fila:
 *  - Botón de regreso (flecha) a la izquierda.
 *  - Título "Mapa Cafetero" centrado en serif itálico.
 *  - Botón de filtros (sliders) a la derecha.
 *
 * El título queda centrado visualmente porque ambos extremos llevan un
 * [IconButton] del mismo ancho.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param onBack callback de la flecha de regreso.
 * @param onFilter callback del ícono de filtros.
 */
@Composable
fun CoffeeMapTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onFilter: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = BrandColors.TextPrimary
            )
        }
        Text(
            text = "Mapa Cafetero",
            style = BrandTypography.MapScreenTitle
        )
        IconButton(onClick = onFilter) {
            Icon(
                imageVector = Icons.Outlined.Tune,
                contentDescription = "Filtros",
                tint = BrandColors.TextPrimary
            )
        }
    }
}

@Preview(name = "CoffeeMapTopBar", showBackground = true, widthDp = 360)
@Composable
private fun CoffeeMapTopBarPreview() {
    CafeterosTheme {
        CoffeeMapTopBar()
    }
}

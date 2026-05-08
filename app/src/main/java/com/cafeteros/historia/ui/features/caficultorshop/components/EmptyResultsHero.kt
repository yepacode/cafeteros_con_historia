package com.cafeteros.historia.ui.features.caficultorshop.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bloque central del empty state.
 *
 * Compone vertical:
 *  1. Imagen circular desaturada con un velo crema sobre la foto y un
 *     pequeño badge circular blanco con la lupa-X en la esquina inferior
 *     derecha.
 *  2. Título serif italic "No encontramos resultados".
 *  3. Cuerpo descriptivo centrado.
 *  4. Botón outline verde "Limpiar filtros" con icono refresh.
 *
 * @param modifier modifier opcional.
 * @param title título serif italic.
 * @param body cuerpo descriptivo.
 * @param onClearFilters callback del botón "Limpiar filtros".
 */
@Composable
fun EmptyResultsHero(
    modifier: Modifier = Modifier,
    title: String = "No encontramos resultados",
    body: String = "Intenta ajustar tus filtros o buscar otro término " +
            "para descubrir los tesoros de nuestras montañas.",
    onClearFilters: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
    ) {
        EmptyResultsImage()

        Text(
            text = title,
            style = BrandTypography.EmptyResultsTitle,
            textAlign = TextAlign.Center
        )
        Text(
            text = body,
            style = BrandTypography.EmptyResultsBody,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = BrandSpacing.md)
        )

        ClearFiltersOutlineButton(onClick = onClearFilters)
    }
}

@Composable
private fun EmptyResultsImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        // Círculo desaturado de fondo + foto recortada de la taza.
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(BrandColors.EmptyResultsImageScrim),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.65f,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
            // Velo crema sobre la imagen para desaturarla aún más.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f))
            )
        }

        // Badge circular blanco con la lupa-X.
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(48.dp)
                .clip(CircleShape)
                .background(BrandColors.EmptyResultsBadgeBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.SearchOff,
                contentDescription = null,
                tint = BrandColors.EmptyResultsBadgeIcon,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ClearFiltersOutlineButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .border(BorderStroke(1.5.dp, BrandColors.ClearFiltersOutlineBorder), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.lg, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = Icons.Outlined.Refresh,
            contentDescription = null,
            tint = BrandColors.ClearFiltersOutlineText,
            modifier = Modifier.size(18.dp)
        )
        Text(text = "Limpiar filtros", style = BrandTypography.ClearFiltersOutlineLabel)
    }
}

@Preview(name = "EmptyResultsHero", showBackground = true, widthDp = 360, heightDp = 600)
@Composable
private fun EmptyResultsHeroPreview() {
    CafeterosTheme {
        EmptyResultsHero(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

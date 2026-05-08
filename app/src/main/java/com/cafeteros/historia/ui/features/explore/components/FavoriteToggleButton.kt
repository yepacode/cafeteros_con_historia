package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Botón circular de favorito que se sobrepone a la imagen del producto.
 *
 * Cambia entre corazón delineado / lleno según [isFavorite] y siempre
 * mantiene un fondo blanco translúcido para garantizar contraste sobre
 * cualquier imagen de fondo.
 *
 * @param modifier modifier opcional.
 * @param isFavorite estado actual del favorito.
 * @param onToggle callback al pulsar; el padre debe invertir el estado.
 */
@Composable
fun FavoriteToggleButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(BrandColors.FavoriteButtonBackground)
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
            tint = if (isFavorite) BrandColors.FavoriteIconActive else BrandColors.FavoriteIconInactive,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Preview(name = "FavoriteToggle – inactive", showBackground = true)
@Composable
private fun FavoriteToggleButtonInactivePreview() {
    CafeterosTheme {
        FavoriteToggleButton(isFavorite = false, onToggle = {})
    }
}

@Preview(name = "FavoriteToggle – active", showBackground = true)
@Composable
private fun FavoriteToggleButtonActivePreview() {
    CafeterosTheme {
        FavoriteToggleButton(isFavorite = true, onToggle = {})
    }
}

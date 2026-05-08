package com.cafeteros.historia.ui.features.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Una fila de la lista "Búsquedas recientes".
 *
 * Visual:
 *  - Ícono de reloj/historial a la izquierda.
 *  - Texto de la búsqueda en el centro (clickable).
 *  - "X" a la derecha para borrar solo este item.
 *
 * @param modifier modifier opcional.
 * @param query texto a renderizar.
 * @param onClick callback al pulsar la fila (re-disparar búsqueda).
 * @param onRemove callback al pulsar la X.
 */
@Composable
fun RecentSearchItem(
    modifier: Modifier = Modifier,
    query: String,
    onClick: () -> Unit = {},
    onRemove: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Icon(
            imageVector = Icons.Outlined.History,
            contentDescription = null,
            tint = BrandColors.RecentSearchIcon,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = query,
            style = BrandTypography.RecentSearchText,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Eliminar búsqueda",
                tint = BrandColors.RecentSearchRemove,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview(name = "RecentSearchItem", showBackground = true, widthDp = 360)
@Composable
private fun RecentSearchItemPreview() {
    CafeterosTheme {
        RecentSearchItem(
            modifier = Modifier.padding(horizontal = BrandSpacing.lg),
            query = "Café de especialidad Huila"
        )
    }
}

package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila de rating con estrella + valor numérico + total de reseñas.
 *
 * Renderiza: ⭐ 4.9 (127). El valor del rating se formatea con un decimal
 * fijo; si llega como entero (5.0) igualmente se muestra "5.0", lo cual es
 * el comportamiento esperado en plataformas tipo Rappi/Uber Eats.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param rating valor del rating (ej. 4.9).
 * @param reviewCount total de reseñas (ej. 127).
 */
@Composable
fun CaficultorRatingRow(
    modifier: Modifier = Modifier,
    rating: Double,
    reviewCount: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = BrandColors.RatingStar,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = String.format(java.util.Locale.US, "%.1f", rating),
            style = BrandTypography.RatingValue
        )
        Text(
            text = "($reviewCount)",
            style = BrandTypography.RatingCount
        )
    }
}

@Preview(name = "RatingRow", showBackground = true)
@Composable
private fun CaficultorRatingRowPreview() {
    CafeterosTheme {
        CaficultorRatingRow(rating = 4.9, reviewCount = 127)
    }
}

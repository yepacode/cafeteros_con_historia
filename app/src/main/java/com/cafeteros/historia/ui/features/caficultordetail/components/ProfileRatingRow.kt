package com.cafeteros.historia.ui.features.caficultordetail.components

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
 * Fila "⭐ 4.9 (127 reseñas)" alineada a la derecha del avatar en la
 * profile card. Reusa el patrón de rating de otros lugares pero con el
 * estilo tipográfico específico de esta pantalla y la palabra "reseñas"
 * incluida.
 *
 * @param modifier modifier opcional.
 * @param rating valor 0–5.
 * @param reviewCount total de reseñas.
 */
@Composable
fun ProfileRatingRow(
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
            style = BrandTypography.ProfileRatingLabel
        )
        Text(
            text = "($reviewCount reseñas)",
            style = BrandTypography.RatingCount
        )
    }
}

@Preview(name = "ProfileRatingRow", showBackground = true)
@Composable
private fun ProfileRatingRowPreview() {
    CafeterosTheme {
        ProfileRatingRow(rating = 4.9, reviewCount = 127)
    }
}

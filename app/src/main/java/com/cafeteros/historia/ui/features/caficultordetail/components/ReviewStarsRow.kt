package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila de 5 estrellas con [filledCount] llenas en dorado y el resto
 * delineadas. Sirve para representar la calificación de una reseña.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param filledCount cuántas estrellas pintar llenas (0–5). Se hace clamp
 *  al rango válido para que valores fuera de rango no rompan el render.
 * @param starSize tamaño de cada estrella (default 14dp).
 */
@Composable
fun ReviewStarsRow(
    modifier: Modifier = Modifier,
    filledCount: Int,
    starSize: androidx.compose.ui.unit.Dp = 14.dp
) {
    val safeCount = filledCount.coerceIn(0, 5)
    Row(modifier = modifier) {
        repeat(safeCount) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = BrandColors.RatingStar,
                modifier = Modifier.size(starSize)
            )
        }
        repeat(5 - safeCount) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = BrandColors.RatingStar,
                modifier = Modifier.size(starSize)
            )
        }
    }
}

@Preview(name = "ReviewStarsRow – 5 estrellas", showBackground = true)
@Composable
private fun ReviewStarsRowFullPreview() {
    CafeterosTheme {
        ReviewStarsRow(filledCount = 5)
    }
}

@Preview(name = "ReviewStarsRow – 3 estrellas", showBackground = true)
@Composable
private fun ReviewStarsRowPartialPreview() {
    CafeterosTheme {
        ReviewStarsRow(filledCount = 3)
    }
}

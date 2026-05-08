package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.explore.model.CaficultorBadge
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Píldora pequeña que muestra el distintivo del caficultor ("ORGÁNICO",
 * "SOSTENIBLE"). Toma su pareja de colores fondo/texto del propio enum
 * [CaficultorBadge], por lo que el componente queda libre de lógica de
 * colores: añadir un nuevo distintivo es sumar un valor al enum.
 *
 * @param modifier modifier opcional.
 * @param badge distintivo a renderizar.
 */
@Composable
fun CaficultorBadgePill(
    modifier: Modifier = Modifier,
    badge: CaficultorBadge
) {
    Text(
        text = badge.label,
        style = BrandTypography.BadgeLabel.copy(color = badge.textColor),
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(badge.backgroundColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Preview(name = "Badge – Orgánico", showBackground = true)
@Composable
private fun CaficultorBadgePillOrganicoPreview() {
    CafeterosTheme {
        CaficultorBadgePill(badge = CaficultorBadge.ORGANICO)
    }
}

@Preview(name = "Badge – Sostenible", showBackground = true)
@Composable
private fun CaficultorBadgePillSosteniblePreview() {
    CafeterosTheme {
        CaficultorBadgePill(badge = CaficultorBadge.SOSTENIBLE)
    }
}

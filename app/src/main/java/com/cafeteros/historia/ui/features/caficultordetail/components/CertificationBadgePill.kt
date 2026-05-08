package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.caficultordetail.model.CertificationBadge
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pill verde con un distintivo del caficultor ("ORGÁNICO CERTIFICADO"…).
 *
 * Toma su texto del propio enum [CertificationBadge] para que añadir un
 * distintivo nuevo no requiera tocar este componente — solo el enum.
 *
 * @param modifier modifier opcional.
 * @param badge distintivo a renderizar.
 */
@Composable
fun CertificationBadgePill(
    modifier: Modifier = Modifier,
    badge: CertificationBadge
) {
    Text(
        text = badge.label,
        style = BrandTypography.CertificationBadgeLabel,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(BrandColors.CertificationBadgeBackground)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    )
}

@Preview(name = "CertificationBadgePill", showBackground = true)
@Composable
private fun CertificationBadgePillPreview() {
    CafeterosTheme {
        CertificationBadgePill(badge = CertificationBadge.ORGANICO_CERTIFICADO)
    }
}

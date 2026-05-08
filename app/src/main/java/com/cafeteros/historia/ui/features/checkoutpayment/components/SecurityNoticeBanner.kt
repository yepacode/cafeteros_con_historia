package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Banner verde claro de tranquilidad bajo el listado de métodos:
 * "Pago 100% seguro. Tus datos están protegidos con cifrado SSL.".
 *
 * Es solo informativo — no admite acciones, por eso no tiene callback.
 *
 * @param modifier modifier opcional.
 * @param text mensaje a mostrar.
 */
@Composable
fun SecurityNoticeBanner(
    modifier: Modifier = Modifier,
    text: String = "Pago 100% seguro. Tus datos están protegidos con cifrado SSL."
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BrandColors.SecurityNoticeBackground)
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.md),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = Icons.Outlined.Shield,
            contentDescription = null,
            tint = BrandColors.SecurityNoticeIcon,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 1.dp)
        )
        Text(text = text, style = BrandTypography.SecurityNoticeText)
    }
}

@Preview(name = "SecurityNoticeBanner", showBackground = true, widthDp = 360)
@Composable
private fun SecurityNoticeBannerPreview() {
    CafeterosTheme {
        SecurityNoticeBanner(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

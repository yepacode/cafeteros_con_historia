package com.cafeteros.historia.ui.features.farmer_registration.components

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Banner verde de confianza usado en el paso 4 para reforzar al caficultor
 * que sus documentos sensibles serán manejados con discreción.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param text mensaje a mostrar (default: copy de privacidad).
 */
@Composable
fun TrustBanner(
    modifier: Modifier = Modifier,
    text: String = "Tus documentos están encriptados y solo los ve el equipo de verificación."
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.TrustBannerBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = Icons.Outlined.Shield,
            contentDescription = null,
            tint = BrandColors.TrustBannerAccent,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = BrandColors.TextPrimary
            )
        )
    }
}

@Preview(name = "TrustBanner", widthDp = 360)
@Composable
private fun TrustBannerPreview() {
    TrustBanner()
}

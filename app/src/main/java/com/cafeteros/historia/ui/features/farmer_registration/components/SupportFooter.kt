package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Bloque inferior del paso 5: aviso de notificación + ¿Tienes dudas? +
 * botón "Contactar soporte" + enlace "Cerrar sesión".
 *
 * @param modifier modifier opcional aplicado al [Column].
 * @param onContactSupport callback al pulsar "Contactar soporte".
 * @param onLogout callback al pulsar "Cerrar sesión".
 */
@Composable
fun SupportFooter(
    modifier: Modifier = Modifier,
    onContactSupport: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(
            text = "Te avisaremos cuando esté aprobado",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp,
                color = BrandColors.TextSecondary
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "¿Tienes dudas?",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        OutlinedButton(
            onClick = onContactSupport,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = BrandColors.CardBackground,
                contentColor = BrandColors.TextPrimary
            ),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                brush = SolidColor(BrandColors.IndicatorInactive)
            )
        ) {
            Text(
                text = "Contactar soporte",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
        }
        TextButton(onClick = onLogout) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = null,
                    tint = BrandColors.TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Cerrar sesión",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = BrandColors.TextSecondary
                    )
                )
            }
        }
    }
}

@Preview(name = "SupportFooter", widthDp = 360)
@Composable
private fun SupportFooterPreview() {
    SupportFooter(
        onContactSupport = {},
        onLogout = {}
    )
}

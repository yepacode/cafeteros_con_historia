package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Banner informativo crema-amarillo con un acento lateral, usado para
 * informar al caficultor de la zona detectada por su ubicación previa.
 *
 * Combina texto plano + texto en negrita + un CTA tappeable. Mantiene la
 * accesibilidad: el CTA es un Composable separado con su propio click,
 * no un span clicable, así los lectores de pantalla lo identifican como
 * una acción.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param leadingText texto introductorio (ej. "Tu café pertenece a la zona cafetera de:").
 * @param highlight texto destacado en negrita (ej. "Huila").
 * @param actionLabel etiqueta del CTA (ej. "CAMBIAR UBICACIÓN").
 * @param onActionClick callback al tocar el CTA.
 */
@Composable
fun InfoBanner(
    modifier: Modifier = Modifier,
    leadingText: String,
    highlight: String,
    actionLabel: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.InfoBannerBackground,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.Top
    ) {
        // Acento lateral izquierdo (4dp de ancho, mismo alto que el banner).
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .width(4.dp)
                .background(
                    color = BrandColors.InfoBannerAccent,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                )
                .fillMaxWidth(0f) // ancho fijo, ignorar fillMaxWidth heredado
        ) {
            // Espaciador vertical para que la Box estire al alto del padre.
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)) {}
        }

        Row(
            modifier = Modifier.padding(BrandSpacing.md),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = BrandColors.InfoBannerAction,
                modifier = Modifier.size(20.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.xs)) {
                Text(
                    text = buildAnnotatedString {
                        append("$leadingText ")
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = BrandColors.TextPrimary
                            )
                        ) {
                            append(highlight)
                        }
                    },
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = BrandColors.TextPrimary
                    )
                )
                Text(
                    text = actionLabel,
                    modifier = Modifier.clickable(onClick = onActionClick),
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = BrandColors.InfoBannerAction
                    )
                )
            }
        }
    }
}

@Preview(name = "InfoBanner – ubicación", widthDp = 360)
@Composable
private fun InfoBannerPreview() {
    InfoBanner(
        leadingText = "Según tu ubicación registrada, tu café pertenece a la zona cafetera de:",
        highlight = "Huila",
        actionLabel = "CAMBIAR UBICACIÓN",
        onActionClick = {}
    )
}

package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Tarjeta de contenido de la sección "Mientras tanto..." del paso 5.
 *
 * Layout: ícono cuadrado (izquierda con fondo de color) + columna (título +
 * descripción + enlace de acción).
 *
 * Tocar el enlace dispara [onActionClick]. La tarjeta entera también es
 * tappeable para mejorar el área de toque (target accesibilidad).
 *
 * @param modifier modifier opcional aplicado a la tarjeta.
 * @param icon ícono Material a la izquierda.
 * @param iconBackground color del cuadrado del ícono (para diferenciar tipos).
 * @param title título de la tarjeta.
 * @param description copy explicativo.
 * @param actionLabel etiqueta del enlace (ej. "Leer guía →").
 * @param onActionClick callback al pulsar el enlace o la tarjeta entera.
 */
@Composable
fun MeantimeContentCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconBackground: Color,
    title: String,
    description: String,
    actionLabel: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.CardBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onActionClick)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        IconSquare(icon = icon, backgroundColor = iconBackground)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = BrandColors.TextSecondary
                )
            )
            Text(
                text = actionLabel,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable(onClick = onActionClick),
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.FarmerPrimary
                )
            )
        }
    }
}

/** Ícono cuadrado con fondo de color (para destacar tipo de contenido). */
@Composable
private fun IconSquare(icon: ImageVector, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(name = "MeantimeContentCard", widthDp = 360)
@Composable
private fun MeantimeContentCardPreview() {
    MeantimeContentCard(
        icon = Icons.Outlined.MenuBook,
        iconBackground = BrandColors.FarmerPrimary,
        title = "Guía del caficultor exitoso",
        description = "Aprende a tomar mejores fotos de tus granos para vender más rápido.",
        actionLabel = "Leer guía →",
        onActionClick = {}
    )
}

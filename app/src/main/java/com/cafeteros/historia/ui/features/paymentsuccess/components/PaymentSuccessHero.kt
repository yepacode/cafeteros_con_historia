package com.cafeteros.historia.ui.features.paymentsuccess.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Hero decorativo del paso 4 (éxito): badge dorado con check sobre un anillo
 * blanco con borde dorado tenue, rodeado de pequeños confetis cuadrados
 * rotados en tres colores distintos para reforzar el momento celebratorio.
 *
 * Las posiciones del confeti son fijas por diseño — replicadas pixel-a-pixel
 * a partir del mock entregado.
 *
 * @param modifier modifier opcional.
 */
@Composable
fun PaymentSuccessHero(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(top = BrandSpacing.sm),
        contentAlignment = Alignment.Center
    ) {
        // Confetis decorativos — orden y posición fijos por fidelidad al diseño.
        ConfettiDot(
            modifier = Modifier
                .offset(x = (-95).dp, y = (-30).dp)
                .rotate(25f),
            color = BrandColors.PaymentSuccessConfettiGold,
            size = 12.dp
        )
        ConfettiDot(
            modifier = Modifier
                .offset(x = (-110).dp, y = 38.dp)
                .rotate(-20f),
            color = BrandColors.PaymentSuccessConfettiGold,
            size = 8.dp
        )
        ConfettiDot(
            modifier = Modifier
                .offset(x = 90.dp, y = (-45).dp)
                .rotate(-30f),
            color = BrandColors.PaymentSuccessConfettiGreen,
            size = 10.dp
        )
        ConfettiDot(
            modifier = Modifier
                .offset(x = 105.dp, y = 10.dp)
                .rotate(35f),
            color = BrandColors.PaymentSuccessConfettiGold,
            size = 9.dp
        )
        ConfettiDot(
            modifier = Modifier
                .offset(x = 80.dp, y = 50.dp)
                .rotate(20f),
            color = BrandColors.PaymentSuccessConfettiTeal,
            size = 8.dp
        )

        // Anillo blanco con borde dorado.
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(BrandColors.PaymentSuccessHeroRingBackground)
                .border(
                    width = 4.dp,
                    color = BrandColors.PaymentSuccessHeroRingBorder,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Badge dorado central.
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(BrandColors.PaymentSuccessHeroBadgeFill),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = BrandColors.PaymentSuccessHeroBadgeIcon,
                    modifier = Modifier.size(46.dp)
                )
            }
        }
    }
}

@Composable
private fun ConfettiDot(
    modifier: Modifier = Modifier,
    color: Color,
    size: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(2.dp))
            .background(color)
    )
}

@Preview(name = "PaymentSuccessHero", showBackground = true, widthDp = 360)
@Composable
private fun PaymentSuccessHeroPreview() {
    CafeterosTheme {
        PaymentSuccessHero()
    }
}

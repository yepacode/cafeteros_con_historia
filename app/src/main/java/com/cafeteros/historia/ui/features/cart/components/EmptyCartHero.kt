package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Bloque central del empty state del carrito.
 *
 * Compone vertical:
 *  1. Ilustración minimalista line-art de una taza grande con asa,
 *     plato y vapor, dibujada con [Canvas] para no depender de un asset.
 *  2. Título serif bold "Tu carrito está vacío".
 *  3. Subtítulo gris descriptivo.
 *  4. CTA oscuro "Explorar café".
 *
 * @param modifier modifier opcional.
 * @param title título del empty state.
 * @param subtitle subtítulo descriptivo.
 * @param ctaLabel texto del botón principal.
 * @param onCtaClick callback del CTA "Explorar café".
 */
@Composable
fun EmptyCartHero(
    modifier: Modifier = Modifier,
    title: String = "Tu carrito está vacío",
    subtitle: String = "Descubre café de caficultores colombianos",
    ctaLabel: String = "Explorar café",
    onCtaClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
    ) {
        EmptyCartIllustration(modifier = Modifier.size(width = 220.dp, height = 220.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            Text(
                text = title,
                style = BrandTypography.EmptyCartTitle,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                style = BrandTypography.EmptyCartSubtitle,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = BrandSpacing.md)
            )
        }

        ExploreCafeCta(label = ctaLabel, onClick = onCtaClick)
    }
}

/**
 * Dibujo de la taza line-art del empty state. Usa [Canvas] para no
 * depender de un asset y para que el grosor del trazo escale con el
 * tamaño asignado al modifier.
 */
@Composable
private fun EmptyCartIllustration(modifier: Modifier = Modifier) {
    val color = BrandColors.EmptyCartIllustration

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = w * 0.012f)

        // Vapor: pequeña línea curva sobre la taza.
        val steamY = h * 0.10f
        drawLine(
            color = color,
            start = Offset(x = w * 0.40f, y = steamY),
            end = Offset(x = w * 0.55f, y = steamY - h * 0.025f),
            strokeWidth = stroke.width
        )

        // Cuerpo de la taza (rectángulo redondeado grande).
        val bodyLeft = w * 0.18f
        val bodyTop = h * 0.18f
        val bodyRight = w * 0.74f
        val bodyBottom = h * 0.78f
        drawRoundRect(
            color = color,
            topLeft = Offset(bodyLeft, bodyTop),
            size = Size(bodyRight - bodyLeft, bodyBottom - bodyTop),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.06f, w * 0.06f),
            style = stroke
        )

        // Asa lateral derecha (rectángulo redondeado).
        val handleLeft = w * 0.74f
        val handleTop = h * 0.32f
        val handleRight = w * 0.86f
        val handleBottom = h * 0.62f
        drawRoundRect(
            color = color,
            topLeft = Offset(handleLeft, handleTop),
            size = Size(handleRight - handleLeft, handleBottom - handleTop),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.06f, w * 0.06f),
            style = stroke
        )

        // Pequeña tacita interior (decoración).
        val miniLeft = w * 0.36f
        val miniTop = h * 0.40f
        val miniRight = w * 0.56f
        val miniBottom = h * 0.55f
        drawRoundRect(
            color = color,
            topLeft = Offset(miniLeft, miniTop),
            size = Size(miniRight - miniLeft, miniBottom - miniTop),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.02f, w * 0.02f),
            style = stroke
        )
        // Línea horizontal dentro de la mini taza (sugiere café).
        drawLine(
            color = color,
            start = Offset(x = miniLeft + w * 0.02f, y = h * 0.50f),
            end = Offset(x = miniRight - w * 0.02f, y = h * 0.50f),
            strokeWidth = stroke.width
        )

        // Plato bajo el cuerpo (línea horizontal larga).
        drawLine(
            color = color,
            start = Offset(x = w * 0.30f, y = h * 0.66f),
            end = Offset(x = w * 0.62f, y = h * 0.66f),
            strokeWidth = stroke.width
        )
    }
}

@Composable
private fun ExploreCafeCta(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(BrandColors.EmptyCartCtaBackground)
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.xl, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, style = BrandTypography.EmptyCartCtaLabel)
    }
}

@Preview(name = "EmptyCartHero", showBackground = true, widthDp = 360, heightDp = 600)
@Composable
private fun EmptyCartHeroPreview() {
    CafeterosTheme {
        EmptyCartHero(modifier = Modifier.padding(BrandSpacing.lg))
    }
}

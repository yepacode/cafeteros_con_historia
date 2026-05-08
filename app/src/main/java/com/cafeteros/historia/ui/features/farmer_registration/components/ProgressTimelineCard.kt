package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.features.farmer_registration.model.VerificationProgressItem
import com.cafeteros.historia.ui.features.farmer_registration.model.VerificationProgressStatus
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Tarjeta con la lista de pasos del timeline de verificación.
 *
 * Cada elemento es una fila con un círculo de color según [VerificationProgressStatus]:
 *  - DONE → verde con check.
 *  - IN_PROGRESS → naranja-mostaza con reloj.
 *  - PENDING → círculo gris vacío.
 *
 * @param modifier modifier opcional aplicado al [Column] contenedor.
 * @param items lista de pasos a mostrar.
 */
@Composable
fun ProgressTimelineCard(
    modifier: Modifier = Modifier,
    items: List<VerificationProgressItem>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.CardBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        items.forEach { item ->
            ProgressTimelineRow(item = item)
        }
    }
}

/** Una fila del timeline: círculo + título + subtítulo. */
@Composable
private fun ProgressTimelineRow(item: VerificationProgressItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        StatusCircle(status = item.status)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    fontWeight = if (item.status == VerificationProgressStatus.PENDING) {
                        FontWeight.Normal
                    } else {
                        FontWeight.SemiBold
                    },
                    color = if (item.status == VerificationProgressStatus.PENDING) {
                        BrandColors.TextSecondary
                    } else {
                        BrandColors.TextPrimary
                    }
                )
            )
            Text(
                text = item.subtitle,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    fontWeight = if (item.status == VerificationProgressStatus.IN_PROGRESS) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    },
                    letterSpacing = if (item.status == VerificationProgressStatus.IN_PROGRESS) {
                        1.sp
                    } else {
                        0.sp
                    },
                    color = when (item.status) {
                        VerificationProgressStatus.DONE -> BrandColors.TextSecondary
                        VerificationProgressStatus.IN_PROGRESS -> BrandColors.StatusInProgress
                        VerificationProgressStatus.PENDING -> BrandColors.TextSecondary
                    }
                )
            )
        }
    }
}

/** Círculo de estado: color y contenido varían según [status]. */
@Composable
private fun StatusCircle(status: VerificationProgressStatus) {
    val backgroundColor = when (status) {
        VerificationProgressStatus.DONE -> BrandColors.StatusDone
        VerificationProgressStatus.IN_PROGRESS -> BrandColors.StatusInProgress
        VerificationProgressStatus.PENDING -> BrandColors.StatusPending
    }
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(color = backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when (status) {
            VerificationProgressStatus.DONE -> Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            VerificationProgressStatus.IN_PROGRESS -> Icon(
                imageVector = Icons.Outlined.HourglassEmpty,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            VerificationProgressStatus.PENDING -> Unit
        }
    }
}

@Preview(name = "ProgressTimelineCard", widthDp = 360)
@Composable
private fun ProgressTimelineCardPreview() {
    ProgressTimelineCard(
        items = listOf(
            VerificationProgressItem(
                "Registro recibido",
                "Hoy, 10 abril 11:55",
                VerificationProgressStatus.DONE
            ),
            VerificationProgressItem(
                "Documentos revisados",
                "Hoy, 14:35",
                VerificationProgressStatus.DONE
            ),
            VerificationProgressItem(
                "Verificación de identidad",
                "EN CURSO",
                VerificationProgressStatus.IN_PROGRESS
            ),
            VerificationProgressItem(
                "Verificación de finca",
                "Pendiente",
                VerificationProgressStatus.PENDING
            ),
            VerificationProgressItem(
                "Activación final",
                "Pendiente",
                VerificationProgressStatus.PENDING
            )
        )
    )
}

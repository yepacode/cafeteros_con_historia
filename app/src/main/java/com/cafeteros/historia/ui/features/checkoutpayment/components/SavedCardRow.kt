package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.checkoutpayment.model.SavedCard
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila de una tarjeta guardada dentro del bloque expandido de
 * "Tarjeta de crédito/débito". Muestra ícono de tarjeta, máscara
 * "**** 4567 Visa" y un check dorado a la derecha cuando está activa.
 *
 * @param modifier modifier opcional.
 * @param card tarjeta guardada a renderizar.
 * @param selected si esta tarjeta es la activa para pagar.
 * @param onClick callback al pulsar la fila — el caller actualiza la
 *   tarjeta activa.
 */
@Composable
fun SavedCardRow(
    modifier: Modifier = Modifier,
    card: SavedCard,
    selected: Boolean,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BrandColors.SavedCardRowBackground)
            .clickable(onClick = onClick)
            .padding(horizontal = BrandSpacing.md, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = Icons.Outlined.CreditCard,
            contentDescription = null,
            tint = BrandColors.TextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = card.maskedLabel,
            style = BrandTypography.SavedCardLabel,
            modifier = Modifier.weight(1f)
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(BrandColors.SavedCardCheckBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = BrandColors.SavedCardCheckIcon,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Preview(name = "SavedCardRow", showBackground = true, widthDp = 360)
@Composable
private fun SavedCardRowPreview() {
    CafeterosTheme {
        SavedCardRow(
            modifier = Modifier.padding(BrandSpacing.lg),
            card = SavedCard(id = "x", brand = "Visa", last4 = "4567"),
            selected = true
        )
    }
}

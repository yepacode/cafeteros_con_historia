package com.cafeteros.historia.ui.features.paymentfailed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
 * Bloque "¿Qué puedes hacer?" del paso 4 (fallo): título serif italic +
 * lista de items con bullet dorado.
 *
 * @param modifier modifier opcional.
 * @param items listado de acciones sugeridas.
 */
@Composable
fun WhatToDoChecklist(
    modifier: Modifier = Modifier,
    items: List<String>
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(
            text = "¿Qué puedes hacer?",
            style = BrandTypography.PaymentFailedChecklistTitle
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = BrandSpacing.xs),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            items.forEach { item ->
                ChecklistRow(text = item)
            }
        }
    }
}

@Composable
private fun ChecklistRow(modifier: Modifier = Modifier, text: String) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(BrandColors.PaymentFailedChecklistBullet)
        )
        Text(text = text, style = BrandTypography.PaymentFailedChecklistItem)
    }
}

@Preview(name = "WhatToDoChecklist", showBackground = true, widthDp = 360)
@Composable
private fun WhatToDoChecklistPreview() {
    CafeterosTheme {
        WhatToDoChecklist(
            modifier = Modifier.padding(BrandSpacing.lg),
            items = listOf(
                "Verifica el saldo de tu tarjeta",
                "Intenta con otro método de pago",
                "Contacta a tu banco",
                "Contáctanos si el problema persiste"
            )
        )
    }
}

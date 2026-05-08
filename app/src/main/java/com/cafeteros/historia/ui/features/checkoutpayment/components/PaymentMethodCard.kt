package com.cafeteros.historia.ui.features.checkoutpayment.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.checkoutpayment.model.PaymentMethod
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card seleccionable de un método de pago.
 *
 * Layout:
 *  - Fila superior con radio + emoji + título y subtítulo opcional.
 *  - [expandedContent] opcional debajo, solo visible cuando [selected] es
 *    true. Para "Tarjeta de crédito/débito" contiene la fila de marcas,
 *    la tarjeta guardada y el enlace "Agregar nueva tarjeta".
 *
 * Cuando [selected] es true, la card pinta fondo blanco, borde dorado y
 * radio relleno con check ✓ blanco. Cuando no, fondo beige sin borde y
 * radio outline vacío.
 *
 * @param modifier modifier opcional.
 * @param method método a renderizar.
 * @param selected estado de selección.
 * @param onSelect callback al pulsar la card.
 * @param expandedContent contenido a renderizar bajo la fila principal
 *   cuando la card está seleccionada (solo se invoca en ese caso).
 */
@Composable
fun PaymentMethodCard(
    modifier: Modifier = Modifier,
    method: PaymentMethod,
    selected: Boolean,
    onSelect: () -> Unit = {},
    expandedContent: (@Composable () -> Unit)? = null
) {
    val shape = RoundedCornerShape(14.dp)
    val containerModifier = if (selected) {
        Modifier
            .clip(shape)
            .background(BrandColors.PaymentMethodCardSelectedBackground)
            .border(BorderStroke(1.5.dp, BrandColors.PaymentMethodCardSelectedBorder), shape)
    } else {
        Modifier
            .clip(shape)
            .background(BrandColors.PaymentMethodCardUnselectedBackground)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(containerModifier)
            .clickable(onClick = onSelect)
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
        ) {
            PaymentRadio(selected = selected)
            Text(text = method.emoji, style = BrandTypography.PaymentMethodEmoji)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = method.title, style = BrandTypography.PaymentMethodTitle)
                if (method.subtitle != null) {
                    Text(
                        text = method.subtitle,
                        style = BrandTypography.PaymentMethodSubtitle
                    )
                }
            }
        }

        if (selected && expandedContent != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                expandedContent()
            }
        }
    }
}

@Composable
private fun PaymentRadio(modifier: Modifier = Modifier, selected: Boolean) {
    if (selected) {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(BrandColors.PaymentMethodRadioSelectedFill),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = BrandColors.PaymentMethodRadioSelectedCheck,
                modifier = Modifier.size(14.dp)
            )
        }
    } else {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = BrandColors.PaymentMethodRadioUnselectedBorder,
                    shape = CircleShape
                )
        )
    }
}

@Preview(name = "PaymentMethodCard — selected", showBackground = true, widthDp = 360)
@Composable
private fun PaymentMethodCardSelectedPreview() {
    CafeterosTheme {
        PaymentMethodCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            method = PaymentMethod.TARJETA,
            selected = true,
            expandedContent = {
                CardBrandStrip()
            }
        )
    }
}

@Preview(name = "PaymentMethodCard — unselected", showBackground = true, widthDp = 360)
@Composable
private fun PaymentMethodCardUnselectedPreview() {
    CafeterosTheme {
        PaymentMethodCard(
            modifier = Modifier.padding(BrandSpacing.lg),
            method = PaymentMethod.NEQUI,
            selected = false
        )
    }
}

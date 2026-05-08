package com.cafeteros.historia.ui.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
 * Fila con el input "Código de descuento" y el botón oscuro "Aplicar".
 *
 * El input usa [BasicTextField] sin línea inferior para que coincida con
 * el resto de inputs de la app (mismo patrón que [com.cafeteros.historia.ui.features.search.components.SearchTopBar]).
 *
 * @param modifier modifier opcional.
 * @param code valor actual del input.
 * @param onCodeChange callback al editar.
 * @param onApply callback del botón "Aplicar".
 */
@Composable
fun DiscountCodeRow(
    modifier: Modifier = Modifier,
    code: String,
    onCodeChange: (String) -> Unit,
    onApply: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrandColors.CartDiscountInputBackground)
                .padding(horizontal = BrandSpacing.md),
            contentAlignment = Alignment.CenterStart
        ) {
            if (code.isEmpty()) {
                Text(
                    text = "Código de descuento",
                    style = BrandTypography.CartDiscountInput
                )
            }
            BasicTextField(
                value = code,
                onValueChange = onCodeChange,
                singleLine = true,
                textStyle = BrandTypography.CartDiscountInput.copy(
                    color = BrandColors.TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrandColors.CartApplyButtonBackground)
                .clickable(onClick = onApply)
                .padding(horizontal = BrandSpacing.lg),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Aplicar", style = BrandTypography.CartApplyButton)
        }
    }
}

@Preview(name = "DiscountCodeRow", showBackground = true, widthDp = 360)
@Composable
private fun DiscountCodeRowPreview() {
    CafeterosTheme {
        DiscountCodeRow(
            modifier = Modifier.padding(BrandSpacing.lg),
            code = "",
            onCodeChange = {},
            onApply = {}
        )
    }
}

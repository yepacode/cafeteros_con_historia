package com.cafeteros.historia.ui.features.orderreview.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Header de cada sección del paso 3 del checkout: título serif a la izquierda
 * y un enlace verde "Editar" opcional a la derecha.
 *
 * El enlace solo se renderiza si [onEdit] no es nulo — sirve para los bloques
 * que sí permiten regresar al paso anterior (Envío / Método de pago) y queda
 * oculto en los puramente informativos (Productos / Entrega estimada).
 *
 * @param modifier modifier opcional.
 * @param title título serif bold ("Envío a", "Productos (3)"…).
 * @param onEdit callback del enlace "Editar"; si es nulo, el enlace no se
 *   muestra.
 * @param editLabel texto del enlace, por defecto "Editar".
 */
@Composable
fun ReviewSectionHeader(
    modifier: Modifier = Modifier,
    title: String,
    onEdit: (() -> Unit)? = null,
    editLabel: String = "Editar"
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = BrandTypography.OrderReviewSectionTitle,
            modifier = Modifier.weight(1f)
        )
        if (onEdit != null) {
            Text(
                text = editLabel,
                style = BrandTypography.OrderReviewEditLink,
                modifier = Modifier.clickable(onClick = onEdit)
            )
        }
    }
}

@Preview(name = "ReviewSectionHeader — con Editar", showBackground = true, widthDp = 360)
@Composable
private fun ReviewSectionHeaderEditablePreview() {
    CafeterosTheme {
        ReviewSectionHeader(
            modifier = Modifier.padding(BrandSpacing.lg),
            title = "Envío a",
            onEdit = {}
        )
    }
}

@Preview(name = "ReviewSectionHeader — solo título", showBackground = true, widthDp = 360)
@Composable
private fun ReviewSectionHeaderTitleOnlyPreview() {
    CafeterosTheme {
        ReviewSectionHeader(
            modifier = Modifier.padding(BrandSpacing.lg),
            title = "Entrega estimada"
        )
    }
}

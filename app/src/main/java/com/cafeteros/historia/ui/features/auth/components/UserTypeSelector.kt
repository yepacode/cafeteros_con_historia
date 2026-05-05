package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Selector "Soy Comprador / Soy Caficultor" del Registro.
 *
 * Renderiza una [UserTypeCard] por cada valor de [UserType] en una fila con
 * peso 1f cada una. La fila usa [IntrinsicSize.Min] en altura para que ambas
 * cards queden de la misma altura aunque sus textos varíen.
 *
 * Stateless: padre mantiene [selected] y reacciona con [onSelect].
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param selected opción actualmente activa.
 * @param onSelect callback al seleccionar otra opción.
 */
@Composable
fun UserTypeSelector(
    modifier: Modifier = Modifier,
    selected: UserType,
    onSelect: (UserType) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        UserType.entries.forEach { type ->
            UserTypeCard(
                modifier = Modifier.weight(1f),
                userType = type,
                selected = selected == type,
                onClick = { onSelect(type) }
            )
        }
    }
}

@Preview(name = "UserTypeSelector", showBackground = true, widthDp = 360)
@Composable
private fun UserTypeSelectorPreview() {
    CafeterosTheme {
        var selected by remember { mutableStateOf(UserType.COMPRADOR) }
        UserTypeSelector(
            selected = selected,
            onSelect = { selected = it }
        )
    }
}

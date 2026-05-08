package com.cafeteros.historia.ui.features.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.productdetail.model.ProductDetailTab
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Fila de tabs ("Descripción / Notas de cata / Proceso") con un indicador
 * subrayado bajo la tab activa.
 *
 * El indicador es una barra fija bajo cada label que solo se pinta para la
 * activa — sin animar — porque la pantalla es estática y se redibuja al
 * cambiar el tab. Si más adelante queremos animar, se sustituye por un
 * `TabRow` de Material 3 con su propio indicator.
 *
 * @param modifier modifier opcional.
 * @param selected tab actualmente activa.
 * @param onTabSelected callback al pulsar una tab.
 */
@Composable
fun ProductDetailTabs(
    modifier: Modifier = Modifier,
    selected: ProductDetailTab,
    onTabSelected: (ProductDetailTab) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
    ) {
        ProductDetailTab.entries.forEach { tab ->
            TabItem(
                label = tab.label,
                selected = tab == selected,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
private fun TabItem(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = BrandSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = if (selected) BrandTypography.TabActive else BrandTypography.TabInactive
        )
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .height(2.dp)
                .width(if (selected) 28.dp else 0.dp)
                .background(BrandColors.TabIndicatorActive)
        )
    }
}

@Preview(name = "ProductDetailTabs", showBackground = true, widthDp = 360)
@Composable
private fun ProductDetailTabsPreview() {
    CafeterosTheme {
        ProductDetailTabs(
            modifier = Modifier.padding(BrandSpacing.lg),
            selected = ProductDetailTab.DESCRIPCION,
            onTabSelected = {}
        )
    }
}

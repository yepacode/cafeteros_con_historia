package com.cafeteros.historia.ui.features.zonedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.zonedetail.components.CaficultorFilterChip
import com.cafeteros.historia.ui.features.zonedetail.components.DiscoverChroniclesCard
import com.cafeteros.historia.ui.features.zonedetail.components.FlavorProfileCard
import com.cafeteros.historia.ui.features.zonedetail.components.MunicipalitiesSection
import com.cafeteros.historia.ui.features.zonedetail.components.ZoneCaficultorCard
import com.cafeteros.historia.ui.features.explore.components.ExploreBottomBar
import com.cafeteros.historia.ui.features.explore.components.ExploreTab
import com.cafeteros.historia.ui.features.zonedetail.components.ZoneDescriptionSection
import com.cafeteros.historia.ui.features.zonedetail.components.ZoneDetailTopBar
import com.cafeteros.historia.ui.features.zonedetail.components.ZoneHeroSection
import com.cafeteros.historia.ui.features.zonedetail.components.ZoneStatsRow
import com.cafeteros.historia.ui.features.zonedetail.components.ZoneTitleBlock
import com.cafeteros.historia.ui.features.zonedetail.model.CaficultorFilter
import com.cafeteros.historia.ui.features.zonedetail.model.ZoneCaficultor
import com.cafeteros.historia.ui.features.zonedetail.model.ZoneDetail
import com.cafeteros.historia.ui.features.zonedetail.model.ZoneDetailSampleData
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla de detalle de una zona cafetera.
 *
 * Layout vertical scrollable:
 *  1. Hero con imagen + top bar transparente sobrepuesto + badge "ZONA CAFETERA".
 *  2. Bloque de título serif + tagline italic.
 *  3. Fila de 3 stats con divisores.
 *  4. Card "Perfil de sabor" (pills de notas + sliders).
 *  5. Sección "Sobre {zona}" con párrafo expandible.
 *  6. Carrusel horizontal de municipios (chips).
 *  7. Sección "Caficultores de {zona}" con chips de filtro + grilla 2x2.
 *  8. Card oscura de cierre "DESCUBRIR CRÓNICAS".
 *  9. Bottom bar unificada de la app (5 tabs).
 *
 * Mantiene en estado: municipio seleccionado, filtro activo, pestaña
 * inferior activa. Las navegaciones a sub-pantallas (detalle de finca,
 * crónicas, etc.) salen por callbacks.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param zoneDetail datos de la zona a renderizar.
 * @param onBack callback de la flecha del top bar.
 * @param onShare callback del ícono de compartir.
 * @param onCaficultorClick callback al pulsar una card de caficultor.
 * @param onDiscoverChronicles callback del botón final.
 */
@Composable
fun ZoneDetailScreen(
    modifier: Modifier = Modifier,
    zoneDetail: ZoneDetail = ZoneDetailSampleData.forName("Santander"),
    onBack: () -> Unit = {},
    onShare: () -> Unit = {},
    onCaficultorClick: (ZoneCaficultor) -> Unit = {},
    onDiscoverChronicles: () -> Unit = {}
) {
    var selectedMunicipality by remember {
        mutableStateOf(zoneDetail.municipalities.firstOrNull()?.name)
    }
    var selectedFilter by remember { mutableStateOf(CaficultorFilter.TODOS) }
    var selectedTab by remember { mutableStateOf(ExploreTab.EXPLORAR) }
    val cartItemCount = 3

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.ZoneDetailBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                ZoneHeroSection(
                    heroImageRes = zoneDetail.heroImageRes,
                    onShare = onShare
                )
                ZoneDetailTopBar(
                    zoneName = zoneDetail.name,
                    onBack = onBack,
                    onShare = onShare,
                    modifier = Modifier.statusBarsPadding()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
            ) {
                ZoneTitleBlock(
                    title = zoneDetail.name,
                    tagline = zoneDetail.tagline
                )

                ZoneStatsRow(
                    caficultorCount = zoneDetail.caficultorCount,
                    altitudeText = zoneDetail.averageAltitudeText,
                    rating = zoneDetail.averageRating
                )

                FlavorProfileCard(
                    tags = zoneDetail.flavorTags,
                    traits = zoneDetail.flavorTraits
                )

                ZoneDescriptionSection(
                    title = "Sobre ${zoneDetail.name}",
                    body = zoneDetail.description
                )

                MunicipalitiesSection(
                    selectedName = selectedMunicipality,
                    municipalities = zoneDetail.municipalities,
                    onSelect = { selectedMunicipality = it.name }
                )

                CaficultoresSection(
                    zoneName = zoneDetail.name,
                    caficultores = zoneDetail.caficultores,
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it },
                    onCaficultorClick = onCaficultorClick
                )

                DiscoverChroniclesCard(
                    zoneName = zoneDetail.name,
                    onDiscoverClick = onDiscoverChronicles
                )

                Spacer(modifier = Modifier.height(BrandSpacing.md))
            }
        }

        ExploreBottomBar(
            selected = selectedTab,
            onTabSelected = { selectedTab = it },
            cartItemCount = cartItemCount
        )
    }
}

/**
 * Sub-sección "Caficultores de {zona}" con su título, fila de filtros y la
 * grilla 2-columnas. Vive como composable interno de la pantalla porque
 * solo se usa aquí — extraerlo a su propio archivo agregaría boilerplate
 * sin valor.
 */
@Composable
private fun CaficultoresSection(
    zoneName: String,
    caficultores: List<ZoneCaficultor>,
    selectedFilter: CaficultorFilter,
    onFilterSelected: (CaficultorFilter) -> Unit,
    onCaficultorClick: (ZoneCaficultor) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
        Text(
            text = "Caficultores de $zoneName",
            style = BrandTypography.ZoneSectionTitle
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm),
            contentPadding = PaddingValues(end = BrandSpacing.lg)
        ) {
            items(items = CaficultorFilter.entries, key = { it.name }) { filter ->
                CaficultorFilterChip(
                    filter = filter,
                    isSelected = filter == selectedFilter,
                    onClick = { onFilterSelected(filter) }
                )
            }
        }

        // Grilla 2-columnas armada como Columns de pares de Rows. No usamos
        // LazyVerticalGrid porque el composable padre ya hace verticalScroll
        // y eso entra en conflicto con grids verticales lazy.
        Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
            caficultores.chunked(2).forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { caficultor ->
                        ZoneCaficultorCard(
                            caficultor = caficultor,
                            modifier = Modifier.weight(1f),
                            onClick = { onCaficultorClick(caficultor) }
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(name = "ZoneDetailScreen", widthDp = 360, heightDp = 1800)
@Composable
private fun ZoneDetailScreenPreview() {
    CafeterosTheme {
        ZoneDetailScreen()
    }
}

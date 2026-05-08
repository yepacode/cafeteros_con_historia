package com.cafeteros.historia.ui.features.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.filters.components.AltitudeFilterSection
import com.cafeteros.historia.ui.features.filters.components.CertificationFilterSection
import com.cafeteros.historia.ui.features.filters.components.FilterBottomBar
import com.cafeteros.historia.ui.features.filters.components.FilterTopBar
import com.cafeteros.historia.ui.features.filters.components.FlavorFilterSection
import com.cafeteros.historia.ui.features.filters.components.PriceRangeSection
import com.cafeteros.historia.ui.features.filters.components.ProcessFilterSection
import com.cafeteros.historia.ui.features.filters.components.RoastFilterSection
import com.cafeteros.historia.ui.features.filters.components.ShippingFilterSection
import com.cafeteros.historia.ui.features.filters.components.SortByFilterSection
import com.cafeteros.historia.ui.features.filters.components.ZoneFilterGrid
import com.cafeteros.historia.ui.features.filters.model.FilterZone
import com.cafeteros.historia.ui.features.filters.model.OriginFilters
import com.cafeteros.historia.ui.features.filters.model.OriginFiltersSampleData
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla "Filtros de Origen".
 *
 * Layout vertical:
 *  1. [FilterTopBar] (X · "Filtros de Origen" · Limpiar).
 *  2. Sección "ZONA CAFETERA" (grid 2 columnas).
 *  3. Sección "RANGO DE PRECIO" (slider + inputs).
 *  4. "PERFIL DE SABOR" (chips con wrap).
 *  5. "PROCESO" (radios, exclusivo).
 *  6. "TUESTE" (5 tazas, exclusivo).
 *  7. "CERTIFICACIONES" (checkboxes, multi).
 *  8. "ALTITUD" (cards, exclusivo).
 *  9. "ENVÍO" (2 toggles).
 *  10. "ORDENAR POR" (radios, exclusivo).
 *  11. [FilterBottomBar] anclado al fondo (LIMPIAR TODO + VER X RESULTADOS).
 *
 * El estado vive como un único [OriginFilters] que se hidrata con
 * [initialFilters]; cuando cambia cualquier sección, la pantalla copia el
 * registro con el nuevo valor. Eso permite que `Limpiar` (tanto del top bar
 * como del bottom) se implemente con un solo `currentFilters = OriginFilters()`.
 *
 * @param modifier modifier opcional.
 * @param initialFilters estado inicial de los filtros — útil para pre-cargar
 *   selecciones cuando el usuario reabre la pantalla.
 * @param zones zonas disponibles. Default: las del repositorio de muestra.
 * @param resultsCount número visible dentro del CTA "VER X RESULTADOS".
 *   Cuando exista backend, este valor reactivo lo provee el padre.
 * @param onClose callback de la X y "Cancelar".
 * @param onApply callback del botón principal — entrega el [OriginFilters]
 *   final.
 */
@Composable
fun OriginFiltersScreen(
    modifier: Modifier = Modifier,
    initialFilters: OriginFilters = OriginFilters(
        selectedZones = setOf("Santander"),
        selectedFlavors = setOf(
            com.cafeteros.historia.ui.features.filters.model.FilterFlavor.CHOCOLATE,
            com.cafeteros.historia.ui.features.filters.model.FilterFlavor.CITRICO
        ),
        certifications = setOf(
            com.cafeteros.historia.ui.features.filters.model.FilterCertification.COMERCIO_JUSTO
        ),
        selectedRoast = com.cafeteros.historia.ui.features.filters.model.FilterRoast.MEDIO,
        altitude = com.cafeteros.historia.ui.features.filters.model.FilterAltitude.ALTURAS
    ),
    zones: List<FilterZone> = OriginFiltersSampleData.zones,
    resultsCount: Int = OriginFiltersSampleData.INITIAL_RESULTS_COUNT,
    onClose: () -> Unit = {},
    onApply: (OriginFilters) -> Unit = {}
) {
    var currentFilters by remember { mutableStateOf(initialFilters) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.FiltersBackground)
            .statusBarsPadding()
    ) {
        FilterTopBar(
            onClose = onClose,
            onClear = { currentFilters = OriginFilters() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.xs))

            Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)) {
                com.cafeteros.historia.ui.features.filters.components.FilterSectionLabel(
                    text = "ZONA CAFETERA"
                )
                ZoneFilterGrid(
                    zones = zones,
                    selectedNames = currentFilters.selectedZones,
                    onZoneToggle = { zone ->
                        currentFilters = currentFilters.copy(
                            selectedZones = currentFilters.selectedZones.toggle(zone.name)
                        )
                    }
                )
            }

            PriceRangeSection(
                value = currentFilters.priceRange,
                onValueChange = { currentFilters = currentFilters.copy(priceRange = it) }
            )

            FlavorFilterSection(
                selected = currentFilters.selectedFlavors,
                onToggle = { flavor ->
                    currentFilters = currentFilters.copy(
                        selectedFlavors = currentFilters.selectedFlavors.toggle(flavor)
                    )
                }
            )

            ProcessFilterSection(
                selected = currentFilters.process,
                onSelect = { currentFilters = currentFilters.copy(process = it) }
            )

            RoastFilterSection(
                selected = currentFilters.selectedRoast,
                onSelect = { currentFilters = currentFilters.copy(selectedRoast = it) }
            )

            CertificationFilterSection(
                selected = currentFilters.certifications,
                onToggle = { cert ->
                    currentFilters = currentFilters.copy(
                        certifications = currentFilters.certifications.toggle(cert)
                    )
                }
            )

            AltitudeFilterSection(
                selected = currentFilters.altitude,
                onSelect = { altitude ->
                    currentFilters = currentFilters.copy(
                        altitude = if (currentFilters.altitude == altitude) null else altitude
                    )
                }
            )

            ShippingFilterSection(
                freeShipping = currentFilters.freeShipping,
                expressShipping = currentFilters.expressShipping,
                onFreeShippingChange = { currentFilters = currentFilters.copy(freeShipping = it) },
                onExpressShippingChange = { currentFilters = currentFilters.copy(expressShipping = it) }
            )

            SortByFilterSection(
                selected = currentFilters.sortBy,
                onSelect = { currentFilters = currentFilters.copy(sortBy = it) }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.md))
        }

        FilterBottomBar(
            resultsCount = resultsCount,
            onClearAll = { currentFilters = OriginFilters() },
            onApply = { onApply(currentFilters) }
        )
    }
}

/** Helper para alternar la pertenencia de un valor a un Set. */
private fun <T> Set<T>.toggle(value: T): Set<T> =
    if (value in this) this - value else this + value

@Preview(name = "OriginFiltersScreen", widthDp = 360, heightDp = 2400)
@Composable
private fun OriginFiltersScreenPreview() {
    CafeterosTheme {
        OriginFiltersScreen()
    }
}

package com.cafeteros.historia.ui.features.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.explore.components.CaficultorCard
import com.cafeteros.historia.ui.features.explore.components.ExploreBottomBar
import com.cafeteros.historia.ui.features.explore.components.ExploreGreeting
import com.cafeteros.historia.ui.features.explore.components.ExploreSearchBar
import com.cafeteros.historia.ui.features.explore.components.ExploreSectionHeader
import com.cafeteros.historia.ui.features.explore.components.ExploreTab
import com.cafeteros.historia.ui.features.explore.components.ExploreTopBar
import com.cafeteros.historia.ui.features.explore.components.ProductCard
import com.cafeteros.historia.ui.features.explore.components.RegionCard
import com.cafeteros.historia.ui.features.explore.components.StoryCard
import com.cafeteros.historia.ui.features.explore.model.CoffeeProduct
import com.cafeteros.historia.ui.features.explore.model.CoffeeRegion
import com.cafeteros.historia.ui.features.explore.model.CoffeeStory
import com.cafeteros.historia.ui.features.explore.model.ExploreSampleData
import com.cafeteros.historia.ui.features.explore.model.FeaturedCaficultor
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Pantalla raíz del flujo del comprador, mostrada justo después de crear la
 * cuenta con rol "Soy Comprador".
 *
 * Compone, en orden vertical:
 *  1. Top bar con menú, wordmark "Origen" y campana de notificaciones.
 *  2. Saludo personalizado y pregunta editorial.
 *  3. Barra de búsqueda + botón de filtros verde.
 *  4. Carrusel "Explora por zona" (regiones cafeteras).
 *  5. Carrusel "Caficultores destacados" (cards con avatar dorado).
 *  6. Listado vertical "Historias que enamoran" (cards horizontales).
 *  7. Carrusel "Lo más pedido esta semana" (productos con favoritos).
 *  8. Barra de navegación inferior con badge en el carrito.
 *
 * Mantiene en estado: query de búsqueda, pestaña activa y conteo del
 * carrito. La navegación a sub-secciones (mapa, detalle de zona, detalle de
 * caficultor, etc.) sale por callbacks para que la Activity decida.
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param userName nombre corto a mostrar en el saludo ("Buenos días, Mich").
 * @param regions listas de zonas (default: muestras estáticas).
 * @param caficultores caficultores destacados.
 * @param stories historias editoriales.
 * @param popularProducts productos del top semanal.
 * @param onMenuClick callback del botón hamburguesa.
 * @param onNotificationsClick callback de la campana.
 * @param onFilterClick callback del botón verde de filtros.
 * @param onSearchClick callback al pulsar la barra de búsqueda — abre la
 *   pantalla dedicada de búsqueda en lugar de habilitar la edición inline.
 * @param onCartTabClick callback al pulsar el tab "CARRITO" del bottom bar —
 *   abre la pantalla del carrito en lugar de quedarse en la home.
 * @param onSeeAllRegions callback de "Ver mapa" en regiones.
 * @param onSeeAllCaficultores callback de "Ver todos" en caficultores.
 * @param onRegionClick callback al pulsar una región.
 * @param onCaficultorClick callback al pulsar un caficultor.
 * @param onStoryClick callback al pulsar una historia.
 * @param onProductClick callback al pulsar un producto.
 */
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    userName: String = "Mich",
    regions: List<CoffeeRegion> = ExploreSampleData.regions,
    caficultores: List<FeaturedCaficultor> = ExploreSampleData.featuredCaficultores,
    stories: List<CoffeeStory> = ExploreSampleData.stories,
    popularProducts: List<CoffeeProduct> = ExploreSampleData.popularProducts,
    onMenuClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onCartTabClick: () -> Unit = {},
    onSeeAllRegions: () -> Unit = {},
    onSeeAllCaficultores: () -> Unit = {},
    onRegionClick: (CoffeeRegion) -> Unit = {},
    onCaficultorClick: (FeaturedCaficultor) -> Unit = {},
    onStoryClick: (CoffeeStory) -> Unit = {},
    onProductClick: (CoffeeProduct) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(ExploreTab.EXPLORAR) }
    val cartItemCount = 3

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        ExploreTopBar(
            onMenuClick = onMenuClick,
            onSettingsClick = onSettingsClick,
            onNotificationsClick = onNotificationsClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.lg)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.xs))

            ExploreGreeting(
                userName = userName,
                modifier = Modifier.padding(horizontal = BrandSpacing.lg)
            )

            ExploreSearchBar(
                query = query,
                onQueryChange = { query = it },
                onFilterClick = onFilterClick,
                onSearchClick = onSearchClick,
                modifier = Modifier.padding(horizontal = BrandSpacing.lg)
            )

            // Sección: Explora por zona ─────────────────────────────────────
            Column(
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                ExploreSectionHeader(
                    title = "Explora por zona",
                    actionLabel = "Ver mapa 🗺",
                    onActionClick = onSeeAllRegions,
                    modifier = Modifier.padding(horizontal = BrandSpacing.lg)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = BrandSpacing.lg),
                    horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
                ) {
                    items(items = regions, key = { it.name }) { region ->
                        RegionCard(
                            region = region,
                            onClick = { onRegionClick(region) }
                        )
                    }
                }
            }

            // Sección: Caficultores destacados ──────────────────────────────
            Column(
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                ExploreSectionHeader(
                    title = "Caficultores destacados",
                    actionLabel = "Ver todos",
                    onActionClick = onSeeAllCaficultores,
                    modifier = Modifier.padding(horizontal = BrandSpacing.lg)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = BrandSpacing.lg),
                    horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
                ) {
                    items(items = caficultores, key = { it.farmName }) { caficultor ->
                        CaficultorCard(
                            caficultor = caficultor,
                            onClick = { onCaficultorClick(caficultor) }
                        )
                    }
                }
            }

            // Sección: Historias que enamoran ───────────────────────────────
            Column(
                modifier = Modifier.padding(horizontal = BrandSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                ExploreSectionHeader(title = "Historias que enamoran ✨")
                stories.forEach { story ->
                    StoryCard(
                        story = story,
                        onClick = { onStoryClick(story) }
                    )
                }
            }

            // Sección: Lo más pedido esta semana ────────────────────────────
            Column(
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
            ) {
                ExploreSectionHeader(
                    title = "Lo más pedido esta semana",
                    modifier = Modifier.padding(horizontal = BrandSpacing.lg)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = BrandSpacing.lg),
                    horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
                ) {
                    items(items = popularProducts, key = { it.name }) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }

        ExploreBottomBar(
            selected = selectedTab,
            onTabSelected = { tab ->
                selectedTab = tab
                if (tab == ExploreTab.CARRITO) onCartTabClick()
            },
            cartItemCount = cartItemCount
        )
    }
}

@Preview(name = "ExploreScreen", widthDp = 360, heightDp = 1500)
@Composable
private fun ExploreScreenPreview() {
    CafeterosTheme {
        ExploreScreen()
    }
}

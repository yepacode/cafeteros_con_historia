package com.cafeteros.historia.ui.features.filters.model

/**
 * Estado completo de la pantalla "Filtros de Origen".
 *
 * Cada campo modela una de las secciones del diseño. Cuando exista backend,
 * esta data class se serializa hacia el endpoint `/search` (o equivalente)
 * y la pantalla deja de mantener su propio estado interno.
 *
 * @property selectedZones zonas marcadas en "ZONA CAFETERA". Vacío =
 *   sin filtro de zona (cualquier zona).
 * @property priceRange par (min, max) en pesos. Default cubre toda la
 *   gama; el usuario lo restringe con el slider.
 * @property selectedFlavors notas marcadas en "PERFIL DE SABOR".
 * @property process opción seleccionada en "PROCESO". Default `TODOS`
 *   significa "no filtrar por proceso".
 * @property selectedRoast nivel de tueste único elegido. `null` significa
 *   "sin filtro de tueste".
 * @property certifications certificaciones marcadas.
 * @property altitude rango de altitud único. `null` = sin filtro.
 * @property freeShipping si true, solo productos con envío gratis.
 * @property expressShipping si true, solo productos con envío express.
 * @property sortBy criterio de ordenación.
 */
data class OriginFilters(
    val selectedZones: Set<String> = emptySet(),
    val priceRange: ClosedFloatingPointRange<Float> = 30_000f..80_000f,
    val selectedFlavors: Set<FilterFlavor> = emptySet(),
    val process: FilterProcess = FilterProcess.TODOS,
    val selectedRoast: FilterRoast? = null,
    val certifications: Set<FilterCertification> = emptySet(),
    val altitude: FilterAltitude? = null,
    val freeShipping: Boolean = false,
    val expressShipping: Boolean = false,
    val sortBy: FilterSortOption = FilterSortOption.RELEVANCIA
)

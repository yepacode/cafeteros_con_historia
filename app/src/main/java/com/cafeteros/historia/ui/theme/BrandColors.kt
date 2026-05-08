package com.cafeteros.historia.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores de la marca "Origen".
 *
 * Centraliza los tokens de color para evitar valores mágicos repartidos en
 * los composables. Cualquier nuevo color de marca debe añadirse aquí y
 * referenciarse desde los composables o desde [CafeterosTheme].
 */
object BrandColors {

    // ── Colores de marca primarios ──────────────────────────────────────────

    /** Café oscuro tipo chocolate amargo. Fondo principal del splash. */
    val CoffeeBrown: Color = Color(0xFF3E2723)

    /** Crema cálido. Texto principal e ícono decorativo del splash. */
    val CreamWhite: Color = Color(0xFFE8DCC4)

    /**
     * Variante atenuada del crema (alpha 70%). Usado en el tagline para crear
     * jerarquía visual sin introducir un color nuevo.
     */
    val CreamWhiteMuted: Color = CreamWhite.copy(alpha = 0.7f)

    // ── Tokens para superficies tipo "card" (onboarding) ────────────────────

    /** Fondo de las tarjetas blancas del onboarding. */
    val CardBackground: Color = Color(0xFFFFFFFF)

    /** Color del texto de títulos sobre tarjetas claras. */
    val TextPrimary: Color = Color(0xFF1A1A1A)

    /** Color del texto descriptivo (menos contraste que [TextPrimary]). */
    val TextSecondary: Color = Color(0xFF6B6B6B)

    // ── Tokens para botones e indicadores ───────────────────────────────────

    /** Color de fondo del botón primario (Siguiente / Comenzar). */
    val PrimaryButton: Color = CoffeeBrown

    /** Color del texto del botón primario. */
    val PrimaryButtonText: Color = Color(0xFFFFFFFF)

    /** Color de los puntos activos del page indicator. */
    val IndicatorActive: Color = CoffeeBrown

    /** Color de los puntos inactivos del page indicator. */
    val IndicatorInactive: Color = Color(0xFFD9D2C5)

    /**
     * Fondo semi-transparente para el botón "Saltar" sobre la imagen.
     * Garantiza legibilidad sin ocultar la imagen de fondo.
     */
    val SkipButtonScrim: Color = Color(0x66000000)

    // ── Tokens del flujo de autenticación (login) ───────────────────────────

    /** Fondo crema de la pantalla de login, ligeramente cálido. */
    val AuthBackground: Color = Color(0xFFF7F1E5)

    /** Fondo gris-beige de los inputs (email, contraseña). */
    val InputBackground: Color = Color(0xFFE8E2D5)

    /** Color del placeholder y de los íconos leading dentro de los inputs. */
    val InputHint: Color = Color(0xFFA89F8E)

    /** Verde oscuro para enlaces secundarios ("Olvidaste contraseña", "Regístrate"). */
    val LinkGreen: Color = Color(0xFF1B5E20)

    /** Borde sutil de los botones sociales (Google, Huella). */
    val SocialButtonBorder: Color = Color(0xFFE5DFD2)

    /** Línea horizontal del divider "O CONTINÚA CON". */
    val DividerLine: Color = Color(0xFFD9D2C5)

    // ── Tokens de cards seleccionables (selector "Soy Comprador / Caficultor") ─

    /** Fondo de la card cuando está seleccionada (blanco puro). */
    val SelectableCardSelectedBackground: Color = CardBackground

    /** Borde de la card seleccionada (café oscuro de marca, ~2dp). */
    val SelectableCardSelectedBorder: Color = CoffeeBrown

    /** Fondo de la card no seleccionada (gris-beige sutil). */
    val SelectableCardUnselectedBackground: Color = InputBackground

    /** Borde de la card no seleccionada (transparente: solo fondo). */
    val SelectableCardUnselectedBorder: Color = Color.Transparent

    // ── Tokens de checkboxes de consentimiento ──────────────────────────────

    /** Borde del checkbox cuando está sin marcar. */
    val CheckboxBorder: Color = Color(0xFFB8AE99)

    /** Color del checkbox marcado y de su tick. */
    val CheckboxChecked: Color = CoffeeBrown

    // ── Tokens de la pantalla de exploración del comprador ─────────────────

    /** Verde profundo de la app de exploración (botón de filtros, links activos). */
    val ForestGreen: Color = Color(0xFF2C5F3F)

    /** Punto rojo de la campana de notificaciones. */
    val NotificationDot: Color = Color(0xFFE53935)

    /** Borde dorado decorativo del avatar del caficultor. */
    val AvatarGoldBorder: Color = Color(0xFFC9A227)

    /** Fondo del badge "ORGÁNICO" (naranja-melocotón cálido). */
    val BadgeOrganicBackground: Color = Color(0xFFF6CFA8)

    /** Texto del badge "ORGÁNICO" (café tostado). */
    val BadgeOrganicText: Color = Color(0xFF8B5A2B)

    /** Fondo del badge "SOSTENIBLE" (verde claro). */
    val BadgeSostenibleBackground: Color = Color(0xFFCDE4D0)

    /** Texto del badge "SOSTENIBLE" (verde profundo). */
    val BadgeSostenibleText: Color = ForestGreen

    /** Color de las estrellas del rating. */
    val RatingStar: Color = Color(0xFFE3A82B)

    /** Fondo blanco translúcido del botón de favorito (corazón) sobre la imagen. */
    val FavoriteButtonBackground: Color = Color(0xCCFFFFFF)

    /** Color del corazón cuando NO está marcado (gris sutil). */
    val FavoriteIconInactive: Color = Color(0xFF8C8C8C)

    /** Color del corazón cuando está marcado (rojo cálido). */
    val FavoriteIconActive: Color = Color(0xFFE53935)

    /** Fondo del badge numérico del carrito en la barra inferior. */
    val CartBadgeBackground: Color = Color(0xFFE3A82B)

    /** Texto del badge del carrito. */
    val CartBadgeText: Color = Color(0xFF1A1A1A)

    /** Fondo de la barra de navegación inferior. */
    val BottomBarBackground: Color = Color(0xFFFAF4E6)

    /** Color de los iconos/labels inactivos en la barra inferior. */
    val BottomBarInactive: Color = Color(0xFF9A9A9A)

    /** Color de los iconos/labels activos en la barra inferior. */
    val BottomBarActive: Color = CoffeeBrown

    // ── Tokens del Mapa Cafetero ────────────────────────────────────────────

    /** Fondo del lienzo del mapa (beige cálido más neutro que [AuthBackground]). */
    val MapCanvasBackground: Color = Color(0xFFF1ECE0)

    /** Color de la zona Sierra Nevada (turquesa/teal). */
    val ZoneSierraNevada: Color = Color(0xFF2C8A86)

    /** Color de la zona Santander (café oscuro tipo tierra). */
    val ZoneSantander: Color = Color(0xFF6E4A36)

    /** Color de la zona Eje Cafetero (café claro). */
    val ZoneEjeCafetero: Color = Color(0xFFB16A4A)

    /** Color de la zona Cundinamarca (verde fresco). */
    val ZoneCundinamarca: Color = Color(0xFF7CA94F)

    /** Color de la zona Huila (naranja terracota). */
    val ZoneHuila: Color = Color(0xFFD17A3A)

    /** Inicio del degradado de "PRODUCCIÓN" (claro). */
    val ProductionGradientStart: Color = Color(0xFFE9D9B6)

    /** Fin del degradado de "PRODUCCIÓN" (alto = oscuro). */
    val ProductionGradientEnd: Color = CoffeeBrown

    /** Fondo del badge numérico que se sobrepone al marcador de zona. */
    val MarkerCountBadgeBackground: Color = CoffeeBrown

    /** Texto del badge numérico del marcador. */
    val MarkerCountBadgeText: Color = Color(0xFFFFFFFF)

    /** Fondo de la card flotante "Toca una zona para explorar". */
    val MapInstructionCardBackground: Color = Color(0xFFFFFFFF)

    /** Fondo del bottom sheet del mapa (color crema más claro). */
    val MapBottomSheetBackground: Color = Color(0xFFFAF4E6)

    /** Fondo del filter chip cuando NO está seleccionado. */
    val FilterChipUnselectedBackground: Color = Color(0xFFEDE6D7)

    /** Texto del filter chip cuando NO está seleccionado. */
    val FilterChipUnselectedText: Color = TextSecondary

    /** Fondo del filter chip cuando está seleccionado (oscuro). */
    val FilterChipSelectedBackground: Color = CoffeeBrown

    /** Texto del filter chip cuando está seleccionado. */
    val FilterChipSelectedText: Color = Color(0xFFFFFFFF)

    /** Fondo de la pill resumen ("Santander 38") en el bottom sheet. */
    val ZonePillBackground: Color = Color(0xFFFFFFFF)

    /** Color del handle/grabber del bottom sheet. */
    val BottomSheetHandle: Color = Color(0xFFC8C0AE)

    // ── Tokens del detalle de zona ──────────────────────────────────────────

    /** Fondo de la pantalla de detalle de zona (crema cálido). */
    val ZoneDetailBackground: Color = AuthBackground

    /** Fondo translúcido de los botones circulares sobre la imagen hero. */
    val HeroOverlayButtonBackground: Color = Color(0xCCFFFFFF)

    /** Color del badge "ZONA CAFETERA" sobre la imagen hero. */
    val ZoneCafeteraBadgeBackground: Color = CoffeeBrown

    /** Texto/ícono del badge "ZONA CAFETERA". */
    val ZoneCafeteraBadgeAccent: Color = RatingStar

    /** Línea vertical fina que separa los stats numéricos. */
    val StatsDivider: Color = Color(0xFFD9D2C5)

    /** Fondo de la card "Perfil de sabor". */
    val FlavorCardBackground: Color = CardBackground

    /** Fondo de las pills de notas de sabor ("Chocolate amargo"). */
    val FlavorTagBackground: Color = InputBackground

    /** Texto de las pills de notas de sabor. */
    val FlavorTagText: Color = TextPrimary

    /** Pista del slider de cada característica de sabor (gris claro). */
    val FlavorSliderTrack: Color = Color(0xFFE5DFD2)

    /** Relleno del slider — gold/ámbar coherente con [RatingStar]. */
    val FlavorSliderFill: Color = RatingStar

    /** Fondo de chip de municipio cuando NO está seleccionado. */
    val MunicipalityChipUnselectedBackground: Color = InputBackground

    /** Fondo de chip de municipio cuando está seleccionado (oscuro). */
    val MunicipalityChipSelectedBackground: Color = CoffeeBrown

    /** Texto de chip de municipio seleccionado. */
    val MunicipalityChipSelectedText: Color = Color(0xFFFFFFFF)

    /** Fondo de avatar placeholder de caficultor cuando no hay foto. */
    val CaficultorAvatarPlaceholderBackground: Color = Color(0xFF8B5A2B)

    /** Fondo de la card "¿Quieres saber más sobre Santander?". */
    val DiscoverChroniclesBackground: Color = CoffeeBrown

    /** Texto principal sobre la card "Descubrir crónicas". */
    val DiscoverChroniclesText: Color = CreamWhite

    /** Fondo del botón "DESCUBRIR CRÓNICAS" (gold). */
    val DiscoverChroniclesButtonBackground: Color = RatingStar

    /** Texto del botón "DESCUBRIR CRÓNICAS". */
    val DiscoverChroniclesButtonText: Color = CoffeeBrown

    // ── Tokens del detalle de caficultor ────────────────────────────────────

    /** Fondo de la pantalla de detalle de caficultor (igual al de zona/auth). */
    val CaficultorDetailBackground: Color = AuthBackground

    /** Fondo de las pills de certificación ("ORGÁNICO CERTIFICADO"...). */
    val CertificationBadgeBackground: Color = Color(0xFFD9EAD3)

    /** Texto de las pills de certificación (verde profundo). */
    val CertificationBadgeText: Color = Color(0xFF2C5F3F)

    /** Color de la "medalla" pequeña sobre el avatar del caficultor. */
    val ProfileMedalBackground: Color = RatingStar

    /** Velo oscuro sobre el thumbnail de video para asegurar contraste del play. */
    val VideoOverlayScrim: Color = Color(0x33000000)

    /** Fondo del botón circular de play sobre el video. */
    val VideoPlayButtonBackground: Color = Color(0xCCFFFFFF)

    /** Color del icono triangular de play. */
    val VideoPlayIconTint: Color = CoffeeBrown

    /** Color del ícono de cada paso de proceso ("COSECHA MANUAL"...). */
    val ProcessStepIconTint: Color = ForestGreen

    /** Texto de la etiqueta bajo cada paso de proceso. */
    val ProcessStepLabel: Color = TextSecondary

    /** Botón circular "+" para agregar producto al carrito. */
    val AddToCartButtonBackground: Color = CoffeeBrown

    /** Ícono "+" sobre el botón de agregar al carrito. */
    val AddToCartButtonIcon: Color = Color(0xFFFFFFFF)

    /** Fondo de la card "¿Tienes preguntas para...?". */
    val ContactCardBackground: Color = Color(0xFFEEE6D5)

    /** Fondo del botón "Enviar mensaje" dentro de la contact card. */
    val ContactButtonBackground: Color = Color(0xFFB8D4BE)

    /** Texto del botón "Enviar mensaje". */
    val ContactButtonText: Color = Color(0xFF1F4D2C)

    /** Fondo del botón ancho oscuro "Ver todos los productos". */
    val WideDarkButtonBackground: Color = CoffeeBrown

    /** Texto del botón ancho oscuro. */
    val WideDarkButtonText: Color = Color(0xFFFFFFFF)

    // ── Tokens del detalle de producto ──────────────────────────────────────

    /** Fondo de la pantalla de detalle de producto (mismo crema cálido). */
    val ProductDetailBackground: Color = AuthBackground

    /** Fondo del hero del producto: bokeh cálido detrás del paquete. */
    val ProductHeroBackground: Color = Color(0xFFD8C7A4)

    /** Punto activo del carrusel del hero. */
    val CarouselDotActive: Color = CoffeeBrown

    /** Punto inactivo del carrusel del hero. */
    val CarouselDotInactive: Color = Color(0xFFD9D2C5)

    /** Fondo del badge "HUILA" (naranja terracota como su zona). */
    val RegionBadgeBackground: Color = Color(0xFFE07B2C)

    /** Texto del badge "HUILA". */
    val RegionBadgeText: Color = Color(0xFFFFFFFF)

    /** Fondo del pill "En stock" (verde claro suave). */
    val StockBadgeBackground: Color = Color(0xFFD9EAD3)

    /** Texto del pill "En stock" (verde profundo). */
    val StockBadgeText: Color = ForestGreen

    /** Color del check del pill "En stock". */
    val StockBadgeCheck: Color = ForestGreen

    /** Color del precio tachado anterior. */
    val PriceStrikethrough: Color = TextSecondary

    /** Fondo del badge de descuento "-15%". */
    val DiscountBadgeBackground: Color = Color(0xFFFCE4E6)

    /** Texto del badge de descuento. */
    val DiscountBadgeText: Color = Color(0xFFC62828)

    /** Fondo de la pill del productor sobre la sección del título. */
    val ProducerPillBackground: Color = Color(0xFFEEE6D5)

    /** Color del enlace "Ver perfil →" del productor. */
    val ViewProfileLink: Color = Color(0xFFB48211)

    /** Fondo del chip de presentación seleccionado (250g, 500g, 1kg). */
    val PresentationChipSelectedBackground: Color = CoffeeBrown

    /** Texto del chip de presentación seleccionado. */
    val PresentationChipSelectedText: Color = Color(0xFFFFFFFF)

    /** Fondo del chip de presentación NO seleccionado. */
    val PresentationChipUnselectedBackground: Color = CardBackground

    /** Borde del chip de presentación NO seleccionado. */
    val PresentationChipUnselectedBorder: Color = Color(0xFFD9D2C5)

    /** Texto del chip de presentación NO seleccionado. */
    val PresentationChipUnselectedText: Color = TextPrimary

    /** Fondo del dropdown de tueste / tipo. */
    val OptionFieldBackground: Color = CardBackground

    /** Borde del dropdown de tueste / tipo. */
    val OptionFieldBorder: Color = Color(0xFFD9D2C5)

    /** Fondo de la pill del stepper de cantidad. */
    val QuantityStepperBackground: Color = Color(0xFFE8E2D5)

    /** Fondo del botón circular "+/-" del stepper. */
    val QuantityStepperButtonBackground: Color = CardBackground

    /** Color del indicador subrayado de la tab activa. */
    val TabIndicatorActive: Color = CoffeeBrown

    /** Fondo de la card de envío. */
    val ShippingCardBackground: Color = Color(0xFFEEE6D5)

    /** Fondo del círculo del ícono de camión. */
    val ShippingIconBackground: Color = CardBackground

    /** Color del ícono de camión. */
    val ShippingIconTint: Color = CoffeeBrown

    // ── Tokens de la pantalla de búsqueda ───────────────────────────────────

    /** Fondo de la pantalla de búsqueda (mismo crema cálido). */
    val SearchBackground: Color = AuthBackground

    /** Texto rojo del enlace "BORRAR TODO". */
    val ClearAllAction: Color = Color(0xFFC62828)

    /** Color del ícono de reloj junto a cada búsqueda reciente. */
    val RecentSearchIcon: Color = TextSecondary

    /** Color de la X que elimina una búsqueda reciente. */
    val RecentSearchRemove: Color = TextSecondary

    /** Fondo de los chips de tendencias de búsqueda. */
    val TrendingChipBackground: Color = Color(0xFFE0EAD5)

    /** Texto de los chips de tendencias. */
    val TrendingChipText: Color = ForestGreen

    /** Fondo de la card del grid "Explora por". */
    val SearchCategoryCardBackground: Color = CardBackground

    /** Color del ícono "Por zona" del grid de categorías (mostaza). */
    val CategoryIconZone: Color = Color(0xFFE3A82B)

    /** Color del ícono "Por perfil de sabor" (verde profundo). */
    val CategoryIconFlavor: Color = ForestGreen

    /** Color del ícono "Por proceso" (terracota cálido). */
    val CategoryIconProcess: Color = Color(0xFFB16A4A)

    /** Color del ícono "Por certificación" (café oscuro). */
    val CategoryIconCertification: Color = CoffeeBrown

    // ── Tokens de la pantalla "Filtros de Origen" ───────────────────────────

    /** Fondo de la pantalla de filtros (igual al crema cálido global). */
    val FiltersBackground: Color = AuthBackground

    /** Color del valor en verde junto al título "RANGO DE PRECIO". */
    val PriceRangeValue: Color = ForestGreen

    /** Track del slider de rango de precio (línea de fondo gris). */
    val PriceSliderTrack: Color = Color(0xFFD9D2C5)

    /** Track activo del slider entre los dos thumbs (oscuro). */
    val PriceSliderActiveTrack: Color = CoffeeBrown

    /** Borde del thumb circular del slider. */
    val PriceSliderThumb: Color = CoffeeBrown

    /** Fondo de los inputs MIN / MAX bajo el slider. */
    val PriceInputBackground: Color = InputBackground

    /** Borde de los chips de "Perfil de sabor" cuando NO están seleccionados. */
    val FlavorChipBorder: Color = Color(0xFFB8AE99)

    /** Borde del chip de sabor cuando está seleccionado (verde profundo). */
    val FlavorChipSelectedBorder: Color = ForestGreen

    /** Texto del chip de sabor seleccionado. */
    val FlavorChipSelectedText: Color = ForestGreen

    /** Fondo de cada taza inactiva del selector de tueste. */
    val RoastCupInactiveTint: Color = Color(0xFFC9B79E)

    /** Fondo de la taza activa del selector de tueste. */
    val RoastCupActiveBackground: Color = CardBackground

    /** Borde de la taza activa del selector de tueste. */
    val RoastCupActiveBorder: Color = CoffeeBrown

    /** Color del ícono de taza activa. */
    val RoastCupActiveTint: Color = CoffeeBrown

    /** Fondo del contenedor de tazas de tueste. */
    val RoastSelectorBackground: Color = InputBackground

    /** Color del checkbox de certificación cuando está marcado. */
    val CertificationChecked: Color = ForestGreen

    /** Borde del checkbox de certificación cuando NO está marcado. */
    val CertificationUnchecked: Color = TextSecondary

    /** Fondo de la card de altitud cuando NO está seleccionada. */
    val AltitudeCardUnselectedBackground: Color = InputBackground

    /** Fondo de la card de altitud cuando está seleccionada (verde claro). */
    val AltitudeCardSelectedBackground: Color = Color(0xFFE0EAD5)

    /** Borde de la card de altitud cuando está seleccionada. */
    val AltitudeCardSelectedBorder: Color = ForestGreen

    /** Texto de la card de altitud seleccionada. */
    val AltitudeCardSelectedText: Color = ForestGreen

    /** Track del switch en estado off. */
    val SwitchTrackOff: Color = Color(0xFFD9D2C5)

    /** Track del switch en estado on. */
    val SwitchTrackOn: Color = ForestGreen

    /** Thumb circular del switch (siempre blanco). */
    val SwitchThumb: Color = CardBackground

    /** Color de los radio buttons cuando están seleccionados. */
    val RadioSelected: Color = CoffeeBrown

    /** Color de los radio buttons cuando NO están seleccionados. */
    val RadioUnselected: Color = TextSecondary

    /** Fondo del botón "LIMPIAR TODO" del bottom bar. */
    val ClearFiltersButtonBackground: Color = CardBackground

    /** Borde del botón "LIMPIAR TODO". */
    val ClearFiltersButtonBorder: Color = Color(0xFFD9D2C5)

    /** Texto del botón "LIMPIAR TODO". */
    val ClearFiltersButtonText: Color = TextPrimary

    // ── Tokens de la pantalla "Resultados de búsqueda" ──────────────────────

    /** Fondo de la pantalla de resultados (igual al crema cálido global). */
    val SearchResultsBackground: Color = AuthBackground

    /** Punto naranja sobre el botón de filtros cuando hay filtros activos. */
    val FilterIndicatorDot: Color = Color(0xFFE07B2C)

    /** Fondo de un chip de filtro activo cuando no tiene dot. */
    val ActiveFilterBackground: Color = Color(0xFFEDE6D7)

    /** Fondo de un chip de filtro activo de zona (con punto). */
    val ActiveFilterZoneBackground: Color = CardBackground

    /** Color del subrayado bajo la tab activa de los resultados. */
    val SearchResultsTabIndicator: Color = CoffeeBrown

    /** Color del icono de vista (grid/list) cuando NO está activo. */
    val ViewModeInactive: Color = Color(0xFFC8C0AE)

    /** Fondo del icono de vista activo. */
    val ViewModeActiveBackground: Color = CoffeeBrown

    /** Color del icono de vista activo. */
    val ViewModeActiveTint: Color = Color(0xFFFFFFFF)

    /** Color del precio en las cards del grid de resultados (dorado). */
    val SearchResultPrice: Color = Color(0xFFC9A227)

    /** Fondo del badge naranja de zona sobre la imagen ("HUILA"). */
    val SearchResultZoneBadgeBackground: Color = Color(0xFFE07B2C)

    /** Texto del badge naranja de zona. */
    val SearchResultZoneBadgeText: Color = Color(0xFFFFFFFF)

    /** Fondo de la bottom bar minimalista de la pantalla de resultados. */
    val ResultsBottomBarBackground: Color = AuthBackground

    /** Color de los iconos inactivos de la bottom bar minimalista. */
    val ResultsBottomBarInactive: Color = Color(0xFFB8B0A0)

    /** Color del icono activo de la bottom bar minimalista. */
    val ResultsBottomBarActive: Color = CoffeeBrown

    /** Punto naranja bajo el icono activo de la bottom bar minimalista. */
    val ResultsBottomBarActiveDot: Color = FilterIndicatorDot

    // ── Tokens de la pantalla "el caficultor" (catálogo / empty state) ──────

    /** Fondo de la pantalla del catálogo del caficultor. */
    val CaficultorShopBackground: Color = AuthBackground

    /** Fondo del botón "Filtros" oscuro de la fila de filtros activos. */
    val ShopFilterButtonBackground: Color = CoffeeBrown

    /** Texto del botón "Filtros" oscuro. */
    val ShopFilterButtonText: Color = Color(0xFFFFFFFF)

    /** Borde de un chip de filtro aplicado ("Origen: Huila"). */
    val ShopFilterChipBorder: Color = Color(0xFFD9D2C5)

    /** Fondo del círculo desaturado tras la imagen del empty state. */
    val EmptyResultsImageScrim: Color = Color(0xFFE8E2D5)

    /** Fondo del badge circular con la lupa-X sobre el empty state. */
    val EmptyResultsBadgeBackground: Color = CardBackground

    /** Color del icono de la lupa-X del empty state. */
    val EmptyResultsBadgeIcon: Color = TextSecondary

    /** Borde del botón outline "Limpiar filtros" del empty state. */
    val ClearFiltersOutlineBorder: Color = ForestGreen

    /** Texto del botón outline "Limpiar filtros". */
    val ClearFiltersOutlineText: Color = ForestGreen

    /** Fondo de las cards de "Sugerencias populares". */
    val SuggestionCardBackground: Color = CardBackground

    // ── Tokens de la pantalla "Tu Selección" (carrito) ──────────────────────

    /** Fondo de la pantalla del carrito. */
    val CartBackground: Color = AuthBackground

    /** Color del texto del progreso de envío gratis ("80%"). */
    val FreeShippingProgressText: Color = Color(0xFFC9A227)

    /** Track de fondo de la barra de progreso de envío gratis. */
    val FreeShippingProgressTrack: Color = Color(0xFFE5DFD2)

    /** Relleno de la barra de progreso de envío gratis. */
    val FreeShippingProgressFill: Color = Color(0xFFC9A227)

    /** Fondo de cada card de item del carrito. */
    val CartItemCardBackground: Color = Color(0xFFEDE6D7)

    /** Fondo del placeholder de la imagen del item. */
    val CartItemImageBackground: Color = Color(0xFFD9D2C5)

    /** Color del icono de eliminar (trash) de cada item. */
    val CartItemDeleteIcon: Color = TextSecondary

    /** Fondo de la pill del stepper -N+ de cada item del carrito. */
    val CartStepperBackground: Color = CardBackground

    /** Color del precio principal en el carrito (dorado). */
    val CartItemPrice: Color = Color(0xFFC9A227)

    /** Color del precio anterior tachado en el carrito. */
    val CartItemOriginalPrice: Color = TextSecondary

    /** Fondo de la card de envío del carrito. */
    val CartShippingCardBackground: Color = Color(0xFFEDE6D7)

    /** Fondo del círculo verde claro del icono de camión. */
    val CartShippingIconBackground: Color = Color(0xFFB5D9C2)

    /** Color del icono de camión. */
    val CartShippingIconTint: Color = ForestGreen

    /** Color del enlace "Cambiar" de la card de envío. */
    val CartShippingChangeLink: Color = ForestGreen

    /** Fondo del input de código de descuento. */
    val CartDiscountInputBackground: Color = Color(0xFFEDE6D7)

    /** Fondo del botón "Aplicar" del código de descuento. */
    val CartApplyButtonBackground: Color = CoffeeBrown

    /** Texto del botón "Aplicar". */
    val CartApplyButtonText: Color = Color(0xFFFFFFFF)

    /** Fondo de la card del resumen de costos. */
    val CartSummaryBackground: Color = Color(0xFFEDE6D7)

    /** Color del valor de descuento en el resumen (verde). */
    val CartDiscountValue: Color = ForestGreen

    /** Color del Total dorado del resumen. */
    val CartTotalValue: Color = Color(0xFFC9A227)

    /** Línea separadora dentro del resumen. */
    val CartSummaryDivider: Color = Color(0xFFD9D2C5)

    /** Fondo del CTA "Continuar al pago". */
    val CartCheckoutButtonBackground: Color = CoffeeBrown

    /** Texto del CTA "Continuar al pago". */
    val CartCheckoutButtonText: Color = Color(0xFFFFFFFF)

    /** Color del enlace "Seguir comprando" debajo del CTA. */
    val CartContinueShoppingLink: Color = ForestGreen

    /** Fondo de la bottom bar del carrito. */
    val CartBottomBarBackground: Color = AuthBackground

    /** Color de los iconos/labels inactivos de la bottom bar del carrito. */
    val CartBottomBarInactive: Color = Color(0xFF9A9A9A)

    /** Color del icono/label activo de la bottom bar del carrito (dorado). */
    val CartBottomBarActive: Color = Color(0xFFC9A227)

    // ── Tokens del estado vacío del carrito ─────────────────────────────────

    /** Color tenue de la ilustración line-art de la taza del empty state. */
    val EmptyCartIllustration: Color = Color(0xFFC8C0AE)

    /** Fondo del CTA "Explorar café" del empty state. */
    val EmptyCartCtaBackground: Color = CoffeeBrown

    /** Texto del CTA "Explorar café". */
    val EmptyCartCtaText: Color = Color(0xFFFFFFFF)

    /** Fondo de cada card de la sección "Recomendaciones del Origen". */
    val RecommendationCardBackground: Color = Color(0xFFEDE6D7)

    // ── Tokens del checkout (paso 1: Dirección de Envío) ────────────────────

    /** Fondo de la pantalla del checkout. */
    val CheckoutBackground: Color = AuthBackground

    /** Color del punto activo del stepper de progreso. */
    val StepperDotActive: Color = CoffeeBrown

    /** Color de los puntos inactivos del stepper. */
    val StepperDotInactive: Color = Color(0xFFD9D2C5)

    /** Color del label "PASO X DE 3". */
    val StepperLabel: Color = TextSecondary

    /** Color del enlace "Gestionar" del header de direcciones. */
    val AddressManageLink: Color = ForestGreen

    /** Color del enlace "Editar" dentro de una card de dirección. */
    val AddressEditLink: Color = ForestGreen

    /** Borde de la card de dirección cuando está seleccionada. */
    val AddressCardSelectedBorder: Color = TextPrimary

    /** Fondo de una card de dirección cuando está seleccionada. */
    val AddressCardSelectedBackground: Color = CardBackground

    /** Fondo de una card de dirección NO seleccionada. */
    val AddressCardUnselectedBackground: Color = Color(0xFFEDE6D7)

    /** Fondo del badge del tipo de dirección ("CASA", "OFICINA"). */
    val AddressTypeBadgeBackground: Color = Color(0xFFE8E2D5)

    /** Color del icono dentro del badge del tipo de dirección. */
    val AddressTypeBadgeIcon: Color = Color(0xFFE07B2C)

    /** Texto del badge del tipo de dirección. */
    val AddressTypeBadgeText: Color = TextPrimary

    /** Borde dasheado de la card "Agregar nueva dirección". */
    val AddNewAddressBorder: Color = Color(0xFFD9C8B8)

    /** Fondo del círculo amarillo con el "+" de "Agregar nueva dirección". */
    val AddNewAddressIconBackground: Color = Color(0xFFF1CD7B)

    /** Color del "+" del círculo amarillo. */
    val AddNewAddressIconTint: Color = TextPrimary

    /** Texto verde "Agregar nueva dirección". */
    val AddNewAddressLabel: Color = ForestGreen

    /** Fondo del textarea de instrucciones para el repartidor. */
    val InstructionsFieldBackground: Color = Color(0xFFEDE6D7)

    /** Fondo del marcador cuadrado seleccionado del bloque de receptor. */
    val RecipientCheckedBackground: Color = CoffeeBrown

    /** Color del check del marcador seleccionado. */
    val RecipientCheckedIcon: Color = Color(0xFFFFFFFF)

    /** Borde del círculo del marcador NO seleccionado del bloque de receptor. */
    val RecipientUncheckedBorder: Color = TextSecondary

    /** Fondo del bottom bar fijo del checkout. */
    val CheckoutBottomBarBackground: Color = AuthBackground

    /** Fondo del CTA "Continuar" del checkout. */
    val CheckoutContinueBackground: Color = CoffeeBrown

    /** Texto del CTA "Continuar". */
    val CheckoutContinueText: Color = Color(0xFFFFFFFF)

    // ── Tokens del checkout (paso 2: Método de Pago) ────────────────────────

    /** Fondo de la pantalla del paso 2 (mismo crema cálido global). */
    val PaymentBackground: Color = AuthBackground

    /** Color del punto activo del stepper compacto del paso 2 (ámbar). */
    val PaymentStepperActive: Color = RatingStar

    /** Color de los puntos inactivos del stepper compacto del paso 2. */
    val PaymentStepperInactive: Color = Color(0xFFD9D2C5)

    /** Fondo de la card de resumen "DIRECCIÓN DE ENVÍO" (beige verde tenue). */
    val PaymentAddressSummaryBackground: Color = Color(0xFFE2E8DC)

    /** Fondo del círculo del pin verde junto a la dirección. */
    val PaymentAddressSummaryIconBackground: Color = Color(0xFFB5D9C2)

    /** Color del pin de ubicación dentro del círculo verde. */
    val PaymentAddressSummaryIconTint: Color = ForestGreen

    /** Color del enlace verde "Cambiar". */
    val PaymentAddressSummaryChangeLink: Color = ForestGreen

    /** Texto pequeño uppercase "DIRECCIÓN DE ENVÍO". */
    val PaymentAddressSummaryLabel: Color = TextSecondary

    /** Fondo de una card de método de pago seleccionada (blanco). */
    val PaymentMethodCardSelectedBackground: Color = CardBackground

    /** Borde dorado de la card de método de pago seleccionada. */
    val PaymentMethodCardSelectedBorder: Color = Color(0xFFC9A227)

    /** Fondo de una card de método de pago NO seleccionada. */
    val PaymentMethodCardUnselectedBackground: Color = Color(0xFFEDE6D7)

    /** Fondo del radio relleno cuando la card está seleccionada (dorado). */
    val PaymentMethodRadioSelectedFill: Color = Color(0xFFC9A227)

    /** Color del check blanco dentro del radio dorado. */
    val PaymentMethodRadioSelectedCheck: Color = Color(0xFFFFFFFF)

    /** Borde del radio cuando la card NO está seleccionada. */
    val PaymentMethodRadioUnselectedBorder: Color = TextSecondary

    /** Fondo de cada chip de marca de tarjeta (Visa, MC, Amex). */
    val CardBrandChipBackground: Color = Color(0xFFE8E2D5)

    /** Texto de cada chip de marca de tarjeta. */
    val CardBrandChipText: Color = TextSecondary

    /** Fondo de la fila de tarjeta guardada ("**** 4567 Visa"). */
    val SavedCardRowBackground: Color = Color(0xFFF1ECE0)

    /** Fondo del círculo del check dorado de la tarjeta guardada activa. */
    val SavedCardCheckBackground: Color = Color(0xFFC9A227)

    /** Color del check blanco dentro del círculo dorado. */
    val SavedCardCheckIcon: Color = Color(0xFFFFFFFF)

    /** Color del enlace verde "+ Agregar nueva tarjeta". */
    val AddNewCardLink: Color = ForestGreen

    /** Fondo del banner inferior "Pago 100% seguro" (verde menta muy claro). */
    val SecurityNoticeBackground: Color = Color(0xFFE0EAD5)

    /** Color del ícono de escudo del banner de seguridad. */
    val SecurityNoticeIcon: Color = ForestGreen

    /** Color del texto del banner de seguridad. */
    val SecurityNoticeText: Color = ForestGreen

    /** Etiqueta uppercase "TOTAL A PAGAR" del bottom bar del paso 2. */
    val PaymentTotalLabel: Color = TextSecondary

    /** Valor grande "$146.000" del total del bottom bar del paso 2. */
    val PaymentTotalValue: Color = TextPrimary

    /** Fondo del CTA "Revisar pedido". */
    val PaymentReviewCtaBackground: Color = CoffeeBrown

    /** Texto del CTA "Revisar pedido". */
    val PaymentReviewCtaText: Color = Color(0xFFFFFFFF)

    /** Color del ícono de la bolsa de compras a la derecha del top bar. */
    val PaymentTopBarBagIcon: Color = TextPrimary

    // ── Tokens de "Mis Direcciones" (libreta de direcciones) ────────────────

    /** Fondo de la pantalla del address book. */
    val AddressBookBackground: Color = AuthBackground

    /** Color del caption italic verde-gris ("Logística del Origen"). */
    val AddressBookEditorialCaption: Color = ForestGreen

    /** Fondo de cada card del address book. */
    val ManagedAddressCardBackground: Color = CardBackground

    /** Fondo del badge verde claro de la categoría. */
    val ManagedAddressBadgeBackground: Color = Color(0xFFD9E5DA)

    /** Color del icono dentro del badge de categoría (verde profundo). */
    val ManagedAddressBadgeIcon: Color = ForestGreen

    /** Texto dentro del badge de categoría. */
    val ManagedAddressBadgeText: Color = ForestGreen

    /** Color del menú contextual (3 puntos verticales). */
    val ManagedAddressMenuIcon: Color = TextSecondary

    /** Color del icono de teléfono al inicio de la línea de número. */
    val ManagedAddressPhoneIcon: Color = TextSecondary

    /** Fondo de la pill amarilla "PREDETERMINADA". */
    val DefaultAddressPillBackground: Color = Color(0xFFF1CD7B)

    /** Texto de la pill "PREDETERMINADA". */
    val DefaultAddressPillText: Color = Color(0xFF6B4F1A)

    /** Color claro de la mitad superior de la imagen decorativa del footer. */
    val AddressBookFooterImageTop: Color = Color(0xFFE8E2D5)

    /** Color medio de la mitad inferior de la imagen decorativa del footer. */
    val AddressBookFooterImageBottom: Color = Color(0xFFCDC6B6)

    /** Color del caption italic gris bajo la imagen del footer. */
    val AddressBookFooterCaption: Color = TextSecondary

    /** Fondo del CTA "Agregar dirección" del bottom bar. */
    val AddAddressCtaBackground: Color = CoffeeBrown

    /** Texto del CTA "Agregar dirección". */
    val AddAddressCtaText: Color = Color(0xFFFFFFFF)

    // ── Tokens del empty state de "Mis Direcciones" ─────────────────────────

    /** Color del cuerpo del pin con la taza del empty state. */
    val EmptyAddressesPinFill: Color = Color(0xFFE8E2D5)

    /** Color del trazo del pin del empty state. */
    val EmptyAddressesPinStroke: Color = Color(0xFFC8C0AE)

    /** Color del icono de la taza dentro del pin. */
    val EmptyAddressesPinIcon: Color = CoffeeBrown

    /** Color tenue de la ilustración line-art de la planta de fondo. */
    val EmptyAddressesPlantStroke: Color = Color(0xFFEDE6D7)

    // ── Bottom bar del perfil (INICIO / TIENDA / PEDIDOS / PERFIL) ──────────

    /** Fondo del bottom bar del perfil. */
    val ProfileBottomBarBackground: Color = AuthBackground

    /** Color de los iconos/labels inactivos del bottom bar del perfil. */
    val ProfileBottomBarInactive: Color = Color(0xFF9A9A9A)

    /** Color del icono/label activo del bottom bar del perfil (dorado). */
    val ProfileBottomBarActive: Color = Color(0xFFC9A227)

    /** Punto bajo el icono activo del bottom bar del perfil. */
    val ProfileBottomBarActiveDot: Color = Color(0xFFC9A227)

    // ── Tokens del checkout (paso 3: Revisar Pedido) ────────────────────────

    /** Fondo de la pantalla de revisión de pedido (mismo crema cálido global). */
    val OrderReviewBackground: Color = AuthBackground

    /** Fondo de los círculos de pasos completados (1 y 2) en el stepper conectado. */
    val OrderReviewStepCompletedFill: Color = ForestGreen

    /** Fondo del círculo dorado del paso activo (3). */
    val OrderReviewStepActiveFill: Color = Color(0xFFC9A227)

    /** Color del anillo interior claro del paso activo. */
    val OrderReviewStepActiveInner: Color = AuthBackground

    /** Color de la línea conectora entre puntos del stepper (rosa tenue). */
    val OrderReviewStepConnector: Color = Color(0xFFE5C8C0)

    /** Color del enlace verde "Editar" alineado a la derecha de cada section header. */
    val OrderReviewEditLink: Color = ForestGreen

    /** Fondo de la card de dirección de envío. */
    val OrderReviewShippingCardBackground: Color = Color(0xFFEEE6D7)

    /** Color del pin de ubicación dentro de la card de dirección. */
    val OrderReviewShippingPinTint: Color = TextPrimary

    /** Fondo del thumbnail circular del producto (café marrón). */
    val OrderReviewProductThumbBackground: Color = CoffeeBrown

    /** Color del texto/ícono crema dentro del thumbnail del producto. */
    val OrderReviewProductThumbAccent: Color = CreamWhite

    /** Fondo del cuadrado placeholder "..." de la fila "Ver todos los productos". */
    val OrderReviewMoreThumbBackground: Color = Color(0xFFE0DAC9)

    /** Color de los puntos "..." dentro del placeholder de "Ver todos". */
    val OrderReviewMoreThumbIcon: Color = TextSecondary

    /** Color del enlace verde "Ver todos los productos". */
    val OrderReviewViewAllLink: Color = ForestGreen

    /** Fondo de la card resumen del método de pago. */
    val OrderReviewPaymentCardBackground: Color = Color(0xFFEEE6D7)

    /** Fondo del badge blanco de VISA en la card de pago. */
    val OrderReviewVisaBadgeBackground: Color = CardBackground

    /** Color del texto "VISA" del badge (azul corporativo). */
    val OrderReviewVisaBadgeText: Color = Color(0xFF1A1F71)

    /** Borde sutil del badge de VISA. */
    val OrderReviewVisaBadgeBorder: Color = Color(0xFFD9D2C5)

    /** Color del ícono de camión de "Entrega estimada" (dorado cálido). */
    val OrderReviewDeliveryTruckTint: Color = Color(0xFFC9A227)

    /** Fondo de la card del resumen de costos (subtotal/envío/total). */
    val OrderReviewTotalsCardBackground: Color = Color(0xFFEEE6D7)

    /** Línea separadora dentro del totals card. */
    val OrderReviewTotalsDivider: Color = Color(0xFFD9D2C5)

    /** Color del valor de descuento (verde negativo). */
    val OrderReviewDiscountValue: Color = ForestGreen

    /** Color dorado grande del total a pagar. */
    val OrderReviewTotalAmount: Color = Color(0xFFC9A227)

    /** Color del enlace subrayado dentro del bloque de Términos. */
    val OrderReviewTermsLink: Color = TextPrimary

    /** Color del cuerpo del párrafo de términos. */
    val OrderReviewTermsBody: Color = TextPrimary

    /** Borde del checkbox de términos cuando está sin marcar. */
    val OrderReviewCheckboxBorder: Color = Color(0xFFB8AE99)

    /** Fondo del checkbox marcado. */
    val OrderReviewCheckboxChecked: Color = CoffeeBrown

    /** Color del check blanco dentro del checkbox marcado. */
    val OrderReviewCheckboxCheckIcon: Color = Color(0xFFFFFFFF)

    /** Fondo del bottom bar de confirmación (mismo crema global). */
    val OrderReviewConfirmBarBackground: Color = AuthBackground

    /** Fondo del CTA "Confirmar y pagar". */
    val OrderReviewConfirmCtaBackground: Color = CoffeeBrown

    /** Texto del CTA "Confirmar y pagar". */
    val OrderReviewConfirmCtaText: Color = Color(0xFFFFFFFF)

    /** Color del texto pequeño "Al pagar aceptas..." debajo del CTA. */
    val OrderReviewFooterTermsText: Color = TextSecondary

    // ── Tokens del checkout (paso 4 — éxito: Pago Confirmado) ───────────────

    /** Fondo de la pantalla de pago exitoso (mismo crema cálido global). */
    val PaymentSuccessBackground: Color = AuthBackground

    /** Fondo del anillo blanco grande detrás del badge de éxito. */
    val PaymentSuccessHeroRingBackground: Color = CardBackground

    /** Color del borde del anillo blanco del hero (dorado tenue). */
    val PaymentSuccessHeroRingBorder: Color = Color(0xFFE8DCB6)

    /** Fondo del badge dorado central (círculo con el check). */
    val PaymentSuccessHeroBadgeFill: Color = Color(0xFFC9A227)

    /** Color del check blanco dentro del badge dorado. */
    val PaymentSuccessHeroBadgeIcon: Color = Color(0xFFFFFFFF)

    /** Color del confeti dorado decorativo alrededor del hero. */
    val PaymentSuccessConfettiGold: Color = Color(0xFFC9A227)

    /** Color del confeti verde decorativo. */
    val PaymentSuccessConfettiGreen: Color = ForestGreen

    /** Color del confeti turquesa decorativo. */
    val PaymentSuccessConfettiTeal: Color = Color(0xFF4DA8A4)

    /** Color del ícono de la X de cerrar del top bar del éxito. */
    val PaymentSuccessCloseIcon: Color = TextPrimary

    /** Color del texto resaltado del número de pedido dentro del subtítulo. */
    val PaymentSuccessOrderHighlight: Color = TextPrimary

    /** Fondo de la card "Tu pedido" (blanco). */
    val OrderReceiptCardBackground: Color = CardBackground

    /** Color del ícono "Tu pedido" (caja). */
    val OrderReceiptIconTint: Color = TextPrimary

    /** Color del título "Tu pedido" de la card. */
    val OrderReceiptCardTitle: Color = TextPrimary

    /** Color de la etiqueta gris de cada fila ("Número", "Total pagado"...). */
    val OrderReceiptRowLabel: Color = TextSecondary

    /** Color del valor de cada fila. */
    val OrderReceiptRowValue: Color = TextPrimary

    /** Color del título italic "Con esta compra apoyaste a:". */
    val SupportedSectionTitle: Color = TextPrimary

    /** Color del emoji decorativo ✨ delante del título "Con esta compra…". */
    val SupportedSectionAccent: Color = Color(0xFFC9A227)

    /** Borde dorado de los avatares de caficultores apoyados. */
    val SupportedCaficultorAvatarBorder: Color = AvatarGoldBorder

    /** Fondo placeholder del avatar cuando no hay foto. */
    val SupportedCaficultorAvatarFallbackBackground: Color = CaficultorAvatarPlaceholderBackground

    /** Color del texto de las iniciales del avatar placeholder. */
    val SupportedCaficultorAvatarFallbackText: Color = CreamWhite

    /** Color del nombre comercial de la finca. */
    val SupportedCaficultorName: Color = TextPrimary

    /** Color del nombre del propietario debajo del nombre de la finca. */
    val SupportedCaficultorOwner: Color = TextSecondary

    /** Color del enlace verde "Ver perfil" alineado a la derecha. */
    val SupportedCaficultorViewProfile: Color = ForestGreen

    /** Color del título italic "¿Qué sigue?". */
    val OrderProgressSectionTitle: Color = TextPrimary

    /** Color del círculo dorado del paso activo del timeline. */
    val OrderProgressStepActiveFill: Color = Color(0xFFC9A227)

    /** Color del texto del paso activo del timeline. */
    val OrderProgressStepActiveText: Color = TextPrimary

    /** Color del círculo outline rosa de los pasos futuros. */
    val OrderProgressStepFutureBorder: Color = Color(0xFFE5C8C0)

    /** Color del texto de los pasos futuros. */
    val OrderProgressStepFutureText: Color = TextSecondary

    /** Color de la línea conectora vertical del timeline. */
    val OrderProgressConnector: Color = Color(0xFFE5C8C0)

    /** Fondo del CTA primario "Seguir mi pedido". */
    val PaymentSuccessPrimaryCtaBackground: Color = CoffeeBrown

    /** Texto del CTA primario "Seguir mi pedido". */
    val PaymentSuccessPrimaryCtaText: Color = Color(0xFFFFFFFF)

    /** Borde del CTA secundario outline "Seguir explorando". */
    val PaymentSuccessSecondaryCtaBorder: Color = ForestGreen

    /** Texto del CTA secundario outline "Seguir explorando". */
    val PaymentSuccessSecondaryCtaText: Color = ForestGreen

    /** Color del enlace dorado "Calificar esta experiencia". */
    val PaymentSuccessTertiaryLink: Color = Color(0xFFC9A227)

    // ── Tokens del checkout (paso 4 — fallo: Pago No Procesado) ─────────────

    /** Fondo de la pantalla de pago fallido (rosa muy tenue). */
    val PaymentFailedBackground: Color = Color(0xFFFAEBEC)

    /** Fondo del círculo rojo del hero. */
    val PaymentFailedHeroBackground: Color = Color(0xFFC62828)

    /** Color del ícono blanco "!" dentro del hero. */
    val PaymentFailedHeroIcon: Color = Color(0xFFFFFFFF)

    /** Color del ícono X de cerrar del top bar del fallo. */
    val PaymentFailedCloseIcon: Color = TextPrimary

    /** Fondo de la card del motivo del fallo (rosa claro). */
    val PaymentFailedReasonCardBackground: Color = Color(0xFFFCE4E6)

    /** Borde de la card del motivo del fallo (rosa más fuerte). */
    val PaymentFailedReasonCardBorder: Color = Color(0xFFF5C2C7)

    /** Color del ícono triángulo de advertencia y del texto del motivo (rojo). */
    val PaymentFailedReasonAccent: Color = Color(0xFFC62828)

    /** Color del texto pequeño "CÓDIGO DE ERROR: 51". */
    val PaymentFailedReasonCode: Color = Color(0xFFD15454)

    /** Color del bullet dorado de cada item del checklist. */
    val PaymentFailedChecklistBullet: Color = Color(0xFFC9A227)

    /** Color del texto del checklist. */
    val PaymentFailedChecklistText: Color = TextPrimary

    /** Color del título italic "¿Qué puedes hacer?". */
    val PaymentFailedChecklistTitle: Color = TextPrimary

    /** Color de la ilustración tenue del grano de café. */
    val PaymentFailedDecorationTint: Color = Color(0xFFC8C0AE)

    /** Fondo del CTA primario "Intentar otra vez". */
    val PaymentFailedPrimaryCtaBackground: Color = CoffeeBrown

    /** Texto del CTA primario "Intentar otra vez". */
    val PaymentFailedPrimaryCtaText: Color = Color(0xFFFFFFFF)

    /** Borde del CTA secundario outline "Cambiar método de pago". */
    val PaymentFailedSecondaryCtaBorder: Color = ForestGreen

    /** Texto del CTA secundario outline "Cambiar método de pago". */
    val PaymentFailedSecondaryCtaText: Color = ForestGreen

    /** Color del enlace "Contactar soporte" (texto primario subrayado). */
    val PaymentFailedSupportLink: Color = TextPrimary
}

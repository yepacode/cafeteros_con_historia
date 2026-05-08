package com.cafeteros.historia.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Tokens tipográficos de la marca "Origen".
 *
 * Se usa [FontFamily.Serif] (fuente serif del sistema) para los títulos de
 * marca y [FontFamily.SansSerif] para textos de UI/lectura. Cuando exista
 * una fuente custom, basta con reemplazar la familia aquí.
 */
object BrandTypography {

    // ── Splash ──────────────────────────────────────────────────────────────

    /** Estilo del nombre de la marca ("Origen"). Tamaño grande, serif elegante. */
    val BrandTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 48.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.CreamWhite
    )

    /**
     * Estilo del tagline ("CAFÉ Y REPOSTERÍA"). Tamaño pequeño, letter-spacing
     * amplio para refuerzo visual de marca; opacidad reducida para jerarquía.
     */
    val BrandTagline: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 6.sp,
        color = BrandColors.CreamWhiteMuted
    )

    // ── Onboarding ──────────────────────────────────────────────────────────

    /** Título destacado de cada página del onboarding (sobre tarjeta blanca). */
    val OnboardingTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 30.sp,
        color = BrandColors.TextPrimary
    )

    /** Texto descriptivo bajo el título de cada página del onboarding. */
    val OnboardingDescription: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        color = BrandColors.TextSecondary
    )

    /** Texto del botón primario (Siguiente / Comenzar). */
    val PrimaryButtonLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.PrimaryButtonText
    )

    /** Estilo del botón "Saltar" sobre la imagen del onboarding. */
    val SkipButtonLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.CardBackground
    )

    /** Estilo del enlace secundario "Ya tengo cuenta" en la última página. */
    val SecondaryLinkLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.CoffeeBrown
    )

    // ── Login / autenticación ───────────────────────────────────────────────

    /** Wordmark "Origen" sobre la pantalla de login. Serif itálico, escala media. */
    val Wordmark: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 32.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Título "Bienvenido de vuelta". Serif bold, jerarquía principal. */
    val LoginTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo bajo el título de login. */
    val LoginSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Etiqueta "CORREO ELECTRÓNICO" / "CONTRASEÑA". Mayúsculas, letter-spacing. */
    val FieldLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextPrimary
    )

    /** Texto del usuario y placeholder de los inputs del login. */
    val FieldText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Enlace "¿Olvidaste tu contraseña?". Verde alineado a la derecha. */
    val ForgotPasswordLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.LinkGreen
    )

    /** Texto del divider "O CONTINÚA CON". Serif itálico, sutil. */
    val DividerLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        color = BrandColors.TextSecondary
    )

    /** Etiqueta de los botones sociales (Google, Huella). */
    val SocialButtonLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Texto neutro del footer "¿No tienes cuenta?". */
    val RegisterFooterText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Acción "Regístrate" del footer. Bold + verde de marca. */
    val RegisterFooterAction: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.LinkGreen
    )

    // ── Registro: cards seleccionables, helpers, consentimiento ─────────────

    /** Título centrado del header de Register ("Crear cuenta"). Serif. */
    val ScreenTitleCentered: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo bajo el título centrado ("Únete a la comunidad..."). */
    val ScreenSubtitleCentered: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Título de las cards seleccionables ("Soy Comprador"). Serif bold. */
    val SelectableCardTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo de las cards seleccionables ("Quiero descubrir café"). */
    val SelectableCardSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Texto de ayuda bajo un campo (ej. requisitos de contraseña). */
    val FieldHelper: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Prefijo bold dentro de un input (ej. "+57" en teléfono). */
    val FieldPrefix: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Texto regular del label de un checkbox de consentimiento. */
    val ConsentText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        color = BrandColors.TextPrimary
    )

    /** Enlace embebido en un consentimiento (ej. "Términos y Condiciones"). */
    val ConsentLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        color = BrandColors.LinkGreen,
        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
    )

    // ── Pantalla de exploración del comprador ───────────────────────────────

    /** Wordmark "Origen" en el top bar (compacto, serif itálico). */
    val ExploreWordmark: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 24.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Saludo personalizado ("Buenos días, Mich ☕"). Sans serif sutil. */
    val ExploreGreeting: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Título principal de la home ("¿Qué café quieres descubrir hoy?"). */
    val ExploreHeroTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 30.sp,
        color = BrandColors.TextPrimary
    )

    /** Título de cada sección ("Explora por zona", "Caficultores destacados"). */
    val SectionTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Acción "Ver todos / Ver mapa" alineada a la derecha de la sección. */
    val SectionAction: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.LinkGreen
    )

    /** Nombre de la zona dentro de la card ("Huila", "Sierra Nevada"). */
    val RegionCardTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.CardBackground
    )

    /** Conteo bajo el nombre de la zona ("42 caficultores"). */
    val RegionCardSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.CardBackground.copy(alpha = 0.9f)
    )

    /** Nombre del caficultor / finca en la card ("Finca La Esperanza"). */
    val CaficultorName: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Ubicación del caficultor ("Huila • Pitalito"). */
    val CaficultorLocation: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Rating numérico junto a la estrella ("4.9"). */
    val RatingValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Total de reseñas entre paréntesis ("(127)"). */
    val RatingCount: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Texto del badge ("ORGÁNICO", "SOSTENIBLE"). Mayúsculas con tracking. */
    val BadgeLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    )

    /** Título de una historia destacada. */
    val StoryTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp,
        color = BrandColors.TextPrimary
    )

    /** Tiempo de lectura ("3 min de lectura"). */
    val StoryMeta: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Acción "Leer historia →" en color verde. */
    val StoryAction: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.LinkGreen
    )

    /** Nombre del producto ("Café Huila Pitalito"). */
    val ProductName: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Nombre de la finca bajo el producto. */
    val ProductFarm: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Precio del producto en marrón café fuerte. */
    val ProductPrice: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.CoffeeBrown
    )

    /** Etiqueta de cada item de la barra inferior. */
    val BottomBarLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
    )

    /** Número del badge del carrito en la barra inferior. */
    val CartBadgeNumber: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.CartBadgeText
    )

    // ── Mapa Cafetero ───────────────────────────────────────────────────────

    /** Título centrado del top bar del mapa ("Mapa Cafetero"). Serif itálico. */
    val MapScreenTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Texto de la card flotante "Toca una zona para explorar". */
    val MapInstructionText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta "PRODUCCIÓN" sobre la barra de degradado. Mayúsculas tracking. */
    val MapLegendLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextSecondary
    )

    /** Texto "Alta" al final del degradado de producción. */
    val MapLegendCaption: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Nombre de la zona bajo el marcador (ej. "SIERRA NEVADA"). */
    val ZoneMarkerLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp
    )

    /** Número del badge del marcador (conteo de caficultores). */
    val ZoneMarkerCount: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.MarkerCountBadgeText
    )

    /** Nombre de la zona en la pill resumen del bottom sheet ("Santander"). */
    val ZonePillName: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Conteo en la pill resumen ("38"). */
    val ZonePillCount: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Texto del filter chip ("Todos", "Orgánico", "Specialty"...). */
    val MapFilterChipLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )

    // ── Detalle de zona ─────────────────────────────────────────────────────

    /** Nombre de la zona en el top bar transparente sobre la imagen hero. */
    val ZoneDetailTopBarTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Texto del badge "ZONA CAFETERA". Mayúsculas con tracking. */
    val ZoneCafeteraBadge: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.ZoneCafeteraBadgeAccent
    )

    /** Título grande del nombre de la zona ("Santander"). Serif bold. */
    val ZoneDetailTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo en itálica bajo el nombre ("Café de altura, carácter fuerte"). */
    val ZoneDetailSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 15.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Valor numérico de cada stat ("38", "1.400m", "4.8"). */
    val ZoneStatValue: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta bajo cada stat ("CAFICULTORES", "ALTITUD PROM."). */
    val ZoneStatLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp,
        color = BrandColors.TextSecondary
    )

    /** Título de la card "Perfil de sabor". */
    val FlavorCardTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Texto de las pills de notas ("Chocolate amargo"). */
    val FlavorTagText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.FlavorTagText
    )

    /** Etiqueta de la característica del slider ("ACIDEZ", "CUERPO"…). */
    val FlavorTraitLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp,
        color = BrandColors.TextSecondary
    )

    /** Valor textual del slider ("BAJA", "ALTO", "MEDIA"). */
    val FlavorTraitValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.TextPrimary
    )

    /** Título de sección ("Sobre Santander", "Municipios", "Caficultores de…"). */
    val ZoneSectionTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Cuerpo de texto del párrafo descriptivo de la zona. */
    val ZoneDescriptionBody: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
        color = BrandColors.TextPrimary
    )

    /** Enlace "Leer más ›" debajo del párrafo. */
    val ZoneDescriptionReadMore: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.LinkGreen
    )

    /** Nombre del municipio dentro de su chip. */
    val MunicipalityName: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    )

    /** Conteo de fincas bajo el nombre del municipio. */
    val MunicipalityCount: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal
    )

    /** Nombre del caficultor en su card ("Don Ricardo"). */
    val ZoneCaficultorName: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Línea bajo el nombre con finca y municipio (mayúsculas, pequeña). */
    val ZoneCaficultorLocation: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.6.sp,
        color = BrandColors.TextSecondary
    )

    /** "Desde $45.000" en la card del caficultor. */
    val ZoneCaficultorPrice: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.CoffeeBrown
    )

    /** Título de la card oscura "¿Quieres saber más sobre…?". */
    val DiscoverChroniclesTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        color = BrandColors.DiscoverChroniclesText
    )

    /** Cuerpo de la card oscura. */
    val DiscoverChroniclesBody: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        color = BrandColors.DiscoverChroniclesText.copy(alpha = 0.85f)
    )

    /** Texto del botón "DESCUBRIR CRÓNICAS". */
    val DiscoverChroniclesButton: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.DiscoverChroniclesButtonText
    )

    // ── Detalle de caficultor ───────────────────────────────────────────────

    /** Nombre de la finca en la card flotante ("Finca La Esperanza"). */
    val ProfileFarmName: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Nombre del caficultor bajo la finca ("Don Alberto Ramírez"). */
    val ProfileCaficultorName: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Línea de ubicación ("Pitalito, Huila · 1.650 msnm"). */
    val ProfileLocationLine: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** "4.9 (127 reseñas)" en la profile card. */
    val ProfileRatingLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Texto pequeño en mayúsculas de las pills de certificación. */
    val CertificationBadgeLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        color = BrandColors.CertificationBadgeText
    )

    /** Cita destacada en italics centrada al inicio de "La historia de…". */
    val HistoryQuote: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
        color = BrandColors.TextPrimary
    )

    /** Caption italic bajo el video ("Conoce la finca y el proceso..."). */
    val VideoCaption: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Etiqueta bajo cada paso de proceso ("COSECHA MANUAL"). */
    val ProcessStepLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.6.sp,
        color = BrandColors.ProcessStepLabel
    )

    /** Encabezado "Productos (8)" — número entre paréntesis. */
    val ProductsCountInline: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 19.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Acción "ORDENAR ⌄" alineada a la derecha del header de productos. */
    val SortLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.TextSecondary
    )

    /** Nombre del producto en su card ("Café Huila Pitalito"). */
    val CaficultorProductName: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Peso del producto bajo el nombre ("250g"). */
    val CaficultorProductWeight: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Precio del producto ("$48.000"). */
    val CaficultorProductPrice: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.CoffeeBrown
    )

    /** Nombre del autor de la reseña ("Camila V."). */
    val ReviewerName: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Cuerpo del texto de la reseña. */
    val ReviewBody: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        color = BrandColors.TextPrimary
    )

    /** Título de la card "¿Tienes preguntas para...?". */
    val ContactCardTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Texto del botón "Enviar mensaje". */
    val ContactButtonLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.ContactButtonText
    )

    /** Texto del botón ancho oscuro "Ver todos los productos". */
    val WideDarkButtonLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.WideDarkButtonText
    )

    // ── Detalle de producto ─────────────────────────────────────────────────

    /** Texto del badge "HUILA" sobre el bloque del título. */
    val RegionBadgeLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.RegionBadgeText
    )

    /** Texto del pill "En stock". */
    val StockBadgeLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.StockBadgeText
    )

    /** Título grande del producto ("Café Huila Pitalito 250g"). Serif bold. */
    val ProductDetailTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo bajo el título ("Tueste medio · Molido o en grano"). */
    val ProductDetailSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** "284 vendidos" junto a la rating row. */
    val ProductSalesCount: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Precio principal del producto ("$48.000"). */
    val ProductDetailPrice: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Precio anterior tachado ("$56.000"). */
    val ProductOriginalPrice: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.PriceStrikethrough,
        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
    )

    /** Texto del badge de descuento ("-15%"). */
    val ProductDiscountBadge: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.DiscountBadgeText
    )

    /** Etiqueta pequeña "PRODUCIDO POR" sobre la pill del productor. */
    val ProducerSectionLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextSecondary
    )

    /** Nombre del productor en la pill. */
    val ProducerName: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Nombre de la finca bajo el productor (italic, secondary). */
    val ProducerFarm: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 13.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Enlace "Ver perfil →" en color amber. */
    val ViewProfileLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.ViewProfileLink
    )

    /** Etiqueta superior de cada bloque ("PRESENTACIÓN", "TUESTE"). */
    val ProductSectionLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextSecondary
    )

    /** Texto del chip de presentación seleccionado. */
    val PresentationChipSelected: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.PresentationChipSelectedText
    )

    /** Texto del chip de presentación NO seleccionado. */
    val PresentationChipUnselected: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.PresentationChipUnselectedText
    )

    /** Texto del valor visible en el dropdown ("Medio", "En grano"). */
    val OptionFieldValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta "Cantidad" del stepper. */
    val QuantityLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Número de cantidad dentro del stepper. */
    val QuantityValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Texto de tab activa ("Descripción"). */
    val TabActive: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Texto de tab inactiva ("Notas de cata", "Proceso"). */
    val TabInactive: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Cita italic en la tab "Descripción". */
    val ProductDescriptionQuote: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 14.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta pequeña uppercase de cada spec ("VARIEDAD"). */
    val ProductSpecLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp,
        color = BrandColors.TextSecondary
    )

    /** Valor de cada spec ("Caturra & Castillo"). */
    val ProductSpecValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Título de la card de envío ("Envío a Bogotá"). */
    val ShippingCardTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo de la card de envío ("Llega en 2-3 días hábiles"). */
    val ShippingCardSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Etiqueta "TOTAL" del bottom bar de checkout. */
    val CheckoutTotalLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp,
        color = BrandColors.TextSecondary
    )

    /** Total grande del bottom bar de checkout. */
    val CheckoutTotalValue: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Texto del botón "Agregar al carrito" del bottom bar. */
    val CheckoutCtaLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.WideDarkButtonText
    )

    // ── Pantalla de búsqueda ────────────────────────────────────────────────

    /** Acción "Cancelar" del top bar de búsqueda. */
    val SearchCancelAction: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Título italic de cada bloque ("Búsquedas recientes", "Tendencias…"). */
    val SearchSectionTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 19.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Acción "BORRAR TODO" en rojo, mayúsculas tracking. */
    val ClearAllLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.ClearAllAction
    )

    /** Texto de cada item en la lista de búsquedas recientes. */
    val RecentSearchText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Texto dentro de los chips de tendencias. */
    val TrendingChipLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TrendingChipText
    )

    /** Título dentro de cada card del grid "Explora por". */
    val SearchCategoryTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Nombre debajo del avatar de "Caficultores destacados". */
    val FeaturedCaficultorName: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    // ── Pantalla "Filtros de Origen" ────────────────────────────────────────

    /** Título centrado del top bar ("Filtros de Origen"). */
    val FiltersTopBarTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Acción "Limpiar" alineada a la derecha del top bar. */
    val FiltersClearAction: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta uppercase de cada bloque de filtro ("ZONA CAFETERA"). */
    val FilterSectionLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextPrimary
    )

    /** Valor en verde junto al título "RANGO DE PRECIO" ("$30.000 - $80.000"). */
    val PriceRangeValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.PriceRangeValue
    )

    /** Etiquetas pequeñas "MIN" / "MAX" sobre los inputs del slider. */
    val PriceFieldCaption: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.TextSecondary
    )

    /** Texto dentro del input de precio. */
    val PriceFieldValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Nombre dentro del chip de zona ("Santander"). */
    val ZoneChipName: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Conteo dentro del chip de zona ("38"). */
    val ZoneChipCount: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Texto del chip de sabor cuando NO está seleccionado. */
    val FlavorChipUnselected: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Texto del chip de sabor cuando está seleccionado. */
    val FlavorChipSelected: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.FlavorChipSelectedText
    )

    /** Etiqueta bajo cada taza del selector de tueste. */
    val RoastCupCaption: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        color = BrandColors.TextSecondary
    )

    /** Etiqueta de cada opción de proceso ("Lavado"). */
    val FilterListItem: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Texto de cada certificación dentro del checkbox row. */
    val CertificationItem: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Texto principal de cada card de altitud. */
    val AltitudeCardLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Texto principal de la card de altitud cuando está seleccionada. */
    val AltitudeCardLabelSelected: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.AltitudeCardSelectedText
    )

    /** Título de cada toggle de envío ("Envío gratis"). */
    val ShippingToggleTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo bajo el título de cada toggle. */
    val ShippingToggleSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Etiqueta del botón "LIMPIAR TODO" en el bottom bar. */
    val FiltersBottomClear: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.ClearFiltersButtonText
    )

    /** Etiqueta del botón "VER X RESULTADOS". */
    val FiltersBottomCta: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.WideDarkButtonText
    )

    // ── Pantalla "Resultados de búsqueda" ───────────────────────────────────

    /** Texto del input del top bar cuando ya tiene query escrita. */
    val SearchResultsQuery: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Texto dentro de un chip de filtro activo. */
    val ActiveFilterLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Tab activa ("Productos") de los resultados. */
    val SearchResultsTabActive: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Tab inactiva ("Caficultores", "Zonas") de los resultados. */
    val SearchResultsTabInactive: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** "47 resultados" alineado a la izquierda. */
    val ResultsCountLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** "Relevancia ⌄" alineado a la derecha del toolbar. */
    val ResultsSortLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta uppercase de la finca/caficultor sobre el nombre del producto. */
    val SearchResultFarmLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        color = BrandColors.TextSecondary
    )

    /** Nombre del producto dentro de la card del grid. */
    val SearchResultProductName: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 21.sp,
        color = BrandColors.TextPrimary
    )

    /** Valor del rating en la card ("4.9"). */
    val SearchResultRatingValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** "(24)" cuenta de reseñas en la card. */
    val SearchResultRatingCount: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Precio dorado en la card de resultado. */
    val SearchResultPriceLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.SearchResultPrice
    )

    /** Texto del badge naranja "HUILA" sobre la imagen. */
    val SearchResultZoneBadge: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.SearchResultZoneBadgeText
    )

    // ── Pantalla "el caficultor" ────────────────────────────────────────────

    /** Wordmark "el caficultor" del top bar (serif itálico). */
    val CaficultorShopWordmark: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Texto del botón oscuro "Filtros" de la fila de filtros aplicados. */
    val ShopFilterButtonLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.ShopFilterButtonText
    )

    /** Texto dentro de un chip de filtro aplicado outline. */
    val ShopFilterChipLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Título serif italic grande del empty state ("No encontramos…"). */
    val EmptyResultsTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 26.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp,
        color = BrandColors.TextPrimary
    )

    /** Cuerpo descriptivo bajo el título del empty state. */
    val EmptyResultsBody: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
        color = BrandColors.TextSecondary
    )

    /** Texto del botón outline "Limpiar filtros". */
    val ClearFiltersOutlineLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.ClearFiltersOutlineText
    )

    /** Etiqueta uppercase "SUGERENCIAS POPULARES". */
    val SuggestionsLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 2.sp,
        color = BrandColors.TextSecondary
    )

    /** Título serif italic dentro de cada card de sugerencia. */
    val SuggestionCardTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 19.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Acción uppercase secundaria de cada card ("VER COLECCIÓN"). */
    val SuggestionCardAction: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextSecondary
    )

    // ── Pantalla "Tu Selección" (carrito) ───────────────────────────────────

    /** Título serif centrado del top bar del carrito. */
    val CartTopBarTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Acción "Limpiar" alineada a la derecha del top bar. */
    val CartClearAction: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Mensaje del progreso de envío gratis ("Te faltan $15.000…"). */
    val FreeShippingMessage: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Texto del porcentaje del progreso de envío gratis ("80%"). */
    val FreeShippingPercent: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.FreeShippingProgressText
    )

    /** Nombre del caficultor agrupador ("Finca La Esperanza"). */
    val CartCaficultorName: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Nombre del producto dentro de cada card del carrito. */
    val CartItemName: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp,
        color = BrandColors.TextPrimary
    )

    /** Línea "peso · tipo" bajo el nombre del producto. */
    val CartItemMeta: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Precio actual de cada item del carrito (dorado). */
    val CartItemPrice: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.CartItemPrice
    )

    /** Precio anterior tachado (cuando aplica). */
    val CartItemOriginalPrice: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.CartItemOriginalPrice,
        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
    )

    /** Cantidad dentro del stepper -N+ del item. */
    val CartItemStepperValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Título de la card de envío ("Llega el martes 23 de abril"). */
    val CartShippingTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo de la card de envío (dirección). */
    val CartShippingSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Enlace "Cambiar" de la card de envío. */
    val CartShippingChange: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.CartShippingChangeLink
    )

    /** Placeholder y texto del input de código de descuento. */
    val CartDiscountInput: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Texto del botón "Aplicar" del código de descuento. */
    val CartApplyButton: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.CartApplyButtonText
    )

    /** Etiqueta de cada fila del resumen ("Subtotal", "Envío"). */
    val CartSummaryLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Valor numérico de cada fila del resumen. */
    val CartSummaryValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Valor del descuento en el resumen (verde, negativo). */
    val CartDiscountValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.CartDiscountValue
    )

    /** "Total" en bold a la izquierda del cierre del resumen. */
    val CartTotalLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Total dorado grande del resumen. */
    val CartTotalValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.CartTotalValue
    )

    /** Texto del CTA "Continuar al pago". */
    val CartCheckoutCta: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.CartCheckoutButtonText
    )

    /** Enlace "Seguir comprando" debajo del CTA. */
    val CartContinueShopping: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.CartContinueShoppingLink
    )

    /** Etiqueta uppercase de cada tab de la bottom bar del carrito. */
    val CartBottomBarLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    )

    // ── Estado vacío del carrito ────────────────────────────────────────────

    /** Título serif bold del empty state ("Tu carrito está vacío"). */
    val EmptyCartTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo bajo el título del empty state. */
    val EmptyCartSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
        color = BrandColors.TextSecondary
    )

    /** Texto del CTA "Explorar café" del empty state. */
    val EmptyCartCtaLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.EmptyCartCtaText
    )

    /** Título italic gris claro de "Recomendaciones del Origen". */
    val RecommendationsHeader: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Nombre serif bold dentro de cada card de recomendación. */
    val RecommendationCardName: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Línea uppercase con metadata bajo el nombre ("HUILA · LAVADO"). */
    val RecommendationCardMeta: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.TextSecondary
    )

    // ── Checkout (paso 1: Dirección de Envío) ───────────────────────────────

    /** Título italic del top bar del checkout ("Dirección de Envío"). */
    val CheckoutTopBarTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta uppercase "PASO X DE Y" del stepper. */
    val StepperLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.StepperLabel
    )

    /** Título serif bold de cada sección del checkout ("Mis direcciones"). */
    val CheckoutSectionTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Enlace verde "Gestionar" del header de direcciones. */
    val AddressManageLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.AddressManageLink,
        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
    )

    /** Enlace verde "Editar" dentro de una card de dirección. */
    val AddressEditLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.AddressEditLink
    )

    /** Texto del badge "CASA" / "OFICINA" dentro de una card. */
    val AddressTypeBadgeLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.AddressTypeBadgeText
    )

    /** Línea bold con la dirección principal de la card. */
    val AddressLine: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Línea con la ciudad bajo la dirección principal. */
    val AddressCity: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Texto del contacto al final de la card ("Mich Cárdenas · 300…"). */
    val AddressContact: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Texto verde del CTA "Agregar nueva dirección". */
    val AddNewAddressLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.AddNewAddressLabel
    )

    /** Etiqueta uppercase de "INSTRUCCIONES PARA EL REPARTIDOR (OPCIONAL)". */
    val InstructionsLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextSecondary
    )

    /** Texto y placeholder dentro del textarea de instrucciones. */
    val InstructionsField: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        color = BrandColors.TextSecondary
    )

    /** Cada opción de receptor ("Usar mis datos", "Alguien más recibirá"). */
    val RecipientOptionLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** "SUBTOTAL" uppercase dentro del bottom bar del checkout. */
    val CheckoutSubtotalLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.TextSecondary
    )

    /** Subtotal grande dentro del bottom bar del checkout. */
    val CheckoutSubtotalValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Texto del CTA "Continuar". */
    val CheckoutContinueLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.CheckoutContinueText
    )

    // ── Checkout paso 2: Método de Pago ─────────────────────────────────────

    /**
     * Título italic del top bar del paso 2 ("Método de pago"). Idéntico a
     * [CheckoutTopBarTitle] — definido aparte para que cualquier ajuste
     * del paso 2 no afecte al paso 1.
     */
    val PaymentTopBarTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta uppercase pequeña "DIRECCIÓN DE ENVÍO" en la summary card. */
    val PaymentAddressSummaryLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.PaymentAddressSummaryLabel
    )

    /** Línea con la dirección dentro de la summary card. */
    val PaymentAddressSummaryLine: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Enlace verde "Cambiar" alineado a la derecha de la summary card. */
    val PaymentAddressSummaryChange: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.PaymentAddressSummaryChangeLink
    )

    /** Título italic serif "Selecciona cómo deseas pagar". */
    val PaymentSectionTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Título principal de cada card de método de pago. */
    val PaymentMethodTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo gris bajo el título de la card de método de pago. */
    val PaymentMethodSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Emoji del prefijo de cada card de método de pago. */
    val PaymentMethodEmoji: TextStyle = TextStyle(
        fontSize = 18.sp
    )

    /** Texto pequeño dentro de cada chip de marca (Visa, Mastercard...). */
    val CardBrandChipLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        color = BrandColors.CardBrandChipText
    )

    /** Texto "**** 4567 Visa" dentro de la fila de tarjeta guardada. */
    val SavedCardLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Enlace verde "+ Agregar nueva tarjeta". */
    val AddNewCardLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.AddNewCardLink
    )

    /** Texto del banner de seguridad inferior. */
    val SecurityNoticeText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp,
        color = BrandColors.SecurityNoticeText
    )

    /** "TOTAL A PAGAR" uppercase del bottom bar del paso 2. */
    val PaymentTotalLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.PaymentTotalLabel
    )

    /** Valor grande "$146.000" del total del bottom bar del paso 2. */
    val PaymentTotalValue: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.PaymentTotalValue
    )

    /** Texto del CTA "Revisar pedido". */
    val PaymentReviewCtaLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.PaymentReviewCtaText
    )

    // ── Checkout paso 3: Revisar Pedido ─────────────────────────────────────

    /** Título italic del top bar del paso 3 ("Revisar pedido"). */
    val OrderReviewTopBarTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Título serif bold de cada sección ("Envío a", "Productos", "Método de pago"...). */
    val OrderReviewSectionTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Enlace verde "Editar" alineado a la derecha del section header. */
    val OrderReviewEditLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.OrderReviewEditLink
    )

    /** Línea principal de la dirección dentro de la card de envío ("Cra 10 #42-15"). */
    val OrderReviewAddressLine: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Línea secundaria de la dirección ("Bogotá, Cundinamarca"). */
    val OrderReviewAddressSecondary: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Nombre del producto en cada fila del listado. */
    val OrderReviewProductName: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Sub-línea con cantidad y precio unitario "2 x $48.000". */
    val OrderReviewProductMeta: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Precio total de la línea de producto, alineado a la derecha. */
    val OrderReviewProductPrice: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Texto serif pequeño del thumbnail circular del producto ("CAFÉ ORIGEN"). */
    val OrderReviewProductThumbLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        color = BrandColors.OrderReviewProductThumbAccent
    )

    /** Enlace verde "Ver todos los productos". */
    val OrderReviewViewAllLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.OrderReviewViewAllLink
    )

    /** Texto principal "Visa **** 4567" del resumen de método de pago. */
    val OrderReviewPaymentSummaryText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.TextPrimary
    )

    /** Texto "VISA" italic dentro del badge blanco del resumen de pago. */
    val OrderReviewVisaBadgeLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        letterSpacing = 0.5.sp,
        color = BrandColors.OrderReviewVisaBadgeText
    )

    /** Texto de la línea de entrega estimada. */
    val OrderReviewDeliveryLine: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta de cada línea del totals card ("Subtotal", "Envío"...). */
    val OrderReviewTotalLineLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Valor de cada línea del totals card. */
    val OrderReviewTotalLineValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta serif "Total a pagar" del bloque inferior del totals card. */
    val OrderReviewTotalLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Etiqueta uppercase pequeña "IVA INCLUIDO". */
    val OrderReviewIvaIncluido: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.TextSecondary
    )

    /** Cifra grande dorada del total ("$146.000"). */
    val OrderReviewTotalAmount: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.OrderReviewTotalAmount
    )

    /** Cuerpo del párrafo de aceptación de términos. */
    val OrderReviewTermsBody: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        color = BrandColors.OrderReviewTermsBody
    )

    /** Etiqueta del CTA "Confirmar y pagar $146.000". */
    val OrderReviewConfirmCtaLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.OrderReviewConfirmCtaText
    )

    /** Texto pequeño "Al pagar aceptas..." bajo el CTA. */
    val OrderReviewFooterTerms: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.OrderReviewFooterTermsText
    )

    // ── Checkout paso 4 — éxito: Pago Confirmado ────────────────────────────

    /** Título serif bold grande "¡Gracias por apoyar al origen!". */
    val PaymentSuccessTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo "Tu pedido **#OR-34521** fue confirmado". */
    val PaymentSuccessSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Título "Tu pedido" dentro de la card receipt. */
    val OrderReceiptCardTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.OrderReceiptCardTitle
    )

    /** Etiqueta de cada fila del receipt ("Número", "Total pagado"...). */
    val OrderReceiptRowLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.OrderReceiptRowLabel
    )

    /** Valor de cada fila del receipt. */
    val OrderReceiptRowValue: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.OrderReceiptRowValue
    )

    /** Título italic serif "Con esta compra apoyaste a:". */
    val SupportedSectionTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = BrandColors.SupportedSectionTitle
    )

    /** Texto del avatar placeholder con iniciales del caficultor. */
    val SupportedCaficultorAvatarLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.SupportedCaficultorAvatarFallbackText
    )

    /** Nombre comercial de la finca apoyada. */
    val SupportedCaficultorName: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.SupportedCaficultorName
    )

    /** Nombre del propietario bajo el nombre de la finca. */
    val SupportedCaficultorOwner: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.SupportedCaficultorOwner
    )

    /** Enlace verde "Ver perfil" alineado a la derecha. */
    val SupportedCaficultorViewProfile: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.SupportedCaficultorViewProfile
    )

    /** Título serif bold "¿Qué sigue?". */
    val OrderProgressSectionTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.OrderProgressSectionTitle
    )

    /** Texto del paso activo del timeline. */
    val OrderProgressStepActive: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.OrderProgressStepActiveText
    )

    /** Texto de los pasos futuros del timeline. */
    val OrderProgressStepFuture: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.OrderProgressStepFutureText
    )

    /** Etiqueta del CTA primario "Seguir mi pedido" / "Intentar otra vez". */
    val PrimaryDarkCtaLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.PaymentSuccessPrimaryCtaText
    )

    /** Etiqueta del CTA secundario outline ("Seguir explorando" / "Cambiar método de pago"). */
    val SecondaryGreenOutlineCtaLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.PaymentSuccessSecondaryCtaText
    )

    /** Enlace dorado "Calificar esta experiencia". */
    val PaymentSuccessTertiaryLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.PaymentSuccessTertiaryLink
    )

    // ── Checkout paso 4 — fallo: Pago No Procesado ──────────────────────────

    /** Título serif bold "No pudimos procesar tu pago". */
    val PaymentFailedTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 30.sp,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo "No te preocupes, no se realizó ningún cobro". */
    val PaymentFailedSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextSecondary
    )

    /** Texto rojo del motivo del fallo ("Motivo: Fondos insuficientes"). */
    val PaymentFailedReasonText: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.PaymentFailedReasonAccent
    )

    /** Texto del código de error ("CÓDIGO DE ERROR: 51"). */
    val PaymentFailedReasonCode: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        color = BrandColors.PaymentFailedReasonCode
    )

    /** Título serif bold "¿Qué puedes hacer?". */
    val PaymentFailedChecklistTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.PaymentFailedChecklistTitle
    )

    /** Texto del checklist "¿Qué puedes hacer?". */
    val PaymentFailedChecklistItem: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.PaymentFailedChecklistText
    )

    /** Enlace "Contactar soporte" (texto primario subrayado). */
    val PaymentFailedSupportLink: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.PaymentFailedSupportLink
    )

    // ── "Mis Direcciones" (libreta de direcciones) ──────────────────────────

    /** Título centrado del top bar ("Mis Direcciones"). */
    val AddressBookTopBarTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Caption editorial italic ("Logística del Origen"). */
    val AddressBookEditorialCaption: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.AddressBookEditorialCaption
    )

    /** Título serif bold grande del hero ("Gestiona tus puntos…"). */
    val AddressBookHeroTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 38.sp,
        color = BrandColors.TextPrimary
    )

    /** Texto del badge de categoría dentro de cada card ("Casa", "Oficina"). */
    val ManagedAddressBadgeLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = BrandColors.ManagedAddressBadgeText
    )

    /** Línea bold con la dirección principal de cada card. */
    val ManagedAddressLine: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.TextPrimary
    )

    /** Línea con la ciudad bajo la dirección principal. */
    val ManagedAddressCity: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Línea de teléfono al final de cada card. */
    val ManagedAddressPhone: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BrandColors.TextPrimary
    )

    /** Texto de la pill amarilla "PREDETERMINADA". */
    val DefaultAddressPill: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandColors.DefaultAddressPillText
    )

    /** Caption italic gris bajo la imagen del footer del address book. */
    val AddressBookFooterCaption: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 13.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
        color = BrandColors.AddressBookFooterCaption
    )

    /** Texto del CTA "Agregar dirección" del bottom bar. */
    val AddAddressCtaLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BrandColors.AddAddressCtaText
    )

    // ── Empty state de "Mis Direcciones" ────────────────────────────────────

    /** Título serif bold del empty state ("No tienes direcciones guardadas"). */
    val EmptyAddressesTitle: TextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 28.sp,
        color = BrandColors.TextPrimary
    )

    /** Subtítulo gris bajo el título del empty state. */
    val EmptyAddressesSubtitle: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
        color = BrandColors.TextSecondary
    )

    // ── Bottom bar del perfil (INICIO / TIENDA / PEDIDOS / PERFIL) ──────────

    /** Etiqueta uppercase de cada tab de la bottom bar del perfil. */
    val ProfileBottomBarLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    )
}

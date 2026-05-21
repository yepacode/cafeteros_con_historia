package com.cafeteros.historia.ui.features.explore.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Producto de café del listado "Lo más pedido esta semana".
 *
 * El precio se modela como String ya formateado para evitar acoplar la UI
 * con un formateador de moneda en este nivel; la fuente real (backend o
 * caso de uso) lo entregará así.
 *
 * @property name nombre comercial del café ("Café Huila Pitalito").
 * @property farmName finca o productor ("Finca La Esperanza").
 * @property formattedPrice precio listo para mostrar ("$48.000").
 * @property imageRes drawable opcional con la foto del producto.
 * @property placeholderColor color de respaldo cuando no hay imagen.
 */
data class CoffeeProduct(
    val name: String,
    val farmName: String,
    val formattedPrice: String,
    @param:DrawableRes val imageRes: Int? = null,
    val placeholderColor: Color,
    /** id del documento en Firestore (para navegar al ProductDetail real). */
    val productId: String = "",
    /** Foto del producto en Base64 (sobreescribe imageRes si está). */
    val imageBase64: String? = null
)

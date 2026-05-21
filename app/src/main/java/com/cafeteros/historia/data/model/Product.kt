package com.cafeteros.historia.data.model

/**
 * Modelo de dominio de un producto del caficultor.
 *
 * Es la representación pura del producto que viaja por la capa `data` y se
 * persiste en Firestore (`/products/{id}`). La capa UI puede tener clases
 * derivadas (estado del formulario, sample data) pero **no** debe leer
 * directamente desde `data` o viceversa fuera de este modelo.
 *
 * **Imagen:** se guarda en el mismo documento como Base64 ([imageBase64]).
 * El proyecto deliberadamente no usa Firebase Storage porque requiere plan
 * Blaze (de pago); para mantener todo en el plan Spark, las fotos se
 * comprimen agresivamente (≤200 KB JPEG) antes de codificar. El límite
 * duro de un documento Firestore es 1 MB.
 *
 * **Propiedad:** [caficultorUid] referencia al usuario que publicó el
 * producto. Es la "foreign key" hacia `/users/{uid}` y se usa para filtrar
 * la lista de "mis productos" en `ProductListActivity`/`InventoryActivity`.
 *
 * @property id identificador del documento en Firestore. Vacío al construir
 *  un producto nuevo (Firestore asigna el id al guardar). Para productos
 *  recuperados ya es el id del documento.
 * @property caficultorUid uid del caficultor dueño del producto.
 * @property name nombre visible del producto.
 * @property category enum [ProductCategory] persistido como `name` (string).
 * @property shortDescription resumen mostrado en cards y listados.
 * @property fullDescription descripción larga mostrada en el detalle.
 * @property tags etiquetas libres del caficultor (separadas por coma en el form).
 * @property varietyChips variedades de café seleccionadas (Caturra, Castillo…).
 * @property tastingNotes notas de cata seleccionadas (Chocolate, Caramelo…).
 * @property format enum [CoffeeFormat] (en grano / molido).
 * @property weightGrams peso de la presentación (250, 500, 1000).
 * @property priceCop precio en pesos colombianos por unidad.
 * @property stockUnits unidades en bodega.
 * @property isOrganic true si el producto tiene certificación orgánica.
 * @property imageBase64 foto del producto codificada Base64 (JPEG). Puede
 *  ser null si el caficultor aún no subió foto.
 * @property isPaused true si el producto está pausado (no visible para
 *  compradores pero sigue existiendo).
 * @property createdAtEpochMillis fecha de creación; Firestore puede
 *  sobrescribirla con `serverTimestamp()` cuando se haga la primera escritura.
 */
data class Product(
    val id: String = "",
    val caficultorUid: String = "",
    val name: String = "",
    val category: ProductCategory = ProductCategory.DEFAULT,
    val shortDescription: String = "",
    val fullDescription: String = "",
    val tags: List<String> = emptyList(),
    val varietyChips: Set<String> = emptySet(),
    val tastingNotes: Set<String> = emptySet(),
    val format: CoffeeFormat = CoffeeFormat.DEFAULT,
    val weightGrams: Int = 250,
    val priceCop: Int = 0,
    val stockUnits: Int = 0,
    val isOrganic: Boolean = false,
    val imageBase64: String? = null,
    val isPaused: Boolean = false,
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)

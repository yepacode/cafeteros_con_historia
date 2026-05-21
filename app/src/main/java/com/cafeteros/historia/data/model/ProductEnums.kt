package com.cafeteros.historia.data.model

/**
 * Categorías que el caficultor puede asignar a un producto al crearlo. El
 * [label] es el texto que se muestra al usuario en el dropdown; el `name`
 * del enum es lo que viaja a Firestore.
 */
enum class ProductCategory(val label: String) {
    GREEN_BEAN(label = "Café verde"),
    ROASTED_BEAN(label = "Café en grano"),
    GROUND(label = "Café molido"),
    CAPSULES(label = "Cápsulas"),
    HONEY(label = "Miel de café"),
    OTHER(label = "Otro");

    companion object {
        /** Valor inicial seleccionado en el form de creación. */
        val DEFAULT: ProductCategory = ROASTED_BEAN

        /**
         * Deserialización tolerante: si el documento Firestore guardó un
         * valor desconocido (por ej. después de borrar un enum), devolvemos
         * [OTHER] en lugar de tirar excepción.
         */
        fun fromNameOrDefault(name: String?): ProductCategory =
            entries.firstOrNull { it.name == name } ?: OTHER
    }
}

/** Formato físico que define cómo se entrega el café al comprador. */
enum class CoffeeFormat(val label: String) {
    WHOLE_BEAN(label = "En grano"),
    GROUND_MEDIUM(label = "Molido medio");

    companion object {
        /** Valor inicial seleccionado en el form de creación. */
        val DEFAULT: CoffeeFormat = WHOLE_BEAN

        /** Deserialización tolerante para Firestore. */
        fun fromNameOrDefault(name: String?): CoffeeFormat =
            entries.firstOrNull { it.name == name } ?: WHOLE_BEAN
    }
}

/** Pesos disponibles como chips en el paso de precios. */
val DEFAULT_WEIGHT_OPTIONS_GRAMS: List<Int> = listOf(250, 500, 1000)

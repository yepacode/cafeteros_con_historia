package com.cafeteros.historia.ui.features.productdetail.model

/** Tipo de molienda seleccionable en el dropdown "TIPO". */
enum class ProductGrindType(val label: String) {
    EN_GRANO(label = "En grano"),
    MOLIDO_FINO(label = "Molido fino"),
    MOLIDO_MEDIO(label = "Molido medio"),
    MOLIDO_GRUESO(label = "Molido grueso")
}

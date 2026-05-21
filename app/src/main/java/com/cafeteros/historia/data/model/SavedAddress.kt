package com.cafeteros.historia.data.model

/**
 * Dirección guardada del comprador. Se persiste en la subcolección
 * `/users/{uid}/addresses/{id}` para que sea trivialmente consultar
 * "mis direcciones" sin queries con where.
 *
 * @property id id del documento Firestore.
 * @property label etiqueta corta para distinguir entre direcciones
 *  ("Casa", "Oficina", "Mamá").
 * @property line dirección completa ("Calle 5 # 12-34, Bogotá").
 * @property recipientName persona que recibe (puede ser el mismo
 *  comprador o alguien distinto).
 * @property recipientPhone teléfono del receptor.
 * @property notes instrucciones para el repartidor (opcional).
 * @property isDefault si es la dirección por defecto del comprador.
 * @property createdAtEpochMillis fecha de creación.
 */
data class SavedAddress(
    val id: String = "",
    val label: String = "",
    val line: String = "",
    val recipientName: String = "",
    val recipientPhone: String = "",
    val notes: String = "",
    val isDefault: Boolean = false,
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)

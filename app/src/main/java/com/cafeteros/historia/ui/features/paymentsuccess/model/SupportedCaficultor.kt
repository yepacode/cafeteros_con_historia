package com.cafeteros.historia.ui.features.paymentsuccess.model

/**
 * Caficultor (productor) al que se está apoyando con la compra.
 *
 * Aparece como tarjeta-fila en la sección "Con esta compra apoyaste a:" del
 * paso 4 (éxito). Cuando exista BD, viene del join entre los productos del
 * pedido y la tabla `caficultores`, agrupado por finca.
 *
 * @property id identificador estable del caficultor.
 * @property fincaName nombre comercial de la finca ("Finca La Esperanza").
 * @property ownerName nombre del caficultor responsable ("Alberto Rodríguez").
 * @property avatarInitials iniciales para el avatar placeholder mientras no
 *   haya foto real (se renderizan dentro del círculo).
 */
data class SupportedCaficultor(
    val id: String,
    val fincaName: String,
    val ownerName: String,
    val avatarInitials: String
)

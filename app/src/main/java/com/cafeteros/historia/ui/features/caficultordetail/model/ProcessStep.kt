package com.cafeteros.historia.ui.features.caficultordetail.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Paso del proceso de beneficio del café que se muestra como ícono + label
 * en la fila "Cómo procesan su café".
 *
 * Cada paso encapsula su [label] visible y el [icon] Material que lo
 * representa. Mantenerlos juntos evita dos listas paralelas y hace
 * trivial agregar un paso nuevo (basta con armar la data class).
 *
 * @property label etiqueta corta en mayúsculas ("COSECHA MANUAL").
 * @property icon ícono Material monocromo asociado.
 */
data class ProcessStep(
    val label: String,
    val icon: ImageVector
)

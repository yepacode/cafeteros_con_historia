package com.cafeteros.historia.ui.features.farmer_registration.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing

/**
 * Fila de un documento opcional del paso 4.
 *
 * Layout: ícono + label (izquierda) + botón circular "+" o "✓" (derecha).
 * Tocar la fila o el botón abre el picker de documentos. Cuando ya se subió
 * un archivo, el "+" se reemplaza por un check verde y el fondo del círculo
 * cambia para indicar el estado completado.
 *
 * @param modifier modifier opcional aplicado a la fila.
 * @param icon ícono Material a la izquierda.
 * @param label nombre del documento opcional.
 * @param uri URI del documento subido, o null si aún no.
 * @param onPicked callback cuando el usuario elige un archivo.
 */
@Composable
fun OptionalDocumentRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    uri: Uri?,
    onPicked: (Uri) -> Unit
) {
    val docLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { picked ->
        if (picked != null) onPicked(picked)
    }
    val launchPicker = {
        docLauncher.launch(arrayOf("application/pdf", "image/*"))
    }
    val isUploaded = uri != null

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.CardBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = launchPicker)
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandColors.FarmerPrimary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BrandColors.TextPrimary
            )
        )
        ActionCircle(isUploaded = isUploaded, onClick = launchPicker)
    }
}

/** Botón circular: "+" cuando vacío, "✓" cuando subido. */
@Composable
private fun ActionCircle(
    isUploaded: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(
                color = if (isUploaded) {
                    BrandColors.FarmerPrimary
                } else {
                    BrandColors.InputBackground
                },
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isUploaded) Icons.Outlined.Check else Icons.Outlined.Add,
            contentDescription = if (isUploaded) "Subido" else "Subir",
            tint = if (isUploaded) BrandColors.PrimaryButtonText else BrandColors.TextPrimary,
            modifier = Modifier.size(16.dp)
        )
    }
}

package com.cafeteros.historia.ui.features.farmer_registration.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
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
 * Tarjeta de un documento obligatorio del paso 4.
 *
 * Layout: ícono (izquierda) + título + descripción (centro) + botón "Subir"
 * o estado completado (derecha). Si [showNotApplicableToggle] es `true`,
 * además se muestra un chip "No aplica" tappeable bajo la descripción.
 *
 * Cuando hay [uri] presente, el botón "Subir" se reemplaza por un check
 * verde y la copy "Subido". Tocar el card vuelve a abrir el picker para
 * cambiar el archivo.
 *
 * @param modifier modifier opcional aplicado a la tarjeta.
 * @param icon ícono Material a la izquierda.
 * @param title título del documento.
 * @param description descripción / ayuda corta.
 * @param uri URI del documento subido, o null si aún no.
 * @param notApplicable true si el caficultor marcó "No aplica" (solo se honra
 *  cuando [showNotApplicableToggle] es true).
 * @param showNotApplicableToggle true para mostrar el chip "No aplica" como
 *  alternativa a subir el documento.
 * @param onPicked callback cuando el usuario elige un archivo.
 * @param onToggleNotApplicable callback al pulsar el chip "No aplica".
 *  Solo se invoca si [showNotApplicableToggle] es true.
 */
@Composable
fun MandatoryDocumentCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
    uri: Uri?,
    notApplicable: Boolean = false,
    showNotApplicableToggle: Boolean = false,
    onPicked: (Uri) -> Unit,
    onToggleNotApplicable: () -> Unit = {}
) {
    val docLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { picked ->
        if (picked != null) onPicked(picked)
    }
    val launchPicker = {
        docLauncher.launch(arrayOf("application/pdf", "image/*"))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.CardBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        DocumentLeadingIcon(icon = icon)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = BrandColors.TextSecondary
                )
            )
            if (showNotApplicableToggle) {
                NotApplicableChip(
                    isActive = notApplicable,
                    onClick = onToggleNotApplicable,
                    modifier = Modifier.padding(top = BrandSpacing.xs)
                )
            }
        }

        when {
            notApplicable -> NotApplicableLabel()
            uri != null -> UploadedBadge(onClick = launchPicker)
            else -> UploadButton(onClick = launchPicker)
        }
    }
}

@Composable
private fun DocumentLeadingIcon(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                color = BrandColors.InputBackground,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandColors.FarmerPrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun UploadButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = BrandColors.InputBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Subir",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.TextPrimary
            )
        )
    }
}

@Composable
private fun UploadedBadge(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .background(
                color = BrandColors.FarmerStatsBadgeBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = BrandColors.FarmerPrimary,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "Subido",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColors.FarmerPrimary
            )
        )
    }
}

@Composable
private fun NotApplicableLabel() {
    Box(
        modifier = Modifier
            .background(
                color = BrandColors.NotApplicableChipBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = "No aplica",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = BrandColors.NotApplicableChipText
            )
        )
    }
}

@Composable
private fun NotApplicableChip(
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = if (isActive) {
                    BrandColors.FarmerStatsBadgeBackground
                } else {
                    BrandColors.NotApplicableChipBackground
                },
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "No aplica",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (isActive) BrandColors.FarmerPrimary else BrandColors.NotApplicableChipText
            )
        )
    }
}

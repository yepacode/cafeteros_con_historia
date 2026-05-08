package com.cafeteros.historia.ui.features.farmer_registration.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import java.io.File

/**
 * Slot punteado para subir una foto (cédula frontal/posterior, selfie).
 *
 * Si [photoUri] es null, muestra el placeholder con ícono + label + ayuda.
 * Si tiene valor, muestra la foto recortada al tamaño del slot. Tocar
 * cualquiera de los dos estados abre [PhotoSourceBottomSheet] para elegir
 * entre cámara o galería.
 *
 * @param modifier modifier opcional aplicado al Box exterior.
 * @param title título del slot (ej. "CÉDULA FRONTAL").
 * @param helperText texto pequeño bajo el título (ej. "PNG, JPG hasta 5MB").
 * @param icon ícono Material a mostrar como placeholder.
 * @param photoUri URI de la foto seleccionada o null si está vacía.
 * @param onPhotoSelected callback con la URI elegida.
 */
@Composable
fun PhotoUploadSlot(
    modifier: Modifier = Modifier,
    title: String,
    helperText: String,
    icon: ImageVector = Icons.Outlined.Badge,
    photoUri: Uri?,
    onPhotoSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onPhotoSelected(uri)
    }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val captured = cameraImageUri.value
        if (success && captured != null) onPhotoSelected(captured)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { showSheet = true }
    ) {
        if (photoUri != null) {
            AsyncImage(
                model = photoUri,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = BrandColors.InputBackground,
                        shape = RoundedCornerShape(16.dp)
                    )
            )
        } else {
            DashedPlaceholder(title = title, helperText = helperText, icon = icon)
        }
    }

    if (showSheet) {
        PhotoSourceBottomSheet(
            onDismiss = { showSheet = false },
            onTakePhoto = {
                val uri = createTempCameraUri(context)
                cameraImageUri.value = uri
                cameraLauncher.launch(uri)
                showSheet = false
            },
            onPickFromGallery = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                showSheet = false
            }
        )
    }
}

/** Estado vacío del slot: borde punteado + icono + texto. */
@Composable
private fun DashedPlaceholder(
    title: String,
    helperText: String,
    icon: ImageVector
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BrandColors.AuthBackground, shape = RoundedCornerShape(16.dp))
            .drawDashedBorder()
            .padding(BrandSpacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = BrandColors.InputBackground, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = title,
            modifier = Modifier.padding(top = BrandSpacing.sm),
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = helperText,
            modifier = Modifier.padding(top = 2.dp),
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.sp,
                color = BrandColors.TextSecondary
            ),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Bottom sheet con dos opciones: tomar foto con cámara o elegir de galería.
 *
 * @param onDismiss callback al cerrar el sheet sin elegir.
 * @param onTakePhoto callback al elegir "Tomar foto".
 * @param onPickFromGallery callback al elegir "Elegir de galería".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoSourceBottomSheet(
    onDismiss: () -> Unit,
    onTakePhoto: () -> Unit,
    onPickFromGallery: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BrandColors.CardBackground
    ) {
        Column(modifier = Modifier.padding(bottom = BrandSpacing.lg)) {
            ListItem(
                headlineContent = { Text("Tomar foto") },
                leadingContent = { Icon(Icons.Outlined.CameraAlt, contentDescription = null) },
                colors = ListItemDefaults.colors(containerColor = BrandColors.CardBackground),
                modifier = Modifier.clickable(onClick = onTakePhoto)
            )
            ListItem(
                headlineContent = { Text("Elegir de galería") },
                leadingContent = { Icon(Icons.Outlined.PhotoLibrary, contentDescription = null) },
                colors = ListItemDefaults.colors(containerColor = BrandColors.CardBackground),
                modifier = Modifier.clickable(onClick = onPickFromGallery)
            )
        }
    }
}

/**
 * Crea un archivo temporal en `cacheDir/camera/` y devuelve su URI vía
 * FileProvider. La autoridad debe coincidir con la declarada en el Manifest.
 */
private fun createTempCameraUri(context: Context): Uri {
    val cameraDir = File(context.cacheDir, "camera").apply { mkdirs() }
    val file = File.createTempFile("photo_", ".jpg", cameraDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

/**
 * Modifier que dibuja un borde punteado redondeado alrededor del contenedor.
 * No existe en Compose por defecto, así que se implementa con [drawBehind] +
 * [Stroke] con [PathEffect].
 */
private fun Modifier.drawDashedBorder(): Modifier = this.drawBehind {
    val strokeWidthPx = 1.5.dp.toPx()
    val dashOnPx = 8.dp.toPx()
    val dashOffPx = 6.dp.toPx()
    val cornerRadiusPx = 16.dp.toPx()
    drawRoundRect(
        color = BrandColors.IndicatorInactive,
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
        style = Stroke(
            width = strokeWidthPx,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashOnPx, dashOffPx), 0f)
        )
    )
}

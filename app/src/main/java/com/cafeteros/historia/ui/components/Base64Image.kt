package com.cafeteros.historia.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.data.util.ImageBase64
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Renderiza una imagen guardada como string Base64 en Firestore.
 *
 * Decodifica el Base64 a `Bitmap` la primera vez que cambia [base64] y lo
 * cachea con [remember]. Si la cadena es null o vacía, muestra un
 * placeholder con un emoji de cámara para que la card no quede "rota".
 *
 * El decodificado es síncrono pero ligero (las imágenes ya están
 * comprimidas a ≤200 KB por [ImageBase64.encodeFromUri]). Para listas con
 * decenas de productos esto se podría mover a `LaunchedEffect`, pero el
 * caso de uso actual del caficultor (decenas, no miles) no lo amerita.
 *
 * El caller controla el tamaño y la forma con el [modifier]; este composable
 * solo dibuja la imagen o el placeholder dentro del área que recibe.
 */
@Composable
fun Base64Image(
    base64: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val bitmap = remember(base64) { ImageBase64.decodeToBitmap(base64) }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier.background(BrandColors.InputBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "📷", fontSize = 22.sp)
        }
    }
}

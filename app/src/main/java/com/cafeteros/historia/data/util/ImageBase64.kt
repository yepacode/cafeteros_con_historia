package com.cafeteros.historia.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.media.ExifInterface
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Utilidades para codificar/decodificar imágenes como Base64.
 *
 * El proyecto guarda las fotos de los productos como string Base64 dentro
 * del documento Firestore (en lugar de Firebase Storage, que requiere
 * plan Blaze). Por eso comprimimos agresivamente antes de codificar para
 * caber holgadamente en el límite de 1 MB de un documento Firestore.
 *
 * Política de compresión:
 *  - Lado mayor de la imagen ≤ [MAX_DIMENSION_PX] (1024 px).
 *  - Calidad JPEG [JPEG_QUALITY] (75%).
 *
 * En la práctica esto produce strings Base64 de ~150–250 KB, dejando
 * margen para los demás campos del documento.
 */
object ImageBase64 {

    private const val MAX_DIMENSION_PX = 1024
    private const val JPEG_QUALITY = 75

    /**
     * Lee la imagen apuntada por [uri], la redimensiona y la comprime, y
     * devuelve el resultado codificado como Base64 listo para guardar en
     * Firestore. Devuelve `null` si la Uri no se puede abrir o si el
     * archivo no es una imagen válida.
     *
     * Esta función hace I/O bloqueante (lee del ContentResolver y decodifica
     * el bitmap) — llamadora debe invocarla desde `Dispatchers.IO`.
     */
    fun encodeFromUri(context: Context, uri: Uri): String? {
        return try {
            val bitmap = decodeAndDownscale(context, uri)
            if (bitmap == null) {
                android.util.Log.w(TAG, "encodeFromUri: decodeAndDownscale returned null for $uri")
                return null
            }
            val oriented = applyExifOrientation(context, uri, bitmap)
            val base64 = ByteArrayOutputStream().use { stream ->
                oriented.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, stream)
                val rawSize = stream.size()
                val encoded = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
                android.util.Log.d(
                    TAG,
                    "encodeFromUri: raw=${rawSize}B base64=${encoded.length}chars (${oriented.width}x${oriented.height})"
                )
                encoded
            }
            base64
        } catch (t: Throwable) {
            android.util.Log.e(TAG, "encodeFromUri failed for $uri", t)
            null
        }
    }

    /**
     * Decodifica un string Base64 producido por [encodeFromUri] de vuelta
     * a [Bitmap]. Devuelve null si la cadena es inválida.
     *
     * Se usa para mostrar la imagen en pantallas que solo conocen el
     * documento Firestore (ProductListScreen, InventoryScreen, etc.).
     */
    fun decodeToBitmap(base64: String?): Bitmap? {
        if (base64.isNullOrBlank()) return null
        return try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            if (bitmap == null) {
                android.util.Log.w(
                    TAG,
                    "decodeToBitmap: BitmapFactory returned null (bytes=${bytes.size}, b64len=${base64.length})"
                )
            }
            bitmap
        } catch (t: Throwable) {
            android.util.Log.e(TAG, "decodeToBitmap failed (b64len=${base64.length})", t)
            null
        }
    }

    private const val TAG = "ImageBase64"

    /**
     * Lee la imagen con `inSampleSize` calculado para que su lado mayor
     * quede ≤ [MAX_DIMENSION_PX] sin perder calidad por sobre-muestreo.
     * Esto evita cargar bitmaps gigantes en memoria al codificar fotos de
     * cámara de 12 MP.
     */
    private fun decodeAndDownscale(context: Context, uri: Uri): Bitmap? {
        val resolver = context.contentResolver

        // Primera pasada: solo lee dimensiones, no carga píxeles.
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        val boundsStream = try {
            resolver.openInputStream(uri)
        } catch (t: Throwable) {
            android.util.Log.e(TAG, "openInputStream (bounds) threw for $uri", t)
            null
        }
        if (boundsStream == null) {
            android.util.Log.w(TAG, "openInputStream (bounds) returned null for $uri")
            return null
        }
        boundsStream.use {
            BitmapFactory.decodeStream(it, null, bounds)
        }

        val largerSide = maxOf(bounds.outWidth, bounds.outHeight)
        android.util.Log.d(
            TAG,
            "decodeAndDownscale: bounds=${bounds.outWidth}x${bounds.outHeight} mime=${bounds.outMimeType}"
        )
        if (largerSide <= 0) {
            android.util.Log.w(
                TAG,
                "decodeAndDownscale: bounds inválidos (largerSide=$largerSide) para $uri — archivo posiblemente vacío o no es imagen"
            )
            return null
        }

        val sampleSize = generateSequence(1) { it * 2 }
            .first { largerSide / it <= MAX_DIMENSION_PX }

        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        }
        val pixelStream = try {
            resolver.openInputStream(uri)
        } catch (t: Throwable) {
            android.util.Log.e(TAG, "openInputStream (pixels) threw for $uri", t)
            null
        }
        if (pixelStream == null) {
            android.util.Log.w(TAG, "openInputStream (pixels) returned null for $uri")
            return null
        }
        val bitmap = pixelStream.use {
            BitmapFactory.decodeStream(it, null, decodeOptions)
        }
        if (bitmap == null) {
            android.util.Log.w(TAG, "BitmapFactory.decodeStream returned null on second pass for $uri")
        }
        return bitmap
    }

    /**
     * Algunas cámaras (sobre todo en Samsung/Pixel) guardan la imagen
     * "rotada" y declaran la orientación en el EXIF. Si no aplicamos esa
     * rotación al bitmap, la foto se ve de lado en la app. Esta función
     * lee el EXIF y aplica la rotación correspondiente.
     */
    private fun applyExifOrientation(
        context: Context,
        uri: Uri,
        bitmap: Bitmap
    ): Bitmap {
        val rotationDegrees = runCatching {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                val exif = ExifInterface(stream)
                when (exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }
            } ?: 0f
        }.getOrDefault(0f)

        if (rotationDegrees == 0f) return bitmap

        val matrix = android.graphics.Matrix().apply { postRotate(rotationDegrees) }
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )
    }
}

package com.cafeteros.historia.ui.features.farmer_profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.core.content.FileProvider
import java.io.File
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.ui.components.Base64Image
import com.cafeteros.historia.ui.features.farmer_registration.components.LabeledTextField
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity contenedora de [EditFarmScreen]. Conecta el formulario al
 * [EditFarmViewModel] que persiste en Firestore vía
 * [com.cafeteros.historia.data.repository.FarmRepository].
 *
 * Carga la finca actual al entrar, deja que el usuario la edite, y al
 * pulsar "Guardar" persiste los cambios (foto comprimida + resto del doc).
 */
class EditFarmActivity : ComponentActivity() {

    private val viewModel: EditFarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val initial by viewModel.initial.collectAsStateWithLifecycle()
                val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
                val outcome by viewModel.outcome.collectAsStateWithLifecycle()

                LaunchedEffect(outcome) {
                    when (val o = outcome) {
                        is FarmSaveOutcome.Success -> {
                            Toast.makeText(
                                this@EditFarmActivity,
                                "Finca guardada",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.consumeOutcome()
                            finish()
                        }
                        is FarmSaveOutcome.Error -> {
                            Toast.makeText(
                                this@EditFarmActivity,
                                o.message,
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.consumeOutcome()
                        }
                        null -> Unit
                    }
                }

                val current = initial
                if (current == null) {
                    LoadingFarm()
                } else {
                    EditFarmScreen(
                        initial = current,
                        isSaving = isSaving,
                        onClose = ::finish,
                        onSave = viewModel::save
                    )
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, EditFarmActivity::class.java))
        }
    }
}

/**
 * Loader inicial mientras el ViewModel lee la finca del caficultor de
 * Firestore. Evita el pestañeo de un form en blanco antes de mostrar los
 * datos persistidos.
 */
@Composable
private fun LoadingFarm() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Cargando tu finca…", color = BrandColors.TextSecondary)
    }
}

private enum class EditFarmTab(val label: String) {
    HISTORY("Historia"), PHOTOS("Fotos"), DATA("Datos"), PROCESS("Proceso")
}

/** Slot al que apunta el resultado del picker de cámara/galería. */
private enum class PhotoSlotTarget { FARM, FARMER }

/**
 * Formulario de edición de la finca.
 *
 * Es stateful localmente: cada campo del form vive en un `mutableStateOf`
 * inicializado con el valor de [initial]. Esto permite editar sin que un
 * cambio remoto en Firestore sobrescriba lo que el usuario está tipiando.
 *
 * Al pulsar "Guardar cambios" se llama [onSave] con un [FarmProfile]
 * construido a partir del state local + la [Uri] de la foto nueva (null
 * si no se cambió, para que el repo conserve la imagen previa).
 *
 * @param initial valores iniciales leídos del repositorio.
 * @param isSaving true mientras la persistencia está en curso; deshabilita
 *  el botón de guardar para evitar dobles taps.
 * @param onClose cerrar la activity sin guardar.
 * @param onSave callback al pulsar "Guardar cambios". Recibe el perfil
 *  construido + la URI de foto seleccionada (o null si no se cambió).
 */
@Composable
fun EditFarmScreen(
    initial: FarmProfile,
    isSaving: Boolean,
    onClose: () -> Unit,
    onSave: (FarmProfile, Uri?, Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(EditFarmTab.HISTORY) }

    // ── Estado local del formulario, inicializado desde [initial] ──
    var name by remember { mutableStateOf(initial.name) }
    var title by remember { mutableStateOf(initial.title) }
    var highlight by remember { mutableStateOf(initial.highlight) }
    var fullStory by remember { mutableStateOf(initial.story) }
    var region by remember { mutableStateOf(initial.region) }
    var altitudeText by remember {
        mutableStateOf(if (initial.altitudeMeters > 0) initial.altitudeMeters.toString() else "")
    }
    var areaText by remember {
        mutableStateOf(if (initial.areaHectares > 0) initial.areaHectares.toString() else "")
    }
    var varietiesText by remember { mutableStateOf(initial.varieties.joinToString(", ")) }
    var shadeType by remember { mutableStateOf(initial.shadeType) }
    var quote by remember { mutableStateOf(initial.quote) }
    var videoUrl by remember { mutableStateOf(initial.videoUrl.orEmpty()) }
    var processStepsText by remember {
        mutableStateOf(
            initial.processSteps
                .ifEmpty { defaultProcessSteps() }
                .joinToString("\n")
        )
    }
    var newPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var newFarmerPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showPhotoSourceSheet by remember { mutableStateOf(false) }
    var showFarmerPhotoSourceSheet by remember { mutableStateOf(false) }
    var pickedLat by remember { mutableStateOf(initial.latitude) }
    var pickedLng by remember { mutableStateOf(initial.longitude) }

    // Identifica a qué slot dirigir el resultado de cámara o galería:
    // FARM = foto principal de la finca; FARMER = retrato del caficultor.
    val activeSlot = remember { mutableStateOf(PhotoSlotTarget.FARM) }

    val galleryPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        android.util.Log.d("EditFarmScreen", "galleryPicker returned uri=$uri slot=${activeSlot.value}")
        if (uri != null) when (activeSlot.value) {
            PhotoSlotTarget.FARM -> newPhotoUri = uri
            PhotoSlotTarget.FARMER -> newFarmerPhotoUri = uri
        }
    }

    // Para la cámara necesitamos un Uri de FileProvider creado antes de
    // lanzar TakePicture; lo guardamos en un state para recuperarlo en el
    // callback (que solo recibe true/false de éxito).
    val pendingCameraUri = remember { mutableStateOf<Uri?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val captured = pendingCameraUri.value
        android.util.Log.d("EditFarmScreen", "cameraLauncher success=$success uri=$captured slot=${activeSlot.value}")
        if (success && captured != null) when (activeSlot.value) {
            PhotoSlotTarget.FARM -> newPhotoUri = captured
            PhotoSlotTarget.FARMER -> newFarmerPhotoUri = captured
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Outlined.Close, contentDescription = "Cerrar", tint = BrandColors.TextPrimary)
            }
            Text(
                text = "Editar mi finca",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
            TextButton(
                enabled = !isSaving,
                onClick = {
                    onSave(
                        buildProfileFromForm(
                            initial = initial,
                            name = name,
                            title = title,
                            highlight = highlight,
                            fullStory = fullStory,
                            region = region,
                            altitudeText = altitudeText,
                            areaText = areaText,
                            varietiesText = varietiesText,
                            shadeType = shadeType,
                            quote = quote,
                            videoUrl = videoUrl,
                            processStepsText = processStepsText,
                            latitude = pickedLat,
                            longitude = pickedLng
                        ),
                        newPhotoUri,
                        newFarmerPhotoUri
                    )
                }
            ) {
                Text(
                    text = if (isSaving) "Guardando…" else "Guardar",
                    color = BrandColors.FarmerPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Tabs (cosméticos: la pantalla scrollea entera, los tabs solo destacan secciones).
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BrandSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EditFarmTab.entries.forEach { tab ->
                val active = tab == activeTab
                Box(
                    modifier = Modifier
                        .clickable { activeTab = tab }
                        .padding(vertical = 8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = tab.label,
                            color = if (active) BrandColors.FarmerPrimary else BrandColors.TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (active) FontWeight.Bold else FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 24.dp, height = 2.dp)
                                .background(if (active) BrandColors.FarmerPrimary else Color.Transparent)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            LabeledTextField(label = "NOMBRE DE LA FINCA", value = name, onValueChange = { name = it })
            LabeledTextField(label = "TÍTULO DE TU HISTORIA", value = title, onValueChange = { title = it })
            LabeledTextField(label = "FRASE DESTACADA", value = highlight, onValueChange = { highlight = it })
            LabeledTextField(label = "CITA REPRESENTATIVA", value = quote, onValueChange = { quote = it })

            // Historia completa
            Text(
                text = "TU HISTORIA COMPLETA",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md)
            ) {
                BasicTextField(
                    value = fullStory,
                    onValueChange = { if (it.length <= 3000) fullStory = it },
                    textStyle = TextStyle(
                        color = BrandColors.TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    ),
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                )
            }
            Text(
                text = "${fullStory.length} / 3.000 caracteres",
                color = BrandColors.TextSecondary,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )

            // Foto principal
            Text(
                text = "FOTO PRINCIPAL DE LA FINCA",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            FarmPhotoSlot(
                existingBase64 = initial.principalPhotoBase64,
                newUri = newPhotoUri,
                onPick = {
                    activeSlot.value = PhotoSlotTarget.FARM
                    showPhotoSourceSheet = true
                },
                onClear = { newPhotoUri = null }
            )

            Spacer(modifier = Modifier.height(BrandSpacing.sm))

            // ── Foto del caficultor (avatar) ───────────────────────────
            Text(
                text = "FOTO DE PERFIL DEL CAFICULTOR",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Esta es la foto que se muestra como tu avatar circular en el perfil y la que verán los compradores.",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic
            )
            FarmerAvatarSlot(
                existingBase64 = initial.farmerPhotoBase64,
                newUri = newFarmerPhotoUri,
                onPick = {
                    activeSlot.value = PhotoSlotTarget.FARMER
                    showFarmerPhotoSourceSheet = true
                },
                onClear = { newFarmerPhotoUri = null }
            )

            if (showPhotoSourceSheet) {
                FarmPhotoSourceSheet(
                    onDismiss = { showPhotoSourceSheet = false },
                    onTakePhoto = {
                        val uri = createFarmCameraUri(context)
                        pendingCameraUri.value = uri
                        showPhotoSourceSheet = false
                        cameraLauncher.launch(uri)
                    },
                    onPickFromGallery = {
                        showPhotoSourceSheet = false
                        galleryPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }

            if (showFarmerPhotoSourceSheet) {
                FarmPhotoSourceSheet(
                    onDismiss = { showFarmerPhotoSourceSheet = false },
                    onTakePhoto = {
                        val uri = createFarmCameraUri(context)
                        pendingCameraUri.value = uri
                        showFarmerPhotoSourceSheet = false
                        cameraLauncher.launch(uri)
                    },
                    onPickFromGallery = {
                        showFarmerPhotoSourceSheet = false
                        galleryPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }

            // Video
            LabeledTextField(
                label = "URL DEL VIDEO (YOUTUBE / VIMEO)",
                value = videoUrl,
                onValueChange = { videoUrl = it }
            )

            // Ubicación en el mapa
            Text(
                text = "UBICACIÓN DE LA FINCA",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Toca el mapa donde está tu finca para que los compradores la vean en el mapa cafetero.",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                com.cafeteros.historia.ui.components.MapLocationPicker(
                    initialLat = pickedLat,
                    initialLng = pickedLng,
                    onLocationPicked = { lat, lng ->
                        pickedLat = lat
                        pickedLng = lng
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            if (pickedLat != null && pickedLng != null) {
                Text(
                    text = "📍 Lat: ${"%.5f".format(pickedLat)}, Lng: ${"%.5f".format(pickedLng)}",
                    color = BrandColors.FarmerPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Datos técnicos
            Text(
                text = "DATOS TÉCNICOS DE LA FINCA",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            LabeledTextField(label = "REGIÓN / DEPARTAMENTO", value = region, onValueChange = { region = it })
            LabeledTextField(
                label = "ALTITUD PROMEDIO",
                value = altitudeText,
                onValueChange = { altitudeText = it.filter { c -> c.isDigit() } },
                suffix = "msnm"
            )
            LabeledTextField(
                label = "ÁREA CULTIVADA",
                value = areaText,
                onValueChange = { areaText = it.filter { c -> c.isDigit() } },
                suffix = "hectáreas"
            )
            LabeledTextField(
                label = "VARIEDADES (separadas por coma)",
                value = varietiesText,
                onValueChange = { varietiesText = it }
            )
            LabeledTextField(label = "TIPO DE SOMBRA", value = shadeType, onValueChange = { shadeType = it })

            // Proceso post-cosecha
            Text(
                text = "PROCESO POST-COSECHA (un paso por línea)",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.md)
            ) {
                BasicTextField(
                    value = processStepsText,
                    onValueChange = { processStepsText = it },
                    textStyle = TextStyle(
                        color = BrandColors.TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    ),
                    modifier = Modifier.fillMaxWidth().height(140.dp)
                )
            }

            Spacer(modifier = Modifier.height(BrandSpacing.lg))
            Button(
                onClick = {
                    onSave(
                        buildProfileFromForm(
                            initial = initial,
                            name = name,
                            title = title,
                            highlight = highlight,
                            fullStory = fullStory,
                            region = region,
                            altitudeText = altitudeText,
                            areaText = areaText,
                            varietiesText = varietiesText,
                            shadeType = shadeType,
                            quote = quote,
                            videoUrl = videoUrl,
                            processStepsText = processStepsText,
                            latitude = pickedLat,
                            longitude = pickedLng
                        ),
                        newPhotoUri,
                        newFarmerPhotoUri
                    )
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.FarmerPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (isSaving) "Guardando…" else "Guardar cambios",
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

/**
 * Construye el [FarmProfile] que se envía al repositorio a partir del
 * state actual del formulario. Las certificaciones se conservan tal cual
 * estaban en [initial] porque se editan desde [CertificationsActivity],
 * no desde este form.
 */
private fun buildProfileFromForm(
    initial: FarmProfile,
    name: String,
    title: String,
    highlight: String,
    fullStory: String,
    region: String,
    altitudeText: String,
    areaText: String,
    varietiesText: String,
    shadeType: String,
    quote: String,
    videoUrl: String,
    processStepsText: String,
    latitude: Double?,
    longitude: Double?
): FarmProfile = initial.copy(
    name = name.trim(),
    title = title.trim(),
    highlight = highlight.trim(),
    story = fullStory.trim(),
    region = region.trim(),
    altitudeMeters = altitudeText.toIntOrNull() ?: 0,
    areaHectares = areaText.toIntOrNull() ?: 0,
    varieties = varietiesText.split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() },
    shadeType = shadeType.trim(),
    quote = quote.trim(),
    videoUrl = videoUrl.trim().ifBlank { null },
    processSteps = processStepsText.split("\n")
        .map { it.trim() }
        .filter { it.isNotBlank() },
    latitude = latitude,
    longitude = longitude
)

/** Lista por defecto de pasos sugeridos cuando el caficultor aún no tiene proceso. */
private fun defaultProcessSteps(): List<String> = listOf(
    "✋ Recolección manual selectiva",
    "🫧 Fermentación controlada",
    "☀️ Secado en marquesina",
    "📦 Estabilización 30 días"
)

/**
 * Crea un archivo temporal en `cacheDir/camera/` y devuelve su `content://`
 * URI vía FileProvider. La autoridad coincide con la declarada en el
 * Manifest (`${applicationId}.fileprovider`).
 */
private fun createFarmCameraUri(context: android.content.Context): Uri {
    val cameraDir = File(context.cacheDir, "camera").apply { mkdirs() }
    val file = File.createTempFile("farm_photo_", ".jpg", cameraDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

/**
 * Bottom sheet con dos opciones: tomar foto con cámara o elegir de galería.
 * Idéntico patrón al [PhotoUploadSlot] del registro de caficultor; se duplica
 * aquí localmente para no acoplar `farmer_profile` con `farmer_registration`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FarmPhotoSourceSheet(
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
                headlineContent = { Text("Tomar foto con la cámara") },
                leadingContent = {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Outlined.CameraAlt,
                        contentDescription = null
                    )
                },
                colors = ListItemDefaults.colors(containerColor = BrandColors.CardBackground),
                modifier = Modifier.clickable(onClick = onTakePhoto)
            )
            ListItem(
                headlineContent = { Text("Elegir de la galería") },
                leadingContent = {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Outlined.PhotoLibrary,
                        contentDescription = null
                    )
                },
                colors = ListItemDefaults.colors(containerColor = BrandColors.CardBackground),
                modifier = Modifier.clickable(onClick = onPickFromGallery)
            )
        }
    }
}

/**
 * Slot circular pequeño para el retrato del caficultor. Mismo
 * comportamiento que [FarmPhotoSlot] pero pensado como avatar (120dp,
 * recortado en círculo). Útil para diferenciar visualmente del slot
 * grande de la foto de la finca.
 */
@Composable
private fun FarmerAvatarSlot(
    existingBase64: String?,
    newUri: Uri?,
    onPick: () -> Unit,
    onClear: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(BrandColors.InputBackground, androidx.compose.foundation.shape.CircleShape)
            .clickable(onClick = onPick)
            .clip(androidx.compose.foundation.shape.CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when {
            newUri != null -> {
                AsyncImage(
                    model = newUri,
                    contentDescription = "Foto nueva del caficultor",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .background(BrandColors.SkipButtonScrim, androidx.compose.foundation.shape.CircleShape)
                        .clickable(onClick = onClear)
                        .padding(4.dp)
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "Quitar nueva foto",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            existingBase64 != null -> {
                Base64Image(
                    base64 = existingBase64,
                    contentDescription = "Foto actual del caficultor",
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.PhotoCamera,
                        contentDescription = null,
                        tint = BrandColors.TextSecondary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Tu foto",
                        color = BrandColors.TextSecondary,
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

/**
 * Slot de foto principal: muestra la imagen guardada (Base64), la nueva
 * que el usuario acaba de seleccionar (Uri), o un placeholder. Tap → abre
 * el selector de fuente (cámara o galería).
 */
@Composable
private fun FarmPhotoSlot(
    existingBase64: String?,
    newUri: Uri?,
    onPick: () -> Unit,
    onClear: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onPick)
            .clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        when {
            newUri != null -> {
                AsyncImage(
                    model = newUri,
                    contentDescription = "Foto nueva de la finca",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(BrandColors.SkipButtonScrim, CircleShape)
                        .clickable(onClick = onClear)
                        .padding(6.dp)
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "Quitar nueva foto",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            existingBase64 != null -> {
                Base64Image(
                    base64 = existingBase64,
                    contentDescription = "Foto actual de la finca",
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.PhotoCamera,
                        contentDescription = null,
                        tint = BrandColors.TextSecondary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Toca para subir foto principal",
                        color = BrandColors.TextSecondary,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

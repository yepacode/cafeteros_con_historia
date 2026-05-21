package com.cafeteros.historia.ui.features.coffeemap

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.ui.features.caficultordetail.CaficultorDetailActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * ViewModel del mapa cafetero. Observa todas las fincas y deja solo las
 * que tienen coordenadas (lat/lng) — son las que se pueden marcar.
 */
class CoffeeMapViewModel(application: Application) : AndroidViewModel(application) {

    private val farmRepository: FarmRepository =
        (application as CafeterosApplication).farmRepository

    val farmsWithLocation: StateFlow<List<FarmProfile>> = farmRepository.observeAllFarms()
        .map { farms -> farms.filter { it.latitude != null && it.longitude != null } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}

/**
 * Activity del mapa cafetero. Reemplaza la ilustración estática anterior
 * por un MapView real de OpenStreetMap con marcadores en las
 * coordenadas reales de cada caficultor. Tap en marcador → perfil
 * público del caficultor.
 */
class CoffeeMapActivity : ComponentActivity() {

    private val viewModel: CoffeeMapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val farms by viewModel.farmsWithLocation.collectAsStateWithLifecycle()
                CoffeeMapScreen(
                    farms = farms,
                    onBack = ::finish,
                    onFarmTap = { farm ->
                        CaficultorDetailActivity.start(this, farm.caficultorUid)
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CoffeeMapActivity::class.java))
        }
    }
}

@Composable
private fun CoffeeMapScreen(
    farms: List<FarmProfile>,
    onBack: () -> Unit,
    onFarmTap: (FarmProfile) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)
        .systemBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandColors.TextPrimary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Mapa Cafetero",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandColors.TextPrimary
                    )
                )
                Text(
                    text = "${farms.size} caficultor${if (farms.size == 1) "" else "es"} en el mapa",
                    color = BrandColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        if (farms.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(BrandSpacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(BrandColors.InputBackground, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = BrandColors.CoffeeBrown,
                        modifier = Modifier.size(56.dp)
                    )
                }
                Spacer(modifier = Modifier.height(BrandSpacing.md))
                Text(
                    text = "Ningún caficultor ha marcado su ubicación",
                    color = BrandColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Cuando un caficultor marque dónde está su finca, aparecerá aquí.",
                    color = BrandColors.TextSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            FarmsMapView(
                farms = farms,
                onFarmTap = onFarmTap,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        }
    }
}

/**
 * Composable que aloja un osmdroid `MapView` con marcadores para cada
 * finca. Reutiliza el patrón ya probado en [com.cafeteros.historia.ui
 * .components.MapLocationPicker].
 */
@Composable
private fun FarmsMapView(
    farms: List<FarmProfile>,
    onFarmTap: (FarmProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", android.content.Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = context.packageName
    }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            isHorizontalMapRepetitionEnabled = false
            isVerticalMapRepetitionEnabled = false
            background = ColorDrawable(BrandColors.MapCanvasBackground.toArgb())
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) { mapView.onResume() }
            override fun onPause(owner: LifecycleOwner) { mapView.onPause() }
            override fun onDestroy(owner: LifecycleOwner) { mapView.onDetach() }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { view ->
            view.overlays.clear()

            // Centro del mapa: en la finca promedio, o en Pereira si no hay.
            val centerPoint = if (farms.isNotEmpty()) {
                val avgLat = farms.mapNotNull { it.latitude }.average()
                val avgLng = farms.mapNotNull { it.longitude }.average()
                GeoPoint(avgLat, avgLng)
            } else {
                GeoPoint(4.8133, -75.6961) // Pereira, Eje Cafetero
            }
            view.controller.setCenter(centerPoint)
            view.controller.setZoom(if (farms.size <= 1) 12.0 else 7.0)

            // Marcadores por finca.
            farms.forEach { farm ->
                val lat = farm.latitude ?: return@forEach
                val lng = farm.longitude ?: return@forEach
                val marker = Marker(view).apply {
                    position = GeoPoint(lat, lng)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = farm.name.ifBlank { "Finca" }
                    snippet = farm.region.ifBlank { "Sin región" }
                    setOnMarkerClickListener { _, _ ->
                        onFarmTap(farm)
                        true
                    }
                }
                view.overlays.add(marker)
            }
            view.invalidate()
        }
    )
}

package com.cafeteros.historia.ui.features.caficultordetail

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.FarmProfile
import com.cafeteros.historia.data.model.Product
import com.cafeteros.historia.data.repository.FarmRepository
import com.cafeteros.historia.data.repository.ProductRepository
import com.cafeteros.historia.ui.components.Base64Image
import com.cafeteros.historia.ui.features.farmer_messages.ChatActivity
import com.cafeteros.historia.ui.features.productdetail.ProductDetailActivity
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Estado UI de la pantalla pública del caficultor. */
data class CaficultorDetailUiState(
    val farm: FarmProfile = FarmProfile(),
    val products: List<Product> = emptyList()
)

/**
 * ViewModel del perfil público del caficultor visto desde el comprador.
 * Carga el FarmProfile y los productos activos del caficultor.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class CaficultorDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val farmRepository: FarmRepository = app.farmRepository
    private val productRepository: ProductRepository = app.productRepository

    private val _caficultorUid = MutableStateFlow("")
    val uiState: StateFlow<CaficultorDetailUiState> = _caficultorUid
        .flatMapLatest { uid ->
            if (uid.isBlank()) flowOf(CaficultorDetailUiState())
            else kotlinx.coroutines.flow.combine(
                farmRepository.observeMyFarm(uid),
                productRepository.observeMyProducts(uid)
            ) { farm, products ->
                CaficultorDetailUiState(
                    farm = farm,
                    products = products.filter { !it.isPaused }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CaficultorDetailUiState()
        )

    fun load(uid: String) {
        _caficultorUid.value = uid
    }
}

/**
 * Activity contenedora del perfil público del caficultor.
 *
 * Recibe `caficultorUid` por intent extra y carga el [FarmProfile] +
 * productos activos. Replaza la antigua versión mock que usaba
 * `CaficultorDetailSampleData`.
 */
class CaficultorDetailActivity : ComponentActivity() {

    private val viewModel: CaficultorDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val uid = intent.getStringExtra(EXTRA_CAFICULTOR_UID).orEmpty()

        setContent {
            CafeterosTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(uid) {
                    if (uid.isNotBlank()) viewModel.load(uid)
                }

                CaficultorDetailScreen(
                    state = state,
                    onBack = ::finish,
                    onProductTap = { product ->
                        ProductDetailActivity.start(this, product.id)
                    },
                    onContact = {
                        ChatActivity.start(
                            context = this,
                            conversationId = null,
                            partnerUid = uid,
                            partnerName = state.farm.name.ifBlank { "Caficultor" }
                        )
                    }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_CAFICULTOR_UID = "extra_caficultor_uid"

        fun start(context: Context, caficultorUid: String) {
            context.startActivity(
                Intent(context, CaficultorDetailActivity::class.java)
                    .putExtra(EXTRA_CAFICULTOR_UID, caficultorUid)
            )
        }
    }
}

@Composable
private fun CaficultorDetailScreen(
    state: CaficultorDetailUiState,
    onBack: () -> Unit,
    onProductTap: (Product) -> Unit,
    onContact: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            HeroSection(state = state, onBack = onBack)
            Spacer(modifier = Modifier.height(BrandSpacing.md))
            FarmInfoSection(farm = state.farm)
            CertificationsSection(certifications = state.farm.certifications)
            ProductsSection(products = state.products, onProductTap = onProductTap)
            Spacer(modifier = Modifier.height(96.dp))
        }
        BottomBar(onContact = onContact)
    }
}

@Composable
private fun HeroSection(state: CaficultorDetailUiState, onBack: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(260.dp)) {
        if (state.farm.principalPhotoBase64 != null) {
            Base64Image(
                base64 = state.farm.principalPhotoBase64,
                contentDescription = state.farm.name,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(BrandColors.FarmerPrimary))
        }
        Box(
            modifier = Modifier
                .systemBarsPadding()
                .padding(BrandSpacing.sm)
                .size(40.dp)
                .background(BrandColors.HeroOverlayButtonBackground, CircleShape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Volver",
                tint = BrandColors.TextPrimary
            )
        }
    }
}

@Composable
private fun FarmInfoSection(farm: FarmProfile) {
    Column(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Avatar + nombre
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (farm.farmerPhotoBase64 != null) {
                Base64Image(
                    base64 = farm.farmerPhotoBase64,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(BrandColors.FarmerPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = BrandSpacing.sm)) {
                Text(
                    text = farm.name.ifBlank { "Caficultor" },
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandColors.TextPrimary,
                        lineHeight = 28.sp
                    )
                )
                if (farm.region.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = BrandColors.FarmerPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = farm.region,
                            color = BrandColors.TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        if (farm.title.isNotBlank()) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            Text(
                text = farm.title,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary,
                    lineHeight = 22.sp
                )
            )
        }
        if (farm.quote.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(8.dp))
                    .padding(BrandSpacing.md)
            ) {
                Text(
                    text = "\"${farm.quote}\"",
                    color = BrandColors.TextPrimary,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 19.sp
                )
            }
        }
        if (farm.story.isNotBlank()) {
            Text(
                text = farm.story,
                color = BrandColors.TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        if (farm.altitudeMeters > 0 || farm.areaHectares > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                if (farm.altitudeMeters > 0) {
                    DataChip(label = "ALTITUD", value = "${farm.altitudeMeters} msnm")
                }
                if (farm.areaHectares > 0) {
                    DataChip(label = "ÁREA", value = "${farm.areaHectares} ha")
                }
            }
        }
    }
}

@Composable
private fun DataChip(label: String, value: String) {
    Column(
        modifier = Modifier
            .background(BrandColors.CardBackground, RoundedCornerShape(8.dp))
            .padding(horizontal = BrandSpacing.md, vertical = 8.dp)
    ) {
        Text(text = label, color = BrandColors.TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
        Text(text = value, color = BrandColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun CertificationsSection(certifications: List<String>) {
    if (certifications.isEmpty()) return
    Column(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg, vertical = BrandSpacing.md)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "CERTIFICACIONES",
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        certifications.forEach { cert ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F4EC), RoundedCornerShape(8.dp))
                    .padding(BrandSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.WorkspacePremium,
                    contentDescription = null,
                    tint = BrandColors.FarmerPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = cert,
                    color = BrandColors.FarmerPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ProductsSection(products: List<Product>, onProductTap: (Product) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = BrandSpacing.lg)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(
            text = "Productos (${products.size})",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            )
        )
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
                    .padding(BrandSpacing.lg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no ha publicado productos.",
                    color = BrandColors.TextSecondary,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        } else {
            products.forEach { product ->
                ProductPreviewRow(product = product, onTap = { onProductTap(product) })
            }
        }
    }
}

@Composable
private fun ProductPreviewRow(product: Product, onTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(BrandSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Base64Image(
            base64 = product.imageBase64,
            contentDescription = product.name,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = BrandSpacing.sm)) {
            Text(
                text = product.name,
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "${product.weightGrams}g · ${product.category.label}",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = "$" + "%,d".format(product.priceCop).replace(',', '.'),
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(text = "›", color = BrandColors.TextSecondary, fontSize = 22.sp)
    }
}

@Composable
private fun BottomBar(onContact: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground)
            .systemBarsPadding()
            .padding(BrandSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onContact,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.FarmerPrimary,
                contentColor = Color.White
            )
        ) {
            Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Contactar al caficultor", fontWeight = FontWeight.SemiBold)
        }
    }
}

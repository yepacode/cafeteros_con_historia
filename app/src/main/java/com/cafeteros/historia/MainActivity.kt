package com.cafeteros.historia

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.ui.features.admin.AdminDashboardActivity
import com.cafeteros.historia.ui.features.auth.LoginActivity
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.cafeteros.historia.data.model.ApprovalStatus
import com.cafeteros.historia.ui.features.buyer_catalog.BuyerCatalogActivity
import com.cafeteros.historia.ui.features.explore.ExploreActivity
import com.cafeteros.historia.ui.features.farmer_registration.FarmerPendingApprovalActivity
import com.cafeteros.historia.ui.features.farmer_home.FarmerHomeScreen
import com.cafeteros.historia.ui.features.farmer_inventory.InventoryActivity
import com.cafeteros.historia.ui.features.farmer_messages.InboxActivity
import com.cafeteros.historia.ui.features.farmer_notifications.NotificationsActivity
import com.cafeteros.historia.ui.features.farmer_products.ProductCreateActivity
import com.cafeteros.historia.ui.features.farmer_products.ProductListActivity
import com.cafeteros.historia.ui.features.farmer_profile.MyProfileActivity
import com.cafeteros.historia.ui.features.farmer_reviews.ReviewsActivity
import com.cafeteros.historia.ui.features.farmer_sales.SalesActivity
import com.cafeteros.historia.ui.features.farmer_stats.StatsActivity
import com.cafeteros.historia.ui.features.farmer_wallet.WalletActivity
import com.cafeteros.historia.ui.features.home.HomeViewModel
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.delay

/**
 * Activity raíz de la home. Su único rol es decidir qué pantalla mostrar
 * según el rol del usuario logueado:
 *
 *  - **COMPRADOR**: redirige a [ExploreActivity] (flujo de exploración con
 *    mapa cafetero, búsqueda y carrito).
 *  - **CAFICULTOR**: panel principal ([FarmerHomeScreen]) con resumen del
 *    día, acciones rápidas, pedidos por empacar, ventas semanales, etc.
 *  - **ADMINISTRADOR**: redirige a [AdminDashboardActivity] (KPIs globales,
 *    CRUD de usuarios, gestión de productos y reporte de ventas).
 *
 * Cuando el `currentUserFlow` emite `null` después de haber emitido un user
 * (es decir, tras un logout), navega a [LoginActivity] y termina.
 *
 * Si la sesión Firebase está activa pero Firestore tarda en devolver el
 * perfil, se muestra un indicador de carga; si pasa demasiado tiempo sin
 * recibirlo (perfil incompleto / sin internet), se vuelve al Login.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val user by viewModel.currentUser.collectAsStateWithLifecycle()

                LaunchedEffect(user) {
                    if (user == null && hasObservedUser) {
                        navigateToLogin()
                    } else if (user != null) {
                        hasObservedUser = true
                        when (user?.userType) {
                            UserType.COMPRADOR -> {
                                // El comprador entra a ExploreActivity, ahora
                                // conectada a Firestore: muestra zonas reales
                                // derivadas de farms.region, caficultores
                                // destacados con sus fotos y productos
                                // populares — el diseño elaborado original
                                // con datos vivos.
                                val intent = Intent(
                                    this@MainActivity,
                                    ExploreActivity::class.java
                                ).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                startActivity(intent)
                                finish()
                            }
                            UserType.ADMINISTRADOR -> {
                                AdminDashboardActivity.start(this@MainActivity)
                                finish()
                            }
                            UserType.CAFICULTOR -> {
                                // Filtro de aprobación: solo APPROVED ve el
                                // panel de caficultor. PENDING_APPROVAL y
                                // REJECTED van a una pantalla de espera /
                                // rechazo desde donde solo pueden cerrar
                                // sesión.
                                val status = user?.approvalStatus
                                if (status != null && status != ApprovalStatus.APPROVED) {
                                    FarmerPendingApprovalActivity.start(this@MainActivity, status)
                                    finish()
                                }
                                // Si está APPROVED, se queda renderizando
                                // en MainActivity con FarmerHomeScreen.
                            }
                            null -> Unit
                        }
                    }
                }

                // Watchdog: si tras 6s sigue null pero esperábamos un perfil
                // (Firebase tenía sesión persistida), forzamos logout y Login.
                // Esto evita pantalla negra indefinida por red caída o doc
                // de Firestore inexistente.
                LaunchedEffect(Unit) {
                    delay(6_000)
                    if (user == null && !hasObservedUser) {
                        Toast.makeText(
                            this@MainActivity,
                            "No pudimos cargar tu perfil. Inicia sesión otra vez.",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.logout()
                        navigateToLogin()
                    }
                }

                val context = LocalContext.current
                val showSoonToast: (String) -> Unit = { sectionName ->
                    Toast.makeText(
                        context,
                        "Próximamente: $sectionName",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                when (user?.userType) {
                    UserType.CAFICULTOR -> FarmerHomeScreen(
                        userFirstName = user?.name?.trim()
                            ?.takeIf { it.isNotBlank() }
                            ?.substringBefore(' '),
                        farmName = null,
                        onLogout = viewModel::logout,
                        onCreateProduct = { ProductCreateActivity.startNew(this) },
                        onOpenProductsList = { ProductListActivity.start(this) },
                        onOpenInventory = { InventoryActivity.start(this) },
                        onOpenSales = { SalesActivity.start(this) },
                        onOpenNotifications = { NotificationsActivity.start(this) },
                        onOpenWallet = { WalletActivity.start(this) },
                        onOpenStats = { StatsActivity.start(this) },
                        onOpenProfile = { MyProfileActivity.start(this) },
                        onOpenInbox = { InboxActivity.start(this) },
                        onOpenReviews = { ReviewsActivity.start(this) },
                        onSectionTap = showSoonToast
                    )

                    // COMPRADOR y ADMINISTRADOR ya redirigieron a su Activity
                    // dedicada en el LaunchedEffect; mientras se hace la
                    // transición o se carga la sesión, mostramos un loader.
                    UserType.COMPRADOR, UserType.ADMINISTRADOR, null -> LoadingScreen()
                }
            }
        }
    }

    private var hasObservedUser: Boolean = false

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

/**
 * Pantalla intermedia mientras se resuelve el perfil del usuario desde
 * Firestore. Evita el flash de pantalla negra entre el splash y el destino
 * final tras un auto-login o un login biométrico.
 */
@androidx.compose.runtime.Composable
private fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = BrandColors.CoffeeBrown)
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Cargando tu perfil…",
            style = BrandTypography.OnboardingDescription
        )
    }
}

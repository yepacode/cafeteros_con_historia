package com.cafeteros.historia

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.ui.features.auth.LoginActivity
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.cafeteros.historia.ui.features.farmer_home.FarmerHomeScreen
import com.cafeteros.historia.ui.features.farmer_inventory.InventoryActivity
import com.cafeteros.historia.ui.features.farmer_products.ProductCreateActivity
import com.cafeteros.historia.ui.features.farmer_products.ProductListActivity
import com.cafeteros.historia.ui.features.farmer_messages.InboxActivity
import com.cafeteros.historia.ui.features.farmer_notifications.NotificationsActivity
import com.cafeteros.historia.ui.features.farmer_profile.MyProfileActivity
import com.cafeteros.historia.ui.features.farmer_reviews.ReviewsActivity
import com.cafeteros.historia.ui.features.farmer_sales.SalesActivity
import com.cafeteros.historia.ui.features.farmer_stats.StatsActivity
import com.cafeteros.historia.ui.features.farmer_wallet.WalletActivity
import com.cafeteros.historia.ui.features.home.HomeScreen
import com.cafeteros.historia.ui.features.home.HomeViewModel
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity raíz de la home. Su único rol es decidir qué pantalla mostrar
 * según el rol del usuario logueado:
 *
 *  - **CAFICULTOR**: panel principal ([FarmerHomeScreen]) con resumen del
 *    día, acciones rápidas, pedidos por empacar, ventas semanales, etc.
 *    La pantalla de "¡Bienvenido!" ([WelcomeApprovedStep]) ahora solo se
 *    muestra una vez al final del wizard de registro — no se reusa aquí.
 *  - **COMPRADOR**: [HomeScreen] placeholder con su info personal.
 *
 * Cuando se cierra la sesión, navega a [LoginActivity] y termina.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val user by viewModel.currentUser.collectAsStateWithLifecycle()
                val roleName by viewModel.roleName.collectAsStateWithLifecycle()

                LaunchedEffect(user) {

                    if (user == null && hasObservedUser) {
                        navigateToLogin()
                    } else if (user != null) {
                        hasObservedUser = true
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
                        // TODO: cuando la tabla users tenga `farm_name`, leerlo
                        // aquí. Hoy se queda null y el composable muestra
                        // "TU FINCA" como placeholder.
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

                    UserType.COMPRADOR,
                    null -> HomeScreen(
                        user = user,
                        roleNameFromDb = roleName,
                        onLogout = viewModel::logout
                    )
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

package com.cafeteros.historia.ui.features.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.cafeteros.historia.ui.features.auth.LoginActivity
import com.cafeteros.historia.ui.features.settings.SettingsActivity
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Activity raíz del rol Administrador.
 *
 * Muestra el [AdminDashboardScreen] con KPIs globales (usuarios totales por
 * rol, productos publicados, ventas estimadas) y atajos a las tres secciones
 * de gestión:
 *
 *  - [UserManagementActivity]    — CRUD de usuarios (Room real).
 *  - [ProductManagementActivity] — listado y pausa de publicaciones.
 *  - [SalesReportActivity]       — gráfico de ventas mock.
 */
class AdminDashboardActivity : ComponentActivity() {

    private val viewModel: AdminDashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                AdminDashboardScreen(
                    viewModel = viewModel,
                    onOpenUsers = { UserManagementActivity.start(this) },
                    onOpenProducts = { ProductManagementActivity.start(this) },
                    onOpenSalesReport = { SalesReportActivity.start(this) },
                    onOpenPendingApprovals = { PendingApprovalsActivity.start(this) },
                    onOpenSettings = { SettingsActivity.start(this) },
                    onLogout = {
                        viewModel.logout {
                            startActivity(
                                Intent(this, LoginActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                            )
                            finish()
                        }
                    }
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, AdminDashboardActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
        }
    }
}

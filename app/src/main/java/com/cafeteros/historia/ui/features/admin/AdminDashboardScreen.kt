package com.cafeteros.historia.ui.features.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PendingActions
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.ui.theme.BrandColors
import java.text.NumberFormat
import java.util.Locale

/**
 * Dashboard principal del rol Administrador.
 *
 * Layout:
 *  1. TopAppBar con título y botón de logout.
 *  2. Grilla 2×2 de KPIs (usuarios totales, productos publicados, ventas mes,
 *     pedidos pendientes).
 *  3. Sección "Distribución por rol" con conteo de compradores, caficultores
 *     y administradores.
 *  4. Lista de accesos a las secciones de gestión (usuarios, productos,
 *     reporte de ventas).
 *
 * Los KPIs de productos y ventas son mock; los de usuarios son reales (Room).
 */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel,
    onOpenUsers: () -> Unit,
    onOpenProducts: () -> Unit,
    onOpenSalesReport: () -> Unit,
    onOpenPendingApprovals: () -> Unit,
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Panel de Administrador",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Acceso a la pantalla de Configuración (toggle de huella).
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Configuración"
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Outlined.Logout,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandColors.CoffeeBrown,
                    titleContentColor = BrandColors.CreamWhite,
                    actionIconContentColor = BrandColors.CreamWhite
                )
            )
        },
        containerColor = BrandColors.AuthBackground
    ) { paddingValues ->
        AdminDashboardContent(
            paddingValues = paddingValues,
            uiState = state,
            onOpenUsers = onOpenUsers,
            onOpenProducts = onOpenProducts,
            onOpenSalesReport = onOpenSalesReport,
            onOpenPendingApprovals = onOpenPendingApprovals
        )
    }
}

@Composable
private fun AdminDashboardContent(
    paddingValues: PaddingValues,
    uiState: AdminDashboardUiState,
    onOpenUsers: () -> Unit,
    onOpenProducts: () -> Unit,
    onOpenSalesReport: () -> Unit,
    onOpenPendingApprovals: () -> Unit
) {
    val pesoFormatter = remember { NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Alerta destacada cuando hay caficultores pendientes de aprobación.
        // Se renderiza arriba del resto del dashboard para que el admin la
        // vea apenas entra.
        if (uiState.pendingApprovalsCount > 0) {
            item {
                PendingApprovalsBanner(
                    count = uiState.pendingApprovalsCount,
                    onClick = onOpenPendingApprovals
                )
            }
        }
        item {
            SectionHeader(title = "Resumen general")
        }
        item {
            KpiGrid(
                userCount = uiState.stats?.totalUsers ?: 0,
                productCount = uiState.publishedProducts,
                monthlySales = pesoFormatter.format(uiState.monthlySalesCop),
                pendingOrders = uiState.pendingOrders
            )
        }
        item {
            SectionHeader(title = "Distribución por rol")
        }
        item {
            RoleDistributionCard(
                buyers = uiState.stats?.buyers ?: 0,
                farmers = uiState.stats?.farmers ?: 0,
                admins = uiState.stats?.admins ?: 0
            )
        }
        item {
            SectionHeader(title = "Gestión")
        }
        item {
            ManagementAccessRow(
                icon = Icons.Outlined.People,
                title = "Gestión de usuarios",
                subtitle = "Listar, crear, editar y eliminar cuentas",
                onClick = onOpenUsers
            )
        }
        item {
            ManagementAccessRow(
                icon = Icons.Outlined.Storefront,
                title = "Gestión de productos",
                subtitle = "Revisar publicaciones y pausar reportadas",
                onClick = onOpenProducts
            )
        }
        item {
            ManagementAccessRow(
                icon = Icons.Outlined.QueryStats,
                title = "Reporte de ventas",
                subtitle = "Gráficos y top productos",
                onClick = onOpenSalesReport
            )
        }
        item {
            ManagementAccessRow(
                icon = Icons.Outlined.PendingActions,
                title = "Aprobaciones pendientes" +
                    if (uiState.pendingApprovalsCount > 0) " (${uiState.pendingApprovalsCount})" else "",
                subtitle = "Revisar caficultores que esperan aprobación de cuenta",
                onClick = onOpenPendingApprovals
            )
        }
    }
}

/**
 * Banner destacado arriba del dashboard que aparece solo cuando hay
 * caficultores esperando revisión. Funciona como llamada a la acción
 * principal para que el admin no se olvide de procesarlos.
 */
@Composable
private fun PendingApprovalsBanner(
    count: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BrandColors.InfoBannerBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "⏳",
            fontSize = 28.sp,
            modifier = Modifier.padding(end = 12.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Tienes $count caficultor${if (count == 1) "" else "es"} por revisar",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = BrandColors.TextPrimary
            )
            Text(
                text = "Revisa sus documentos y aprueba o rechaza la cuenta.",
                fontSize = 12.sp,
                color = BrandColors.TextSecondary
            )
        }
        Text(
            text = "Ver →",
            color = BrandColors.InfoBannerAction,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 1.5.sp,
        color = BrandColors.TextSecondary,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun KpiGrid(
    userCount: Int,
    productCount: Int,
    monthlySales: String,
    pendingOrders: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.People,
                accent = BrandColors.ForestGreen,
                value = userCount.toString(),
                label = "Usuarios totales"
            )
            KpiCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Inventory2,
                accent = BrandColors.CoffeeBrown,
                value = productCount.toString(),
                label = "Productos publicados"
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.AttachMoney,
                accent = BrandColors.RatingStar,
                value = monthlySales,
                label = "Ventas del mes"
            )
            KpiCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.PendingActions,
                accent = Color(0xFFC62828),
                value = pendingOrders.toString(),
                label = "Pedidos pendientes"
            )
        }
    }
}

@Composable
private fun KpiCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    accent: Color,
    value: String,
    label: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = BrandColors.CardBackground,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = accent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = value,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = BrandColors.TextPrimary
            )
            Text(
                text = label,
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp,
                color = BrandColors.TextSecondary
            )
        }
    }
}

@Composable
private fun RoleDistributionCard(buyers: Int, farmers: Int, admins: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BrandColors.CardBackground,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RoleRow(
                icon = Icons.Outlined.People,
                accent = BrandColors.ForestGreen,
                label = "Compradores",
                count = buyers
            )
            RoleRow(
                icon = Icons.Outlined.Storefront,
                accent = BrandColors.CoffeeBrown,
                label = "Caficultores",
                count = farmers
            )
            RoleRow(
                icon = Icons.Outlined.AdminPanelSettings,
                accent = BrandColors.RatingStar,
                label = "Administradores",
                count = admins
            )
        }
    }
}

@Composable
private fun RoleRow(icon: ImageVector, accent: Color, label: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            fontFamily = FontFamily.SansSerif,
            fontSize = 14.sp,
            color = BrandColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = count.toString(),
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = accent
        )
    }
}

@Composable
private fun ManagementAccessRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = BrandColors.CardBackground,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = BrandColors.CoffeeBrown.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BrandColors.CoffeeBrown,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = BrandColors.TextPrimary
                )
                Text(
                    text = subtitle,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = BrandColors.TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = BrandColors.TextSecondary
            )
        }
    }
}


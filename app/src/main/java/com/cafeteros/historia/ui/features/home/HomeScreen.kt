package com.cafeteros.historia.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Home placeholder personalizado por rol.
 *
 * Muestra el nombre y rol del usuario logueado, una **insignia visible** con
 * el nombre del rol leído directamente desde la tabla `roles` de la DB
 * (prueba viva del JOIN users.role_id → roles.id), y un botón de
 * "Cerrar sesión".
 *
 * @param modifier modifier opcional aplicado al [Column] raíz.
 * @param user usuario logueado a renderizar; si es `null`, se muestra un
 *  estado de carga simple.
 * @param roleNameFromDb nombre del rol resuelto desde la tabla `roles` por
 *  el ViewModel; null mientras carga.
 * @param onLogout callback al pulsar "Cerrar sesión".
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user: User?,
    roleNameFromDb: String?,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColors.AuthBackground)
            .systemBarsPadding()
            .padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (user == null) {
            Text(
                text = "Cargando…",
                style = BrandTypography.OnboardingDescription
            )
        } else {
            UserAvatar(userType = user.userType)
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
            Text(
                text = "¡Hola, ${user.name.substringBefore(' ')}!",
                style = BrandTypography.OnboardingTitle,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            Text(
                text = roleWelcomeMessage(user.userType),
                style = BrandTypography.OnboardingDescription,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
            RoleBadge(roleNameFromDb = roleNameFromDb, roleId = user.userType.roleId)
            Spacer(modifier = Modifier.height(BrandSpacing.xl))
            OutlinedButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(BrandSpacing.sm))
                Text(text = "Cerrar sesión")
            }
        }
    }
}

@Composable
private fun UserAvatar(userType: UserType) {
    val icon: ImageVector = when (userType) {
        UserType.COMPRADOR -> Icons.Outlined.ShoppingBag
        UserType.CAFICULTOR -> Icons.Outlined.Eco
        UserType.ADMINISTRADOR -> Icons.Outlined.AdminPanelSettings
    }
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(96.dp)
            .background(color = BrandColors.CoffeeBrown, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandColors.CreamWhite,
            modifier = Modifier.size(48.dp)
        )
    }
}

/**
 * Insignia visible con el nombre del rol del usuario.
 *
 * El valor se deriva del campo `roleId` del documento `/users/{uid}` en
 * Firestore (vía `UserType.fromRoleId`), no de una tabla local.
 */
@Composable
private fun RoleBadge(roleNameFromDb: String?, roleId: Int) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .background(
                color = BrandColors.CoffeeBrown.copy(alpha = 0.08f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = BrandSpacing.md, vertical = BrandSpacing.sm)
    ) {
        Text(
            text = if (roleNameFromDb != null) {
                "Rol en DB: $roleNameFromDb (id $roleId)"
            } else {
                "Cargando rol…"
            },
            style = BrandTypography.OnboardingDescription.copy(
                color = BrandColors.CoffeeBrown
            )
        )
    }
}

/** Mensaje de bienvenida específico al rol del usuario. */
private fun roleWelcomeMessage(userType: UserType): String = when (userType) {
    UserType.COMPRADOR ->
        "Aquí descubrirás cafés únicos de cada región de Colombia, " +
                "directo de los caficultores. Pronto verás el catálogo."
    UserType.CAFICULTOR ->
        "Aquí podrás contar tu historia, cargar tus cafés y conectar " +
                "con compradores en toda Colombia. Pronto activamos tu panel."
    UserType.ADMINISTRADOR ->
        "Desde aquí gestionas la plataforma: usuarios, vendedores y " +
                "contenido. Pronto verás el panel de administración."
}

@Preview(name = "HomeScreen – Comprador", widthDp = 360, heightDp = 720)
@Composable
private fun HomeScreenBuyerPreview() {
    CafeterosTheme {
        HomeScreen(
            user = User(
                id = "preview-user-1",
                email = "maria@correo.com",
                name = "María González",
                phone = "+57 300 123 4567",
                userType = UserType.COMPRADOR
            ),
            roleNameFromDb = "Comprador",
            onLogout = {}
        )
    }
}

@Preview(name = "HomeScreen – Caficultor", widthDp = 360, heightDp = 720)
@Composable
private fun HomeScreenFarmerPreview() {
    CafeterosTheme {
        HomeScreen(
            user = User(
                id = "preview-user-2",
                email = "carlos@finca.co",
                name = "Carlos Restrepo",
                phone = "+57 311 555 7788",
                userType = UserType.CAFICULTOR
            ),
            roleNameFromDb = "Vendedor",
            onLogout = {}
        )
    }
}

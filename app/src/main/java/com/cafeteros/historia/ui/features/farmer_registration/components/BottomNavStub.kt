package com.cafeteros.historia.ui.features.farmer_registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Barra de navegación inferior visualmente presente pero no funcional.
 *
 * El diseño del paso 5 incluye un tabbar que pertenece a la **app principal**
 * (Home / Mis ventas / Perfil), no al flujo de registro. Como aún no
 * construimos esa app principal, este composable solo lo dibuja como
 * placeholder; tocar los íconos no hace nada todavía.
 *
 * Cuando exista la `MainScaffold` de la app, esta barra se reemplaza por
 * la real (probablemente con `NavigationBar` de Material 3 + Navigation
 * Compose).
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 */
@Composable
fun BottomNavStub(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground)
            .height(68.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomTab(icon = Icons.Outlined.Home, label = "Inicio", isActive = false)
        BottomTab(icon = Icons.Outlined.Inventory2, label = "Verificación", isActive = true)
        BottomTab(icon = Icons.Outlined.Person, label = "Perfil", isActive = false)
    }
}

@Composable
private fun BottomTab(icon: ImageVector, label: String, isActive: Boolean) {
    val tint = if (isActive) BrandColors.FarmerPrimary else BrandColors.TextSecondary
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            modifier = Modifier.size(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 10.sp,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                color = tint
            )
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun BottomNavStubPreview() {
    BottomNavStub()
}

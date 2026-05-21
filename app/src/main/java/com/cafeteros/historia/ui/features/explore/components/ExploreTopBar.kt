package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Barra superior de la pantalla de exploración del comprador.
 *
 * Muestra:
 *  - Botón hamburguesa (acceso a menú lateral / drawer).
 *  - Wordmark "Origen" en serif itálico (identidad de marca compacta).
 *  - Botón de configuración (acceso a la pantalla de ajustes, incluido el
 *    toggle de huella).
 *  - Botón de notificaciones con punto rojo cuando [hasUnreadNotifications].
 *
 * Es stateless: el padre maneja la navegación vía callbacks.
 *
 * @param modifier modifier opcional aplicado al [Row] contenedor.
 * @param hasUnreadNotifications dibuja el punto rojo sobre la campana.
 * @param onMenuClick callback del botón hamburguesa.
 * @param onSettingsClick callback del botón de configuración.
 * @param onNotificationsClick callback de la campana de notificaciones.
 */
@Composable
fun ExploreTopBar(
    modifier: Modifier = Modifier,
    hasUnreadNotifications: Boolean = true,
    onMenuClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "Abrir menú",
                    tint = BrandColors.TextPrimary
                )
            }
            Text(
                text = "Origen",
                style = BrandTypography.ExploreWordmark
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Configuración",
                    tint = BrandColors.TextPrimary
                )
            }
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = onNotificationsClick) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notificaciones",
                        tint = BrandColors.TextPrimary
                    )
                }
                if (hasUnreadNotifications) {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp, end = 12.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(BrandColors.NotificationDot)
                    )
                }
            }
        }
    }
}

@Preview(name = "ExploreTopBar", showBackground = true, widthDp = 360)
@Composable
private fun ExploreTopBarPreview() {
    CafeterosTheme {
        ExploreTopBar()
    }
}

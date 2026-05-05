package com.cafeteros.historia.ui.features.auth.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
 * Card individual del selector de tipo de usuario en Registro.
 *
 * Muestra: ícono arriba a la izquierda, título y subtítulo en columna debajo.
 * El borde y el fondo se animan suavemente cuando cambia [selected].
 *
 * Es stateless: el padre ([UserTypeSelector]) decide cuál está activa.
 *
 * @param modifier modifier opcional (típ. `Modifier.weight(1f)` desde el padre).
 * @param userType opción que esta card representa.
 * @param selected true si esta es la opción activa actualmente.
 * @param onClick callback al pulsar la card.
 */
@Composable
fun UserTypeCard(
    modifier: Modifier = Modifier,
    userType: UserType,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            BrandColors.SelectableCardSelectedBackground
        } else {
            BrandColors.SelectableCardUnselectedBackground
        },
        label = "cardBackground"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            BrandColors.SelectableCardSelectedBorder
        } else {
            BrandColors.SelectableCardUnselectedBorder
        },
        label = "cardBorder"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .border(
                border = BorderStroke(2.dp, borderColor),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(BrandSpacing.md),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Icon(
            imageVector = userType.icon,
            contentDescription = null,
            tint = BrandColors.CoffeeBrown,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(BrandSpacing.xs))
        Text(
            text = userType.title,
            style = BrandTypography.SelectableCardTitle
        )
        Text(
            text = userType.subtitle,
            style = BrandTypography.SelectableCardSubtitle
        )
    }
}

@Preview(name = "UserTypeCard – seleccionada", showBackground = true, widthDp = 180)
@Composable
private fun UserTypeCardSelectedPreview() {
    CafeterosTheme {
        UserTypeCard(
            userType = UserType.COMPRADOR,
            selected = true,
            onClick = {}
        )
    }
}

@Preview(name = "UserTypeCard – no seleccionada", showBackground = true, widthDp = 180)
@Composable
private fun UserTypeCardUnselectedPreview() {
    CafeterosTheme {
        UserTypeCard(
            userType = UserType.CAFICULTOR,
            selected = false,
            onClick = {}
        )
    }
}

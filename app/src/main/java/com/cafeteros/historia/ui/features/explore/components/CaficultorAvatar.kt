package com.cafeteros.historia.ui.features.explore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.explore.model.FeaturedCaficultor
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Avatar circular del caficultor con borde dorado decorativo.
 *
 * Si [caficultor] expone un `avatarRes` lo usa con [ContentScale.Crop]; en
 * caso contrario rellena el círculo con el color de placeholder y sobrepone
 * un ícono de persona en blanco translúcido. Independientemente del modo,
 * siempre se aplica el borde dorado de marca para conservar la identidad
 * visual del componente.
 *
 * @param modifier modifier opcional aplicado al contenedor circular.
 * @param caficultor datos del caficultor cuyo avatar se está renderizando.
 * @param size diámetro del avatar (default 64dp como en el diseño).
 */
@Composable
fun CaficultorAvatar(
    modifier: Modifier = Modifier,
    caficultor: FeaturedCaficultor,
    size: Dp = 64.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(caficultor.avatarPlaceholderColor)
            .border(width = 2.dp, color = BrandColors.AvatarGoldBorder, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when {
            // Foto real del caficultor desde Firestore (Base64).
            caficultor.avatarBase64 != null -> {
                com.cafeteros.historia.ui.components.Base64Image(
                    base64 = caficultor.avatarBase64,
                    contentDescription = caficultor.farmName,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Drawable mock como fallback.
            caficultor.avatarRes != null -> {
                Image(
                    painter = painterResource(id = caficultor.avatarRes),
                    contentDescription = caficultor.farmName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = caficultor.farmName,
                    tint = Color.White.copy(alpha = 0.65f),
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }
    }
}

@Preview(name = "CaficultorAvatar", showBackground = true, widthDp = 100, heightDp = 100)
@Composable
private fun CaficultorAvatarPreview() {
    CafeterosTheme {
        CaficultorAvatar(
            caficultor = FeaturedCaficultor(
                farmName = "Finca La Esperanza",
                location = "Huila",
                rating = 4.9,
                reviewCount = 127,
                badge = com.cafeteros.historia.ui.features.explore.model.CaficultorBadge.ORGANICO,
                avatarPlaceholderColor = Color(0xFF8B5A2B)
            )
        )
    }
}

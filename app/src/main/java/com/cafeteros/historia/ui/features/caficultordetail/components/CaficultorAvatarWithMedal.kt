package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Avatar circular del caficultor con borde dorado y una pequeña medalla
 * en la esquina inferior-derecha que comunica su distintivo.
 *
 * Visual:
 *  - Círculo principal de 64dp con borde dorado de 2dp y la foto del
 *    caficultor recortada como `ContentScale.Crop`.
 *  - Mini círculo de 22dp en la esquina inferior-derecha con un ícono
 *    de medalla blanco sobre fondo dorado.
 *
 * @param modifier modifier opcional.
 * @param avatarRes drawable con la foto del caficultor.
 */
@Composable
fun CaficultorAvatarWithMedal(
    modifier: Modifier = Modifier,
    avatarRes: Int
) {
    Box(modifier = modifier.size(72.dp)) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = BrandColors.AvatarGoldBorder, shape = CircleShape)
                .align(Alignment.TopStart),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(22.dp)
                .clip(CircleShape)
                .background(BrandColors.ProfileMedalBackground)
                .border(width = 2.dp, color = Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.WorkspacePremium,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Preview(name = "CaficultorAvatarWithMedal", showBackground = true)
@Composable
private fun CaficultorAvatarWithMedalPreview() {
    CafeterosTheme {
        CaficultorAvatarWithMedal(avatarRes = R.drawable.ima_1)
    }
}

package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.R
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Video de la finca": título + thumbnail con botón de play
 * circular sobrepuesto + caption italic debajo.
 *
 * El play button es decorativo a este nivel — la integración real con un
 * reproductor (ExoPlayer / Media3) llegará en una iteración futura. Por
 * ahora el toque sobre el thumbnail dispara [onPlayClick].
 *
 * @param modifier modifier opcional.
 * @param thumbnailRes drawable a usar como portada del video.
 * @param caption texto italic bajo el thumbnail.
 * @param onPlayClick callback al pulsar el thumbnail / botón play.
 */
@Composable
fun CaficultorVideoCard(
    modifier: Modifier = Modifier,
    thumbnailRes: Int,
    caption: String,
    onPlayClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Text(text = "Video de la finca", style = BrandTypography.ZoneSectionTitle)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable(onClick = onPlayClick)
        ) {
            Image(
                painter = painterResource(id = thumbnailRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BrandColors.VideoOverlayScrim)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(BrandColors.VideoPlayButtonBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Reproducir video",
                    tint = BrandColors.VideoPlayIconTint,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(text = caption, style = BrandTypography.VideoCaption)
    }
}

@Preview(name = "CaficultorVideoCard", showBackground = true, widthDp = 360)
@Composable
private fun CaficultorVideoCardPreview() {
    CafeterosTheme {
        CaficultorVideoCard(
            thumbnailRes = R.drawable.img_3,
            caption = "Conoce la finca y el proceso de Don Alberto"
        )
    }
}

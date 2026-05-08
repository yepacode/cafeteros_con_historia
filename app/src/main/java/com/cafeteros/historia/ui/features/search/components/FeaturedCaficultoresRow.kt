package com.cafeteros.historia.ui.features.search.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.cafeteros.historia.ui.features.search.model.FeaturedSearchCaficultor
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Caficultores destacados" del shortcut de búsqueda.
 *
 * Visual: título italic seguido de un [LazyRow] de avatares circulares con
 * borde dorado y nombre debajo. El LazyRow permite scroll horizontal cuando
 * hay más caficultores de los que caben en pantalla.
 *
 * @param modifier modifier opcional.
 * @param caficultores lista a renderizar.
 * @param contentPadding padding horizontal del LazyRow para alinearse con
 *   las demás secciones de la pantalla.
 * @param onCaficultorClick callback al pulsar un avatar.
 */
@Composable
fun FeaturedCaficultoresRow(
    modifier: Modifier = Modifier,
    caficultores: List<FeaturedSearchCaficultor>,
    contentPadding: PaddingValues = PaddingValues(horizontal = BrandSpacing.lg),
    onCaficultorClick: (FeaturedSearchCaficultor) -> Unit = {}
) {
    if (caficultores.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Text(
            text = "Caficultores destacados",
            style = BrandTypography.SearchSectionTitle,
            modifier = Modifier.padding(horizontal = BrandSpacing.lg)
        )
        LazyRow(
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            items(items = caficultores, key = { it.displayName }) { caficultor ->
                FeaturedCaficultorAvatar(
                    caficultor = caficultor,
                    onClick = { onCaficultorClick(caficultor) }
                )
            }
        }
    }
}

@Composable
private fun FeaturedCaficultorAvatar(
    modifier: Modifier = Modifier,
    caficultor: FeaturedSearchCaficultor,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = BrandSpacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
    ) {
        Image(
            painter = painterResource(id = caficultor.avatarRes),
            contentDescription = caficultor.displayName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = BrandColors.AvatarGoldBorder,
                    shape = CircleShape
                )
        )
        Text(
            text = caficultor.displayName,
            style = BrandTypography.FeaturedCaficultorName
        )
    }
}

@Preview(name = "FeaturedCaficultoresRow", showBackground = true, widthDp = 360)
@Composable
private fun FeaturedCaficultoresRowPreview() {
    CafeterosTheme {
        FeaturedCaficultoresRow(
            caficultores = listOf(
                FeaturedSearchCaficultor("Don Alberto", R.drawable.ima_1),
                FeaturedSearchCaficultor("Doña Elena", R.drawable.imag_2)
            )
        )
    }
}

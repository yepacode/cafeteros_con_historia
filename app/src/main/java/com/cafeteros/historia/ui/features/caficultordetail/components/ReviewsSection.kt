package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cafeteros.historia.ui.features.caficultordetail.model.CaficultorReview
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Sección "Reseñas": header con "VER TODAS" + lista vertical de
 * [ReviewCard].
 *
 * @param modifier modifier opcional.
 * @param reviews reseñas a renderizar (top-N normalmente).
 * @param onSeeAllReviews callback del enlace "VER TODAS".
 */
@Composable
fun ReviewsSection(
    modifier: Modifier = Modifier,
    reviews: List<CaficultorReview>,
    onSeeAllReviews: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Reseñas", style = BrandTypography.ZoneSectionTitle)
            Text(
                text = "VER TODAS",
                style = BrandTypography.SortLink,
                modifier = Modifier.clickable(onClick = onSeeAllReviews)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)) {
            reviews.forEach { review ->
                ReviewCard(review = review)
            }
        }
    }
}

@Preview(name = "ReviewsSection", showBackground = true, widthDp = 360)
@Composable
private fun ReviewsSectionPreview() {
    CafeterosTheme {
        ReviewsSection(
            reviews = listOf(
                CaficultorReview(
                    authorName = "Camila V.",
                    rating = 5,
                    body = "El mejor café que he probado este año.",
                    avatarPlaceholderColor = androidx.compose.ui.graphics.Color(0xFFB16A4A)
                )
            )
        )
    }
}

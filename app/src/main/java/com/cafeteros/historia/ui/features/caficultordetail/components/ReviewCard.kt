package com.cafeteros.historia.ui.features.caficultordetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeteros.historia.ui.features.caficultordetail.model.CaficultorReview
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.BrandTypography
import com.cafeteros.historia.ui.theme.CafeterosTheme

/**
 * Card de una reseña individual.
 *
 * Layout horizontal: avatar circular a la izquierda y a la derecha una
 * columna con (nombre del autor + estrellas) en la primera línea y el
 * cuerpo de la reseña debajo.
 *
 * @param modifier modifier opcional.
 * @param review datos a renderizar.
 */
@Composable
fun ReviewCard(
    modifier: Modifier = Modifier,
    review: CaficultorReview
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BrandSpacing.md),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(review.avatarPlaceholderColor),
            contentAlignment = Alignment.Center
        ) {
            if (review.avatarRes != null) {
                Image(
                    painter = painterResource(id = review.avatarRes),
                    contentDescription = review.authorName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = review.authorName,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = review.authorName, style = BrandTypography.ReviewerName)
                ReviewStarsRow(filledCount = review.rating)
            }
            Text(text = review.body, style = BrandTypography.ReviewBody)
        }
    }
}

@Preview(name = "ReviewCard", showBackground = true, widthDp = 360)
@Composable
private fun ReviewCardPreview() {
    CafeterosTheme {
        ReviewCard(
            review = CaficultorReview(
                authorName = "Camila V.",
                rating = 5,
                body = "El mejor café que he probado este año. Se nota la frescura y el amor en cada grano.",
                avatarPlaceholderColor = Color(0xFFB16A4A)
            )
        )
    }
}

package com.mbh.moviebrowser.features.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mbh.moviebrowser.R

@Composable
fun MovieAudienceContainer(
    loading: Boolean,
    rating: Float,
    formattedRating: String,
    adult: Boolean,
    genres: List<String>
) {
    Box(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                RatingIndicator(
                    loading = loading,
                    rating = rating,
                    label = formattedRating
                )
                AdultIndicator(
                    loading = loading,
                    adult = adult,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                GenresRow(
                    loading = loading,
                    genres = genres,
                )
            }
        }
    }
}

@Composable
private fun RatingIndicator(
    loading: Boolean,
    rating: Float,
    label: String,
) {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                progress = rating,
                modifier = Modifier
                    .padding(8.dp)
                    .width(24.dp)
                    .height(24.dp),
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            val ratingString = buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(label)
                pushStyle(SpanStyle(fontWeight = FontWeight.Light))
                append(" / 10")
            }
            Text(
                text = ratingString,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun AdultIndicator(
    loading: Boolean,
    adult: Boolean
) {
    if (adult) {
        Box(
            Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_adult),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
fun MovieAudienceContainerPreview() {
    MovieAudienceContainer(
        loading = false,
        rating = 6.995f,
        formattedRating = "6.9",
        adult = true,
        genres = listOf("Action", "Adventure", "Comedy")
    )
}
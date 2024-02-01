package com.mbh.moviebrowser.features.details.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GenresRow(
    loading: Boolean,
    genres: List<String>
) {
    Box {
        LazyRow {
            items(genres) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            shape = RoundedCornerShape(8.dp),
                        )
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GenresListPreview() {
    GenresRow(
        loading = false,
        genres = listOf("Action", "Adventure", "Comedy")
    )
}
package com.mbh.moviebrowser.features.trending.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mbh.moviebrowser.domain.data.MovieEntity

@Composable
fun MovieList(
    movies: List<MovieEntity>,
    favorites: List<MovieEntity>,
    onDetailsClicked: (MovieEntity) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(movies) { item ->
            MovieListItem(
                movie = item,
                favorite = favorites.map { it.id }.contains(item.id),
                onDetailsClicked,
            )
        }
    }
}

@Preview
@Composable
private fun MovieListPreview() {
    Surface {
        MovieList(
            movies = listOf(
                MovieEntity.SAMPLE_MOVIE,
                MovieEntity.SAMPLE_MOVIE,
            ),
            favorites = listOf(MovieEntity.SAMPLE_MOVIE),
            onDetailsClicked = {},
        )
    }
}
package com.mbh.moviebrowser.features.trending.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mbh.moviebrowser.domain.Movie

@Composable
fun MovieList(
    loading: Boolean,
    movies: List<Movie>,
    onDetailsClicked: (Movie) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(movies) { item ->
            MovieListItem(
                movie = item,
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
                Movie(
                    id = 455476,
                    title = "Knights of the Zodiac",
                    genres = "Action, Sci-fi",
                    overview = "When a headstrong street orphan, Seiya, in search of his abducted sister unwittingly taps into hidden powers, he discovers he might be the only person alive who can protect a reincarnated goddess, sent to watch over humanity. Can he let his past go and embrace his destiny to become a Knight of the Zodiac?",
                    coverUrl = "https://image.tmdb.org/t/p/w500/qW4crfED8mpNDadSmMdi7ZDzhXF.jpg",
                    rating = 6.5f,
                    isFavorite = true,
                ),
                Movie(
                    id = 385687,
                    title = "Fast X",
                    genres = "Action",
                    overview = "Over many missions and against impossible odds, Dom Toretto and his family have outsmarted, out-nerved and outdriven every foe in their path. Now, they confront the most lethal opponent they've ever faced: A terrifying threat emerging from the shadows of the past who's fueled by blood revenge, and who is determined to shatter this family and destroy everything—and everyone—that Dom loves, forever.",
                    coverUrl = "https://image.tmdb.org/t/p/w500/fiVW06jE7z9YnO4trhaMEdclSiC.jpg",
                    rating = 7.4f,
                    isFavorite = false,
                ),
            ),
            onDetailsClicked = {},
            loading = false,
        )
    }
}
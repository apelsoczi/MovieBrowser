package com.mbh.moviebrowser.store

import com.mbh.moviebrowser.domain.Movie
import kotlinx.coroutines.flow.MutableStateFlow

class MovieStore {
    val movies: MutableStateFlow<List<Movie>> = MutableStateFlow(
        listOf(
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
    )
    val detailsId: MutableStateFlow<Long> = MutableStateFlow(-1)
}

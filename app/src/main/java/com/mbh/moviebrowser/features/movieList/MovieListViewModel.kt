package com.mbh.moviebrowser.features.movieList

import androidx.lifecycle.ViewModel
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.store.MovieStore

class MovieListViewModel(
    private val movieStore: MovieStore,
) : ViewModel() {
    val movies = movieStore.movies

    fun storeMovieForNavigation(movie: Movie) {
        movieStore.detailsId.value = movie.id
    }
}

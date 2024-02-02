package com.mbh.moviebrowser.domain.usecase

import com.mbh.moviebrowser.api.TmdbRepository
import com.mbh.moviebrowser.api.common.DomainResult
import com.mbh.moviebrowser.api.services.details.DetailsApiResponse.MovieDetailDTO
import com.mbh.moviebrowser.api.services.trending.model.MovieDTO
import com.mbh.moviebrowser.domain.data.MovieEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject


class FormatMovieUseCase @Inject constructor(
    private val tmdbRepository: TmdbRepository,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(dto: List<MovieDTO>): List<MovieEntity> = withContext(ioDispatcher) {
        return@withContext dto.map {
            MovieEntity(
                id = it.id,
                title = it.title,
                tagline = "",
                genres = formatGenres(it.genres),
                overview = it.overview,
                coverUrl = coverUrl(it.posterPath),
                backdropUrl = backdropUrl(it.backdropPath),
                rating = rating(it.voteAverage),
                formattedRating = formattedRating(it.voteAverage),
                adult = it.adult,
                releaseDate = releaseDate(it.releaseDate),
            )
        }
    }

    suspend operator fun invoke(dto: MovieDetailDTO): MovieEntity = withContext(ioDispatcher) {
        return@withContext MovieEntity(
            id = dto.id,
            title = dto.title,
            tagline = dto.tagline,
            overview = dto.overview,
            genres = formatGenres(dto.genres.map { it.id }),
            coverUrl = coverUrl(dto.posterPath),
            backdropUrl = backdropUrl(dto.backdropPath),
            rating = rating(dto.voteAverage),
            formattedRating = formattedRating(dto.voteAverage),
            adult = dto.adult,
            releaseDate = releaseDate(dto.releaseDate),
        )
    }

    private suspend fun formatGenres(genreIds: List<String>): List<String> {
        // update genre labels if possible
        val genresDTO = tmdbRepository.requestGenres()
        val genres = if (genresDTO is DomainResult.Success) {
            genresDTO.data.genres
                .filter { genreIds.contains(it.id) }
                .map { it.name }
        } else {
            emptyList()
        }
        return genres
    }

    private fun coverUrl(posterPath: String) = "https://image.tmdb.org/t/p/w500/${posterPath}"

    private fun backdropUrl(backdropPath: String) = "https://image.tmdb.org/t/p/original/${backdropPath}"

    private fun rating(voteAverage: Double) = voteAverage.toFloat() / 10.0f

    private fun formattedRating(voteAverage: Double) = voteAverage.toString().substring(
        startIndex = 0,
        endIndex = voteAverage.toString().indexOf('.') + 2
    ).replace(".0", "")

    private fun releaseDate(releaseDate: String) = LocalDate.parse(releaseDate).year.toString()

}


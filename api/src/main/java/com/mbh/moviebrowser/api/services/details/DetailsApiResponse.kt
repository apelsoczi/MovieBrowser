package com.mbh.moviebrowser.api.services.details

import com.mbh.moviebrowser.api.services.details.model.GenreDTO
import com.mbh.moviebrowser.api.services.details.model.MovieCollectionDTO
import com.mbh.moviebrowser.api.services.details.model.ProductionCompanyDTO
import com.mbh.moviebrowser.api.services.details.model.ProductionCountryDTO
import com.mbh.moviebrowser.api.services.details.model.SpokenLanguageDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface DetailsApiResponse {

    @Serializable
    data class MovieDetailDTO(
        val adult: Boolean,
        @SerialName("backdrop_path") val backdropPath: String,
        @SerialName("belongs_to_collection") val collection: MovieCollectionDTO?,
        val budget: Int,
        val genres: List<GenreDTO>,
        val homepage: String,
        val id: Int,
        @SerialName("imdb_id") val imdbId: String,
        @SerialName("original_language") val originalLanguage: String,
        @SerialName("original_title") val originalTitle: String,
        val overview: String,
        val popularity: Double,
        @SerialName("poster_path") val posterPath: String,
        @SerialName("production_companies") val productionCompanies: List<ProductionCompanyDTO>,
        @SerialName("production_countries") val productionCountries: List<ProductionCountryDTO>,
        @SerialName("release_date") val releaseDate: String,
        val revenue: Int,
        val runtime: Int,
        @SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguageDTO>,
        val status: String,
        val tagline: String,
        val title: String,
        val video: Boolean,
        @SerialName("vote_average") val voteAverage: Double,
        @SerialName("vote_count") val voteCount: Int
    ) : DetailsApiResponse

    @Serializable
    data class DetailsErrorDTO(
        @SerialName("status_code") val statusCode: Int,
        @SerialName("status_message") val statusMessage: String,
    ) : DetailsApiResponse

}